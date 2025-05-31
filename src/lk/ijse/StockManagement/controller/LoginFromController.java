package lk.ijse.StockManagement.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lk.ijse.StockManagement.util.CrudUtil;
import lk.ijse.StockManagement.util.Navigation;
import lk.ijse.StockManagement.util.Notification;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginFromController implements Initializable {


    public JFXTextField txtUserName;
    public JFXPasswordField txtPassword;
    public Text txtWarning;
    public JFXButton btn;
    public AnchorPane anchorpane;


    public void loginOnAction(ActionEvent actionEvent) throws IOException {

        try {
            ResultSet resultSet = CrudUtil.crudUtil("SELECT * FROM users WHERE username=? AND password=?", txtUserName.getText(), txtPassword.getText());
            if (resultSet.next()) {
                String role = resultSet.getString(6);
                if (role.equals("Admin")) {
                    Navigation.swatchNavigation("AdminDashBoardForm.fxml", actionEvent);
                    Notification.notification("Login Successful  ", " your Login Successful ");
                }
                if (role.equals("Magasinier")) {
                    Navigation.swatchNavigation("AdminDashBoardForm.fxml", actionEvent);
                    Notification.notification("Login Successful  ", " your Login Successful ");
                }
            } else {
                Notification.notificationWARNING("Wrong Infromation Details", "Check your Username and your Password");
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void user(ActionEvent actionEvent) {
        txtPassword.requestFocus();
    }

    public void password(ActionEvent actionEvent) {
        btn.fire();
    }

    public void closeMouseClick(MouseEvent mouseEvent) {
        Navigation.exit();
    }

    public void minimizMouseClick(MouseEvent mouseEvent) {
        Stage stage = (Stage) anchorpane.getScene().getWindow();
        stage.setIconified(true);
    }
}
