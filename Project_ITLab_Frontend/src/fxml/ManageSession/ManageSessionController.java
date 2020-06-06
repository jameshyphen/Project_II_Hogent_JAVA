package fxml.ManageSession;

import controllers.SessionCalendarController;
import controllers.SessionController;
import domain.models.Session;
import domain.models.SessionCalendar;
import fxml.Calendar.CalendarController;
import fxml.SessionDataInput.SessionDataInputController;
import io.reactivex.Observable;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import managers.ControllerManager;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

//import java.time.LocalDateTime;

public class ManageSessionController implements Initializable {

    private final Observable<Boolean> sessionChangeListener;
    private SessionController sesController;
    private SessionCalendarController calController;


    private List<MultipartFile> mpf = new ArrayList<>();

    /**
     * Contains all the sessions. Will be get updated when data changes.
     * When a filter is selected, a copy of this list will get filtered and added to the ObservableList.
     */


    private List<Session> sessions = new ArrayList<>();
    private ObservableList<Session> list;
    private FilteredList<Session> filteredList;


    private List<SessionCalendar> calendars;
    private ObservableList<SessionCalendar> listCal;


    @FXML
    private Pane backPane;

    @FXML
    private ComboBox<SessionCalendar> cmbCalendars;

    @FXML
    private TableView<Session> sessionTableview;

    @FXML
    private Button btnCalendar;

    private Parent sidePanel;
    private boolean sidePanelClickable;


    public void filteredByCalender() {
      /*  list.removeAll();
        list.add(sessions.stream().filter(s -> s.getStartDate().getYear() == (year.getValue() == null ? LocalDate.now() : year.getValue()).getYear()));
        //TODO Make method in Session to filter by SessionCalendar
        //TODO Change predicate of FilteredList in combo with SessionCalendar
        /*
        //Ensures list is empty
//        list.removeAll();
//
//        //Get new sessions from that year
//
//
//        list.add(controller.getSessions().stream().filter(s -> s.getStartDate().getYear() == (year.getValue() == null ? LocalDate.now() : year.getValue()).getYear()));
//
//        //Add all to displayed List
//
//        sessionTableview.getItems().setAll(list);
*/


    }

    public ManageSessionController() {
        this.sesController = ControllerManager.getSessionController();
        this.calController = ControllerManager.getSessionCalendarController();

        this.sessionChangeListener = sesController.getChangeListener();
        //On Session change, renew sessions and add to ObservableList
        this.sessionChangeListener.subscribe(s -> {
            this.sessions = new ArrayList<>(sesController.getSessions());
            this.list.setAll(this.sessions);
        });

        //this.sessions = new ArrayList<>(sesController.getSessions());
//        this.calendars = new ArrayList<>(calController.getSessionCalendars());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Alle sessions tonen
        sidePanelClickable = true;
        list = FXCollections.observableArrayList(sessions);
        filteredList = new FilteredList(list);

        listCal = FXCollections.observableArrayList();

        //Tableview aanmaken
        initTable();
        initCalendarCombobox();

        sessionTableview.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                try {
                    Session chosenSession = sessionTableview.getSelectionModel().getSelectedItem();
                    slideScreen(chosenSession.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        new Thread(() -> setSessions(new ArrayList<Session>(sesController.getSessions()))).start();

        new Thread(() -> setSessionCalendars(new ArrayList<SessionCalendar>(calController.getSessionCalendars()))).start();


    }


    public void manage(ActionEvent actionEvent) throws Exception {
        var button = (Button) actionEvent.getSource();
        if (button.getId().equals("btnCreateSession")) {
            slideScreen(-1);
        }
    }

    public void manageCalendars(ActionEvent actionEvent) throws IOException {
        var button = (Button) actionEvent.getSource();
        if (button.getId().equals("btnManageCalendars"))
            slideScreenCalendars();
    }

    private void slideScreenCalendars() throws IOException {
        if (sidePanelClickable) {
            sidePanelClickable = false;
            if (sidePanel != null) {
                backPane.getChildren().remove(sidePanel);//Remove old panel
                sidePanel = null;
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../" + "Calendar" + "/" + "Calendar" + ".fxml"));
            sidePanel = loader.load();
            CalendarController cont = loader.getController();
            cont.setData(this.calendars);

            sidePanel.setTranslateX(1050);
            backPane.getChildren().add(sidePanel);
            sidePanel.toFront();

            Timeline timeline = new Timeline();
            KeyValue keyValue = new KeyValue(sidePanel.translateXProperty(), 700, Interpolator.EASE_BOTH);
            KeyFrame keyFrame = new KeyFrame(Duration.millis(500), keyValue);
            timeline.getKeyFrames().add(keyFrame);
            timeline.setOnFinished(x -> {
                sidePanelClickable = true;
            });
            timeline.play();
        }
    }


    //if index is -1 then you're creating a new session, else it's the index of the session you want to edit
    private void slideScreen(int index) throws Exception {
//        int index = sessionTableview.getSelectionModel().getSelectedIndex() + 1;
        Session session;
        boolean newSession = true;
        if(index<0)
            session = new Session();
        else{
            session = sessions.stream().filter(x -> x.getId() == index).findFirst().orElseThrow(Exception::new);
            newSession = false;
        }
//
//        if (!view.equals("New")) {
//            session = sessions.get(index);
//        }

        if (sidePanelClickable) {
            sidePanelClickable = false;
            if (sidePanel != null) {
                backPane.getChildren().remove(sidePanel);//Remove old panel
                sidePanel = null;
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../" + "SessionDataInput" + "/" + "SessionDataInput" + ".fxml"));
            sidePanel = loader.load();


            if(!newSession){
                SessionDataInputController cont = loader.getController();
                cont.setData(session);
            }


            sidePanel.setTranslateX(1050);
            backPane.getChildren().add(sidePanel);
            sidePanel.toFront();

            Timeline timeline = new Timeline();
            KeyValue keyValue = new KeyValue(sidePanel.translateXProperty(), 725, Interpolator.EASE_BOTH);
            KeyFrame keyFrame = new KeyFrame(Duration.millis(500), keyValue);
            timeline.getKeyFrames().add(keyFrame);
            timeline.setOnFinished(x -> {
                sidePanelClickable = true;
            });
            timeline.play();
        }
    }

    private void initTable() {

        //Columns aanmaken en linken met attribute van de model
        TableColumn<Session, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Session, String> leaderColumn = new TableColumn<>("Leader");
        leaderColumn.setCellValueFactory(new PropertyValueFactory<>("firstLeader"));

        TableColumn<Session, LocalDateTime> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        TableColumn<Session, Long> attendeesColumn = new TableColumn<>("Inschrijvingen");
        attendeesColumn.setCellValueFactory(new PropertyValueFactory<>("amountOfRegisters"));


        //Columns toevoegen aan tableview
        sessionTableview.getColumns().add(titleColumn);
        sessionTableview.getColumns().add(leaderColumn);
        sessionTableview.getColumns().add(dateColumn);
        sessionTableview.getColumns().add(attendeesColumn);

        //ObservableList linken aan tableview
        sessionTableview.setItems(filteredList);

        //Zorg dat tableview selection weg gaat bij click van een empty cell OF background
        backPane.addEventFilter(MouseEvent.MOUSE_CLICKED, evt -> {
            if (!(evt.getTarget() instanceof Button)) {
                Node source = evt.getPickResult().getIntersectedNode();

                // move up through the node hierarchy until a TableRow or scene root is found
                while (source != null && !(source instanceof TableRow)) {
                    source = source.getParent();
                }

                // clear selection on click anywhere but on a filled row
                if (source == null || (source instanceof TableRow && ((TableRow) source).isEmpty())) {
                    sessionTableview.getSelectionModel().clearSelection();

                }
            }

        });

    }

    private void initCalendarCombobox() {
        cmbCalendars.setItems(listCal);
        cmbCalendars.valueProperty().addListener((observableValue, sessionCalendar, t1) -> {
            filteredList.setPredicate(new CalendarPredicate(t1));
        });
    }

    public void deselectAll() {
        sessionTableview.getSelectionModel().select(-1);
    }

    private void setSessions(List<Session> list) {
        this.sessions = list;
        this.list.setAll(sessions);
        this.sessionTableview.refresh();
    }

    private void setSessionCalendars(ArrayList<SessionCalendar> list) {
        this.calendars = list;
        this.listCal.setAll(calendars);
        this.cmbCalendars.setItems(listCal);

        var item = calendars.stream().filter(s -> s.containsDate(LocalDateTime.now())).findFirst().get();//get calendar corresponding to current year

        this.cmbCalendars.getSelectionModel().select(calendars.indexOf(item));
    }

    /**
     * Inner class defining a predicate to filter Sessions based on a SessionCalendar.
     */
    private class CalendarPredicate implements Predicate<Session> {

        private SessionCalendar cal;

        public CalendarPredicate(SessionCalendar cal) {
            this.cal = cal;
        }

        @Override
        public boolean test(Session session) {
            return cal.containsDate(session.getStartDate()) && cal.containsDate(session.getEndDate());
        }
    }

}



