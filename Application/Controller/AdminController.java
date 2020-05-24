package Application.Controller;

import Application.ChangePassWindow;
import Application.Database.OtherModelsUtils;
import Application.Database.PrisonerUtils;
import Application.Database.UserUtils;
import Application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextFormatter;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    private Main app;
    private PrisonerUtils pu;
    private OtherModelsUtils ou;
    private UserUtils uu;

    public void setApp(Main main) {
        app = main;
        pu = new PrisonerUtils(app.db);
        ou = new OtherModelsUtils(app.db);
        uu = new UserUtils(app.db);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void changePassword(ActionEvent event) throws IOException {
        //to be coded:
        ChangePassWindow cPw = new ChangePassWindow(uu);
        cPw.display();
    }

    public void logOut(ActionEvent event) {
        app.logOut();
    }
}