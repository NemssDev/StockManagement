package lk.ijse.StockManagement.controller.Admin;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import lk.ijse.StockManagement.modelController.StorageController;
import lk.ijse.StockManagement.to.Storage;
import lk.ijse.StockManagement.util.Navigation;
import lk.ijse.StockManagement.util.Notification;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateStorageFromController implements Initializable {
    public JFXTextField txtId;
    public JFXTextField txtName;
    public JFXTextField txtDescription;
    public JFXButton btnAdd;
    public JFXButton btn;
    @FXML
    private ComboBox<String> comboType;

    public void updateOnAction(ActionEvent actionEvent) {
        String name = txtName.getText();
        String id = txtId.getText();
        String type = comboType.getValue();
        String description = txtDescription.getText();

        if (name.isEmpty() || type.isEmpty() || description.isEmpty()) {
            Notification.notificationWARNING("Missing Fields", "Please fill in required fields.");
            return;
        }

        try {
            boolean isUpdated = StorageController.updateStorage(new Storage(
                    id,
                    name,
                    type,
                    description
            ));

            if (isUpdated) {
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


    private void setData() {

        try {
            // Assuming txtRef.getText() holds the reference you are searching for
            String searchReference = StorageController.storageID;

            ResultSet set = StorageController.getAllStorages(); // Or get by reference if needed

            boolean found = false;  // Flag to check if the reference is found

            while (set.next()) {  // Loop through all the rows
                String reference = set.getString("id");

                // Check if the current row's reference matches the search reference
                if (reference.equals(searchReference)) {
                    found = true;


                    // Set the data for this article into UI components
                    txtId.setText(reference);  // 1. reference (primary key)
                    txtName.setText(set.getString("location_name"));
                    comboType.setValue(set.getString("location_type"));
                    txtDescription.setText(set.getString("location_description"));
                    break;  // Exit the loop once the reference is found
                }
            }

            // If the reference wasn't found
            if (!found) {
                Notification.notificationWARNING("Not Found", "Storage with the given reference does not exist.");
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Notification.notificationWARNING("Error", "Failed to load storage data.");
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comboType.getItems().addAll("Bibliotheque", "Amphis", "Salles d'enseignement", "Administration", "Magasin", "Bureaux Enseignants");

        if (StorageController.storageID != null) {
            setData();
        } else {
            System.out.println("No Storage ID found.");
        }

    }


    public void close(MouseEvent mouseEvent) {
        Navigation.close(mouseEvent);
    }


}
