package com.inventory.inventory_management_system.database.entity;
import jakarta.persistence.*;
@Entity
@Table(name = "products")
public class Product{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long productId;
    private String productname;
    private String category;
    private Double price;
    private Long supplierId;

    public Long getProductId(){
        return productId;
    }
    public void setProductId(Long productId){
        this.productId = productId;
    }
    public String getProductname(){
        return productname;
    }
    public void setProductname(String productname){
        this.productname = productname;
    }
    public String getCategory(){
        return category;
    }
    public void setCategory(String category){
        this.category = category;
    }
    public Double getPrice(){
        return price;
    }
    public void setPrice(Double price){
        this.price = price;
    }
    public Long getSupplierId(){
        return supplierId;
    }
    public void setSupplierId(Long supplierId){
        this.supplierId = supplierId;
    }
}


