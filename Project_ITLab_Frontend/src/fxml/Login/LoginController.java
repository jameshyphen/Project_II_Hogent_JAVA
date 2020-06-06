package fxml.Login;

import controllers.AuthController;
import enums.NavRef;
import fxml.FxmlView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import managers.ControllerManager;
import services.NavService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private TextField txfUsername;

    @FXML
    private Label lblWrongcreds;

    @FXML
    private PasswordField psfPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private BorderPane parentContainer;

    @FXML
    private BorderPane borderRoot;

    @FXML
    private Button btnExit;

    private final AuthController controller;

    public LoginController(){
        controller = ControllerManager.getAuthController();
    }


    public void login(ActionEvent actionEvent) throws IOException {
        System.out.println("Login button clicked.");
        //check if login correct
        if (!validateFields()){
            setError("Username or password fields are empty");
            return;
        }

        if(!controller.login(txfUsername.getText(), psfPassword.getText())){
            setError("Username or password are incorrect, try again");
            return;
        }

        NavService.get().navigateTo(NavRef.HOME);

        //on success get home screen
        //StageManager.getInstance().changeScreenAnimated(FxmlView.HOME, borderRoot);

    }

    private boolean validateFields(){
        String password = psfPassword.getText();
        String username = txfUsername.getText();
        return !(psfPassword.getText().isEmpty()  && txfUsername.getText().isEmpty());
    }

    private void setError(String er){
        lblWrongcreds.setText(er);
        lblWrongcreds.setVisible(true);
    }



    public void exit(ActionEvent actionEvent) {
        Stage stage = (Stage) btnExit.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       // StageManager.getInstance().setParentContainer(parentContainer);
    }
}