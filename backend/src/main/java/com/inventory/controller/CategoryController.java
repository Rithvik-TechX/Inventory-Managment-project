package com.inventory.controller;

import com.inventory.dto.CategoryDTO;
import com.inventory.entity.Category;
import com.inventory.service.CategoryService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService){this.categoryService=categoryService;}
    @GetMapping public ResponseEntity<List<CategoryDTO>> all(){return ResponseEntity.ok(categoryService.getAllCategories());}
    @GetMapping("/{id}") public ResponseEntity<Category> one(@PathVariable Long id){return ResponseEntity.ok(categoryService.getCategoryById(id));}
    @PostMapping public ResponseEntity<Category> create(@RequestBody Category category){return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(category));}
    @PutMapping("/{id}") public ResponseEntity<Category> update(@PathVariable Long id,@RequestBody Category category){return ResponseEntity.ok(categoryService.updateCategory(id,category));}
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id){categoryService.deleteCategory(id);return ResponseEntity.noContent().build();}
}
