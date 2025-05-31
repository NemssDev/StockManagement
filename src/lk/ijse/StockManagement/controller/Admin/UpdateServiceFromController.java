package lk.ijse.StockManagement.controller.Admin;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import lk.ijse.StockManagement.modelController.ServiceController;
import lk.ijse.StockManagement.to.Service;
import lk.ijse.StockManagement.util.Navigation;
import lk.ijse.StockManagement.util.Notification;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateServiceFromController implements Initializable {
    public JFXTextField txtId;
    public JFXTextField txtName;
    public JFXTextField txtType;
    public JFXTextField txtDescription;

    public JFXButton btnAdd;
    public JFXButton btn;

    public void updateOnAction(ActionEvent actionEvent) {
        String name = txtName.getText();
        String id = txtId.getText();
        String type = txtType.getText();
        String description = txtDescription.getText();

        if (name.isEmpty() || type.isEmpty() || description.isEmpty()) {
            Notification.notificationWARNING("Missing Fields", "Please fill in required fields.");
            return;
        }

        try {
            boolean isUpdated = ServiceController.updateService(new Service(
                    id,
                    name,
                    type,
                    description
            ));

            if (isUpdated) {
                Notification.notification("Success", "Service added successfully.");
                Navigation.close(actionEvent);
                Navigation.swatchNavigation("ServiceFrom.fxml", actionEvent);
            } else {
                Notification.notificationWARNING("Error", "Failed to add service.");
            }
        } catch (Exception e) {

            e.printStackTrace();
            Notification.notificationWARNING("Error", "Invalid input or database error.");
        }


    }


    private void setData() {

        try {
            // Assuming txtRef.getText() holds the reference you are searching for
            String searchReference = ServiceController.serviceID;

            ResultSet set = ServiceController.getAllServices(); // Or get by reference if needed

            boolean found = false;  // Flag to check if the reference is found

            while (set.next()) {  // Loop through all the rows
                String reference = set.getString("id");

                // Check if the current row's reference matches the search reference
                if (reference.equals(searchReference)) {
                    found = true;


                    // Set the data for this article into UI components
                    txtId.setText(reference);  // 1. reference (primary key)
                    txtName.setText(set.getString("name"));
                    txtType.setText(set.getString("type"));
                    txtDescription.setText(set.getString("description"));
                    break;  // Exit the loop once the reference is found
                }
            }

            // If the reference wasn't found
            if (!found) {
                Notification.notificationWARNING("Not Found", "Service with the given reference does not exist.");
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Notification.notificationWARNING("Error", "Failed to load service data.");
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if (ServiceController.serviceID != null) {
            setData();
        } else {
            System.out.println("No Service ID found.");
        }

    }


    public void close(MouseEvent mouseEvent) {
        Navigation.close(mouseEvent);
    }


}
