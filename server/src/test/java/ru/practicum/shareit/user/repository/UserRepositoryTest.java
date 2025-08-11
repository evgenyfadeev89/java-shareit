package ru.practicum.shareit.user.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("save и findById сохраняет и возвращает пользователя")
    void saveAndFindById() {
        User user = new User();
        user.setName("repoTest");
        user.setEmail("repo@test.com");

        User saved = userRepository.save(user);

        Optional<User> found = userRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("repo@test.com");
    }

    @Test
    @DisplayName("findByEmail возвращает правильного пользователя")
    void findByEmailShouldWork() {
        User user = new User();
        user.setName("Repo FindByEmail");
        user.setEmail("email@repo.com");
        userRepository.save(user);

        Optional<User> found = userRepository.findByEmail("email@repo.com");

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Repo FindByEmail");
    }

    @Test
    @DisplayName("findByEmail для несуществующего email возвращает пустой Optional")
    void findByEmailNotFound() {
        Optional<User> user = userRepository.findByEmail("nonexistent@repo.com");
        assertThat(user).isNotPresent();
    }

    @Test
    @DisplayName("findAll возвращает все сохранённые сущности")
    void findAllWorks() {
        User u1 = new User();
        u1.setName("A");
        u1.setEmail("a@a.com");
        User u2 = new User();
        u2.setName("B");
        u2.setEmail("b@b.com");
        userRepository.save(u1);
        userRepository.save(u2);

        assertThat(userRepository.findAll()).hasSize(2);
    }

    @Test
    @DisplayName("deleteById удаляет пользователя")
    void deleteByIdWorks() {
        User user = new User();
        user.setName("UserToDelete");
        user.setEmail("delete@repo.com");
        user = userRepository.save(user);

        userRepository.deleteById(user.getId());
        assertThat(userRepository.findById(user.getId())).isNotPresent();
    }
}
