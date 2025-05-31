package lk.ijse.StockManagement.controller.Admin;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lk.ijse.StockManagement.modelController.InventoryController;
import lk.ijse.StockManagement.util.Navigation;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ViewItemDetailsFromController implements Initializable {
    public Text txtId;
    public Text txtName;
    public Text txtQtt;
    public Text txtLoc;
    public Text txtCond;
    public Text txtLastUpd;

    public void closeOnAction(ActionEvent actionEvent) {
        Navigation.close(actionEvent);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setData();
    }

    private void setData() {
        try {
            ResultSet set = InventoryController.getItemById(InventoryController.itemID);
            if (set.next()) {

                txtId.setText(set.getString(1));
                txtName.setText(set.getString(2));
                txtQtt.setText(set.getString(3));
                txtLoc.setText(set.getString(4));
                txtCond.setText(set.getString(5));
                txtLastUpd.setText(set.getString(6));

            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void UpdateOnAction(ActionEvent actionEvent) throws IOException {
        InventoryController.itemID = txtId.getText();  // Use articleID
        Navigation.swatchNavigation("UpdateItemFrom.fxml", actionEvent);  // Navigate to article update view
    }

    public void deleteOnAction(ActionEvent actionEvent) throws IOException {
        InventoryController.itemID = txtId.getText();
        Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.setAlwaysOnTop(true);
        alert.showAndWait();
        if (alert.getResult().equals(ButtonType.YES)) {
            try {
                boolean isDeleted = InventoryController.removeItem(txtId.getText());  // Use articleID for deletion
                if (isDeleted) {
                    InventoryFromController.getInstance().vBox.getChildren().clear();  // Clear the current list
                    InventoryFromController.getInstance().loadAllItems();  // Reload the list of articles
                    Navigation.close(actionEvent);

                } else {
                    System.out.println("Item not deleted!");
                }
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
