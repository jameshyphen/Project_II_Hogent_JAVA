package panes;

import enums.NavLevel;
import enums.NavRef;
import io.reactivex.Observable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import services.NavService;

public class Root extends BorderPane {

    private Stage stage;

    @SuppressWarnings("FieldCanBeLocal")
    private final NavService navService;

    private final Observable<String> titleChanger;
    private final Observable<Pane> rootChanger;


    public Root(Stage stage) {
        this.stage = stage;
        navService = NavService.get();
        titleChanger = navService.TitleChanger();
        rootChanger = navService.GetNavigator(NavLevel.ROOT);

        setupObservables();

        navService.navigateTo(NavRef.LOGIN);
    }

    private void setupObservables() {
        titleChanger.subscribe(test -> stage.setTitle(test));

        rootChanger.subscribe(test -> {
            this.setCenter(test);
        });

    }
}
