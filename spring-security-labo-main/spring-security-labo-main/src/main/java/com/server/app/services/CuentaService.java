package com.server.app.services;

import com.server.app.dto.cuenta.CuentaCreateDto;
import com.server.app.dto.cuenta.CuentaResponseDto;
import com.server.app.entities.Cuenta;
import com.server.app.entities.User;
import com.server.app.exceptions.NotFoundException;
import com.server.app.repositories.CuentaRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class CuentaService {

    private final CuentaRepository cuentaRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public Page<CuentaResponseDto> findAllByUser(
            int userId,
            int page,
            int size
    ) {
        return cuentaRepository
                .findAllByUsuarioId(
                        userId,
                        PageRequest.of(page, size)
                )
                .map(this::toResponseDto);
    }

    @Transactional
    public CuentaResponseDto create(
            int userId,
            CuentaCreateDto dto
    ) {
        User user = userService.findById(userId);

        Cuenta cuenta = new Cuenta();
        cuenta.setAlias(dto.getAlias());
        cuenta.setMoneda(dto.getMoneda());
        cuenta.setTipo(dto.getTipo());
        cuenta.setSaldoBase(BigDecimal.ZERO);
        cuenta.setUsuario(user);

        return toResponseDto(
                cuentaRepository.save(cuenta)
        );
    }

    @Transactional(readOnly = true)
    public Cuenta findEntityByIdAndUser(
            Long cuentaId,
            int userId
    ) {
        return cuentaRepository
                .findByIdAndUsuarioId(cuentaId, userId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Cuenta no encontrada"
                        )
                );
    }

    @Transactional(readOnly = true)
    public Cuenta findEntityById(Long cuentaId) {
        return cuentaRepository
                .findById(cuentaId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Cuenta no encontrada"
                        )
                );
    }

    private CuentaResponseDto toResponseDto(Cuenta cuenta) {
        return new CuentaResponseDto(
                cuenta.getId(),
                cuenta.getAlias(),
                cuenta.getMoneda(),
                cuenta.getSaldoBase(),
                cuenta.getTipo()
        );
    }
}
