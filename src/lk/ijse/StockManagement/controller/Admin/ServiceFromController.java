package lk.ijse.StockManagement.controller.Admin;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lk.ijse.StockManagement.controller.User.StoreFromController;
import lk.ijse.StockManagement.modelController.ServiceController;
import lk.ijse.StockManagement.util.Navigation;
import lombok.Getter;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class ServiceFromController implements Initializable {
    @Getter
    private static ServiceFromController instance;
    public VBox vBox;
    public Text txtTodayAttendance;
    public JFXTextField search;
    public JFXDatePicker selectDate;
    public JFXRadioButton rBtnAllServices;
    public Text or;
    public Text txtOptionTime;
    public ScrollPane scrollPane;
    public Text txtallServices;

    public AnchorPane anchorpane;

    ArrayList<String> month = new ArrayList<>();

    public ServiceFromController() {
        instance = this;
    }

    // Load all Services from ServiceController
    public void loadAllServices() {
        vBox.getChildren().clear();
        try {
            List<String> Services = ServiceController.loadAllServices();
            txtallServices.setText(String.valueOf(Services.size()));


            for (String reference : Services) {
                loadServiceData(reference);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadServiceData(String ServiceReference) {
        try {
            FXMLLoader loader = new FXMLLoader(StoreFromController.class.getResource("/lk/ijse/StockManagement/view/bar/ServiceDetailBar.fxml"));
            Parent root = loader.load();
            lk.ijse.StockManagement.controller.Admin.bar.ServiceDetailBar controller = loader.getController();
            controller.setData(ServiceReference);
            vBox.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        scrollPane.lookup(".scroll-pane").setStyle("-fx-background-color: transparent");
        setSelectMonth();

        loadAllServices(); // Load on init
        rBtnAllServices.setSelected(true);
        if (rBtnAllServices.isSelected()) {
            or.setVisible(false);
            selectDate.setVisible(false);
            selectDate.setDisable(true);
        }
    }

    public void searchOnKeyReleased(KeyEvent keyEvent) {
        vBox.getChildren().clear();
        try {
            if (search.getText().isEmpty()) {
                loadAllServices();
            } else {
                List<String> matchedServices = ServiceController.searchServices(search.getText());
                for (String reference : matchedServices) {
                    loadServiceData(reference);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void backOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.swatchNavigation("AdminDashBoardForm.fxml", actionEvent);
    }

    public void newServiceOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.popupNavigation("AddServiceFrom.fxml");


    }

    public void update(ActionEvent actionEvent) throws IOException {
        loadAllServices();

    }

    public void allServicesOnAction(ActionEvent actionEvent) {
        if (rBtnAllServices.isSelected()) {
            loadAllServices();
        }
    }

    private void setSelectMonth() {
        if (month.isEmpty()) {
            month.add("");
            month.add("January");
            month.add("February");
            month.add("March");
            month.add("April");
            month.add("May");
            month.add("June");
            month.add("July");
            month.add("August");
            month.add("September");
            month.add("October");
            month.add("November");
            month.add("December");
        }
    }

    public void closeMouseClick(MouseEvent mouseEvent) {
        Navigation.exit();
    }

    public void minimizMouseClick(MouseEvent mouseEvent) {
        Stage stage = (Stage) anchorpane.getScene().getWindow();
        stage.setIconified(true);
    }

}
