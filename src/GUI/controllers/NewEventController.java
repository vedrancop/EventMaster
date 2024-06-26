package GUI.controllers;

import BE.Event;
import BLL.EventLogic;
import BLL.Notifications;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class NewEventController {
    @FXML
    private TextField eventName;
    @FXML
    private TextField eventLoc;
    @FXML
    private TextField eventStart;
    @FXML
    private TextField eventDescription;
    @FXML
    private Label imageUrl;
    ArrayList<Image> eventImages = new ArrayList<>();
    EventLogic el=new EventLogic();
    private EventMasterController emc;
    private Notifications nt=new Notifications();


    public void setEventMasterController(EventMasterController eventMasterController) {
        this.emc = eventMasterController;
    }
    //Creates event.
    public void createEvent(ActionEvent actionEvent) throws IOException, SQLException {
        String time = eventStart.getText();
        String location = eventLoc.getText();
        String description = eventDescription.getText();
        String name = eventName.getText();
        if(fieldCheck()) {
            Event event = new Event(time, description, location, name);
            el.createEvent(event);
            emc.addEvent(event, eventImages);
            emc.updateUIMain(el.getAllEvents());
            nt.showSuccess("Successfully created an event");

            Stage currentStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            currentStage.close();
        }else {
            nt.showError("Please fill out all of the fields");
        }
    }


    //Closes the create event FXML file.
    public void cancelEvent(ActionEvent actionEvent) {
        Stage currentStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        currentStage.close();
    }


    //On click loades the choosen image.
    public void loadEventImage(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {

            Image image = new Image(file.toURI().toString());
            imageUrl.setText(image.getUrl());

            eventImages.add(image);
        }
    }

    public boolean fieldCheck(){
        if (!eventStart.getText().isEmpty() && !eventLoc.getText().isEmpty()&&!eventName.getText().isEmpty()){
            return true;
        }

        return false;

    }


}
