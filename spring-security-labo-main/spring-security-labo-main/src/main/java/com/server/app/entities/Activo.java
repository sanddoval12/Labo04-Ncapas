package com.server.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "activos",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_activo_simbolo",
                        columnNames = "simbolo"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Activo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String simbolo;

    @Column(nullable = false, length = 100)
    private String mercado;

    @Column(
            name = "precio_mercado",
            nullable = false,
            precision = 19,
            scale = 4
    )
    private BigDecimal precioMercado;

    @Column(nullable = false, length = 100)
    private String sector;

    @OneToMany(mappedBy = "activo")
    @JsonIgnore
    private List<Inversion> inversiones = new ArrayList<>();
}