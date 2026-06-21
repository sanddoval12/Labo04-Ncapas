package com.server.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimientos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal monto;

    @Column(
            name = "moneda_original",
            nullable = false,
            length = 10
    )
    private String monedaOriginal;

    @Column(
            name = "tasa_cambio",
            nullable = false,
            precision = 19,
            scale = 6
    )
    private BigDecimal tasaCambio = BigDecimal.ONE;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(length = 255)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cuenta_id", nullable = false)
    @JsonIgnore
    private Cuenta cuenta;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;
    
    @Column(name = "transferencia_id")
    private String transferenciaId;

    @PrePersist
    public void prePersist() {
        if (fecha == null) {
            fecha = LocalDateTime.now();
        }

        if (tasaCambio == null) {
            tasaCambio = BigDecimal.ONE;
        }
    }
}
