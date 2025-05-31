package lk.ijse.StockManagement.controller.Admin.bar;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import lk.ijse.StockManagement.controller.Admin.OrderFromController;
import lk.ijse.StockManagement.modelController.OrderController;
import lk.ijse.StockManagement.to.Order;
import lk.ijse.StockManagement.util.Navigation;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderDetailBar {
    // Article-specific fields
    public Text txtOrderID;
    public Text txtArticle;
    public Text txtService;
    public Text txtQuantity;
    public Text txtStorage;
    public Text txtDate;
    public JFXButton btnDelete;
    public JFXButton btnUpdate;
    private String reference;
    @FXML
    private AnchorPane rootPane;

    // View article details when clicked
    public void viewDataOnMouseClick(MouseEvent mouseEvent) throws IOException {
        OrderController.orderID = txtOrderID.getText();  // Use articleID
        Navigation.popupNavigation("ViewOrderDetailsFrom.fxml");  // Navigate to article details view
    }

    // Update article details
    public void UpdateOnAction(ActionEvent actionEvent) throws IOException {
        OrderController.orderID = txtOrderID.getText();  // Use articleID
        Navigation.popupNavigation("UpdateOrderFrom.fxml");  // Navigate to article update view
    }

    // Delete article after confirmation
    public void deleteOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult().equals(ButtonType.YES)) {
            try {
                boolean isDeleted = OrderController.removeOrder(txtOrderID.getText());  // Use articleID for deletion
                if (isDeleted) {
                    OrderFromController.getInstance().vBox.getChildren().clear();  // Clear the current list
                    OrderFromController.getInstance().loadAllOrders();  // Reload the list of articles
                } else {
                    System.out.println("Order not deleted!");
                }
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    // Set article data from the database based on articleID
    public void setData(String orderID) {
        this.reference = reference;
        try {
            ResultSet set = OrderController.getOrderById(orderID);  // Use OrderController to fetch article data
            if (set.next()) {
                txtOrderID.setText(set.getString("order_id"));  // Set articleID
                txtArticle.setText(set.getString("article"));  // Set article designation
                txtService.setText(set.getString("service"));  // Set article category
                txtQuantity.setText(set.getString("quantity"));  // Set article price
                txtStorage.setText(set.getString("to_storage"));  // Set article price
                txtDate.setText(set.getString("order_date"));  // Set article price


            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    // Set article data using Article object
    public void setData(Order order) {
        txtOrderID.setText(order.getOrder_id());  // Set articleID
        txtArticle.setText(order.getArticle());  // Set article designation
        txtService.setText(order.getService());  // Set article category
        txtQuantity.setText(String.valueOf(order.getQuantity()));  // Set article price
        txtStorage.setText(String.valueOf(order.getFromStorage()));
        txtDate.setText(order.getOrder_date());// Set article price


    }

    public void initialize() {
        //checkExpiration();
    }

    public String getOrderID() {
        return txtOrderID.getText();
    }

    public String getArticle() {
        return txtArticle.getText();
    }

    public String getService() {
        return txtService.getText();
    }

    public String getQuantity() {
        return txtQuantity.getText();
    }

    public String getStorage() {
        return txtStorage.getText();
    }

    public String getDate() {
        return txtDate.getText();
    }

    // Show delete and update buttons when mouse enters the area
}
