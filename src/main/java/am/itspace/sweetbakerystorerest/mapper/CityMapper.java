package am.itspace.sweetbakerystorerest.mapper;

import am.itspace.sweetbakerystorecommon.dto.cityDto.CityResponseDto;
import am.itspace.sweetbakerystorecommon.dto.cityDto.CreateCityDto;
import am.itspace.sweetbakerystorecommon.dto.cityDto.UpdateCityDto;
import am.itspace.sweetbakerystorecommon.entity.City;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CityMapper {

    City map(CreateCityDto createCityDto);

    List<City> map(List<CityResponseDto> allCities);

    CityResponseDto map(City city);

    City map(UpdateCityDto updateCityDto);
}
