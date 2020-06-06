package fxml.SessionFeedbackData;

import controllers.DTO.FeedbackDTO;
import controllers.FeedbackController;
import domain.models.Feedback;
import io.reactivex.Observable;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import managers.ControllerManager;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SessionFeedbackDataController implements Initializable {

    @FXML
    private TableView<Feedback> feedbackTableView;


    private final Observable<Boolean> feedbackChangeListener;
    private List<Feedback> feedbacks;
    private ObservableList<Feedback> list;
    private FilteredList<Feedback> filteredList;

    private FeedbackController cont;

    private FeedbackDTO dto;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnDelete;

    @FXML
    private TextField txfAuthor;

    @FXML
    private TextArea txaMessage;

    @FXML
    private Pane backPane;

    public SessionFeedbackDataController() {
        cont = ControllerManager.getFeedbackController();
        this.feedbackChangeListener = cont.getChangeListener();
        //On User change, renew users and add to ObservableList
        this.feedbackChangeListener.subscribe(s -> {
            this.feedbacks = new ArrayList<>(cont.getFeedbacks());
            this.list.setAll(this.feedbacks);
        });

        this.feedbacks = new ArrayList<>(cont.getFeedbacks());
    }

    public void setData(Feedback feedback) {

        dto = new FeedbackDTO(feedback.getId());

        txfAuthor.setText(feedback.getAuthor().getUsername());
        txfAuthor.setDisable(true);
        txaMessage.setText(feedback.getMessage());
    }

    public void setTable(List<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
        TableColumn<Feedback, String> authorColumn = new TableColumn<>("Auteur");
//        authorColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        authorColumn.setCellValueFactory(stringFeedbackCellDataFeatures -> new ReadOnlyObjectWrapper(stringFeedbackCellDataFeatures.getValue().getAuthor().getFullName()));

        TableColumn<Feedback, String> messageColumn = new TableColumn<>("Bericht");
        messageColumn.setCellValueFactory(new PropertyValueFactory<>("Message"));

        TableColumn<Feedback, LocalDateTime> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("Date"));

        //Columns toevoegen aan tableview
        feedbackTableView.getColumns().add(authorColumn);
        feedbackTableView.getColumns().add(messageColumn);
        feedbackTableView.getColumns().add(dateColumn);

        //ObservableList linken aan tableview
        feedbackTableView.setItems(filteredList);

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
                    feedbackTableView.getSelectionModel().clearSelection();

                }
            }

        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Alle users tonen
        list = FXCollections.observableList(feedbacks);
        //list = FXCollections.emptyObservableList();
        filteredList = new FilteredList(list);

        feedbackTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                try {
                    setData(feedbackTableView.getSelectionModel().getSelectedItem());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void saveFeedback() {
        dto.message = txaMessage.getText();
        cont.updateFeedback(dto);

    }

    public void deleteFeedback() {
        cont.deleteFeedback(feedbacks.get(feedbackTableView.getSelectionModel().getSelectedIndex() + 1).getId());
    }
}
