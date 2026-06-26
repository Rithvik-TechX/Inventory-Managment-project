package com.inventory.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Product {
    private int id;
    private String name;
    private String sku;
    private double price;
    private int stockQuantity;
    private String description;
    private Date expiryDate;
    private String imageUrl;
    private Timestamp createdAt;
    private int categoryId;

    public Product() {}
    public Product(String name, String sku, double price, int stockQuantity,
                   String description, Date expiryDate,
                   String imageUrl, int categoryId) {
        this.name = name;
        this.sku = sku;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.description = description;
        this.expiryDate = expiryDate;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
    }

    public int getId()                    { return id; }
    public void setId(int id)             { this.id = id; }
    public String getName()               { return name; }
    public void setName(String name)      { this.name = name; }
    public String getSku()                { return sku; }
    public void setSku(String sku)        { this.sku = sku; }
    public double getPrice()              { return price; }
    public void setPrice(double price)    { this.price = price; }
    public int getStockQuantity()         { return stockQuantity; }
    public void setStockQuantity(int qty) { this.stockQuantity = qty; }
    public String getDescription()        { return description; }
    public void setDescription(String d)  { this.description = d; }
    public Date getExpiryDate()           { return expiryDate; }
    public void setExpiryDate(Date d)     { this.expiryDate = d; }
    public String getImageUrl()           { return imageUrl; }
    public void setImageUrl(String url)   { this.imageUrl = url; }
    public Timestamp getCreatedAt()       { return createdAt; }
    public void setCreatedAt(Timestamp t) { this.createdAt = t; }
    public int getCategoryId()            { return categoryId; }
    public void setCategoryId(int cid)    { this.categoryId = cid; }

    @Override
    public String toString() {
        return "Product{id=" + id + ", name='" + name + "', sku='" + sku +
                "', stock=" + stockQuantity + ", price=" + price + "}";
    }
}