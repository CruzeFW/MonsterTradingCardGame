package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.UserRepositoryDatabase;
import at.technikum.apps.mtcg.util.ResponseParser;
import at.technikum.server.http.Request;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class UserService {
    private final UserRepositoryDatabase userRepositoryDatabase;

    public UserService(UserRepositoryDatabase userRepositoryDatabase) {
        this.userRepositoryDatabase = userRepositoryDatabase;
    }

    // reform request and send it to next function to save it, checks if it doesn't already exist
    public Integer postMethodCalled(Request request) throws SQLException {
        ObjectMapper objectMapper = new ObjectMapper();
        User user;
        try {
            user = objectMapper.readValue(request.getBody(), User.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // check if user is already in DB
        Optional<User> user1 = find(user);
        if(user1.isPresent()){
            return 1;
        }
        // if not in DB save in DB
        save(user);
        return 0;
    }

    // call DB to find user by username
    public Optional<User> find(User user) throws SQLException {
        return userRepositoryDatabase.find(user);
    }

    // get user data from DB
    //TODO QUESTION: ADD ADMIN ACCESS / ASK PROF
    public Object[] getMethodCalled(Request request) throws SQLException {
        Object[] arr = new Object[2];
        // ObjectMapper objectMapper = new ObjectMapper();
        User user = new User();

        // check if token and path are corresponding
        if(checkCorrectPathAndToken(request)) {
            user.setAuthorization(request.getAuthorization());
            Optional<User> returnedUser;
            returnedUser = findWithToken(user);

            // check if user has been found
            if (returnedUser.isPresent()) {
                User foundUser = returnedUser.get();
                boolean authorized = checkIfAuthorized(user, foundUser);        //that's kinda wonky
                if (authorized) {
                    ResponseParser responseParser = new ResponseParser();
                    // reworked to responseParser TODO delete this or centralize
//                    String userJson;
//                    try {
//                        userJson = objectMapper.writeValueAsString(foundUser);
//                    } catch (JsonProcessingException e) {
//                        throw new RuntimeException(e);
//                    }
                    arr[0] = 0;
                    arr[1] = responseParser.userDataParser(foundUser);  // user found
                }
            }else{
                arr[0] = 1; // not found
            }
        }else{
            arr[0] = 2; // unauthorized
        }
        return arr;
    }

    // call DB to find user by token
    public Optional<User> findWithToken(User user) throws SQLException {
        return userRepositoryDatabase.findWithToken(user);
    }

    // call DB to save user
    public User save(User user) throws SQLException {
        user.setId(UUID.randomUUID().toString());
        return userRepositoryDatabase.save(user);
    }

    // update user data in DB
    //TODO QUESTION: ADD ADMIN ACCESS / ASK PROF
    public Integer putMethodCalled(Request request) throws SQLException {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = new User();

        // check if token and path are corresponding
        if(checkCorrectPathAndToken(request)) {
            try {
                user = objectMapper.readValue(request.getBody(), User.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            user.setAuthorization(request.getAuthorization());

            Optional<User> returnedUser;
            returnedUser = findWithToken(user);

            // check if user has been found
            if(returnedUser.isPresent()){
                User finalUser = returnedUser.get();
                finalUser = setDataForUpdate(finalUser, user);
                finalUser = update(finalUser);
                return 0;   // successfully updated
            }else{
                return 1;   // user not found
            }
        }else{
            return 2;       // unauthorized
        }
    }

    // merge data into one user
    public User setDataForUpdate(User finalUser, User user){
        if(user.getId() != null){
            finalUser.setId(user.getId());
        }
        if(user.getName() != null){
            finalUser.setName(user.getName());
        }
        if(user.getBio() != null){
            finalUser.setBio(user.getBio());
        }
        if(user.getImage() != null){
            finalUser.setImage(user.getImage());
        }
        finalUser.setAuthorization(user.getAuthorization());
        return finalUser;
    }

    // call DB to perform user update
    public User update(User updatedUser) throws SQLException {
        return userRepositoryDatabase.update(updatedUser);
    }

    // check if request route and token are corresponding
    public boolean checkCorrectPathAndToken(Request request){
        return request.getRoute().split("/")[2].equals(request.getAuthorization().split("-")[0]);
    }

    // jaaaa keine ahnung ob die das macht was ich will, soll true returnen wenn beide gleich sind oder wenns der admin is,
    // könnte aber mit dem code davor schon bissl fucked sein..
    //TODO what is this?
    public boolean checkIfAuthorized(User user, User foundUser){
        if(user.getAuthorization().equals(foundUser.getAuthorization())){
            return true;
        }else return user.getAuthorization().equals("admin-mtcgToken");
    }
}
