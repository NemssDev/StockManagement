package lk.ijse.StockManagement.controller.Admin.bar;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import lk.ijse.StockManagement.controller.Admin.ArticleFromController;
import lk.ijse.StockManagement.modelController.ArticleController;
import lk.ijse.StockManagement.to.Article;
import lk.ijse.StockManagement.util.Navigation;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ArticleDetailBar {
    // Article-specific fields
    public Text txtArticleID;  // Changed to articleID
    public Text txtName;
    public Text txtCategory;
    public Text txtExpirationDate;
    public Text txtQuantity;
    public Text txtPrice;
    public JFXButton btnDelete;
    public JFXButton btnUpdate;
    private String reference;
    @FXML
    private AnchorPane rootPane;

    // View article details when clicked
    public void viewDataOnMouseClick(MouseEvent mouseEvent) throws IOException {
        ArticleController.articleID = txtArticleID.getText();  // Use articleID
        Navigation.popupNavigation("ViewArticleDetailsFrom.fxml");  // Navigate to article details view
    }

    // Update article details
    public void UpdateOnAction(ActionEvent actionEvent) throws IOException {
        ArticleController.articleID = txtArticleID.getText();  // Use articleID
        Navigation.popupNavigation("UpdateArticleFrom.fxml");  // Navigate to article update view
    }

    // Delete article after confirmation
    public void deleteOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult().equals(ButtonType.YES)) {
            try {
                boolean isDeleted = ArticleController.removeArticle(txtArticleID.getText());  // Use articleID for deletion
                if (isDeleted) {
                    ArticleFromController.getInstance().vBox.getChildren().clear();  // Clear the current list
                    ArticleFromController.getInstance().loadAllArticles();  // Reload the list of articles
                } else {
                    System.out.println("Article not deleted!");
                }
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    // Set article data from the database based on articleID
    public void setData(String articleID) {
        this.reference = reference;
        try {
            ResultSet set = ArticleController.getArticleById(articleID);  // Use ArticleController to fetch article data
            if (set.next()) {
                txtArticleID.setText(set.getString("reference"));  // Set articleID
                txtName.setText(set.getString("name"));  // Set article designation
                txtCategory.setText(set.getString("category"));  // Set article category
                txtExpirationDate.setText(set.getString("expiration_date"));  // Set article category
                txtQuantity.setText(set.getString("quantity"));  // Set article price
                txtPrice.setText(set.getString("prix_achat"));  // Set article price


                String expDateStr = set.getString("expiration_date");
                if (expDateStr != null && !expDateStr.isEmpty()) {
                    LocalDate expDate = LocalDate.parse(expDateStr); // assuming format is yyyy-MM-dd
                    LocalDate today = LocalDate.now();
                    long daysBetween = ChronoUnit.DAYS.between(today, expDate);

                    if (daysBetween >= 0 && daysBetween <= 7) {
                        rootPane.setStyle("-fx-background-color: #ff5151;"); // Light red background
                    } else {
                        rootPane.setStyle(""); // reset if not near expiration
                    }
                }

            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    // Set article data using Article object
    public void setData(Article article) {
        txtArticleID.setText(article.getReference());  // Set articleID
        txtName.setText(article.getDesignation());  // Set article designation
        txtCategory.setText(article.getIdCat());  // Set article category
        txtPrice.setText(String.valueOf(article.getPrixAchat()));  // Set article price


    }

    public void initialize() {
        //checkExpiration();
    }

    private void checkExpiration() {
        String dateStr = txtExpirationDate.getText(); // assuming format: yyyy-MM-dd

        try {
            LocalDate expDate = LocalDate.parse(dateStr); // adjust format if needed
            LocalDate now = LocalDate.now();

            long daysBetween = ChronoUnit.DAYS.between(now, expDate);
            if (daysBetween >= 0 && daysBetween <= 7) {
            } else {
            }
        } catch (Exception e) {
            System.out.println("Invalid date: " + e.getMessage());
        }
    }
    // Show delete and update buttons when mouse enters the area
}
