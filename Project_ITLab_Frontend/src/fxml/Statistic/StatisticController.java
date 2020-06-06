package fxml.Statistic;

import controllers.RegisteredUserController;
import controllers.SessionController;
import controllers.SessionLeaderController;
import controllers.UserController;
import domain.enums.SessionState;
import domain.enums.UserRole;
import domain.models.RegisteredUser;
import domain.models.Session;
import domain.models.SessionLeader;
import domain.models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import managers.ControllerManager;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class StatisticController implements Initializable {
    SessionController sesController;
    UserController userController;
    SessionLeaderController sesLeaderController;
    RegisteredUserController registeredUserController;

    private FilteredList<Session> filteredSessions;
    private ObservableList<Session> sessionsObslist;
    private List<Session> sessions;

    private FilteredList<User> filteredUsers;
    private ObservableList<User> usersObslist;
    private List<User> users;

    @FXML
    private Pane backPane;

    @FXML
    private ComboBox<String> cmbType;

    @FXML
    private TableView<User> usersTable;

    @FXML
    private TableView<Session> sessionsTable;

    @FXML
    private Pane rightPane;

    @FXML
    private TextField txfSearchSession;

    @FXML
    private TextField txfSearchUser;

    private Set<RegisteredUser> registeredUsers;
    private Set<SessionLeader> sessionLeaders;

    public void deselectAll() {
        sessionsTable.getSelectionModel().select(-1);
        usersTable.getSelectionModel().select(-1);
    }


    public StatisticController(){
        this.sesController = ControllerManager.getSessionController();
        this.userController = ControllerManager.getUserController();
        this.sesLeaderController = ControllerManager.getSessionLeaderController();
        this.registeredUserController = ControllerManager.getRegisteredUserController();

        //NOW HAPPENS IN INITIALIZE()

        //grab all sessions
//        this.sessions = new ArrayList<>(sesController.getSessions());
//
//        //grabs all student users
//        this.users =
//                userController
//                        .getAllUsers()
//                        .stream()
//                        .filter(x -> x.getUserRole() == UserRole.STUDENT)
//                        .collect(Collectors.toList());
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        txfSearchSession.textProperty().addListener((observable, oldValue, newValue) -> searchSessions());
        txfSearchUser.textProperty().addListener((observable, oldValue, newValue) -> searchUsers());


        sessionsObslist = FXCollections.observableArrayList();
        usersObslist = FXCollections.observableArrayList();

        filteredSessions = new FilteredList(sessionsObslist);
        filteredUsers = new FilteredList(usersObslist);
        filteredUsers.setPredicate(x -> x.getUserRole() == UserRole.STUDENT);

        initSessionsTable();
        initUsersTable();

        cmbType.getItems().addAll("Gebruikers","Sessies");

        cmbType.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) ->{
            if(newValue.toString() == "Sessies"){
                sessionsTable.setVisible(true);
                txfSearchSession.setVisible(true);
                txfSearchUser.setVisible(false);
                usersTable.setVisible(false);

            }else if(newValue.toString() == "Gebruikers"){
                sessionsTable.setVisible(false);
                usersTable.setVisible(true);
                txfSearchUser.setVisible(true);
                txfSearchSession.setVisible(false);

            }else{
                sessionsTable.setVisible(false);
                usersTable.setVisible(false);
                txfSearchUser.setVisible(false);
                txfSearchSession.setVisible(false);
            }
        });

        usersTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            this.rightPane.getChildren().clear();
            if (newSelection != null) {
                try {
                    fillUserStatistics(newSelection);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        sessionsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            this.rightPane.getChildren().clear();
            if (newSelection != null) {
                try {
                    fillSessionsStatistics(newSelection);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        new Thread(() -> {
            this.sessions = sesController
                    .getSessions()
                    .stream()
                    .filter(x -> x.getState() == SessionState.FINISHED)
                    .collect(Collectors.toList());
            this.sessionsObslist.setAll(sessions);
        }).start();

        new Thread(() -> {
            this.users = new ArrayList<User>(userController.getAllUsers());
            this.usersObslist.setAll(users);
        }).start();

        new Thread(() ->{
            registeredUsers = this.registeredUserController.getSessions();
            sessionLeaders = this.sesLeaderController.getSessions();
        }).start();

    }
    //crate labels and make place them right
    //like this:
    //X always 30
    //Y difference between label and stat is 30, between two different ones is 35
    //XY 30, 25
    //xy 30, 55
    //
    //XY 30, 90
    //xy 30, 120
    //etc
    private void fillUserStatistics(User chosenUser) throws InterruptedException {
    while(registeredUsers==null){
        TimeUnit.MILLISECONDS.sleep(500);
    }
        long lngAbsentSessions = registeredUsers.stream().filter(x -> x.getUser().getId() == chosenUser.getId() && !x.hasAttended()).count();
        long lngAttendedSessions = registeredUsers.stream().filter(x -> x.getUser().getId() == chosenUser.getId() && x.hasAttended()).count();
        long lngTotalSessions = lngAttendedSessions + lngAbsentSessions;
        double dblPercentageAbsent;
        if(lngTotalSessions==0)
            dblPercentageAbsent=0.00;
        else
            dblPercentageAbsent = ((double) lngAbsentSessions / (double) lngTotalSessions) *100;


        Label lblAbsent = new Label("Sessies afwezig:");
        lblAbsent.setFont(new Font(22));
        lblAbsent.setTranslateX(30);
        lblAbsent.setTranslateY(25);

        rightPane.getChildren().add(lblAbsent);

        Label lblAbsentAmount = new Label(lngAbsentSessions+"");
        lblAbsentAmount.setFont(new Font(16));
        lblAbsentAmount.setTranslateX(30);
        lblAbsentAmount.setTranslateY(55);

        rightPane.getChildren().add(lblAbsentAmount);

        Label lblAttended = new Label("Sessies aanwezig:");
        lblAttended.setFont(new Font(22));
        lblAttended.setTranslateX(30);
        lblAttended.setTranslateY(90);

        rightPane.getChildren().add(lblAttended);

        Label lblAttendedAmount = new Label(lngAttendedSessions+"");
        lblAttendedAmount.setFont(new Font(16));
        lblAttendedAmount.setTranslateX(30);
        lblAttendedAmount.setTranslateY(120);

        rightPane.getChildren().add(lblAttendedAmount);

        Label lblTotal = new Label("Totaal sessies ingeschreven:");
        lblTotal.setFont(new Font(22));
        lblTotal.setTranslateX(30);
        lblTotal.setTranslateY(155);

        rightPane.getChildren().add(lblTotal);

        Label lblTotalAmount = new Label(lngTotalSessions+"");
        lblTotalAmount.setFont(new Font(16));
        lblTotalAmount.setTranslateX(30);
        lblTotalAmount.setTranslateY(185);

        rightPane.getChildren().add(lblTotalAmount);

        Label lblPercentage = new Label("Percentage afwezigheid:");
        lblPercentage.setFont(new Font(22));
        lblPercentage.setTranslateX(30);
        lblPercentage.setTranslateY(220);

        rightPane.getChildren().add(lblPercentage);

        Label lblPercentageAmount = new Label(String.format("%.2f",dblPercentageAbsent)+"%");
        lblPercentageAmount.setFont(new Font(16));
        lblPercentageAmount.setTranslateX(30);
        lblPercentageAmount.setTranslateY(250);

        rightPane.getChildren().add(lblPercentageAmount);
    }
    private void fillSessionsStatistics(Session chosenSession) throws InterruptedException {
        while(sessionLeaders==null){
            TimeUnit.MILLISECONDS.sleep(500);
        }
        long lngAttendedAmount =
                chosenSession
                        .getRegisteredUsers()
                        .stream()
                        .filter(x -> x.hasAttended())
                        .count();
        long lngMaxAttendees = chosenSession.getMaxAttendees();
        long lngAbsentAmount = lngMaxAttendees - lngAttendedAmount;

        double dblPercentageAbsentDouble = ((double)lngAbsentAmount/(double)lngMaxAttendees)*100;

        int intFeedbacksAmount = chosenSession.getFeedbacks().size();


        User leader = users.stream()
                .filter(x -> x.getUsername().equals(chosenSession.getFirstLeaderUsername()))
                .findFirst()
                .orElseThrow();

        long lngSessionsHostedByLeader = sessionLeaders.stream().filter(x -> x.getLeader().getId() == leader.getId()).count();

        Label lblAttended = new Label("Aantal mensen aanwezig:");
        lblAttended.setFont(new Font(22));
        lblAttended.setTranslateX(30);
        lblAttended.setTranslateY(25);

        rightPane.getChildren().add(lblAttended);

        Label lblAttendedValue = new Label(String.format("%d", lngAttendedAmount));
        lblAttendedValue.setFont(new Font(16));
        lblAttendedValue.setTranslateX(30);
        lblAttendedValue.setTranslateY(55);

        rightPane.getChildren().add(lblAttendedValue);

        Label lblAbsent = new Label("Aantal mensen afwezig:");
        lblAbsent.setFont(new Font(22));
        lblAbsent.setTranslateX(30);
        lblAbsent.setTranslateY(90);

        rightPane.getChildren().add(lblAbsent);

        Label lblAbsentAmount = new Label(lngAbsentAmount+"");
        lblAbsentAmount.setFont(new Font(16));
        lblAbsentAmount.setTranslateX(30);
        lblAbsentAmount.setTranslateY(120);

        rightPane.getChildren().add(lblAbsentAmount);

        Label lblPercentAbsent = new Label("Aantal mensen afwezig percentage:");
        lblPercentAbsent.setFont(new Font(22));
        lblPercentAbsent.setTranslateX(30);
        lblPercentAbsent.setTranslateY(155);

        rightPane.getChildren().add(lblPercentAbsent);

        Label lblPercentAbsentAmount = new Label(String.format("%.2f", dblPercentageAbsentDouble) + "%");
        lblPercentAbsentAmount.setFont(new Font(16));
        lblPercentAbsentAmount.setTranslateX(30);
        lblPercentAbsentAmount.setTranslateY(185);

        rightPane.getChildren().add(lblPercentAbsentAmount);

        Label lblFeedbacks = new Label("Aantal feedbacks:");
        lblFeedbacks.setFont(new Font(22));
        lblFeedbacks.setTranslateX(30);
        lblFeedbacks.setTranslateY(220);

        rightPane.getChildren().add(lblFeedbacks);

        Label lblFeedbacksAmount = new Label(intFeedbacksAmount+"");
        lblFeedbacksAmount.setFont(new Font(16));
        lblFeedbacksAmount.setTranslateX(30);
        lblFeedbacksAmount.setTranslateY(250);

        rightPane.getChildren().add(lblFeedbacksAmount);

        Label lblSessionsHostedByLeader = new Label("Aantal sessies gehost door leider:");
        lblSessionsHostedByLeader.setFont(new Font(22));
        lblSessionsHostedByLeader.setTranslateX(30);
        lblSessionsHostedByLeader.setTranslateY(285);

        rightPane.getChildren().add(lblSessionsHostedByLeader);

        Label lblSessionsHostedByLeaderAmount = new Label(lngSessionsHostedByLeader+"");
        lblSessionsHostedByLeaderAmount.setFont(new Font(16));
        lblSessionsHostedByLeaderAmount.setTranslateX(30);
        lblSessionsHostedByLeaderAmount.setTranslateY(315);

        rightPane.getChildren().add(lblSessionsHostedByLeaderAmount);

    }

    private void initSessionsTable() {

        //Columns aanmaken en linken met attribute van de model
        TableColumn<Session, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Session, String> leaderColumn = new TableColumn<>("Leader");
        leaderColumn.setCellValueFactory(new PropertyValueFactory<>("firstLeader"));

        TableColumn<Session, LocalDateTime> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        TableColumn<Session, String> maxAttendeesColumn = new TableColumn<>("Max inschr.");
        maxAttendeesColumn.setCellValueFactory(new PropertyValueFactory<>("maxAttendees"));

        //Columns toevoegen aan tableview
        sessionsTable.getColumns().add(titleColumn);
        sessionsTable.getColumns().add(leaderColumn);
        sessionsTable.getColumns().add(dateColumn);
        sessionsTable.getColumns().add(maxAttendeesColumn);
        //ObservableList linken aan tableview
        sessionsTable.setItems(filteredSessions);


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
                    sessionsTable.getSelectionModel().clearSelection();

                }
            }

        });
    }

    private void initUsersTable() {

        //Columns aanmaken en linken met attribute van de model
//        TableColumn<Session, String> titleColumn = new TableColumn<>("Title");
//        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
//
//        TableColumn<Session, String> leaderColumn = new TableColumn<>("Leader");
//        leaderColumn.setCellValueFactory(new PropertyValueFactory<>("firstLeader"));
//
//        TableColumn<Session, LocalDateTime> dateColumn = new TableColumn<>("Date");
//        dateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
//
//        TableColumn<Session, String> maxAttendeesColumn = new TableColumn<>("Max inschr.");
//        maxAttendeesColumn.setCellValueFactory(new PropertyValueFactory<>("maxAttendees"));

        TableColumn<User, String> firstNameColumn = new TableColumn<>("Voornaam");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<User, String> lastNameColumn = new TableColumn<>("Achternaam");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<User, String> usernameColumn = new TableColumn<>("Gebruikersnaam");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<User, String> cardNumberColumn = new TableColumn<>("Kaartnummer");
        cardNumberColumn.setCellValueFactory(new PropertyValueFactory<>("cardNumber"));

        TableColumn<User, Integer> timesAbsentColumn = new TableColumn<>("Afwezig");
        timesAbsentColumn.setCellValueFactory(new PropertyValueFactory<>("timesAbsent"));



        //Columns toevoegen aan tableview
        usersTable.getColumns().add(firstNameColumn);
        usersTable.getColumns().add(lastNameColumn);
        usersTable.getColumns().add(usernameColumn);
        usersTable.getColumns().add(emailColumn);
        usersTable.getColumns().add(cardNumberColumn);
        usersTable.getColumns().add(timesAbsentColumn);



        //ObservableList linken aan tableview
        usersTable.setItems(filteredUsers);

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
                    usersTable.getSelectionModel().clearSelection();

                }
            }

        });
    }
    public void searchSessions() {
        var input = txfSearchSession.getText().toLowerCase();
//        var list2 = sessionsObslist.filtered(s -> s.getTitle().toLowerCase().contains(input));
        if (input.equals(""))
            filteredSessions.setPredicate(s -> true);
        filteredSessions.setPredicate(s -> s.getTitle().toLowerCase().contains(input) ||
                s.getFirstLeader().toLowerCase().contains(input));

        sessionsTable.refresh();
    }

    public void searchUsers() {
        var input = txfSearchUser.getText().toLowerCase();
//        var list2 = sessionsObslist.filtered(s -> s.getTitle().toLowerCase().contains(input));
        if (input.equals(""))
            filteredUsers.setPredicate(s -> true);
        filteredUsers.setPredicate(s ->
                s.getUsername().toLowerCase().contains(input) ||
                s.getFullName().toLowerCase().contains(input) ||
                s.getCardNumber().contains(input));

        usersTable.refresh();
    }
}
