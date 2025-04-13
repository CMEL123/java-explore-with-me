package ru.practicum.category;


import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

public class CategoryMapper {

    public static CategoryDto toCategoryDto(Category user) {
        return new CategoryDto(
                user.getId(),
                user.getName()
        );
    }

    public static Category toCategory(NewCategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        return category;
    }


    public static Category toCategory(NewCategoryDto categoryDto, long id) {
        Category category = toCategory(categoryDto);
        category.setId(id);
        return category;
    }
}
