package com.server.app.controllers;

import com.server.app.dto.abono.AbonoCreateDto;
import com.server.app.dto.abono.AbonoResponseDto;
import com.server.app.dto.activo.ActivoCreateDto;
import com.server.app.dto.activo.ActivoResponseDto;
import com.server.app.dto.inversion.InversionCreateDto;
import com.server.app.dto.inversion.InversionResponseDto;
import com.server.app.dto.planpago.PlanPagoResponseDto;
import com.server.app.dto.portafolio.PortafolioCreateDto;
import com.server.app.dto.portafolio.PortafolioResponseDto;
import com.server.app.dto.portafolio.RendimientoResponseDto;
import com.server.app.dto.prestamo.PrestamoCreateDto;
import com.server.app.dto.prestamo.PrestamoResponseDto;
import com.server.app.dto.prestamo.ResumenCreditoResponseDto;
import com.server.app.dto.response.Pagination;
import com.server.app.entities.User;
import com.server.app.services.AbonoService;
import com.server.app.services.ActivoService;
import com.server.app.services.InversionService;
import com.server.app.services.PortafolioService;
import com.server.app.services.PrestamoService;
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
    private final PrestamoService prestamoService;
    private final AbonoService abonoService;

    public FinanzasController(
            PortafolioService portafolioService,
            ActivoService activoService,
            InversionService inversionService,
            PrestamoService prestamoService,
            AbonoService abonoService
    ) {
        this.portafolioService = portafolioService;
        this.activoService = activoService;
        this.inversionService = inversionService;
        this.prestamoService = prestamoService;
        this.abonoService = abonoService;
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

    @GetMapping("/prestamos")
    public ResponseEntity<Pagination<PrestamoResponseDto>> getPrestamos(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                Pagination.from(
                        prestamoService.findAllByUser(
                                user.getId(),
                                page,
                                size
                        )
                )
        );
    }

    @PostMapping("/prestamos")
    public ResponseEntity<PrestamoResponseDto> createPrestamo(
            Authentication authentication,
            @Valid @RequestBody PrestamoCreateDto dto
    ) {
        User user = (User) authentication.getPrincipal();

        PrestamoResponseDto response =
                prestamoService.create(
                        user.getId(),
                        dto
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/prestamos/{id}/planes-pago")
    public ResponseEntity<Pagination<PlanPagoResponseDto>> getPlanesPago(
            Authentication authentication,
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                Pagination.from(
                        prestamoService.getPlanesPagoPendientes(
                                id,
                                user.getId(),
                                page,
                                size
                        )
                )
        );
    }

    @PostMapping("/abonos")
    public ResponseEntity<AbonoResponseDto> createAbono(
            Authentication authentication,
            @Valid @RequestBody AbonoCreateDto dto
    ) {
        User user = (User) authentication.getPrincipal();

        AbonoResponseDto response =
                abonoService.registrarAbono(
                        user.getId(),
                        dto
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/resumen-credito")
    public ResponseEntity<ResumenCreditoResponseDto> getResumenCredito(
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                abonoService.getResumenCredito(user.getId())
        );
    }
}