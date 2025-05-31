package lk.ijse.StockManagement.controller.Admin;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lk.ijse.StockManagement.util.Navigation;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class AdminDashBordFromController implements Initializable {
    public Text txtHour;
    public Text txtMin;
    public Text txtDate;
    public Text txtEmployeeAttendance;
    public Text txtCoaches;
    public Text txtAdminFistName;
    public Text txtAdminLastName;
    public AnchorPane anchorpane;

    public void closeMouseClick(MouseEvent mouseEvent) {
        Navigation.exit();
    }

    public void minimizMouseClick(MouseEvent mouseEvent) {
        Stage stage = (Stage) anchorpane.getScene().getWindow();
        stage.setIconified(true);
    }

    public void EmployeeOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.swatchNavigation("ArticleFrom.fxml", actionEvent);

    }

    public void ArticleOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.swatchNavigation("ArticleFrom.fxml", actionEvent);
    }

    public void SuppliersOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.swatchNavigation("SupplierFrom.fxml", actionEvent);
    }

    public void StorageOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.swatchNavigation("StorageFrom.fxml", actionEvent);
    }

    public void ServiceOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.swatchNavigation("ServiceFrom.fxml", actionEvent);
    }

    public void InventoryOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.swatchNavigation("InventoryFrom.fxml", actionEvent);
    }

    public void CommandeOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.swatchNavigation("OrderFrom.fxml", actionEvent);
    }

    public void logoutMouseClick(MouseEvent mouseEvent) throws IOException {
        Navigation.swatchNavigation("LoginFrom.fxml", mouseEvent);
    }

    private void setTime() {
        Thread clock = new Thread() {
            public void run() {
                while (true) {
                    DateFormat hour = new SimpleDateFormat("hh");
                    txtHour.setText(hour.format(new Date()));
                    DateFormat min = new SimpleDateFormat("mm");
                    txtMin.setText(min.format(new Date()));
                    try {
                        sleep(1000);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        };
        clock.start();
    }

    private void setDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMMMM ");
        txtDate.setText(simpleDateFormat.format(new Date()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setDate();
        setTime();
    }

}
