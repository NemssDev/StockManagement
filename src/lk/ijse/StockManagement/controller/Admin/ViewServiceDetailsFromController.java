package lk.ijse.StockManagement.controller.Admin;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lk.ijse.StockManagement.modelController.ServiceController;
import lk.ijse.StockManagement.util.Navigation;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ViewServiceDetailsFromController implements Initializable {
    public Text txtId;
    public Text txtName;
    public Text txtType;
    public Text txtDescription;

    public void closeOnAction(ActionEvent actionEvent) {
        Navigation.close(actionEvent);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setData();
    }

    private void setData() {
        try {
            ResultSet set = ServiceController.getServiceById(ServiceController.serviceID);
            if (set.next()) {

                txtId.setText(set.getString(1));
                txtName.setText(set.getString(2));
                txtType.setText(set.getString(3));
                txtDescription.setText(set.getString(4));

            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void UpdateOnAction(ActionEvent actionEvent) throws IOException {
        ServiceController.serviceID = txtId.getText();  // Use articleID
        Navigation.swatchNavigation("UpdateServiceFrom.fxml", actionEvent);  // Navigate to article update view
    }

    public void deleteOnAction(ActionEvent actionEvent) throws IOException {
        ServiceController.serviceID = txtId.getText();
        Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.setAlwaysOnTop(true);
        alert.showAndWait();
        if (alert.getResult().equals(ButtonType.YES)) {
            try {
                boolean isDeleted = ServiceController.removeService(txtId.getText());  // Use articleID for deletion
                if (isDeleted) {
                    ServiceFromController.getInstance().vBox.getChildren().clear();  // Clear the current list
                    ServiceFromController.getInstance().loadAllServices();  // Reload the list of articles
                    Navigation.close(actionEvent);

                } else {
                    System.out.println("Article not deleted!");
                }
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
