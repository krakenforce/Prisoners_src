package Application;

import Application.Controller.ChangePasswordController;
import Application.Database.UserUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ChangePassWindow {
    public Stage window;
    private Main app;
    private UserUtils uu;

    public void display() throws IOException {
        window = new Stage();
        window.setTitle("Reset Password");
        window.initModality(Modality.APPLICATION_MODAL);

        // set loader:
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ForgetPW.class.getResource("../Resources/Fxml/ChangePassword.fxml"));
        Parent root = loader.load();
        window.setScene(new Scene(root));


        // set the Utils:
        ChangePasswordController ctrl = loader.getController();
        ctrl.setUp(this);

        window.showAndWait();
    }

    public void close() {
        window.close();
    }

    public void setApp(Main app, UserUtils uu) {
        this.app = app;
        this.uu = uu;
    }

    public Main getApp() {
        return app;
    }
    public UserUtils getUserUtils() {
        return uu;
    }

}
