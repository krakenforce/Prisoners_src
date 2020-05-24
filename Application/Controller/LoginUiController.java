package Application.Controller;

import Application.Database.DatabaseConnection;
import Application.Database.UserUtils;
import Application.ForgetPW;
import Application.Main;
import Application.Models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginUiController implements Initializable {
    private Main app;
    private UserUtils uu;

    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField pfPW;
    @FXML
    private Button btnLogin;
    @FXML
    private CheckBox cbShowPW;
    @FXML
    private Hyperlink hlForgetPW;
    @FXML
    private TextField tfShowPW;

    @FXML
    private Label prompt;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pfPW.textProperty().bindBidirectional(tfShowPW.textProperty());
        tfShowPW.setVisible(false);

    }

    public void showPassword(ActionEvent actionEvent) {
        if (cbShowPW.isSelected()) {
            pfPW.setVisible(false);
            tfShowPW.setVisible(true);
        } else {
            pfPW.setVisible(true);
            tfShowPW.setVisible(false);

        }

    }


    public void logIn(ActionEvent event) {
        String username = tfUsername.getText();
        char[] pw = pfPW.getText().toCharArray();

        User user = uu.validateAccount(username, pw);
        if (user != null) {
            app.setLoggedUser(user);
            if (user.getType() == 0) {
                // go to staff
                app.goToStaff();
            } else if (user.getType() == 1) {
                // go to admin
                app.goToAdmin();
            }
        } else {
            prompt.setText("Wrong username or password");
        }


    }

    public void setApp(Main main) {
        app = main;
        // make the utils objects so we can use database methods/functions:
        uu = new UserUtils(app.db);
    }

    public void forgetPW(ActionEvent event) throws IOException {
        ForgetPW fg = new ForgetPW(uu);
        fg.display();
    }
}
