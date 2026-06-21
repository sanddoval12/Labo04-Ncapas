package com.server.app.controllers;

import com.server.app.dto.categoria.CategoriaResponseDto;
import com.server.app.dto.cuenta.CuentaCreateDto;
import com.server.app.dto.cuenta.CuentaResponseDto;
import com.server.app.dto.movimiento.MovimientoResponseDto;
import com.server.app.dto.response.Pagination;
import com.server.app.dto.transferencia.TransferenciaCreateDto;
import com.server.app.dto.transferencia.TransferenciaResponseDto;
import com.server.app.entities.User;
import com.server.app.services.CategoriaService;
import com.server.app.services.CuentaService;
import com.server.app.services.MovimientoService;
import com.server.app.services.TransferenciaService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/finanzas")
public class CuentasController {

    private final CuentaService cuentaService;
    private final MovimientoService movimientoService;
    private final TransferenciaService transferenciaService;
    private final CategoriaService categoriaService;

    public CuentasController(
            CuentaService cuentaService,
            MovimientoService movimientoService,
            TransferenciaService transferenciaService,
            CategoriaService categoriaService
    ) {
        this.cuentaService = cuentaService;
        this.movimientoService = movimientoService;
        this.transferenciaService = transferenciaService;
        this.categoriaService = categoriaService;
    }

    @GetMapping("/cuentas")
    public ResponseEntity<Pagination<CuentaResponseDto>> getCuentas(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                Pagination.from(
                        cuentaService.findAllByUser(
                                user.getId(),
                                page,
                                size
                        )
                )
        );
    }

    @PostMapping("/cuentas")
    public ResponseEntity<CuentaResponseDto> createCuenta(
            Authentication authentication,
            @Valid @RequestBody CuentaCreateDto dto
    ) {
        User user = (User) authentication.getPrincipal();

        CuentaResponseDto response =
                cuentaService.create(
                        user.getId(),
                        dto
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/movimientos")
    public ResponseEntity<Pagination<MovimientoResponseDto>> getMovimientos(
            Authentication authentication,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime desde,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime hasta,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                Pagination.from(
                        movimientoService.findAllByUser(
                                user.getId(),
                                desde,
                                hasta,
                                page,
                                size
                        )
                )
        );
    }

    @PostMapping("/transferencias")
    public ResponseEntity<TransferenciaResponseDto> createTransferencia(
            Authentication authentication,
            @Valid @RequestBody TransferenciaCreateDto dto
    ) {
        User user = (User) authentication.getPrincipal();

        TransferenciaResponseDto response =
                transferenciaService.realizarTransferencia(
                        user.getId(),
                        dto
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/categorias")
    public ResponseEntity<Pagination<CategoriaResponseDto>> getCategorias(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                Pagination.from(
                        categoriaService.findAll(page, size)
                )
        );
    }
}
