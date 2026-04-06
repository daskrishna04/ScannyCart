package com.krishna.scan.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "pdt_seq")
    @SequenceGenerator(name = "pdt_seq",initialValue = 301,allocationSize = 1)
    private int id;

    private String name;

    @Column(unique = true)
    private String barcode;

    private BigDecimal price;

    private int stock;

    @CreationTimestamp
    private LocalDateTime created_at;

}
