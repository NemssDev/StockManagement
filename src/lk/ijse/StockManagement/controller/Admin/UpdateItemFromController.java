package lk.ijse.StockManagement.controller.Admin;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import lk.ijse.StockManagement.modelController.InventoryController;
import lk.ijse.StockManagement.to.Item;
import lk.ijse.StockManagement.util.Navigation;
import lk.ijse.StockManagement.util.Notification;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateItemFromController implements Initializable {
    public JFXTextField txtId;
    public JFXTextField txtName;
    public JFXTextField txtQtt;
    public JFXTextField txtLoc;
    public ComboBox<String> comboCond;

    public JFXButton btnAdd;
    public JFXButton btn;

    public void updateOnAction(ActionEvent actionEvent) {
        String name = txtName.getText();
        String id = txtId.getText();
        String qtt = txtQtt.getText();
        String loc = txtLoc.getText();
        String cond = comboCond.getValue();
        if (name.isEmpty() || qtt.isEmpty() || loc.isEmpty()) {
            Notification.notificationWARNING("Missing Fields", "Please fill in required fields.");
            return;
        } else if (Integer.parseInt(qtt) < 0) {
            Notification.notificationWARNING("Quantity Check", "Quantity Should be positive!");
            return;


        }

        try {
            boolean isUpdated = InventoryController.updateItem(new Item(
                    id,
                    name,
                    qtt,
                    loc,
                    cond
            ));

            if (isUpdated) {
                Notification.notification("Success", "Item updated successfully.");
                Navigation.close(actionEvent);
                Navigation.swatchNavigation("InventoryFrom.fxml", actionEvent);
            } else {
                Notification.notificationWARNING("Error", "Failed to update item.");
            }
        } catch (Exception e) {

            e.printStackTrace();
            Notification.notificationWARNING("Error", "Invalid input or database error.");
        }


    }


    private void setData() {

        try {

            // Assuming txtRef.getText() holds the reference you are searching for
            String searchReference = InventoryController.itemID;

            ResultSet set = InventoryController.getAllItems(); // Or get by reference if needed

            boolean found = false;  // Flag to check if the reference is found

            while (set.next()) {  // Loop through all the rows
                String reference = set.getString("id");

                // Check if the current row's reference matches the search reference
                if (reference.equals(searchReference)) {
                    found = true;


                    // Set the data for this article into UI components
                    txtId.setText(reference);  // 1. reference (primary key)
                    txtName.setText(set.getString("name"));
                    txtQtt.setText(set.getString("quantity"));
                    txtLoc.setText(set.getString("location"));
                    comboCond.setValue(set.getString("item_condition"));
                    break;  // Exit the loop once the reference is found
                }
            }

            // If the reference wasn't found
            if (!found) {
                Notification.notificationWARNING("Not Found", "Item with the given reference does not exist.");
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Notification.notificationWARNING("Error", "Failed to load service data.");
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comboCond.getItems().addAll("NEW", "USED", "DAMAGED", "UNDER_MAINTENANCE", "DISPOSED");

        if (InventoryController.itemID != null) {
            setData();
        } else {
            System.out.println("No Item ID found.");
        }

    }


    public void close(MouseEvent mouseEvent) {
        Navigation.close(mouseEvent);
    }


}
