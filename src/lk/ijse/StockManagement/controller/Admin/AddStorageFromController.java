package lk.ijse.StockManagement.controller.Admin;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import lk.ijse.StockManagement.modelController.StorageController;
import lk.ijse.StockManagement.to.Storage;
import lk.ijse.StockManagement.util.Navigation;
import lk.ijse.StockManagement.util.Notification;
import lk.ijse.StockManagement.util.RegexUtil;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class AddStorageFromController implements Initializable {

    public JFXTextField txtId;
    public JFXTextField txtName;
    public JFXTextField txtDescription;

    public JFXButton btnAdd;
    public JFXTextField txtUserName;
    public JFXTextField txtPassword;

    @FXML
    private ComboBox<String> comboType;

    public void addOnAction(ActionEvent actionEvent) {
        String name = txtName.getText();
        String id = getNextID();
        String type = comboType.getValue();
        String description = txtDescription.getText();
        if (name.isEmpty() || type.isEmpty() || description.isEmpty()) {
            Notification.notificationWARNING("Missing Fields", "Please fill in required fields.");
            return;
        }

        try {
            boolean isAdded = StorageController.addStorage(new Storage(
                    id,
                    name,
                    type,
                    description
            ));

            if (isAdded) {
                Notification.notification("Success", "Storage added successfully.");
                Navigation.close(actionEvent);
                Navigation.swatchNavigation("StorageFrom.fxml", actionEvent);
            } else {
                Notification.notificationWARNING("Error", "Failed to add storage.");
            }
        } catch (Exception e) {

            e.printStackTrace();
            Notification.notificationWARNING("Error", "Invalid input or database error.");
        }
    }


    private String getNextID() {
        try {
            ResultSet set = StorageController.getAllStorages();
            String lastID = "S000";

            while (set.next()) {
                String currentID = set.getString("id"); // 'code' should match your column name
                if (currentID.compareTo(lastID) > 0) {
                    lastID = currentID;
                }
            }

            // Extract the numeric part and increment it
            int num = Integer.parseInt(lastID.substring(1)) + 1;
            return "S" + String.format("%03d", num);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return "S001"; // fallback if there's an error
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        comboType.getItems().addAll("Bibliotheque", "Amphis", "Salles d'enseignement", "Administration", "Magasin", "Bureaux Enseignants");
    }


    public void userName(KeyEvent keyEvent) {
        RegexUtil.regex(btnAdd, txtUserName, txtUserName.getText(), "^(?!.*\\.\\.)(?!.*\\.$)[^\\W][\\w.]{0,29}$", "-fx-text-fill: white");
    }

    public void password(KeyEvent keyEvent) {
        RegexUtil.regex(btnAdd, txtPassword, txtPassword.getText(), "^(?=.{8,})(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\\s).*$", "-fx-text-fill: white");

    }

    public void close(MouseEvent mouseEvent) {
        Navigation.close(mouseEvent);
    }


}
