package am.itspace.sweetbakerystorerest.mapper;

import am.itspace.sweetbakerystorecommon.dto.categoryDto.CategoryResponseDto;
import am.itspace.sweetbakerystorecommon.dto.categoryDto.CreateCategoryDto;
import am.itspace.sweetbakerystorecommon.dto.categoryDto.UpdateCategoryDto;
import am.itspace.sweetbakerystorecommon.entity.Category;
import org.mapstruct.Mapper;


import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category map(CreateCategoryDto createCategoryDto);

    CategoryResponseDto map(Category category);

    List<CategoryResponseDto> map(List<Category> categoryList);

    Category map(UpdateCategoryDto updateCategoryDto);






}
