package com.cock.baitap7.service;

import com.cock.baitap7.entity.Category;
import com.cock.baitap7.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
        @Autowired
        private CategoryRepository repository;

    public Page<Category> listCategories(String keyword, Pageable pageable) {
        if (keyword != null && !keyword.isEmpty()) {
            return repository.findByNameContainingIgnoreCase(keyword, pageable);
        }
        return repository.findAll(pageable);
    }

    public Category getCategory(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Category saveCategory(Category category) {
        return repository.save(category);
    }

    public void deleteCategory(Long id) {
        repository.deleteById(id);
    }
}
