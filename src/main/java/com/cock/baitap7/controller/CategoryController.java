package com.cock.baitap7.controller;

import com.cock.baitap7.entity.Category;
import com.cock.baitap7.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categories")
public class CategoryController {


    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String list(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Category> pageResult = categoryService.listCategories(q, pageable);

        model.addAttribute("categories", pageResult.getContent());
        model.addAttribute("currentPage", pageResult.getNumber()); // zero-based
        model.addAttribute("totalPages", pageResult.getTotalPages());
        model.addAttribute("q", q);
        return "category/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("category", new Category());
        return "category/create";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Category c = categoryService.getCategory(id);
        if (c == null) {
            ra.addFlashAttribute("error", "Không tìm thấy danh mục");
            return "redirect:/categories";
        }
        model.addAttribute("category", c);
        return "category/edit";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("category") Category category,
                       BindingResult br,
                       RedirectAttributes ra) {
        if (br.hasErrors()) {
            return category.getId() == null ? "category/create" : "category/edit";
        }
        categoryService.saveCategory(category);
        ra.addFlashAttribute("success", "Lưu danh mục thành công");
        return "redirect:/categories";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        categoryService.deleteCategory(id);
        ra.addFlashAttribute("success", "Xoá danh mục thành công");
        return "redirect:/categories";
    }
    @GetMapping("/view/{id}")
    public String viewCategory(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Category category = categoryService.getCategory(id);
        if (category == null) {
            ra.addFlashAttribute("error", "Không tìm thấy danh mục");
            return "redirect:/categories";
        }
        model.addAttribute("category", category);
        return "category/view"; // make sure you have category/view.html
    }
}
