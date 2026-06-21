package com.server.app.services;

import com.server.app.dto.planpago.PlanPagoResponseDto;
import com.server.app.dto.prestamo.PrestamoCreateDto;
import com.server.app.dto.prestamo.PrestamoResponseDto;
import com.server.app.entities.PlanPago;
import com.server.app.entities.Prestamo;
import com.server.app.entities.User;
import com.server.app.entities.enums.EstadoCuota;
import com.server.app.entities.enums.EstadoPrestamo;
import com.server.app.exceptions.NotFoundException;
import com.server.app.repositories.PlanPagoRepository;
import com.server.app.repositories.PrestamoRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PrestamoService {

    private static final int ESCALA = 2;
    private static final int ESCALA_INTERMEDIA = 10;

    private final PrestamoRepository prestamoRepository;
    private final PlanPagoRepository planPagoRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public Page<PrestamoResponseDto> findAllByUser(
            int userId,
            int page,
            int size
    ) {
        return prestamoRepository
                .findAllByUsuarioId(
                        userId,
                        PageRequest.of(page, size)
                )
                .map(this::toResponseDto);
    }

    @Transactional
    public PrestamoResponseDto create(
            int userId,
            PrestamoCreateDto dto
    ) {
        User user = userService.findById(userId);

        Prestamo prestamo = new Prestamo();
        prestamo.setCapitalSolicitado(dto.getCapitalSolicitado());
        prestamo.setTasaInteresAnual(dto.getTasaInteresAnual());
        prestamo.setPlazoMeses(dto.getPlazoMeses());
        prestamo.setEstado(EstadoPrestamo.APROBADO);
        prestamo.setUsuario(user);

        prestamo = prestamoRepository.save(prestamo);

        List<PlanPago> planesPago = generarTablaAmortizacion(prestamo);
        planPagoRepository.saveAll(planesPago);

        return toResponseDto(prestamo);
    }

    @Transactional(readOnly = true)
    public Prestamo findEntityByIdAndUser(
            Long prestamoId,
            int userId
    ) {
        return prestamoRepository
                .findByIdAndUsuarioId(prestamoId, userId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Préstamo no encontrado"
                        )
                );
    }

    @Transactional(readOnly = true)
    public Page<PlanPagoResponseDto> getPlanesPagoPendientes(
            Long prestamoId,
            int userId,
            int page,
            int size
    ) {
        // Valida que el préstamo exista y pertenezca al usuario autenticado
        findEntityByIdAndUser(prestamoId, userId);

        return planPagoRepository
                .findAllByPrestamoIdAndEstado(
                        prestamoId,
                        EstadoCuota.PENDIENTE,
                        PageRequest.of(page, size)
                )
                .map(this::toPlanPagoResponseDto);
    }

    /**
     * Genera la tabla de amortización bajo el sistema francés
     * (cuota fija): el monto total de cada cuota es siempre el
     * mismo, pero la proporción de capital e interés cambia mes
     * a mes (el interés disminuye, el capital aumenta).
     */
    private List<PlanPago> generarTablaAmortizacion(Prestamo prestamo) {
        BigDecimal capital = prestamo.getCapitalSolicitado();
        int plazoMeses = prestamo.getPlazoMeses();

        // Tasa mensual = tasa anual / 12 / 100 (la tasa anual viene en %)
        BigDecimal tasaMensual = prestamo.getTasaInteresAnual()
                .divide(BigDecimal.valueOf(12), ESCALA_INTERMEDIA, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(100), ESCALA_INTERMEDIA, RoundingMode.HALF_UP);

        BigDecimal cuotaFija = calcularCuotaFija(capital, tasaMensual, plazoMeses);

        List<PlanPago> planesPago = new ArrayList<>();
        BigDecimal saldoPendiente = capital;

        // La primera cuota vence al día siguiente de solicitar el
        // préstamo (en vez de a un mes) para facilitar las pruebas
        // manuales del escenario de mora sin tener que esperar.
        // Las cuotas siguientes mantienen una periodicidad mensual.
        LocalDate fechaVencimiento = LocalDate.now().plusDays(1);

        for (int numeroCuota = 1; numeroCuota <= plazoMeses; numeroCuota++) {
            BigDecimal interesDelMes = saldoPendiente
                    .multiply(tasaMensual)
                    .setScale(ESCALA, RoundingMode.HALF_UP);

            BigDecimal capitalDelMes;

            if (numeroCuota == plazoMeses) {
                // En la última cuota se ajusta el capital para
                // que el saldo pendiente quede exactamente en cero,
                // evitando que queden centavos de diferencia por
                // los redondeos acumulados en cuotas anteriores.
                capitalDelMes = saldoPendiente;
            } else {
                capitalDelMes = cuotaFija
                        .subtract(interesDelMes)
                        .setScale(ESCALA, RoundingMode.HALF_UP);
            }

            PlanPago planPago = new PlanPago();
            planPago.setNumeroCuota(numeroCuota);
            planPago.setMontoCapital(capitalDelMes);
            planPago.setMontoInteres(interesDelMes);
            planPago.setFechaVencimiento(fechaVencimiento);
            planPago.setEstado(EstadoCuota.PENDIENTE);
            planPago.setPrestamo(prestamo);

            planesPago.add(planPago);

            saldoPendiente = saldoPendiente.subtract(capitalDelMes);
            fechaVencimiento = fechaVencimiento.plusMonths(1);
        }

        return planesPago;
    }

    /**
     * Fórmula de cuota fija (sistema francés):
     * cuota = capital * [ i * (1+i)^n ] / [ (1+i)^n - 1 ]
     * donde i = tasa mensual, n = número de cuotas.
     * Si la tasa es cero, la cuota es simplemente capital / n.
     */
    private BigDecimal calcularCuotaFija(
            BigDecimal capital,
            BigDecimal tasaMensual,
            int plazoMeses
    ) {
        if (tasaMensual.compareTo(BigDecimal.ZERO) == 0) {
            return capital.divide(
                    BigDecimal.valueOf(plazoMeses),
                    ESCALA,
                    RoundingMode.HALF_UP
            );
        }

        BigDecimal unoMasTasa = BigDecimal.ONE.add(tasaMensual);
        BigDecimal factor = unoMasTasa.pow(plazoMeses);

        BigDecimal numerador = tasaMensual.multiply(factor);
        BigDecimal denominador = factor.subtract(BigDecimal.ONE);

        return capital
                .multiply(numerador)
                .divide(denominador, ESCALA_INTERMEDIA, RoundingMode.HALF_UP)
                .setScale(ESCALA, RoundingMode.HALF_UP);
    }

    private PrestamoResponseDto toResponseDto(Prestamo prestamo) {
        return new PrestamoResponseDto(
                prestamo.getId(),
                prestamo.getCapitalSolicitado(),
                prestamo.getTasaInteresAnual(),
                prestamo.getPlazoMeses(),
                prestamo.getEstado()
        );
    }

    private PlanPagoResponseDto toPlanPagoResponseDto(PlanPago planPago) {
        return new PlanPagoResponseDto(
                planPago.getId(),
                planPago.getNumeroCuota(),
                planPago.getMontoCapital(),
                planPago.getMontoInteres(),
                planPago.getFechaVencimiento(),
                planPago.getEstado(),
                planPago.getPrestamo().getId()
        );
    }
}
