package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.CategoryService;
import ru.practicum.category.dto.CategoryDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping()
    public List<CategoryDto> findAll(
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {
        return categoryService.findAll( from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto findByCatId(@PathVariable() Long catId) {
        return categoryService.findByCatId(catId);
    }

}
