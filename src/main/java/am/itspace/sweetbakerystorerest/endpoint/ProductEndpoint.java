package am.itspace.sweetbakerystorerest.endpoint;

import am.itspace.sweetbakerystorecommon.dto.productDto.CreateProductDto;
import am.itspace.sweetbakerystorecommon.dto.productDto.ProductResponseDto;
import am.itspace.sweetbakerystorecommon.dto.productDto.UpdateProductDto;

import am.itspace.sweetbakerystorecommon.entity.Product;
import am.itspace.sweetbakerystorecommon.security.CurrentUser;
import am.itspace.sweetbakerystorecommon.service.ProductService;
import am.itspace.sweetbakerystorerest.mapper.ProductMapper;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/products")
public class ProductEndpoint {

    private final ProductService productService;
    private final ProductMapper productMapper;


    @GetMapping
    public List<ProductResponseDto> findAllCategories(@PageableDefault(size = 9) Pageable pageable,
                                                      @AuthenticationPrincipal CurrentUser currentUser) {
        Page<Product> paginated = productService.findPaginated(pageable);
        log.info("Endpoint /api/products called by {}", currentUser.getUser().getEmail());
        return paginated.stream().map(productMapper::map).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable("id") int id,
                                            @AuthenticationPrincipal CurrentUser currentUser) {
        Optional<Product> byId = productService.findById(id);
        if (byId.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        log.info("Endpoint /api/products called by {}", currentUser.getUser().getEmail());
        return ResponseEntity.ok(productMapper.map(byId.get()));
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody CreateProductDto createProductDto,
                                                 @RequestParam("productImage") MultipartFile file,
                                                 @AuthenticationPrincipal CurrentUser currentUser) throws IOException {
        if (!file.isEmpty() && file.getSize() > 0) {
            return ResponseEntity.notFound().build();
        }
        Product savedProduct = productService.saveProduct(productMapper.map(createProductDto), file, currentUser.getUser());
        log.info("Endpoint /api/products created by {}", currentUser.getUser().getEmail());
        return ResponseEntity.ok(productMapper.map(savedProduct));
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@RequestBody UpdateProductDto updateProductDto,
                                                 @AuthenticationPrincipal CurrentUser currentUser,
                                                 @RequestParam("productImage") MultipartFile file,
                                                 @PathVariable("id") int id) throws IOException {
        if (!file.isEmpty() && file.getSize() > 0) {
            return ResponseEntity.notFound().build();
        }
        Optional<Product> productById = productService.findById(id);
        if (currentUser.getUser().getId() == productById.get().getUser().getId()) {
            return ResponseEntity.ok((productService.saveProduct(productMapper.map(updateProductDto), file, currentUser.getUser())));
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ProductResponseDto> deleteProduct(@PathVariable("id") int id,
                                           @AuthenticationPrincipal CurrentUser currentUser) {
        productService.deleteProductById(id, currentUser);
        log.info("Endpoint /api/products deleted by {}", currentUser.getUser().getEmail());
        return ResponseEntity.noContent().build();
    }


}

