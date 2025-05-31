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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;


public class AddSupplierFromController implements Initializable {

    public JFXTextField txtId;
    public JFXTextField txtName;
    public JFXTextField txtAddress;
    public JFXTextField txtEmail;
    public JFXTextField txtPhone;

    public JFXButton btnAdd;
    public JFXTextField txtUserName;
    public JFXTextField txtPassword;

    public void addOnAction(ActionEvent actionEvent) {
        String name = txtName.getText();
        String id = getNextID();
        String address = txtAddress.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();
        System.out.println("name: " + name);
        System.out.println("id: " + id);
        System.out.println("address: " + address);
        System.out.println("email: " + email);
        System.out.println("phone: " + phone);
        if (name.isEmpty() || address.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Notification.notificationWARNING("Missing Fields", "Please fill in required fields.");
            return;
        }

        try {
            boolean isAdded = SupplierController.addSupplier(new Supplier(
                    id,
                    name,
                    address,
                    phone,
                    email

            ));

            if (isAdded) {
                Notification.notification("Success", "Article added successfully.");
                Navigation.close(actionEvent);
                Navigation.swatchNavigation("SupplierFrom.fxml", actionEvent);
            } else {
                Notification.notificationWARNING("Error", "Failed to add article.");
            }
        } catch (Exception e) {

            e.printStackTrace();
            Notification.notificationWARNING("Error", "Invalid input or database error.");
        }
    }

    private String getNextID() {
        try {
            ResultSet set = SupplierController.getAllSuppliers();
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
    }


    private String dateNow() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(new Date());
    }

    public void phoneCheck(KeyEvent keyEvent) {
        RegexUtil.regex(btnAdd, txtPhone, txtPhone.getText(), "[1-9][0-9]{7}", "-fx-text-fill: white");
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
