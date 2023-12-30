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

    public Optional<User> find(User user) {
        return userRepositoryDatabase.find(user);
    }

    public User save(User user){
        user.setId(UUID.randomUUID().toString());
        //maybe add coins and elo here? need variables in User though
        return userRepositoryDatabase.save(user);
    }

    public User update(int updateId, User updatedUser) {
        return null;
    }

    public boolean checkIfAuthorized(User user, User foundUser){
        if(user.getAuthorization().equals(foundUser.getAuthorization())){
            return true;
        }else return user.getAuthorization().equals("admin-mtcgToken");
    }
}
