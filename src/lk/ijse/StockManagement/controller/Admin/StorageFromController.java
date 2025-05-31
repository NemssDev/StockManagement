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
import lk.ijse.StockManagement.modelController.StorageController;
import lk.ijse.StockManagement.util.Navigation;
import lombok.Getter;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class StorageFromController implements Initializable {
    @Getter
    private static StorageFromController instance;
    public VBox vBox;
    public Text txtTodayAttendance;
    public JFXTextField search;
    public JFXDatePicker selectDate;
    public JFXRadioButton rBtnAllStorages;
    public Text or;
    public Text txtOptionTime;
    public ScrollPane scrollPane;
    public Text txtallStorages;

    public AnchorPane anchorpane;

    ArrayList<String> month = new ArrayList<>();

    public StorageFromController() {
        instance = this;
    }

    // Load all Storages from StorageController
    public void loadAllStorages() {
        vBox.getChildren().clear();
        try {
            List<String> Storages = StorageController.loadAllStorages();
            txtallStorages.setText(String.valueOf(Storages.size()));


            for (String reference : Storages) {
                loadStorageData(reference);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadStorageData(String StorageReference) {
        try {
            FXMLLoader loader = new FXMLLoader(StoreFromController.class.getResource("/lk/ijse/StockManagement/view/bar/StorageDetailBar.fxml"));
            Parent root = loader.load();
            lk.ijse.StockManagement.controller.Admin.bar.StorageDetailBar controller = loader.getController();
            controller.setData(StorageReference);
            vBox.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        scrollPane.lookup(".scroll-pane").setStyle("-fx-background-color: transparent");
        setSelectMonth();

        loadAllStorages(); // Load on init
        rBtnAllStorages.setSelected(true);
        if (rBtnAllStorages.isSelected()) {
            or.setVisible(false);
            selectDate.setVisible(false);
            selectDate.setDisable(true);
        }
    }

    public void searchOnKeyReleased(KeyEvent keyEvent) {
        vBox.getChildren().clear();
        try {
            if (search.getText().isEmpty()) {
                loadAllStorages();
            } else {
                List<String> matchedStorages = StorageController.searchStorages(search.getText());
                for (String reference : matchedStorages) {
                    loadStorageData(reference);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void backOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.swatchNavigation("AdminDashBoardForm.fxml", actionEvent);
    }

    public void newStorageOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.popupNavigation("AddStorageFrom.fxml");


    }

    public void update(ActionEvent actionEvent) throws IOException {
        loadAllStorages();

    }

    public void allStoragesOnAction(ActionEvent actionEvent) {
        if (rBtnAllStorages.isSelected()) {
            loadAllStorages();
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
