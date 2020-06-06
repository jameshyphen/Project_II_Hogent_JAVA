package fxml.UserDataInput;

import controllers.DTO.UserDTO;
import controllers.UserController;
import domain.enums.UserRole;
import domain.enums.UserStatus;
import domain.models.User;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import managers.ControllerManager;
import org.apache.commons.io.IOUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class UserDataInputController implements Initializable {

    @FXML
    private TextField txfUsername;

    @FXML
    private TextField txfFirstName;

    @FXML
    private TextField txfLastName;

    @FXML
    private ImageView imgvPhoto;

    @FXML
    private Button btnUpload;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnDelete;

    @FXML
    private ComboBox cmbStatus;

    @FXML
    private ComboBox cmbRole;
    @FXML
    private Pane backPane;

    private User user;

    private UserDTO dto;

    private UserController cont;

    private List<MultipartFile> mpf = new ArrayList<>();

    @FXML
    public void multiFileChooser(ActionEvent event) {
        try {
            FileChooser fc = new FileChooser();
            List<File> f = fc.showOpenMultipleDialog(null);
            for (File fi : f) {
                FileInputStream input = new FileInputStream(fi);
                mpf.add(new MockMultipartFile(String.valueOf(fi), fi.getName(), "text/plain", IOUtils.toByteArray(input)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UserDataInputController() {
        cont = ControllerManager.getUserController();
    }

    public void setData(User user) {
        this.user = user;
        dto = new UserDTO(user.getId());

        txfUsername.setText(user.getUsername());

        txfFirstName.setText(user.getFirstName());

        txfLastName.setText(user.getLastName());

        cmbStatus.setItems(FXCollections.observableList(Arrays.asList(UserStatus.values())));
        cmbStatus.getSelectionModel().select(user.getUserStatus());

        cmbRole.setItems(FXCollections.observableList(Arrays.asList(UserRole.values())));
        cmbRole.getSelectionModel().select(user.getUserRole());

        setImage();
    }

    public void setImage(){
        try {
            imgvPhoto.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/media/users/" + user.getUrl())));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void cancel() {
        Parent parentContainer = backPane.getParent();

        Timeline timeline = new Timeline();

        KeyValue keyValue = new KeyValue(backPane.translateXProperty(), 1050, Interpolator.EASE_BOTH);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(500), keyValue);

        timeline.getKeyFrames().add(keyFrame);
        timeline.setOnFinished(x -> {
            //find a way to remove the scene

        });
        timeline.play();
    }

    public void saveUser() {
        dto.username = txfUsername.getText();
        dto.firstName = txfFirstName.getText();
        dto.lastName = txfLastName.getText();
        dto.role = (UserRole) cmbRole.getSelectionModel().getSelectedItem();
        dto.status = (UserStatus) cmbStatus.getSelectionModel().getSelectedItem();
        dto.mpf = mpf;

        cont.updateUser(dto);
        setImage();
    }

    public void deleteUser() {
        cont.deleteUser(user.getId());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
