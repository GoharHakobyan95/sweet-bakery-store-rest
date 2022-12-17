package am.itspace.sweetbakerystorerest.mapper;

import am.itspace.sweetbakerystorecommon.dto.addressDto.AddressResponseDto;
import am.itspace.sweetbakerystorecommon.dto.addressDto.CreateAddressDto;
import am.itspace.sweetbakerystorecommon.dto.addressDto.UpdateAddressDto;
import am.itspace.sweetbakerystorecommon.entity.Address;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    Address map(CreateAddressDto createAddressDto);

    AddressResponseDto map(Address address);

    List<AddressResponseDto> map(List<Address> addressList);

    Address map(UpdateAddressDto updateAddressDto);
}
