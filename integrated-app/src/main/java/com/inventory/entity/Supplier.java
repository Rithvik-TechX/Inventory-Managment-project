package com.inventory.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "suppliers", indexes = {
    @Index(name = "idx_supplier_name", columnList = "name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Supplier name is required")
    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 200)
    private String contactInfo;

    @Column(length = 500)
    private String address;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    @JsonIgnore
    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Product> products = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Transaction> transactions = new ArrayList<>();
}
