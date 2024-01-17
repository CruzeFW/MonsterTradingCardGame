package at.technikum.apps.mtcg.util;

import at.technikum.apps.mtcg.entity.User;

public class ResponseParser {

    public String userDataParser(User user){

        return "UserData:\n Name: " + user.getName() + "\n" +
                " Bio: " + user.getBio() + "\n" +
                " Image: " + user.getImage() + "\n";
    }
}
