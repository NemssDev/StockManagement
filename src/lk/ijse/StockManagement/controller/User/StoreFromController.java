package lk.ijse.StockManagement.controller.User;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lk.ijse.StockManagement.util.Navigation;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StoreFromController implements Initializable {
    private static StoreFromController instance;
    public Text txtAllItem;
    public Text txtLimiedItem;
    public VBox vBox;
    public JFXTextField txtSearch;

    public StoreFromController() {
        instance = this;
    }

    public StoreFromController getInstance() {
        return instance;
    }

    public void homeOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.swatchNavigation("AdminDashBoardForm.fxml", actionEvent);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
