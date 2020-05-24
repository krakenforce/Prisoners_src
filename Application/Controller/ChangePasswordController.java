package Application.Controller;

;


import Application.ChangePassWindow;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ChangePasswordController {
    private ChangePassWindow main;

    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField pfOldPw;
    @FXML
    private PasswordField pfNewPw;
    @FXML
    private PasswordField pfReNewPw;
    @FXML
    private Button btnConfirm;

    public void setUp(ChangePassWindow changePassWindow) {
         main = changePassWindow;
    }
}
