package lk.ijse.StockManagement.controller.Admin.bar;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import lk.ijse.StockManagement.controller.Admin.InventoryFromController;
import lk.ijse.StockManagement.modelController.InventoryController;
import lk.ijse.StockManagement.to.Item;
import lk.ijse.StockManagement.util.Navigation;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InventoryDetailBar {
    // Service-specific fields
    public Text txtItemID;
    public Text txtName;
    public Text txtQtt;
    public Text txtLocation;
    public Text txtCond;
    public JFXButton btnDelete;
    public JFXButton btnUpdate;
    @FXML
    private AnchorPane rootPane;

    // View service details when clicked
    public void viewDataOnMouseClick(MouseEvent mouseEvent) throws IOException {
        InventoryController.itemID = txtItemID.getText();
        Navigation.popupNavigation("ViewItemDetailsFrom.fxml");  // Navigate to service details view
    }

    // Update service details
    public void UpdateOnAction(ActionEvent actionEvent) throws IOException {
        InventoryController.itemID = txtItemID.getText(); // Use serviceID
        Navigation.popupNavigation("UpdateItemFrom.fxml");  // Navigate to service update view
    }

    // Delete service after confirmation
    public void deleteOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult().equals(ButtonType.YES)) {
            try {
                boolean isDeleted = InventoryController.removeItem(txtItemID.getText());  // Use serviceID for deletion
                if (isDeleted) {
                    InventoryFromController.getInstance().vBox.getChildren().clear();  // Clear the current list
                    InventoryFromController.getInstance().loadAllItems();  // Reload the list of services
                } else {
                    System.out.println("Item not deleted!");
                }
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    // Set service data from the database based on serviceID
    public void setData(String itemID) {
        try {
            ResultSet set = (ResultSet) InventoryController.getItemById(itemID);  // Fetch service data
            if (set.next()) {
                txtItemID.setText(set.getString("id"));  // Set serviceID
                txtName.setText(set.getString("name"));  // Set service name
                txtQtt.setText(set.getString("quantity"));  // Set service address
                txtLocation.setText(set.getString("location"));
                txtCond.setText(set.getString("item_condition"));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Set service data using Service object
    public void setData(Item item) {
        txtItemID.setText(item.getId());  // Set itemID
        txtName.setText(item.getName());  // Set item name
        txtQtt.setText(item.getQuantity());  // Set item address
        txtLocation.setText(item.getLocation());
        txtCond.setText(item.getCondtion());
    }
}
