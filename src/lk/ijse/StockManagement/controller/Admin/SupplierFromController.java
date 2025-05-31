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
import lk.ijse.StockManagement.modelController.SupplierController;
import lk.ijse.StockManagement.util.Navigation;
import lombok.Getter;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SupplierFromController implements Initializable {
    @Getter
    private static SupplierFromController instance;
    public VBox vBox;
    public Text txtTodayAttendance;
    public JFXTextField search;
    public JFXDatePicker selectDate;
    public JFXRadioButton rBtnAllSuppliers;
    public Text or;
    public Text txtCategoryDate;
    public Text txtOptionTime;
    public ScrollPane scrollPane;
    public Text txtallSuppliers;

    public AnchorPane anchorpane;

    ArrayList<String> month = new ArrayList<>();

    public SupplierFromController() {
        instance = this;
    }

    // Load all Suppliers from SupplierController
    public void loadAllSuppliers() {
        vBox.getChildren().clear();
        try {
            List<String> Suppliers = SupplierController.loadAllSuppliers();
            txtallSuppliers.setText(String.valueOf(Suppliers.size()));


            for (String reference : Suppliers) {
                loadSupplierData(reference);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadSupplierData(String SupplierReference) {
        try {
            FXMLLoader loader = new FXMLLoader(StoreFromController.class.getResource("/lk/ijse/StockManagement/view/bar/SupplierDetailBar.fxml"));
            Parent root = loader.load();
            lk.ijse.StockManagement.controller.Admin.bar.SupplierDetailBar controller = loader.getController();
            controller.setData(SupplierReference);
            vBox.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        scrollPane.lookup(".scroll-pane").setStyle("-fx-background-color: transparent");
        setSelectMonth();

        loadAllSuppliers(); // Load on init
        rBtnAllSuppliers.setSelected(true);
        if (rBtnAllSuppliers.isSelected()) {
            or.setVisible(false);
            selectDate.setVisible(false);
            selectDate.setDisable(true);
        }
    }

    public void searchOnKeyReleased(KeyEvent keyEvent) {
        vBox.getChildren().clear();
        try {
            if (search.getText().isEmpty()) {
                loadAllSuppliers();
            } else {
                List<String> matchedSuppliers = SupplierController.searchSuppliers(search.getText());
                for (String reference : matchedSuppliers) {
                    loadSupplierData(reference);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void backOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.swatchNavigation("AdminDashBoardForm.fxml", actionEvent);
    }

    public void newSupplierOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.popupNavigation("AddSupplierFrom.fxml");


    }

    public void update(ActionEvent actionEvent) throws IOException {
        loadAllSuppliers();

    }

    public void allSuppliersOnAction(ActionEvent actionEvent) {
        if (rBtnAllSuppliers.isSelected()) {
            loadAllSuppliers();
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
