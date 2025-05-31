package lk.ijse.StockManagement.controller.Admin.bar;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import lk.ijse.StockManagement.controller.Admin.ServiceFromController;
import lk.ijse.StockManagement.modelController.ServiceController;
import lk.ijse.StockManagement.to.Service;
import lk.ijse.StockManagement.util.Navigation;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceDetailBar {
    // Service-specific fields
    public Text txtServiceID;
    public Text txtName;
    public Text txtType;
    public Text txtDescription;
    public JFXButton btnDelete;
    public JFXButton btnUpdate;
    @FXML
    private AnchorPane rootPane;

    // View service details when clicked
    public void viewDataOnMouseClick(MouseEvent mouseEvent) throws IOException {
        ServiceController.serviceID = txtServiceID.getText();
        Navigation.popupNavigation("ViewServiceDetailsFrom.fxml");  // Navigate to service details view
    }

    // Update service details
    public void UpdateOnAction(ActionEvent actionEvent) throws IOException {
        ServiceController.serviceID = txtServiceID.getText(); // Use serviceID
        Navigation.popupNavigation("UpdateServiceFrom.fxml");  // Navigate to service update view
    }

    // Delete service after confirmation
    public void deleteOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult().equals(ButtonType.YES)) {
            try {
                boolean isDeleted = ServiceController.removeService(txtServiceID.getText());  // Use serviceID for deletion
                if (isDeleted) {
                    ServiceFromController.getInstance().vBox.getChildren().clear();  // Clear the current list
                    ServiceFromController.getInstance().loadAllServices();  // Reload the list of services
                } else {
                    System.out.println("Service not deleted!");
                }
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    // Set service data from the database based on serviceID
    public void setData(String serviceID) {
        try {
            ResultSet set = (ResultSet) ServiceController.getServiceById(serviceID);  // Fetch service data
            if (set.next()) {
                txtServiceID.setText(set.getString("id"));  // Set serviceID
                txtName.setText(set.getString("name"));  // Set service name
                txtType.setText(set.getString("type"));  // Set service address
                txtDescription.setText(set.getString("description"));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Set service data using Service object
    public void setData(Service service) {
        txtServiceID.setText(service.getId());  // Set serviceID
        txtName.setText(service.getName());  // Set service name
        txtType.setText(service.getType());  // Set service address
        txtDescription.setText(service.getDesciption());
    }
}
