package am.itspace.sweetbakerystorerest.service;

import am.itspace.sweetbakerystorecommon.entity.Category;
import am.itspace.sweetbakerystorecommon.entity.User;
import am.itspace.sweetbakerystorecommon.repository.CategoryRepository;
import am.itspace.sweetbakerystorecommon.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @MockBean
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryService categoryService;


    // JUnit test for saveCategory method
    @Test
    void saveCategory() {
        Category category = Category.builder()
                .id(1)
                .name("bulki")
                .description("chamichov")
                .user(new User())
                .build();
        when(categoryRepository.save(any())).thenReturn(category);

        categoryService.saveCategory(Category.builder()
                .name("bulki")
                .description("chamichov")
                .user(new User())
                .build());
        verify(categoryRepository, times(1)).save(any());
    }

    @Test
    void saveNull() {
        Category category = Category.builder()
                .id(1)
                .name("bulki")
                .description("chamichov")
                .user(new User())
                .build();
        when(categoryRepository.save(any())).thenReturn(category);

        assertThrows(RuntimeException.class, () -> {
            categoryService.saveCategory(null);
        });

        verify(categoryRepository, times(0)).save(any());
    }

}






