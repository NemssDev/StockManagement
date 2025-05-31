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
import lk.ijse.StockManagement.modelController.InventoryController;
import lk.ijse.StockManagement.util.Navigation;
import lombok.Getter;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class InventoryFromController implements Initializable {
    @Getter
    private static InventoryFromController instance;
    public VBox vBox;
    public Text txtTodayAttendance;
    public JFXTextField search;
    public JFXDatePicker selectDate;
    public JFXRadioButton rBtnAllItems;
    public Text or;
    public Text txtOptionTime;
    public ScrollPane scrollPane;
    public Text txtallItems;

    public AnchorPane anchorpane;

    ArrayList<String> month = new ArrayList<>();

    public InventoryFromController() {
        instance = this;
    }

    public void loadAllItems() {
        vBox.getChildren().clear();
        try {
            List<String> Items = InventoryController.loadAllItems();
            txtallItems.setText(String.valueOf(Items.size()));


            for (String reference : Items) {
                loadItemData(reference);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadItemData(String ItemReference) {
        try {
            FXMLLoader loader = new FXMLLoader(StoreFromController.class.getResource("/lk/ijse/StockManagement/view/bar/ItemDetailBar.fxml"));
            Parent root = loader.load();
            lk.ijse.StockManagement.controller.Admin.bar.InventoryDetailBar controller = loader.getController();
            controller.setData(ItemReference);
            vBox.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        scrollPane.lookup(".scroll-pane").setStyle("-fx-background-color: transparent");
        setSelectMonth();

        loadAllItems(); // Load on init
        rBtnAllItems.setSelected(true);
        if (rBtnAllItems.isSelected()) {
            or.setVisible(false);
            selectDate.setVisible(false);
            selectDate.setDisable(true);
        }
    }

    public void searchOnKeyReleased(KeyEvent keyEvent) {
        vBox.getChildren().clear();
        try {
            if (search.getText().isEmpty()) {
                loadAllItems();
            } else {
                List<String> matchedItems = InventoryController.searchItems(search.getText());
                for (String reference : matchedItems) {
                    loadItemData(reference);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void backOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.swatchNavigation("AdminDashBoardForm.fxml", actionEvent);
    }

    public void newItemOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.popupNavigation("AddItemFrom.fxml");


    }

    public void update(ActionEvent actionEvent) throws IOException {
        loadAllItems();

    }

    public void allItemsOnAction(ActionEvent actionEvent) {
        if (rBtnAllItems.isSelected()) {
            loadAllItems();
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
