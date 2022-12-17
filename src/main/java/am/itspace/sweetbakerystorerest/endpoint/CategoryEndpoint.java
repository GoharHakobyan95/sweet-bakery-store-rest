package am.itspace.sweetbakerystorerest.endpoint;

import am.itspace.sweetbakerystorecommon.dto.categoryDto.CategoryResponseDto;
import am.itspace.sweetbakerystorecommon.dto.categoryDto.CreateCategoryDto;
import am.itspace.sweetbakerystorecommon.dto.categoryDto.UpdateCategoryDto;
import am.itspace.sweetbakerystorecommon.entity.Category;
import am.itspace.sweetbakerystorecommon.security.CurrentUser;
import am.itspace.sweetbakerystorecommon.service.CategoryService;
import am.itspace.sweetbakerystorerest.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/categories")
public class CategoryEndpoint {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public List<CategoryResponseDto> findAllCategories(@PageableDefault(size = 9) Pageable pageable,
                                                       @AuthenticationPrincipal CurrentUser currentUser) {

        Page<Category> paginated = categoryService.findPaginated(pageable);
        log.info("Endpoint /api/categories called by {}", currentUser.getUser().getEmail());
        return paginated.stream().map(categoryMapper::map).toList();
    }

    @GetMapping("/{id}")
    public CategoryResponseDto getCategoryById(@PathVariable("id") int id,
                                               @AuthenticationPrincipal CurrentUser currentUser
    ) {
        Category category = categoryService.findByCategoryId(id);
        log.info("Endpoint /api/categories called by {}", currentUser.getUser().getEmail());
        return categoryMapper.map(category);
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(@RequestBody CreateCategoryDto createCategoryDto,
                                                   @AuthenticationPrincipal CurrentUser currentUser) {
        Category category = categoryMapper.map(createCategoryDto);
        category.setUser(currentUser.getUser());
        Category savedCategory = categoryService.saveCategory(category);
        log.info("Endpoint /api/categories created by {}", currentUser.getUser().getEmail());
        return ResponseEntity.status(CREATED).body(categoryMapper.map(savedCategory));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@RequestBody UpdateCategoryDto updateCategoryDto,
                                                   @PathVariable("id") int id,
                                                   @AuthenticationPrincipal CurrentUser currentUser) {
        Category category = categoryMapper.map(updateCategoryDto);
        category.setUser(currentUser.getUser());
        categoryService.saveCategory(category);
        Optional<Category> categoryById = categoryService.findById(id);

        if (categoryById.isPresent() && currentUser.getUser().getId() == categoryById.get().getUser().getId()) {
            log.info("Endpoint /api/categories updated by {}", currentUser.getUser().getEmail());
            return ResponseEntity.ok(categoryService.saveCategory(categoryMapper.map(updateCategoryDto)));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> deleteCategory(@PathVariable("id") int id,
                                            @AuthenticationPrincipal CurrentUser currentUser) {
        categoryService.deleteById(id, currentUser);
        log.info("Endpoint ipa/categories deleted by {}", currentUser.getUser().getEmail());
        return ResponseEntity.noContent().build();
    }

}
