package enums;

import static enums.NavLevel.*;

public enum NavRef {
    HOME("../fxml/Home/Home.fxml", "Home", ROOT),
    MANAGE("../fxml/ManageSession/ManageSession.fxml", "Beheer sessies", MENU),
    STATS("../fxml/Statistic/Statistic.fxml", "Statistieken", MENU),
    USER("../fxml/EditUser/EditUser.fxml", "Beheer gebruiker", MENU),
    HOMEPAGE("../fxml/HomePage/HomePage.fxml", "Welkom pagina", MENU),
    LOGIN("../fxml/Login/Login.fxml", "Log in", ROOT);

    /**
     * Represents the URL of the view. Should only be used directly below the package level, else "../" needs to be appended.
     */
    public final String ref;
    /**
     * Represents the title that needs to be set whenever a navigation event takes place.
     */
    public final String title;
    /**
     * At what level the navigation takes place.
     */
    public final NavLevel level;

    NavRef(String classRef, String title, NavLevel level) {
        this.ref = classRef;
        this.title = title;
        this.level = level;
    }
}
