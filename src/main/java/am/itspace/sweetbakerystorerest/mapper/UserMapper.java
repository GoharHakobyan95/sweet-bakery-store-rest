package am.itspace.sweetbakerystorerest.mapper;

import am.itspace.sweetbakerystorecommon.dto.userDto.CreateUserDto;
import am.itspace.sweetbakerystorecommon.dto.userDto.UserDto;
import am.itspace.sweetbakerystorecommon.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User map(CreateUserDto createUserDto);
    UserDto map(User user);






}
