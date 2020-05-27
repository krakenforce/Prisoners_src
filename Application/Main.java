package Application;

import Application.Controller.AdminController;
import Application.Controller.LoginUiController;
import Application.Controller.StaffController;
import Application.DAO.ConfirmBox;
import Application.Database.DatabaseConnection;
import Application.Models.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class Main extends Application {
    public DatabaseConnection db;
    public Stage window;
    private User loggedUser;

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    @Override
    public void start(Stage stage) throws Exception {
        db = new DatabaseConnection();
        window = stage;

        // setting up:
        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });


        // jump to login:
        goToLogin();
        window.show();
    }

    public void logOut() {
        loggedUser = null;
        goToLogin();
    }

    public void goToStaff() {
        StaffController staff = null;
        try {
            staff = (StaffController) changeScene("../Resources/Fxml/Staff.fxml", "Staff window");
            staff.setApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToAdmin() {
        AdminController admin = null;
        try {
            admin = (AdminController) changeScene("../Resources/Fxml/AdminView.fxml", "Admin window");
            admin.setApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToLogin() {

        LoginUiController login = null;
        try {
            login = (LoginUiController) changeScene("../Resources/Fxml/LoginUI.fxml", "Login");
            login.setApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Initializable changeScene(String fxml, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = getClass().getResourceAsStream(fxml);
        loader.setLocation(getClass().getResource(fxml));
        Parent root;

        root = loader.load(in);

        window.setScene(new Scene(root));
        window.setTitle(title);

        return (Initializable) loader.getController();
    }


    public void closeProgram() {
        try {
            boolean answer = ConfirmBox.display("Closing Application", "Do you want to quit?");
            if (answer) {
                db.close();
                window.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

