package main;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import managers.ControllerManager;
import managers.StartupManager;
import panes.Root;
import services.NavService;

import java.io.IOException;

public class MainFX extends Application {

    public static void main(String[] args) throws IOException {
        launch(args);
    }

    private static double xOffset = 0;
    private static double yOffset = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {

        StartupManager.signalStartup();
        ControllerManager.getStartupController().getEagerLoadingSignal().onNext(true);

        NavService.get();
        var root = new Root(primaryStage);
        Scene scene = new Scene(root);
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = primaryStage.getX() - event.getScreenX();
                yOffset = primaryStage.getY() - event.getScreenY();
            }
        });
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setX(event.getScreenX() + xOffset);
                primaryStage.setY(event.getScreenY() + yOffset);
            }
        });
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
        //stageManager = StageManager.getInstance();
        //stageManager.setMainStage(primaryStage);
        //stageManager.Initialize();
    }
}
