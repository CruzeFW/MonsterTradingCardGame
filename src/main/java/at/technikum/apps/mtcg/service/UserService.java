package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.UserRepositoryDatabase;

import java.util.Optional;
import java.util.UUID;

public class UserService {
    private final UserRepositoryDatabase userRepositoryDatabase;

    public UserService(UserRepositoryDatabase userRepositoryDatabase) {
        this.userRepositoryDatabase = userRepositoryDatabase;
    }

    public Optional<User> find(int id) {
        return Optional.empty();
    }

    public User save(User user){
        user.setId(UUID.randomUUID().toString());
        //maybe add coins and elo here? need variables in User though
        return userRepositoryDatabase.save(user);
    }

    public User update(int updateId, User updatedUser) {
        return null;
    }
}
