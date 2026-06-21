package com.server.app.dto.transferencia;

import com.server.app.dto.movimiento.MovimientoResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TransferenciaResponseDto {

    private String transferenciaId;
    private MovimientoResponseDto movimientoEgreso;
    private MovimientoResponseDto movimientoIngreso;
}
