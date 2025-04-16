package ru.practicum.user;

import org.mapstruct.Mapper;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserShortDto;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);

    User toUser(UserDto userDto);

    UserShortDto toUserShortDto(User user);
}
