package lk.ijse.StockManagement.controller.Admin;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import lk.ijse.StockManagement.modelController.OrderController;
import lk.ijse.StockManagement.util.Navigation;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ViewOrderDetailsFromController implements Initializable {
    public Text txtOrderId;
    public Text txtArticle;
    public Text txtService;
    public Text txtQtt;
    public Text txtStorage;
    public Text txtOrderDate;

    public void closeOnAction(ActionEvent actionEvent) {
        Navigation.close(actionEvent);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setData();
    }

    private void setData() {
        try {
            ResultSet set = OrderController.getOrderById(OrderController.orderID);
            if (set.next()) {

                txtOrderId.setText(set.getString(1));
                txtService.setText(set.getString(2));
                txtArticle.setText(set.getString(3));
                txtQtt.setText(set.getString(4));
                txtStorage.setText(set.getString(5));
                txtOrderDate.setText(set.getString(6));

            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

}
