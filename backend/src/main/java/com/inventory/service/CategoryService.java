package com.inventory.service;

import com.inventory.dto.CategoryDTO;
import com.inventory.entity.Category;
import com.inventory.repository.CategoryRepository;
import com.inventory.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service @Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository; private final ProductRepository productRepository;
    public CategoryService(CategoryRepository categoryRepository,ProductRepository productRepository){this.categoryRepository=categoryRepository;this.productRepository=productRepository;}
    @Transactional(readOnly=true) public List<CategoryDTO> getAllCategories(){return categoryRepository.findAll().stream().map(c->new CategoryDTO(c,productRepository.countByCategoryId(c.getId()))).toList();}
    @Transactional(readOnly=true) public Category getCategoryById(Long id){return categoryRepository.findById(id).orElseThrow(()->new RuntimeException("Category not found with id: "+id));}
    public Category createCategory(Category category){if(category.getName()==null||category.getName().isBlank())throw new RuntimeException("Category name is required");if(categoryRepository.existsByName(category.getName()))throw new RuntimeException("Category with name '"+category.getName()+"' already exists");return categoryRepository.save(category);}
    public Category updateCategory(Long id,Category details){Category category=getCategoryById(id);if(details.getName()!=null)category.setName(details.getName());if(details.getDescription()!=null)category.setDescription(details.getDescription());if(details.getActive()!=null)category.setActive(details.getActive());return categoryRepository.save(category);}
    public void deleteCategory(Long id){categoryRepository.delete(getCategoryById(id));}
}
