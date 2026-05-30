package pl.wsb.fitnesstracker.user.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.wsb.fitnesstracker.user.api.UserDto;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
class UserController {

    private final UserServiceImpl userService;
    private final UserMapper userMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@RequestBody UserDto userDto) {

        var user = userMapper.fromDto(userDto);
        var savedUser = userService.createUser(user);

        return userMapper.toDto(savedUser);
    }
    @GetMapping
    public List<UserDto> getAllUsers() {

        return userService.findAllUsers()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {

        return userService.getUser(id)
                .map(userMapper::toDto)
                .orElseThrow();
    }

    @GetMapping("/email")
    public List<UserDto> getUserByEmail(@RequestParam String email) {

        return userService.getUserByEmail(email)
                .map(user -> List.of(userMapper.toDto(user)))
                .orElse(List.of());
    }

    @GetMapping("/simple")
    public List<UserDto> getAllSimpleUsers() {

        return userService.findAllUsers()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @GetMapping("/older/{time}")
    public List<UserDto> getUsersOlderThan(@PathVariable java.time.LocalDate time) {

        return userService.findAllUsers()
                .stream()
                .filter(user -> user.getBirthdate().isBefore(time))
                .map(userMapper::toDto)
                .toList();
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {

        userService.deleteUser(userId);
    }
    @PutMapping("/{userId}")
    public UserDto updateUser(
            @PathVariable Long userId,
            @RequestBody UserDto userDto) {

        var updatedUser = userService.updateUser(userId, userDto);

        return userMapper.toDto(updatedUser);
    }
}

