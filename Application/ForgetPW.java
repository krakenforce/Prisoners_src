package Application;

import Application.Controller.ForgetPWController;
import Application.Database.UserUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ForgetPW {
    public Stage window;
    public UserUtils uu;

    public ForgetPW(UserUtils userUtils) {
        uu = userUtils;
    }
    public void display() throws IOException {
        window = new Stage();
        window.setTitle("Reset Password");
        window.initModality(Modality.APPLICATION_MODAL);

        // set loader:
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ForgetPW.class.getResource("../Resources/Fxml/ForgetPW.fxml"));
        Parent root = loader.load();
        window.setScene(new Scene(root));


        // set the Utils:
        ForgetPWController ctrl = loader.getController();
        ctrl.setForgetPWClass(this);

        window.showAndWait();
    }
}
