package com.inventory.model;

import java.sql.Timestamp;

public class Transaction {
    private int id;
    private int totalProducts;
    private double totalPrice;
    private String transactionType;
    private String transactionStatus;
    private String description;
    private String note;
    private Timestamp updatedAt;
    private Timestamp createdAt;
    private int productId;
    private int userId;
    private int supplierId;

    public Transaction() {}
    public Transaction(int totalProducts, double totalPrice,
                       String transactionType, String transactionStatus,
                       String description, String note,
                       int productId, int userId, int supplierId) {
        this.totalProducts = totalProducts;
        this.totalPrice = totalPrice;
        this.transactionType = transactionType;
        this.transactionStatus = transactionStatus;
        this.description = description;
        this.note = note;
        this.productId = productId;
        this.userId = userId;
        this.supplierId = supplierId;
    }

    public int getId()                         { return id; }
    public void setId(int id)                  { this.id = id; }
    public int getTotalProducts()              { return totalProducts; }
    public void setTotalProducts(int tp)       { this.totalProducts = tp; }
    public double getTotalPrice()              { return totalPrice; }
    public void setTotalPrice(double tp)       { this.totalPrice = tp; }
    public String getTransactionType()         { return transactionType; }
    public void setTransactionType(String t)   { this.transactionType = t; }
    public String getTransactionStatus()       { return transactionStatus; }
    public void setTransactionStatus(String s) { this.transactionStatus = s; }
    public String getDescription()             { return description; }
    public void setDescription(String d)       { this.description = d; }
    public String getNote()                    { return note; }
    public void setNote(String note)           { this.note = note; }
    public Timestamp getUpdatedAt()            { return updatedAt; }
    public void setUpdatedAt(Timestamp t)      { this.updatedAt = t; }
    public Timestamp getCreatedAt()            { return createdAt; }
    public void setCreatedAt(Timestamp t)      { this.createdAt = t; }
    public int getProductId()                  { return productId; }
    public void setProductId(int pid)          { this.productId = pid; }
    public int getUserId()                     { return userId; }
    public void setUserId(int uid)             { this.userId = uid; }
    public int getSupplierId()                 { return supplierId; }
    public void setSupplierId(int sid)         { this.supplierId = sid; }

    @Override
    public String toString() {
        return "Transaction{id=" + id + ", type='" + transactionType +
                "', status='" + transactionStatus + "', total=" + totalPrice + "}";
    }
}