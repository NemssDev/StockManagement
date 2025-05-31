package lk.ijse.StockManagement.controller.Admin.bar;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import lk.ijse.StockManagement.controller.Admin.StorageFromController;
import lk.ijse.StockManagement.modelController.StorageController;
import lk.ijse.StockManagement.to.Storage;
import lk.ijse.StockManagement.util.Navigation;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StorageDetailBar {
    // Storage-specific fields
    public Text txtStorageID;
    public Text txtName;
    public Text txtType;
    public Text txtDescription;
    public JFXButton btnDelete;
    public JFXButton btnUpdate;
    @FXML
    private AnchorPane rootPane;

    // View storage details when clicked
    public void viewDataOnMouseClick(MouseEvent mouseEvent) throws IOException {
        StorageController.storageID = txtStorageID.getText();
        Navigation.popupNavigation("ViewStorageDetailsFrom.fxml");  // Navigate to storage details view
    }

    // Update storage details
    public void UpdateOnAction(ActionEvent actionEvent) throws IOException {
        StorageController.storageID = txtStorageID.getText(); // Use storageID
        Navigation.popupNavigation("UpdateStorageFrom.fxml");  // Navigate to storage update view
    }

    // Delete storage after confirmation
    public void deleteOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult().equals(ButtonType.YES)) {
            try {
                boolean isDeleted = StorageController.removeStorage(txtStorageID.getText());  // Use storageID for deletion
                if (isDeleted) {
                    StorageFromController.getInstance().vBox.getChildren().clear();  // Clear the current list
                    StorageFromController.getInstance().loadAllStorages();  // Reload the list of storages
                } else {
                    System.out.println("Storage not deleted!");
                }
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    // Set storage data from the database based on storageID
    public void setData(String storageID) {
        try {
            ResultSet set = (ResultSet) StorageController.getStorageById(storageID);  // Fetch storage data
            if (set.next()) {
                txtStorageID.setText(set.getString("id"));  // Set storageID
                txtName.setText(set.getString("location_name"));  // Set storage name
                txtType.setText(set.getString("location_type"));  // Set storage address
                txtDescription.setText(set.getString("location_description"));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Set storage data using Storage object
    public void setData(Storage storage) {
        txtStorageID.setText(storage.getId());  // Set storageID
        txtName.setText(storage.getName());  // Set storage name
        txtType.setText(storage.getType());  // Set storage address
        txtDescription.setText(storage.getDesciption());
    }
}
