package test;

import Application.Database.DatabaseConnection;
import Application.Database.UserUtils;

public class TEst {
    public static void main(String[] args) {
        DatabaseConnection conn = new DatabaseConnection();
        UserUtils uu = new UserUtils(conn);

       uu.createNewUser("user12","ngotoanlibra@gmail.com",1);

    }
}
