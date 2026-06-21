package com.server.app.services;

import com.server.app.dto.portafolio.PortafolioCreateDto;
import com.server.app.dto.portafolio.PortafolioResponseDto;
import com.server.app.dto.portafolio.RendimientoResponseDto;
import com.server.app.entities.Inversion;
import com.server.app.entities.Portafolio;
import com.server.app.entities.User;
import com.server.app.entities.enums.EstadoInversion;
import com.server.app.exceptions.NotFoundException;
import com.server.app.repositories.InversionRepository;
import com.server.app.repositories.PortafolioRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@AllArgsConstructor
public class PortafolioService {

    private final PortafolioRepository portafolioRepository;
    private final InversionRepository inversionRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public Page<PortafolioResponseDto> findAllByUser(
            int userId,
            int page,
            int size
    ) {
        return portafolioRepository
                .findAllByUsuarioId(
                        userId,
                        PageRequest.of(page, size)
                )
                .map(this::toResponseDto);
    }

    @Transactional
    public PortafolioResponseDto create(
            int userId,
            PortafolioCreateDto dto
    ) {
        User user = userService.findById(userId);

        Portafolio portafolio = new Portafolio();
        portafolio.setNombre(dto.getNombre());
        portafolio.setRiesgoPerfil(dto.getRiesgoPerfil());
        portafolio.setBalanceTotal(BigDecimal.ZERO);
        portafolio.setUsuario(user);

        return toResponseDto(
                portafolioRepository.save(portafolio)
        );
    }

    @Transactional(readOnly = true)
    public Portafolio findEntityByIdAndUser(
            Long portafolioId,
            int userId
    ) {
        return portafolioRepository
                .findByIdAndUsuarioId(portafolioId, userId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Portafolio no encontrado"
                        )
                );
    }

    @Transactional(readOnly = true)
    public RendimientoResponseDto getRendimiento(
            Long portafolioId,
            int userId
    ) {
        Portafolio portafolio =
                findEntityByIdAndUser(portafolioId, userId);

        List<Inversion> inversiones =
                inversionRepository
                        .findAllByPortafolioIdAndEstado(
                                portafolioId,
                                EstadoInversion.ABIERTA
                        );

        BigDecimal capitalInvertido = BigDecimal.ZERO;
        BigDecimal valorActual = BigDecimal.ZERO;

        for (Inversion inversion : inversiones) {
            BigDecimal costoCompra =
                    inversion.getCantidad()
                            .multiply(inversion.getPrecioCompra());

            BigDecimal valorMercado =
                    inversion.getCantidad()
                            .multiply(
                                    inversion.getActivo()
                                            .getPrecioMercado()
                            );

            capitalInvertido =
                    capitalInvertido.add(costoCompra);

            valorActual =
                    valorActual.add(valorMercado);
        }

        BigDecimal gananciaPerdida =
                valorActual.subtract(capitalInvertido);

        BigDecimal porcentaje = BigDecimal.ZERO;

        if (capitalInvertido.compareTo(BigDecimal.ZERO) > 0) {
            porcentaje = gananciaPerdida
                    .divide(
                            capitalInvertido,
                            6,
                            RoundingMode.HALF_UP
                    )
                    .multiply(BigDecimal.valueOf(100));
        }

        return new RendimientoResponseDto(
                portafolio.getId(),
                portafolio.getNombre(),
                capitalInvertido.setScale(
                        2,
                        RoundingMode.HALF_UP
                ),
                valorActual.setScale(
                        2,
                        RoundingMode.HALF_UP
                ),
                gananciaPerdida.setScale(
                        2,
                        RoundingMode.HALF_UP
                ),
                porcentaje.setScale(
                        2,
                        RoundingMode.HALF_UP
                )
        );
    }

    private PortafolioResponseDto toResponseDto(
            Portafolio portafolio
    ) {
        return new PortafolioResponseDto(
                portafolio.getId(),
                portafolio.getNombre(),
                portafolio.getBalanceTotal(),
                portafolio.getRiesgoPerfil()
        );
    }
}