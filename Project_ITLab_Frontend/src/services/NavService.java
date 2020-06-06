package services;

import enums.NavLevel;
import enums.NavRef;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Service used to handle all levels of navigation. Uses caching and reactive subjects to keep controllers and view clean.
 * Values of the NavRef enum are used to represent pages. Whenever a page is to be added, it should also be added to this enum. Caching and navigation are done dynamically.
 * This service only keeps the collection and returns and pushes values based on the input.
 */
public class NavService {

    /**
     * Singleton instance
     */
    private static NavService INSTANCE;

    /**
     * Returns an instance of the NavService singleton.
     *
     * @return NavService.
     */
    public static NavService get() {
        if (INSTANCE == null) {
            INSTANCE = new NavService();
        }
        return INSTANCE;
    }

    /**
     * Page cache.
     */
    private final Map<NavRef, Pane> pages;
    /**
     * Page subjects. Contains 1 subject per navigation level.
     */
    private final Map<NavLevel, Subject<Pane>> subjects;
    /**
     * Subject used for title changes
     */
    private final Subject<String> titleSubject = BehaviorSubject.create();

    /**
     * Loader for FXML pages.
     */
    private final FXMLLoader loader = new FXMLLoader();

    /**
     * Creates a new instance of the NavService. Not parameters are needed as it is a standalone service.
     */
    private NavService() {
        pages = new HashMap<>();
        subjects = new HashMap<>();
        initializeSubjects();
    }

    /**
     * Dynamically and generically creates a subject per navigation level.
     */
    private void initializeSubjects() {
        Arrays.stream(NavLevel.values()).forEach(s -> subjects.put(s, BehaviorSubject.create()));
    }

    /**
     * Navigate to a certain page. Page refs can be accesses using the NavRef enum where the class ref is stored as a property #ref.
     *
     * @param ref Class reference of the page. Should only be given as a NavRef#ref.
     */
    public void navigateTo(NavRef ref) {
        var title = ref.title;

        checkIfCached(ref);//Make sure page is cached

        var page = pages.get(ref);//Get page from cache

        subjects.get(ref.level).onNext(page);

        titleSubject.onNext(title);
    }


    /**
     * Gets the Observable used to react to title changes
     *
     * @return Observable pushing new titles
     */
    public Observable<String> TitleChanger() {
        return titleSubject;
    }

    /**
     * Returns an Observable for pane changing depending on the level.
     *
     * @param level Navigation Level
     * @return Observable
     */
    public Observable<Pane> GetNavigator(NavLevel level) {
        return subjects.get(level);
    }


    /**
     * Checks if a page is already cached. If not, adds it to the cache.
     *
     * @param ref NafRev enum value that represents the page.
     */
    private void checkIfCached(NavRef ref) {
        if (!pages.containsKey(ref.title))
            try {
                var page = (Pane) FXMLLoader.load(getClass().getResource(ref.ref));
                pages.put(ref, page);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.getCause().printStackTrace();
                //  System.out.println("These devs are dumb");
            }
    }


    public void logout() {
        refresh();
        navigateTo(NavRef.LOGIN);
    }

    private void refresh() {
        var keys = this.pages.keySet().stream().filter(s -> s.level == NavLevel.MENU);
        keys.forEach(s -> this.pages.remove(this.pages.get(s)));
    }

}



