package am.itspace.sweetbakerystorerest.service;

import am.itspace.sweetbakerystorecommon.entity.*;
import am.itspace.sweetbakerystorecommon.repository.CategoryRepository;
import am.itspace.sweetbakerystorecommon.security.CurrentUser;
import am.itspace.sweetbakerystorecommon.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    private Category category;


    @BeforeEach
    public void createCategory() {
        //save user with Admin Role
        User admin = User.builder()
                .id(2)
                .name("John")
                .surname("Smith")
                .role(Role.ADMIN)
                .createAt(new Date())
                .isActive(true)
                .email("admin@mail.ru")
                .password("Admin555$")
                .phone("88888888")
                .verifyToken("%token555$")
                .address(new Address(2, "5 building", new City(1, "Yerevan")))
                .build();
        //create category by admin
        category = Category.builder()
                .id(1)
                .name("Cakes")
                .description("With chocolate")
                .user(admin)
                .build();
    }

    @Test
    void shouldFindByName() {
        when(categoryRepository.findByName(category.getName())).thenReturn(Optional.of(category));
        Optional<Category> cakes = categoryService.findByName("Cakes");
        assertTrue(cakes.isPresent());
        assertEquals(cakes.get().getName(), "Cakes");
    }

    @Test
    void shouldDeleteCategoryById() {
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        assertEquals(category.getId(), 1);
        Optional<Category> byIdForEdit = categoryService.findByIdForEdit(1);
        assertTrue(byIdForEdit.isPresent());
        categoryService.deleteById(byIdForEdit.get().getId(), category.getUser() );
        assertEquals(0, categoryRepository.count());
        verify(categoryRepository, times(1)).deleteById(category.getId());
    }

    @Test
    void shouldSave() throws Exception {
        CurrentUser currentUser = new CurrentUser(category.getUser());
        Category category2 = Category.builder()
                .id(2)
                .name("Classic")
                .description("With chocolate")
                .user(currentUser.getUser())
                .build();
        when(categoryRepository.save(any())).thenReturn(category2);
        categoryService.save(category2, currentUser.getUser());

        when(categoryRepository.findById(2)).thenReturn(Optional.of(category));
        Optional<Category> catById = categoryService.findByIdForEdit(2);
        assertTrue(catById.isPresent());
    }

    @Test
    void shouldFindAll() {
        when(categoryRepository.findAll()).thenReturn(List.of(category));
        List<Category> allCategories = categoryService.findAll();
        assertEquals(1, allCategories.size());
        assertFalse(allCategories.isEmpty());
    }

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






