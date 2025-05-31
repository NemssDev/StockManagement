package lk.ijse.StockManagement.controller.Admin;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lk.ijse.StockManagement.modelController.SupplierController;
import lk.ijse.StockManagement.util.Navigation;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ViewSupplierDetailsFromController implements Initializable {
    public Text txtId;
    public Text txtName;
    public Text txtAddress;
    public Text txtEmail;
    public Text txtPhone;

    public void closeOnAction(ActionEvent actionEvent) {
        Navigation.close(actionEvent);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setData();
    }

    private void setData() {
        try {
            ResultSet set = SupplierController.getSupplierById(SupplierController.supplierID);
            if (set.next()) {
                txtId.setText(set.getString(1));
                txtName.setText(set.getString(2));
                txtAddress.setText(set.getString(3));
                txtPhone.setText(set.getString(4));
                txtEmail.setText(set.getString(5));


            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void UpdateOnAction(ActionEvent actionEvent) throws IOException {
        SupplierController.supplierID = txtId.getText();  // Use articleID
        Navigation.swatchNavigation("UpdateSupplierFrom.fxml", actionEvent);  // Navigate to article update view
    }

    public void deleteOnAction(ActionEvent actionEvent) throws IOException {
        SupplierController.supplierID = txtId.getText();
        Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.setAlwaysOnTop(true);
        alert.showAndWait();
        if (alert.getResult().equals(ButtonType.YES)) {
            try {
                boolean isDeleted = SupplierController.removeSupplier(txtId.getText());  // Use articleID for deletion
                if (isDeleted) {
                    SupplierFromController.getInstance().vBox.getChildren().clear();  // Clear the current list
                    SupplierFromController.getInstance().loadAllSuppliers();  // Reload the list of articles
                    Navigation.close(actionEvent);

                } else {
                    System.out.println("Supplier not deleted!");
                }
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
