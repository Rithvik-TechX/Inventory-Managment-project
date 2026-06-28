package com.inventory.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="suppliers", indexes=@Index(name="idx_supplier_name", columnList="name"))
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler"}, ignoreUnknown=true)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Supplier {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @NotBlank @Column(nullable=false,length=150) private String name;
    @Column(name="contact_person",length=150) private String contactPerson;
    @Column(length=150) private String email;
    @Column(length=30) private String phone;
    @Column(columnDefinition="TEXT") private String address;
    @Builder.Default @Column(nullable=false) private Boolean active=true;
    @CreationTimestamp @Column(name="created_at",updatable=false) private LocalDateTime createdAt;
    @JsonIgnore @OneToMany(mappedBy="supplier",fetch=FetchType.LAZY) @Builder.Default private List<Product> products=new ArrayList<>();
    @JsonIgnore @OneToMany(mappedBy="supplier",fetch=FetchType.LAZY) @Builder.Default private List<Transaction> transactions=new ArrayList<>();
}
