package com.server.app.controllers;

import com.server.app.dto.activo.ActivoCreateDto;
import com.server.app.dto.activo.ActivoResponseDto;
import com.server.app.dto.inversion.InversionCreateDto;
import com.server.app.dto.inversion.InversionResponseDto;
import com.server.app.dto.portafolio.PortafolioCreateDto;
import com.server.app.dto.portafolio.PortafolioResponseDto;
import com.server.app.dto.portafolio.RendimientoResponseDto;
import com.server.app.dto.response.Pagination;
import com.server.app.entities.User;
import com.server.app.services.ActivoService;
import com.server.app.services.InversionService;
import com.server.app.services.PortafolioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/finanzas")
public class FinanzasController {

    private final PortafolioService portafolioService;
    private final ActivoService activoService;
    private final InversionService inversionService;

    public FinanzasController(
            PortafolioService portafolioService,
            ActivoService activoService,
            InversionService inversionService
    ) {
        this.portafolioService = portafolioService;
        this.activoService = activoService;
        this.inversionService = inversionService;
    }

    @GetMapping("/portafolios")
    public ResponseEntity<Pagination<PortafolioResponseDto>> getPortafolios(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                Pagination.from(
                        portafolioService.findAllByUser(
                                user.getId(),
                                page,
                                size
                        )
                )
        );
    }

    @PostMapping("/portafolios")
    public ResponseEntity<PortafolioResponseDto> createPortafolio(
            Authentication authentication,
            @Valid @RequestBody PortafolioCreateDto dto
    ) {
        User user = (User) authentication.getPrincipal();

        PortafolioResponseDto response =
                portafolioService.create(
                        user.getId(),
                        dto
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/activos")
    public ResponseEntity<Pagination<ActivoResponseDto>> getActivos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                Pagination.from(
                        activoService.findAll(page, size)
                )
        );
    }

    @PostMapping("/activos")
    public ResponseEntity<ActivoResponseDto> createActivo(
            @Valid @RequestBody ActivoCreateDto dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(activoService.create(dto));
    }

    @PostMapping("/inversiones")
    public ResponseEntity<InversionResponseDto> createInversion(
            Authentication authentication,
            @Valid @RequestBody InversionCreateDto dto
    ) {
        User user = (User) authentication.getPrincipal();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        inversionService.create(
                                user.getId(),
                                dto
                        )
                );
    }

    @GetMapping("/portafolios/{id}/rendimiento")
    public ResponseEntity<RendimientoResponseDto> getRendimiento(
            Authentication authentication,
            @PathVariable Long id
    ) {
        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                portafolioService.getRendimiento(
                        id,
                        user.getId()
                )
        );
    }
}