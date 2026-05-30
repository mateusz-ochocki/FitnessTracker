package pl.wsb.fitnesstracker.user.internal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.wsb.fitnesstracker.user.api.User;
import pl.wsb.fitnesstracker.user.api.UserProvider;
import pl.wsb.fitnesstracker.user.api.UserService;
import pl.wsb.fitnesstracker.user.api.UserDto;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
class UserServiceImpl implements UserService, UserProvider {

    private final UserRepository userRepository;

    @Override
    public User createUser(final User user) {
        log.info("Creating User {}", user);
        if (user.getId() != null) {
            throw new IllegalArgumentException("User has already DB ID, update is not permitted!");
        }
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUser(final Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> getUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public User updateUser(Long userId, UserDto dto) {

        User user = userRepository.findById(userId)
                .orElseThrow();

        try {
            var firstNameField = User.class.getDeclaredField("firstName");
            firstNameField.setAccessible(true);
            firstNameField.set(user, dto.firstName());

            var lastNameField = User.class.getDeclaredField("lastName");
            lastNameField.setAccessible(true);
            lastNameField.set(user, dto.lastName());

            var birthdateField = User.class.getDeclaredField("birthdate");
            birthdateField.setAccessible(true);
            birthdateField.set(user, dto.birthdate());

            var emailField = User.class.getDeclaredField("email");
            emailField.setAccessible(true);
            emailField.set(user, dto.email());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return userRepository.save(user);
    }
}