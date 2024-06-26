package GUI.controllers;

import BE.Coordinator;
import BE.Event;
import BLL.CoordinatorLogic;
import BLL.EventEvCoLogic;
import BLL.EventLogic;
import BLL.Notifications;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.stage.Window;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IEController {
    public Label coordinatorLbl;
    public Button AssignCo;
    @FXML
    private ImageView infoImage;
    @FXML
    private Label nameInfo;
    @FXML
    private Label locationInfo;
    @FXML
    private Label timendateInfo;
    @FXML
    private Label descInfo;
    private Event selectedEvent;
    EventLogic el=new EventLogic();

    EventEvCoLogic eventevcoLogic = new EventEvCoLogic();
    CoordinatorLogic coorLogic = new CoordinatorLogic();

    private EventMasterController emc;
    Notifications nt=new Notifications();
    private ObjectProperty<Event> updatedEventProperty = new SimpleObjectProperty<>(null);


    public void setEventMasterController(EventMasterController eventMasterController) {
        this.emc=eventMasterController;
    }
    /**
     * On click closes info FXML
     */
    public void closeInfo(ActionEvent actionEvent) {
        Stage currentStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        currentStage.close();
    }

    /**
     * Sets the passed event as a selected event.
     * @param event
     */
    public void setEvent(Event event) {
        this.selectedEvent = event;
        updateUIInfo();
    }

    /**
     * Updates labels with info corresponding to the selected event.
     */
    public void updateUIInfo() {
        if (selectedEvent != null) {
            nameInfo.setText(selectedEvent.getName());
            locationInfo.setText(selectedEvent.getLocation());
            timendateInfo.setText(selectedEvent.getTime());
            descInfo.setText(selectedEvent.getDescription());
            final String[] text = {coordinatorLbl.getText()};
            eventevcoLogic.getByEvent(selectedEvent.getId()).forEach( coorId ->{
                text[0] = text[0] + coorLogic.getCoordinatorbyId(coorId) + " ";
            });
            coordinatorLbl.setText(text[0]);
        }
    }

    /**
     * Open EditEventController for editing the selected event.
     */
    public void editEvent(ActionEvent actionEvent) throws IOException {
        if (LogInController.loggedUser == 1) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/EditEvent.fxml"));
            Parent root = loader.load();


            EditEventController editEventController = loader.getController();
            editEventController.setSelectedEvent(selectedEvent); // Pass the selected event
            editEventController.setEventInfoController(this); // Pass instance of IEController

            Stage primaryStage = new Stage();
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }
        else{
            showError("An Admin cannot edit Events");
        }
    }
    /**
     * Set the updated event and notify listeners.
     */

    public void updateEvent(List<Event> events) throws SQLException{
        emc.updateUIMain(events);
    }

    /**
     * Deletes the event.
     * @param actionEvent
     */
    public void deleteEvent(ActionEvent actionEvent) throws SQLException, InterruptedException {
        if (selectedEvent != null) {
            Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationDialog.setTitle("Confirmation");
            confirmationDialog.setHeaderText("Are you sure you want to delete this event?");
            confirmationDialog.setContentText("Any unsaved changes will be lost.");
            Optional<ButtonType> result = confirmationDialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {

                el.deleteEvent(selectedEvent);

                emc.removeEvent(selectedEvent);

                emc.updateUIMain(el.getAllEvents());

                Stage currentStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                currentStage.close();
                nt.showSuccess("Successfully deleted the event");
            }
        }
    }

    /**
     * Opens FXML file for choosing ticket type and creating it.
     * @param actionEvent
     */
    public void genTicket(ActionEvent actionEvent) throws IOException {
        if(LogInController.loggedUser == 1) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/TicketGen.fxml"));
            Parent root = loader.load();

            TGController controller = loader.getController();
            controller.setInfoEventController(this);

            Stage primaryStage = new Stage();
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }
        else {
            showError("An Admin cannot generate tickets");
        }
    }


    //Dont forget to make them not be able to be duplicated
    public void assignCoordinator(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/ViewCoordinator.fxml"));
        Parent root = loader.load();
        ViewCoordinator viewCoordinator = loader.getController();
        viewCoordinator.setIEController(this);
        Stage primaryStage = new Stage();
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public void addEvCo(Coordinator coor){
        int eventId = selectedEvent.getId();
        int coorId = coor.getId();
        eventevcoLogic.createEventEvCo(eventId, coorId);
        String prevText = coordinatorLbl.getText();
        String newText = prevText + " " + coor.getUsername();
        coordinatorLbl.setText(newText);
    }
    public ArrayList<Integer> getEventID(){
        int eventId = selectedEvent.getId();
        return el.getEventID(eventId);
    }
    private void showError(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
}
