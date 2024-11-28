package com.upiiz.examen.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;


@AllArgsConstructor
@NoArgsConstructor
@Data // Para los gets y sets (no se ven pero si funcionan)
@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long order_id;
    private Date order_date;
    private Long customer_id;
    private Float total;
}
