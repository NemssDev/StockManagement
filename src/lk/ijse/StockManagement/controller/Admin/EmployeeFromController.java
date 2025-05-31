package lk.ijse.StockManagement.controller.Admin;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lk.ijse.StockManagement.controller.User.StoreFromController;
import lk.ijse.StockManagement.modelController.ArticleController;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EmployeeFromController implements Initializable {
    private static EmployeeFromController instance;
    public VBox vBox;
    public Text txtTodayAttendance;
    public JFXTextField search;
    public JFXComboBox selectMonth;
    public JFXDatePicker selectDate;
    public JFXRadioButton rBtnAllEmployee;
    public JFXRadioButton rBtnAttendance;
    public Text or;
    public Text txtCityDate;
    public Text txtOptionTime;
    public ScrollPane scrollPane;

    ArrayList<String> month = new ArrayList<>();

    public EmployeeFromController() {
        instance = this;
    }

    public static EmployeeFromController getInstance() {
        return instance;
    }

    // Loads all articles instead of employee IDs
    public void loadAllArticles() {
        vBox.getChildren().clear();
        try {
            ResultSet articles = (ResultSet) ArticleController.loadAllArticles(); // You should have this method

            while (articles.next()) {
                loadArticleData(articles.getString("code")); // assuming article code is the primary key
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void loadArticleData(String articleCode) {
        try {
            FXMLLoader loader = new FXMLLoader(StoreFromController.class.getResource("/lk/ijse/StockManagement/view/bar/ArticleDetailBar.fxml")); // Your FXML
            Parent root = loader.load();
            // Replace EmployeeDetailBar with ArticleDetailBar
            lk.ijse.StockManagement.controller.Admin.bar.ArticleDetailBar controller = loader.getController();
            controller.setData(articleCode);
            vBox.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        scrollPane.lookup(".scroll-pane").setStyle("-fx-background-color: transparent");
        setSelectMonth();
        loadAllArticles(); // Load articles on init
        rBtnAllEmployee.setSelected(true);
        if (rBtnAllEmployee.isSelected()) {
            txtCityDate.setText("Category"); // optional
            txtOptionTime.setText("Price");  // optional
            or.setVisible(false);
            selectDate.setVisible(false);
            selectDate.setDisable(true);
            selectMonth.setDisable(true);
            selectMonth.setValue(false);
            rBtnAttendance.setSelected(false);
        }
    }

    public void searchOnKeyReleased(KeyEvent keyEvent) {
        vBox.getChildren().clear();
        try {
            if (search.getText().equals("")) {
                loadAllArticles();
            } else {
                ResultSet set = (ResultSet) ArticleController.searchArticles(search.getText()); // implement this
                while (set.next()) {
                    loadArticleData(set.getString("code"));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // You can keep or remove these depending on your use case
    public void backOnAction(ActionEvent actionEvent) throws IOException {
        // Navigation to another screen
    }

    public void newEmployeeOnAction(ActionEvent actionEvent) throws IOException {
        // Open add-article popup
    }

    public void allEmployeeOnAction(ActionEvent actionEvent) {
        if (rBtnAllEmployee.isSelected()) {
            loadAllArticles();
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
        selectMonth.getItems().addAll(month);
    }

    // You can clean up attendance/date-related logic if not needed anymore.
}
