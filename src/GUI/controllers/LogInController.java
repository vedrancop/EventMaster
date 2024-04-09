package GUI.controllers;

import BE.Admin;
import BE.Coordinator;
import DAL.AdminDAO;
import DAL.CoordinatorDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.ResourceBundle;

public class LogInController implements Initializable {
    @FXML
    private Button btnBig;
    @FXML
    private Button logInBTN;
    @FXML
    private ImageView imageLog;
    @FXML
    private TextField ussernameLbl;
    @FXML
    private TextField passwordLbl;
    Random rand = new Random();
    private static String username = "admin";
    private static String password = "admin1";


    //When the FXML file is loaded sets background image to the one mentioned.
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image image = new Image("file:src/background.jpg");
        imageLog.setImage(image);

        passwordLbl.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    ClickLogInBTN();
                } catch (IOException | NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void ClickLogInBTN() throws IOException, NoSuchAlgorithmException {
        // Perform authentication checks
        String enteredUsername = ussernameLbl.getText();
        String enteredPassword = passwordLbl.getText();

        // Hash the entered password
        String hashedPassword = hashPassword(enteredPassword);

        AdminDAO adminDAO = new AdminDAO();
        Admin admin = adminDAO.getAdmin();

        // Check if the user is an Admin
        if (enteredUsername.equals(admin.getUsername()) && hashedPassword.equals(admin.getPassword())) {
            // Redirect to Admin interface
            // Example: Load Admin dashboard
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/EventMaster.fxml"));
            Parent root = loader.load();
            //AdminDashboardController controller = loader.getController();
            // Pass admin data to the dashboard controller if needed
            Stage primaryStage = new Stage();
            primaryStage.setScene(new Scene(root));
            primaryStage.show();

            // Close the login window
            Stage stage = (Stage) passwordLbl.getScene().getWindow();
            stage.close();
        } else {
            // Check if the user is an Event Coordinator
            CoordinatorDAO coordinatorDAO = new CoordinatorDAO();
            Coordinator coordinator = coordinatorDAO.getCoordinator();

            if (enteredUsername.equals(coordinator.getUsername()) && hashedPassword.equals(coordinator.getPassword())) {
                // Redirect to Coordinator interface
                // Example: Load Coordinator dashboard
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/EventMaster.fxml"));
                Parent root = loader.load();
                //CoordinatorDashboardController controller = loader.getController();
                // Pass coordinator data to the dashboard controller if needed
                Stage primaryStage = new Stage();
                primaryStage.setScene(new Scene(root));
                primaryStage.show();

                // Close the login window
                Stage stage = (Stage) passwordLbl.getScene().getWindow();
                stage.close();
            } else {
                showError("Incorrect username or password");
            }
        }
    }

    //Error message to display the user in case of wrong username or password.
    private void showError(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(encodedhash);
    }
}
