package am.itspace.sweetbakerystorerest.mapper;

import am.itspace.sweetbakerystorecommon.dto.productDto.CreateProductDto;
import am.itspace.sweetbakerystorecommon.dto.productDto.ProductResponseDto;
import am.itspace.sweetbakerystorecommon.dto.productDto.UpdateProductDto;
import am.itspace.sweetbakerystorecommon.entity.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product map(CreateProductDto createProductDto);

    ProductResponseDto map(Product product);

    List<ProductResponseDto> map(List<Product> productList);

    Product map(UpdateProductDto updateProductDto);
}
