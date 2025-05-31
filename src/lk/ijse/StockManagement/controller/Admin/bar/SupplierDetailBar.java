package lk.ijse.StockManagement.controller.Admin.bar;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import lk.ijse.StockManagement.controller.Admin.SupplierFromController;
import lk.ijse.StockManagement.modelController.SupplierController;
import lk.ijse.StockManagement.to.Supplier;
import lk.ijse.StockManagement.util.Navigation;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SupplierDetailBar {
    // Supplier-specific fields
    public Text txtSupplierID;
    public Text txtName;
    public Text txtAddress;
    public Text txtPhone;
    public Text txtEmail;
    public JFXButton btnDelete;
    public JFXButton btnUpdate;
    @FXML
    private AnchorPane rootPane;

    // View supplier details when clicked
    public void viewDataOnMouseClick(MouseEvent mouseEvent) throws IOException {
        SupplierController.supplierID = txtSupplierID.getText();
        Navigation.popupNavigation("ViewSupplierDetailsFrom.fxml");  // Navigate to supplier details view
    }

    // Update supplier details
    public void UpdateOnAction(ActionEvent actionEvent) throws IOException {
        SupplierController.supplierID = txtSupplierID.getText(); // Use supplierID
        Navigation.popupNavigation("UpdateSupplierFrom.fxml");  // Navigate to supplier update view
    }

    // Delete supplier after confirmation
    public void deleteOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult().equals(ButtonType.YES)) {
            try {
                boolean isDeleted = SupplierController.removeSupplier(txtSupplierID.getText());  // Use supplierID for deletion
                if (isDeleted) {
                    SupplierFromController.getInstance().vBox.getChildren().clear();  // Clear the current list
                    SupplierFromController.getInstance().loadAllSuppliers();  // Reload the list of suppliers
                } else {
                    System.out.println("Supplier not deleted!");
                }
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    // Set supplier data from the database based on supplierID
    public void setData(String supplierID) {
        try {
            ResultSet set = (ResultSet) SupplierController.getSupplierById(supplierID);  // Fetch supplier data
            if (set.next()) {
                txtSupplierID.setText(set.getString("id"));  // Set supplierID
                txtName.setText(set.getString("name"));  // Set supplier name
                txtAddress.setText(set.getString("address"));  // Set supplier address
                txtPhone.setText(set.getString("phone_number"));  // Set supplier phone number
                txtEmail.setText(set.getString("email"));  // Set supplier email
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Set supplier data using Supplier object
    public void setData(Supplier supplier) {
        txtSupplierID.setText(supplier.getId());  // Set supplierID
        txtName.setText(supplier.getName());  // Set supplier name
        txtAddress.setText(supplier.getAddress());  // Set supplier address
        txtPhone.setText(supplier.getPhoneNumber());  // Set supplier phone number
        txtEmail.setText(supplier.getEmail());  // Set supplier email
    }
}
