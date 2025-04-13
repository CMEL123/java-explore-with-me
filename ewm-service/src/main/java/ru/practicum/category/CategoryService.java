package ru.practicum.category;


import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.event.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.DuplicatedDataException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Transactional
    public CategoryDto create(NewCategoryDto categoryDto) {
        checkName(categoryDto.getName());

        Category category = CategoryMapper.toCategory(categoryDto);
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Transactional
    public CategoryDto update(NewCategoryDto categoryDto, Long id) {
        Category oldCategory = findById(id);
        if (!oldCategory.getName().equals(categoryDto.getName())){
            checkName(categoryDto.getName());
        }
        Category category = CategoryMapper.toCategory(categoryDto, id);
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Transactional
    public void delete(Long id) {
        if (eventRepository.existsByCategoryId(id)) {
            throw new ConflictException("Попытка удалить категорию с привязанными событиями");
        }
        categoryRepository.deleteById(id);
        log.info("Категория с id = {}  - удален", id);
    }

    public Category findById(Long id) {
        Optional<Category> category =  categoryRepository.findById(id);
        if  (category.isPresent()) {
            log.info("Категория c id = {} найден", id);
            return category.get();
        }
        log.warn("Категория с id = {} не найден", id);
        throw new NotFoundException(String.format("Category with id=%d was not found", id));
    }

    public List<CategoryDto> findAll(int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Category> categories = categoryRepository.findAll(pageable).getContent();

        log.info("Получено {} категорий", categories.size());

        return categories.stream()
                .map(CategoryMapper::toCategoryDto)
                .toList();
    }

    public CategoryDto findByCatId(long catId) {
        return CategoryMapper.toCategoryDto(findById(catId));
    }

    private void checkName(String currName) {

        if (currName.isBlank()) {
            throw new ValidationException("Field: name. Error: must not be blank. Value: null");
        }

        if (categoryRepository.findByName(currName).isPresent()) {
            throw new DuplicatedDataException("Field: name. Error: The name is not unique");
        }
    }

}
