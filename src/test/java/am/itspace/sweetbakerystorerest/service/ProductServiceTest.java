package am.itspace.sweetbakerystorerest.service;

import am.itspace.sweetbakerystorecommon.entity.*;
import am.itspace.sweetbakerystorecommon.repository.ProductRepository;
import am.itspace.sweetbakerystorecommon.security.CurrentUser;
import am.itspace.sweetbakerystorecommon.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(Extension.class)
class ProductServiceTest {
    @InjectMocks
    private ProductService productService;
    @Mock
    private ProductRepository productRepository;
    private Product product;
    private Category category;
    @Mock
    private MultipartFile file;

    @BeforeEach
    public void createAdminAndCategory() {
        //save user with Admin Role and create Category by Admin
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
        category = Category.builder()
                .id(1)
                .name("Cakes")
                .description("With chocolate")
                .user(admin)
                .build();
    }

    @BeforeEach
    public void createProduct() {
        product = Product.builder()
                .id(1)
                .name("Cupcake")
                .description("With chocolate")
                .productPic(file.getContentType())
                .price(15)
                .inStore(5)
                .category(category)
                .user(category.getUser())
                .build();

    }

    @Test
    void shouldSaveProductByAdmin() throws IOException {
        CurrentUser currentUser = new CurrentUser(product.getUser());
        when(productRepository.save(any())).thenReturn(product);
        productService.saveProduct(product, file, currentUser.getUser());

        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        Optional<Product> byId = productService.findById(1);

        assertTrue(byId.isPresent());
        assertEquals(byId.get().getUser().getRole(), Role.ADMIN);
    }

    @Test
    void shouldDeleteById() {
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        assertEquals(product.getId(), 1);

        Optional<Product> productById = productService.findById(1);
        assertTrue(productById.isPresent());

        productService.deleteById(productById.get().getId());
        assertEquals(0, productRepository.count());
        verify(productRepository, times(1)).deleteById(category.getId());
    }


    @Test
    void shouldFindById() {
        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        Optional<Product> byId = productService.findById(1);
        assertTrue(byId.isPresent());
        assertNotNull(product);
    }

}