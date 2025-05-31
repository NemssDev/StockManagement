package lk.ijse.StockManagement.controller.Admin;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import lk.ijse.StockManagement.modelController.SupplierController;
import lk.ijse.StockManagement.to.Supplier;
import lk.ijse.StockManagement.util.Navigation;
import lk.ijse.StockManagement.util.Notification;
import lk.ijse.StockManagement.util.RegexUtil;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateSupplierFromController implements Initializable {
    public JFXTextField txtId;
    public JFXTextField txtName;
    public JFXTextField txtAddress;
    public JFXTextField txtEmail;
    public JFXTextField txtPhone;

    public JFXButton btnAdd;
    public JFXButton btn;

    public void updateOnAction(ActionEvent actionEvent) {
        String name = txtName.getText();
        String id = txtId.getText();
        String address = txtAddress.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();

        if (name.isEmpty() || address.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Notification.notificationWARNING("Missing Fields", "Please fill in required fields.");
            return;
        }

        try {
            boolean isUpdated = SupplierController.updateSupplier(new Supplier(
                    id,
                    name,
                    address,
                    phone,
                    email
            ));

            if (isUpdated) {
                Notification.notification("Success", "Supplier added successfully.");
                Navigation.close(actionEvent);
                Navigation.swatchNavigation("SupplierFrom.fxml", actionEvent);
            } else {
                Notification.notificationWARNING("Error", "Failed to add supplier.");
            }
        } catch (Exception e) {

            e.printStackTrace();
            Notification.notificationWARNING("Error", "Invalid input or database error.");
        }


    }


    private void setData() {

        try {
            // Assuming txtRef.getText() holds the reference you are searching for
            String searchReference = SupplierController.supplierID;

            ResultSet set = SupplierController.getAllSuppliers(); // Or get by reference if needed

            boolean found = false;  // Flag to check if the reference is found

            while (set.next()) {  // Loop through all the rows
                String reference = set.getString("id");

                // Check if the current row's reference matches the search reference
                if (reference.equals(searchReference)) {
                    found = true;


                    // Set the data for this article into UI components
                    txtId.setText(reference);  // 1. reference (primary key)
                    txtName.setText(set.getString("name"));
                    txtAddress.setText(set.getString("address"));
                    txtEmail.setText(set.getString("email"));
                    txtPhone.setText(set.getString("phone_number"));
                    break;  // Exit the loop once the reference is found
                }
            }

            // If the reference wasn't found
            if (!found) {
                Notification.notificationWARNING("Not Found", "Supplier with the given reference does not exist.");
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Notification.notificationWARNING("Error", "Failed to load supplier data.");
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if (SupplierController.supplierID != null) {
            setData();
        } else {
            System.out.println("No Supplier ID found.");
        }

    }


    public void close(MouseEvent mouseEvent) {
        Navigation.close(mouseEvent);
    }


    public void phoneCheck(KeyEvent keyEvent) {
        RegexUtil.regex(btnAdd, txtPhone, txtPhone.getText(), "[1-9][0-9]{7}", "-fx-text-fill: white");
    }


}
