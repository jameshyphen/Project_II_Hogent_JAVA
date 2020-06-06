package fxml.EditUser;

import controllers.UserController;
import domain.enums.UserStatus;
import domain.models.User;
import fxml.UserDataInput.UserDataInputController;
import io.reactivex.Observable;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EditUserController implements Initializable {

    private final Observable<Boolean> userChangeListener;
    private UserController userController;
    private User user;

    private List<User> users;

    private FilteredList<User> filteredList;
    private ObservableList<User> list;

    @FXML
    private Pane backPane;

    @FXML
    private TextField textFieldSearch;

    @FXML
    private TableView<User> userTableview;

    private Parent sidePanel;

    private boolean hasOpenedFields = false;

    private boolean sidePanelClickable;

    public EditUserController() {
        this.userController = ControllerManager.getUserController();

        this.userChangeListener = userController.getChangeListener();
        //On User change, renew users and add to ObservableList
        this.userChangeListener.subscribe(s -> {
            this.users = new ArrayList<>(userController.getAllUsers());
            this.list.setAll(this.users);
        });

        this.users = new ArrayList<>(userController.getAllUsers());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        textFieldSearch.textProperty().addListener((observable, oldValue, newValue) -> search());
        sidePanelClickable = true;
        //Alle users tonen
        list = FXCollections.observableList(users);
        //list = FXCollections.emptyObservableList();
        filteredList = new FilteredList(list);

        //Tableview aanmaken
        initTable();


        userTableview.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                try {
                    slideScreen(newSelection);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initTable() {
        //Columns aanmaken en linken met attribute van de model
        TableColumn<User, String> fnColumn = new TableColumn<>("Voornaam");
        fnColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<User, String> lnColumn = new TableColumn<>("Achternaam");
        lnColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<User, String> usernameColumn = new TableColumn<>("Gebruikersnaam");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> emailColumn = new TableColumn<>("email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<User, UserStatus> statusColumn = new TableColumn<>("status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("UserStatus"));

        TableColumn<User, UserStatus> roleColumn = new TableColumn<>("role");
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("UserRole"));

        //Columns toevoegen aan tableview
        userTableview.getColumns().add(fnColumn);
        userTableview.getColumns().add(lnColumn);
        userTableview.getColumns().add(usernameColumn);
        userTableview.getColumns().add(emailColumn);
        userTableview.getColumns().add(statusColumn);
        userTableview.getColumns().add(roleColumn);


        //ObservableList linken aan tableview
        userTableview.setItems(filteredList);

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
                    userTableview.getSelectionModel().clearSelection();

                }
            }
        });
    }

    public void deselectAll() {
        userTableview.getSelectionModel().select(-1);
    }

    private void slideScreen(User user) throws IOException {
        if (sidePanelClickable) {
            sidePanelClickable = false;
            if (sidePanel != null) {
                backPane.getChildren().remove(sidePanel);//Remove old panel
                sidePanel = null;
            }


            FXMLLoader loader = new FXMLLoader(getClass().getResource("../" + "UserDataInput" + "/" + "UserDataInput" + ".fxml"));
            sidePanel = loader.load();
            UserDataInputController cont = loader.getController();
            cont.setData(user);
            backPane.getChildren().add(sidePanel);
            sidePanel.toFront();
            sidePanel.setTranslateX(1050);

            Timeline timeline = new Timeline();

            KeyValue keyValue = new KeyValue(sidePanel.translateXProperty(), 725, Interpolator.EASE_BOTH);
            KeyFrame keyFrame = new KeyFrame(Duration.millis(500), keyValue);

            timeline.getKeyFrames().add(keyFrame);
            timeline.setOnFinished(x -> sidePanelClickable = true);
            timeline.play();

        }
    }

    public void search() {
        var input = textFieldSearch.getText().toLowerCase();
//        var list2 = list.filtered(s -> s.getUsername().toLowerCase().contains(input));
        if (input.equals(""))
            filteredList.setPredicate(s -> true);
        filteredList.setPredicate(s -> s.getUsername().contains(input));

        userTableview.refresh();
    }


}
