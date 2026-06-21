package com.server.app.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "abonos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Abono {

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
            name = "fecha_pago",
            nullable = false
    )
    private LocalDate fechaPago;

    @Column(
            name = "recargo_mora",
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal recargoMora = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plan_pago_id", nullable = false)
    private PlanPago planPago;

    @PrePersist
    public void prePersist() {
        if (fechaPago == null) {
            fechaPago = LocalDate.now();
        }

        if (recargoMora == null) {
            recargoMora = BigDecimal.ZERO;
        }
    }
}
