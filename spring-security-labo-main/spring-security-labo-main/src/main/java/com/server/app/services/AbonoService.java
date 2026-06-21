package com.server.app.services;

import com.server.app.dto.abono.AbonoCreateDto;
import com.server.app.dto.abono.AbonoResponseDto;
import com.server.app.dto.prestamo.ResumenCreditoResponseDto;
import com.server.app.entities.Abono;
import com.server.app.entities.PlanPago;
import com.server.app.entities.Prestamo;
import com.server.app.entities.enums.EstadoCuota;
import com.server.app.entities.enums.EstadoPrestamo;
import com.server.app.exceptions.BadRequestException;
import com.server.app.exceptions.ForbiddenException;
import com.server.app.exceptions.NotFoundException;
import com.server.app.repositories.AbonoRepository;
import com.server.app.repositories.PlanPagoRepository;
import com.server.app.repositories.PrestamoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class AbonoService {

    private static final int ESCALA = 2;

    private static final BigDecimal PORCENTAJE_MORA = new BigDecimal("0.05");

    private final AbonoRepository abonoRepository;
    private final PlanPagoRepository planPagoRepository;
    private final PrestamoRepository prestamoRepository;

    @Transactional
    public AbonoResponseDto registrarAbono(
            int userId,
            AbonoCreateDto dto
    ) {
        PlanPago planPago = planPagoRepository
                .findById(dto.getPlanPagoId())
                .orElseThrow(() ->
                        new NotFoundException(
                                "Plan de pago no encontrado"
                        )
                );

        if (planPago.getPrestamo().getUsuario().getId() != userId) {
            throw new ForbiddenException(
                    "Este plan de pago no pertenece al usuario autenticado"
            );
        }

        if (planPago.getEstado() == EstadoCuota.PAGADO) {
            throw new BadRequestException(
                    "Esta cuota ya fue pagada"
            );
        }

        LocalDate fechaPago = dto.getFechaPago() != null
                ? dto.getFechaPago()
                : LocalDate.now();

        BigDecimal recargoMora = calcularRecargoMora(planPago, fechaPago);

        Abono abono = new Abono();
        abono.setMonto(dto.getMonto());
        abono.setFechaPago(fechaPago);
        abono.setRecargoMora(recargoMora);
        abono.setPlanPago(planPago);

        abono = abonoRepository.save(abono);

        planPago.setEstado(EstadoCuota.PAGADO);
        planPagoRepository.save(planPago);

        actualizarEstadoPrestamoSiCorresponde(planPago.getPrestamo());

        return toResponseDto(abono);
    }

    @Transactional(readOnly = true)
    public ResumenCreditoResponseDto getResumenCredito(int userId) {
        List<Prestamo> prestamos = prestamoRepository.findAllByUsuarioId(userId);

        int prestamosActivos = 0;
        BigDecimal capitalTotalSolicitado = BigDecimal.ZERO;
        BigDecimal saldoCapitalPendiente = BigDecimal.ZERO;
        BigDecimal interesPendiente = BigDecimal.ZERO;
        BigDecimal moraAcumulada = BigDecimal.ZERO;
        int cuotasPendientes = 0;
        int cuotasVencidas = 0;

        LocalDate hoy = LocalDate.now();

        for (Prestamo prestamo : prestamos) {
            capitalTotalSolicitado =
                    capitalTotalSolicitado.add(prestamo.getCapitalSolicitado());

            if (prestamo.getEstado() != EstadoPrestamo.PAGADO) {
                prestamosActivos++;
            }

            List<PlanPago> planesPago = planPagoRepository
                    .findAllByPrestamoIdOrderByNumeroCuotaAsc(prestamo.getId());

            for (PlanPago planPago : planesPago) {
                if (planPago.getEstado() != EstadoCuota.PENDIENTE) {
                    continue;
                }

                cuotasPendientes++;
                saldoCapitalPendiente =
                        saldoCapitalPendiente.add(planPago.getMontoCapital());
                interesPendiente =
                        interesPendiente.add(planPago.getMontoInteres());

                if (planPago.getFechaVencimiento().isBefore(hoy)) {
                    cuotasVencidas++;

                    BigDecimal montoCuota = planPago.getMontoCapital()
                            .add(planPago.getMontoInteres());

                    moraAcumulada = moraAcumulada.add(
                            montoCuota
                                    .multiply(PORCENTAJE_MORA)
                                    .setScale(ESCALA, RoundingMode.HALF_UP)
                    );
                }
            }
        }

        BigDecimal deudaTotalPendiente = saldoCapitalPendiente
                .add(interesPendiente)
                .add(moraAcumulada);

        return new ResumenCreditoResponseDto(
                prestamosActivos,
                capitalTotalSolicitado.setScale(ESCALA, RoundingMode.HALF_UP),
                saldoCapitalPendiente.setScale(ESCALA, RoundingMode.HALF_UP),
                interesPendiente.setScale(ESCALA, RoundingMode.HALF_UP),
                moraAcumulada.setScale(ESCALA, RoundingMode.HALF_UP),
                deudaTotalPendiente.setScale(ESCALA, RoundingMode.HALF_UP),
                cuotasPendientes,
                cuotasVencidas
        );
    }

    private BigDecimal calcularRecargoMora(
            PlanPago planPago,
            LocalDate fechaPago
    ) {
        if (!fechaPago.isAfter(planPago.getFechaVencimiento())) {
            return BigDecimal.ZERO;
        }

        BigDecimal montoCuota = planPago.getMontoCapital()
                .add(planPago.getMontoInteres());

        return montoCuota
                .multiply(PORCENTAJE_MORA)
                .setScale(ESCALA, RoundingMode.HALF_UP);
    }

    private void actualizarEstadoPrestamoSiCorresponde(Prestamo prestamo) {
        List<PlanPago> planesPago = planPagoRepository
                .findAllByPrestamoIdOrderByNumeroCuotaAsc(prestamo.getId());

        boolean todasPagadas = planesPago.stream()
                .allMatch(p -> p.getEstado() == EstadoCuota.PAGADO);

        if (todasPagadas) {
            prestamo.setEstado(EstadoPrestamo.PAGADO);
            prestamoRepository.save(prestamo);
        }
    }

    private AbonoResponseDto toResponseDto(Abono abono) {
        return new AbonoResponseDto(
                abono.getId(),
                abono.getMonto(),
                abono.getFechaPago(),
                abono.getRecargoMora(),
                abono.getPlanPago().getId()
        );
    }
}
