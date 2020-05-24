package Application.Controller;

import Application.AlertBox;
import Application.Database.DatabaseConnection;
import Application.Database.UserUtils;
import Application.ForgetPW;
import Application.ResetPWCallback;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.print.PageLayout;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

public class ForgetPWController {
    public ForgetPW fg;
    public UserUtils uu;

    @FXML
    private TextField tfEmail;
    @FXML
    private Button btnGetPw;
    @FXML
    private Label prompt;
    @FXML
    private TextField tfUsername;


    @FXML
    public void resetPassword(ActionEvent event) {
        String email = tfEmail.getText().trim();

        String username = tfUsername.getText().trim();

        if (uu != null) {
            // set up callback + timer:
            StringBuilder text = new StringBuilder("Please wait");
            Timer t = new Timer();


            ResetPWCallback callback = new ResetPWCallback() {
                @Override
                public void callback() {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            t.cancel();
                            prompt.setText("Done. Please Check your email.");
                        }
                    });
                }

                @Override
                public void callbefore() {
                    t.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    prompt.setText(text.toString());
                                    text.append(".");
                                    if (text.toString().endsWith("....")) {
                                        text.replace(0, text.length() - 1, "Please wait");
                                    }
                                }
                            });
                        }
                    }, 100, 700);
                }

            };

            int error = uu.resetPassword(username, email, callback);

            if (error != 0) {
                if (error == 1) {
                    prompt.setText("Username or email don't match");
                } else if (error == 2) {
                    prompt.setText("Error resting password");
                }
            }

        } else {
            System.out.println("user utils is null");
        }
    }

    public void setForgetPWClass(ForgetPW forgetPW) {
        fg = forgetPW;
        uu = fg.uu;
    }
}
