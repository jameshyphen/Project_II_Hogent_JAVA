package fxml.SessionDataInput;

import controllers.AuthController;
import controllers.DTO.SessionDTO;
import controllers.SessionController;
import controllers.UserController;
import controllers.enums.Permissions;
import domain.enums.UserRole;
import domain.models.Feedback;
import domain.models.Session;
import fxml.SessionFeedbackData.SessionFeedbackDataController;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import managers.ControllerManager;
import org.apache.commons.io.IOUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import tornadofx.control.DateTimePicker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SessionDataInputController implements Initializable {

    @FXML
    private Label lblError;

    @FXML
    private TextField txfTitle;

    @FXML
    private TextField txfSpeaker;

    @FXML
    private TextField txfRoom;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnCancelFeedback;

    @FXML
    private Button btnDelete;

    @FXML
    private DateTimePicker dtpStart;

    @FXML
    private DateTimePicker dtpEnd;

    @FXML
    private TextField txfMax;

    @FXML
    private CheckBox chkReminder;

    @FXML
    private DateTimePicker dtpReminder;

    @FXML
    private Button btnSendReminder;

    @FXML
    private Button btnAddFile;

    @FXML
    private TextArea txfDescription;

    @FXML
    private Button btnCancel;

    @FXML
    private Pane backPane;

    private Parent sidePanel;
    private boolean sidePanelClickable = true;

    private SessionController cont;
    private AuthController authCont;
    private UserController userController;
    private Permissions perms;

    private Session session;

    private SessionDTO sessionDTO;

    private List<MultipartFile> mpf = new ArrayList<>();

    public SessionDataInputController() {
        cont = ControllerManager.getSessionController();
        authCont = ControllerManager.getAuthController();
        userController = ControllerManager.getUserController();
    }

    @FXML
    public void multiFileChooser(ActionEvent event) {
        try {
            slideScreen();
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

    public void sendReminder() {
        session.reminder = chkReminder.isSelected();
        if (dtpReminder != null)
            session.sendReminder(dtpReminder.getDateTimeValue());
    }

    public void setData(Session session) {
        this.session = session;
        sessionDTO = new SessionDTO(session.getId());

        txfTitle.setText(session.getTitle());
        txfSpeaker.setText(session.getSpeaker());
        txfDescription.setText(session.getDescription());
        txfMax.setText(Integer.toString(session.getMaxAttendees()));
        txfRoom.setText(session.getRoom());
        dtpStart.setDateTimeValue(session.getStartDate());
        dtpEnd.setDateTimeValue(session.getEndDate());
        chkReminder.setSelected(session.getReminder());

        dtpReminder.setDateTimeValue(session.getReminderDate());
        this.perms = authCont.getSessionPermissions(this.session);
        setPermissionEffects();
    }

    private void setPermissionEffects() {
        var cantEdit = perms != Permissions.EDIT;//True if no editing is possible
        this.btnSave.setDisable(cantEdit);//If true, cant save
        this.btnDelete.setDisable(cantEdit);//If true, cant delete
        this.btnAddFile.setDisable(cantEdit);//If true, cant add file
        this.btnSendReminder.setDisable(cantEdit);//If true, cant send reminder
    }

    public void deleteSession(ActionEvent event) {
        if (this.session == null)
            return;

        cont.deleteSession(this.session.getId());
    }

    public void saveSession(ActionEvent event) {
        if (validateFields()) {
            //TODO validation of fields #Dzhem fix da nekeer anders #blamedzhem
            //todo wtf fuk off
        }
        final var dto = fillDTO();

        if (this.session == null)
            this.cont.createSession(dto);
        else {
            if (authCont.getCurrentUser().getUserRole() != UserRole.HEADADMIN)
                if (!authCont.getCurrentUser().getUsername().equals(dto.leader.getUsername())){
                    btnSave.setDisable(true);
                    return;
                }
            this.cont.updateSession(dto);

        }

    }

    private boolean validateFields() {
        return true;
    }

    private SessionDTO fillDTO() {
        SessionDTO dto = null;

        dto = this.session != null ? new SessionDTO(this.session.getId()) : new SessionDTO();

        //Set values in DTO
        dto.title = this.txfTitle.getText();
        dto.leader = userController.getByUsername(authCont.getCurrentUser().getUsername());
        dto.startDate = this.dtpStart.getDateTimeValue();
        dto.endDate = this.dtpEnd.getDateTimeValue();
        dto.room = this.txfRoom.getText();
        dto.speaker = this.txfSpeaker.getText();
        dto.description = this.txfDescription.getText();
        dto.maxAttendees = Integer.parseInt(this.txfMax.getText());
        dto.reminder = this.chkReminder.isSelected();
        dto.reminderDate = this.dtpReminder.getDateTimeValue();
        if (!chkReminder.isSelected())
            dto.reminderDate = null;

        return dto;
    }

    public void cancel() {
        Parent parentContainer = backPane.getParent();

        Timeline timeline = new Timeline();

        KeyValue keyValue = new KeyValue(backPane.translateXProperty(), 1050, Interpolator.EASE_BOTH);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(500), keyValue);

        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    public void cancelFeedback() {
        Parent parentContainer = backPane.getParent();


        Timeline timeline = new Timeline();

        KeyValue keyValue = new KeyValue(backPane.translateXProperty(), 725, Interpolator.EASE_BOTH);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(500), keyValue);

        timeline.getKeyFrames().add(keyFrame);
        timeline.setOnFinished(x -> {
            backPane.getChildren().remove(sidePanel);
        });
        timeline.play();
        btnCancelFeedback.setVisible(false);
    }

    private void openUp() {
        Parent parentContainer = backPane.getParent();

        Timeline timeline = new Timeline();

        KeyValue keyValue = new KeyValue(backPane.translateXProperty(), 400, Interpolator.EASE_BOTH);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(500), keyValue);

        timeline.getKeyFrames().add(keyFrame);
        timeline.setOnFinished(x -> {
            //find a way to remove the scene

        });
        timeline.play();
    }

    public void slideScreen() throws IOException {
        if (sidePanelClickable) {
            sidePanelClickable = false;
            if (sidePanel != null)
                sidePanel = null;

            openUp();
            Timeline timeline = new Timeline();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("../" + "SessionFeedbackData" + "/" + "SessionFeedbackData" + ".fxml"));
            sidePanel = loader.load();
            SessionFeedbackDataController cont = loader.getController();
            cont.setTable(new ArrayList<>(session.getFeedbacks()));

            sidePanel.setTranslateX(330);

            backPane.getChildren().add(sidePanel);
            sidePanel.toFront();


            KeyValue keyValue = new KeyValue(sidePanel.translateXProperty(), 325, Interpolator.EASE_BOTH);
            KeyFrame keyFrame = new KeyFrame(Duration.millis(500), keyValue);

            timeline.getKeyFrames().add(keyFrame);
            timeline.setOnFinished(x -> {
                sidePanelClickable = true;
            });
            timeline.play();
            btnCancelFeedback.setVisible(true);
        }
    }

    public void deleteFeedback(Feedback f) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sidePanelClickable = true;
        chkReminder.selectedProperty().addListener((observable, oldValue, newValue) -> btnSendReminder.setVisible(newValue));

        //Set formatter to only allow ints
        txfMax.setTextFormatter(new TextFormatter<>(c -> !(c.getControlNewText().matches("\\d*")) ? null : c));

    }
}
