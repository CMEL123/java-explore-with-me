package ru.practicum.user;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.exception.DuplicatedDataException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.user.dto.UserDto;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    public List<UserDto> findAll( List<Integer> ids, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<User> users;

        if (ids == null || ids.isEmpty()) {
            users = userRepository.findAll(pageable).getContent();
        } else {
            users = userRepository.findByIdIn(ids, pageable).getContent();
        }
        log.info("Получено {} пользователей", users.size());
        return users.stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    @Transactional
    public UserDto create(UserDto userDto) {
        log.info("Добавление пользователя");
        if (userDto.getName().isBlank()) {
            throw new ValidationException("Field: name. Error: must not be blank. Value: null");
        }

        User user = UserMapper.toUser(userDto);
        checkEmail(user);
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Transactional
    public void delete(Long id) {
        userRepository.delete(findById(id));
        log.info("Пользователь с id = {}  - удален", id);
    }

    public User findById(Long id) {
        Optional<User> user =  userRepository.findById(id);
        if  (user.isPresent()) {
            log.info("Пользователь c id = {} найден", id);
            return user.get();
        }
        log.warn("Пользователь с id = {} не найден", id);
        throw new NotFoundException(String.format("User with id=%d was not found", id));
    }

    private void checkEmail(User currUser) {

        if (currUser.getEmail().isBlank()) {
            throw new ValidationException("email не может быть пустым");
        }

        if (userRepository.findByEmail(currUser.getEmail()).isPresent()) {
            throw new DuplicatedDataException("Этот email уже используется");
        }
    }
}
