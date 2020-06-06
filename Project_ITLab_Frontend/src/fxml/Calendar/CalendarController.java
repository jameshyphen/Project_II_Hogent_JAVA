package fxml.Calendar;

import controllers.SessionCalendarController;
import domain.models.SessionCalendar;
import io.reactivex.Observable;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import managers.ControllerManager;
import tornadofx.control.DateTimePicker;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CalendarController implements Initializable {

    @FXML
    private Pane backPane;

    @FXML
    private TableView<SessionCalendar> table;

    @FXML
    private Button btnCreate;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private DateTimePicker dpStart;

    @FXML
    private DateTimePicker dpEnd;

    private ObservableList<SessionCalendar> cals;

    private boolean isUpdatingCalendar = false;

    private SessionCalendar selectedCalendar;

    private SessionCalendarController cont;

    private Observable<Boolean> listener;

    public void delete(ActionEvent event) {
        if (this.selectedCalendar == null)
            return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Kalender verwijderen");
        alert.setContentText("Weet u zeker dat u deze kalender wil verwijderen?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != ButtonType.OK)
            return;
        this.cont.deleteSessionCalendar(this.selectedCalendar.getId());
        clearFields();
        this.table.getSelectionModel().clearSelection();
    }

    public void create() {
        this.selectedCalendar = null;
        this.isUpdatingCalendar = false;
        save();
    }

    /**
     * Clears the 2 input fields
     */
    private void clearFields() {
        this.dpStart.getEditor().clear();
        this.dpStart.setValue(null);

        this.dpEnd.getEditor().clear();
        this.dpEnd.setValue(null);
    }

    public void save() {
        if (!validateFields()) {
            //TODO validation
        }
        var start = dpStart.getValue();
        var end = dpEnd.getValue();


        if (isUpdatingCalendar) {
            this.selectedCalendar.setStart(LocalDateTime.of(start, LocalTime.MIN));
            this.selectedCalendar.setEnd(LocalDateTime.of(end, LocalTime.MAX));
            cont.updateSessionCalendar(this.selectedCalendar);
        } else {
            this.selectedCalendar = new SessionCalendar();
            this.selectedCalendar.setStart(LocalDateTime.of(start, LocalTime.MIN));
            this.selectedCalendar.setEnd(LocalDateTime.of(end, LocalTime.MAX));
            cont.createSessionCalendar(this.selectedCalendar);
        }


    }

    private boolean validateFields() {
        return this.dpStart.getValue() != null && this.dpEnd != null && dpStart.getValue().isBefore(dpEnd.getValue());
    }

    public void setData(List<SessionCalendar> list) {
        this.cals.setAll(list.stream().sorted(Comparator.comparing(SessionCalendar::getStart)).collect(Collectors.toList()));
    }

    public CalendarController() {
        this.cals = FXCollections.observableArrayList();
        this.cont = ControllerManager.getSessionCalendarController();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTable();
        this.listener = this.cont.getChangeListener();
        this.listener.subscribe(s ->
                new Thread(() -> {
                    this.cals.setAll(this.cont.getSessionCalendars());
                    this.table.refresh();
                }).start()
        );

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                try {
                    setSelectedCalendar(newSelection);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initTable() {
        //Columns aanmaken en linken met attribute van de model
        TableColumn<SessionCalendar, String> start = new TableColumn<>("Start date");
        start.setCellValueFactory(new PropertyValueFactory<>("startingDate"));

        TableColumn<SessionCalendar, String> end = new TableColumn<>("End date");
        end.setCellValueFactory(new PropertyValueFactory<>("endingDate"));


        //Columns toevoegen aan tableview
        table.getColumns().add(start);
        table.getColumns().add(end);

        //ObservableList linken aan tableview
        table.setItems(this.cals);

        //Zorg dat tableview selection weg gaat bij click van een empty cell OF background
        backPane.addEventFilter(MouseEvent.MOUSE_CLICKED, evt -> {
            if (!(evt.getTarget() instanceof Button)) {
                Node source = evt.getPickResult().getIntersectedNode();
                // move up through the node hierarchy until a TableRow or scene root is found
                while (source != null && !(source instanceof TableRow))
                    source = source.getParent();
                // clear selection on click anywhere but on a filled row
                if (source == null || (source instanceof TableRow && ((TableRow) source).isEmpty()))
                    table.getSelectionModel().clearSelection();
            }
        });

        table.getSelectionModel().selectedItemProperty().addListener((observableValue, oldV, newV) -> {
            clearTableSelectionExceptIndex(cals.indexOf(newV));
            setSelectedCalendar(newV);

        });

    }

    private void setSelectedCalendar(SessionCalendar cal) {
        this.selectedCalendar = cal;
        this.isUpdatingCalendar = true;
        dpStart.setDateTimeValue(selectedCalendar.getStart());
        dpEnd.setDateTimeValue(selectedCalendar.getEnd());
    }

    private void clearTableSelectionExceptIndex(int index) {
        table.getSelectionModel().clearSelection();
        table.getSelectionModel().select(index);
    }
    public void cancel(){
        Timeline timeline = new Timeline();

        KeyValue keyValue = new KeyValue(backPane.translateXProperty(), 1050, Interpolator.EASE_BOTH);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(500), keyValue);

        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }
}
