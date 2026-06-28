package com.inventory.dto;

import com.inventory.entity.Category;
import java.time.LocalDateTime;

public class CategoryDTO {
    private Long id; private String name; private String description; private boolean active;
    private LocalDateTime createdAt; private long productCount;
    public CategoryDTO() {}
    public CategoryDTO(Category category,long productCount){this.id=category.getId();this.name=category.getName();this.description=category.getDescription();this.active=Boolean.TRUE.equals(category.getActive());this.createdAt=category.getCreatedAt();this.productCount=productCount;}
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getName(){return name;} public void setName(String name){this.name=name;}
    public String getDescription(){return description;} public void setDescription(String description){this.description=description;}
    public boolean isActive(){return active;} public void setActive(boolean active){this.active=active;}
    public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime value){this.createdAt=value;}
    public long getProductCount(){return productCount;} public void setProductCount(long count){this.productCount=count;}
}
