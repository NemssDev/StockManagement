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
import lk.ijse.StockManagement.modelController.ArticleController;
import lk.ijse.StockManagement.util.Navigation;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ArticleFromController implements Initializable {
    private static ArticleFromController instance;
    public VBox vBox;
    public Text txtTodayAttendance;
    public JFXTextField search;
    public JFXDatePicker selectDate;
    public JFXRadioButton rBtnAllArticles;
    public Text or;
    public Text txtCategoryDate;
    public Text txtOptionTime;
    public ScrollPane scrollPane;
    public Text txtallArticles;

    public AnchorPane anchorpane;

    ArrayList<String> month = new ArrayList<>();

    public ArticleFromController() {
        instance = this;
    }

    public static ArticleFromController getInstance() {
        return instance;
    }

    // Load all articles from ArticleController
    public void loadAllArticles() {
        vBox.getChildren().clear();
        try {
            List<String> articles = ArticleController.loadAllArticles();
            txtallArticles.setText(String.valueOf(articles.size()));

            for (String reference : articles) {
                loadArticleData(reference);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadArticleData(String articleReference) {
        try {
            FXMLLoader loader = new FXMLLoader(StoreFromController.class.getResource("/lk/ijse/StockManagement/view/bar/ArticleDetailBar.fxml"));
            Parent root = loader.load();
            lk.ijse.StockManagement.controller.Admin.bar.ArticleDetailBar controller = loader.getController();
            controller.setData(articleReference);
            vBox.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        scrollPane.lookup(".scroll-pane").setStyle("-fx-background-color: transparent");
        setSelectMonth();

        loadAllArticles(); // Load on init
        rBtnAllArticles.setSelected(true);
        if (rBtnAllArticles.isSelected()) {
            or.setVisible(false);
            selectDate.setVisible(false);
            selectDate.setDisable(true);
        }
    }

    public void searchOnKeyReleased(KeyEvent keyEvent) {
        vBox.getChildren().clear();
        try {
            if (search.getText().isEmpty()) {
                loadAllArticles();
            } else {
                List<String> matchedArticles = ArticleController.searchArticles(search.getText());
                for (String reference : matchedArticles) {
                    loadArticleData(reference);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void backOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.swatchNavigation("AdminDashBoardForm.fxml", actionEvent);
    }

    public void newArticleOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.popupNavigation("AddArticleFrom.fxml");


    }

    public void update(ActionEvent actionEvent) throws IOException {
        loadAllArticles();

    }

    public void allArticlesOnAction(ActionEvent actionEvent) {
        if (rBtnAllArticles.isSelected()) {
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
    }

    public void closeMouseClick(MouseEvent mouseEvent) {
        Navigation.exit();
    }

    public void minimizMouseClick(MouseEvent mouseEvent) {
        Stage stage = (Stage) anchorpane.getScene().getWindow();
        stage.setIconified(true);
    }


}
