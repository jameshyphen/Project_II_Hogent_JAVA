package fxml.Home;

import controllers.AuthController;
import enums.NavLevel;
import enums.NavRef;
import io.reactivex.Observable;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import managers.ControllerManager;
import services.NavService;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class HomeController implements Initializable {
    @FXML
    private Button btnLogout;

//    @FXML
//    private Button btnHome;

    @FXML
    private Button btnStatistic;

    @FXML
    private Button btnManageSessions;

    @FXML
    private Button btnEditUser;

    @FXML
    private BorderPane pane;

    @FXML
    private Pane paneMenu;

    @FXML
    private Button btnExpandMenu;

    private final AuthController controller;
    private List<Button> MenuButtons;
    //Set false for automatically open on startup
    private AtomicBoolean canExpand = new AtomicBoolean(true);


    /**
     * Navigation Service. Singleton service that handles navigation, signalling changes via Subjects.
     */
    private final NavService navService;

    private final Observable<Pane> navChanger;

    public void exit(ActionEvent actionEvent) {
        Stage stage = (Stage) btnLogout.getScene().getWindow();
        stage.close();
    }

    public void logout(ActionEvent actionEvent) {
        controller.logout();
        navService.logout();
    }

    /**
     * Function to navigate to a different part of the main menu
     *
     * @param actionEvent Click event of the nav buttons.
     */
    public void navigate(ActionEvent actionEvent) {
        var button = (Button) actionEvent.getSource();
        navService.navigateTo((NavRef) button.getUserData());
        button.arm();
        PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
        pause.setOnFinished(e -> button.disarm());
        pause.play();
        changeSelectedButton(button);
    }

    private void changeSelectedButton(Button button) {
        //selected button  rgb(112,128,144)
        String selectedButton = "rgb(112,128,144)";
        //not selected button  rgb(211,211,211)
        String notSelectedButton = "rgb(211,211,211)";

        if (MenuButtons.contains(button)) {
            for (Button menuButton : MenuButtons) {
                if (menuButton != button) {
                    menuButton.setStyle(String.format("-fx-text-fill: %s; -fx-background-color: %s", selectedButton, notSelectedButton));
                } else {
                    menuButton.setStyle(String.format("-fx-text-fill: %s; -fx-background-color: %s", "white", selectedButton));
                }
            }
        }
    }

    public HomeController() {
        System.out.println("constructor");
        controller = ControllerManager.getAuthController();
        navService = NavService.get();
        navChanger = navService.GetNavigator(NavLevel.MENU);
    }

    private void openMenu() {
        canExpand.set(false);
        Timeline timeline = new Timeline();
        Parent btnExpand = btnExpandMenu;
        Parent scene = paneMenu;
        Node centerScene = null;
        if (pane.getCenter() != null) {
            centerScene = pane.getCenter();
        }

        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), new KeyValue(btnExpand.rotateProperty(), 180, Interpolator.EASE_BOTH)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), new KeyValue(scene.translateXProperty(), 0, Interpolator.EASE_BOTH)));
        if (centerScene != null)
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), new KeyValue(centerScene.translateXProperty(), 200, Interpolator.EASE_BOTH)));
        timeline.play();
    }

    private void closeMenu() {
        canExpand.set(true);
        Timeline timeline = new Timeline();
        Parent btnExpand = btnExpandMenu;
        Parent scene = paneMenu;
        Node centerScene = null;
        if (pane.getCenter() != null) {
            centerScene = pane.getCenter();
        }
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), new KeyValue(btnExpand.rotateProperty(), 0, Interpolator.EASE_BOTH)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), new KeyValue(scene.translateXProperty(), -200, Interpolator.EASE_BOTH)));
        if (centerScene != null)
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), new KeyValue(centerScene.translateXProperty(), 0, Interpolator.EASE_BOTH)));
        timeline.play();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MenuButtons = Arrays.asList(btnManageSessions, btnStatistic, btnEditUser);
        System.out.println("init");
        initializeButtons();

        navChanger.subscribe(s -> {
            pane.setCenter(s);
            s.setTranslateX(200);
            closeMenu();
        });

        navService.navigateTo(NavRef.MANAGE);
        changeSelectedButton(this.btnManageSessions);

        Parent btnExpand = btnExpandMenu;
        Parent scene = paneMenu;
        paneMenu.toBack();
        paneMenu.setTranslateX(-200);
        //closeMenu();//Default should be open after getting to the menu
        btnExpand.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (canExpand.get())
                    openMenu();
                else
                    closeMenu();


            }
        });
//        sceneOnhover.setOnMouseExited(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                    canClose.set(false);
//                    Timeline timeline = new Timeline();
//                    KeyValue keyValue = new KeyValue(scene.translateXProperty(),-210, Interpolator.EASE_BOTH);
//                    KeyFrame keyFrame = new KeyFrame(Duration.millis(500), keyValue);
//                    timeline.getKeyFrames().add(keyFrame);
//                    timeline.setOnFinished(ev->{
//                        canExpand.set(true);
//                    });
//                    timeline.play();
//            }
//        });

    }

    /**
     * Initializes buttons that correspond to navigation within the main menu. Each button gets assigned a NavRef enum value to the UserData.
     */
    private void initializeButtons() {
        btnManageSessions.setUserData(NavRef.MANAGE);
        btnStatistic.setUserData(NavRef.STATS);
        btnEditUser.setUserData(NavRef.USER);
    }
}

//Old button actions
 /*   public void openManageSession(ActionEvent actionEvent) throws IOException {
        //System.out.println(FxmlView.MANAGESESSION.name());
        System.out.println("Navigating to Manage Session");
//        borderPage.setCenter(FXMLLoader.load(getClass().getResource("../" + FxmlView.ManageSession.name() + "/"+ FxmlView.ManageSession.name() +".fxml")));
    }

    public void openStatistic(ActionEvent actionEvent) throws IOException {
        System.out.println(FxmlView.STATISTIC.name());
    }

    public void openEditSessions(ActionEvent actionEvent) throws IOException {
        System.out.println(FxmlView.EDITSESSION.name());
    }

    public void openAnnouncement(ActionEvent actionEvent) throws IOException {
        System.out.println(FxmlView.ANNOUNCEMENT.name());
    }

    public void openEditUser(ActionEvent actionEvent) throws IOException {
        System.out.println(FxmlView.EDITUSER.name());
    }*/