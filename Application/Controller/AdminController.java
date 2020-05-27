package Application.Controller;

import Application.DAO.ChangePassWindow;
import Application.Database.OtherModelsUtils;
import Application.Database.PrisonerUtils;
import Application.Database.UserUtils;
import Application.Main;
import Application.Models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    private Main app;
    private PrisonerUtils pu;
    private OtherModelsUtils ou;
    private UserUtils uu;
    private User loggedUser;
    private Main main;

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
        ChangePassWindow cPw = new ChangePassWindow();
        cPw.setApp(app, uu);
        cPw.display();
    }

    @FXML
    public void createNewAccount(ActionEvent event) {
        // to be coded...
    }

    public void logOut(ActionEvent event) {
        loggedUser = null;
        main.goToLogin();
    }

    public void addTabOrther(ActionEvent actionEvent) {
    }

    public void addAccountManager(ActionEvent actionEvent) {
    }

    public void out(ActionEvent actionEvent) {

    }

    public void addTabPrisoners(ActionEvent actionEvent) {
    }
}
