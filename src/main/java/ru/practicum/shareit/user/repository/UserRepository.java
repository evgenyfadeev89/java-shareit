package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
@Slf4j
public class UserRepository {

    private final Map<Long, User> users = new HashMap<>();


    private Long getId() {
        return users.keySet().stream().max(Long::compare).orElse(0L) + 1L;
    }


    public Collection<User> findAll() {
        log.info("Вызов показа всех пользователей");
        return users.values();
    }


    public Optional<User> findById(Long userId) {
        return Optional.ofNullable(users.get(userId));
    }


    public Optional<User> findByEmail(String userEmail) {
        return users.values()
                .stream()
                .filter(usr -> usr.getEmail().equals(userEmail))
                .findFirst();
    }


    public User save(User user) {
        user.setId(getId());
        users.put(user.getId(), user);

        return user;
    }


    public User update(User user) {
        User usr = users.get(user.getId());
        usr.setName(user.getName());
        usr.setEmail(user.getEmail());

        return users.put(usr.getId(), usr);
    }


    public void deleteById(Long userId) {
        users.remove(userId);
    }
}
