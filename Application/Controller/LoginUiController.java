package Application.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginUiController {
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

    public void showPassword(ActionEvent actionEvent) {
        if(cbShowPW.isSelected()){
            pfPW.setVisible(false);
            tfShowPW.setVisible(true);
            tfShowPW.setText(pfPW.getText());
        }else{
            pfPW.setVisible(true);
            tfShowPW.setVisible(false);
            pfPW.setText(tfShowPW.getText());
        }

    }

}
