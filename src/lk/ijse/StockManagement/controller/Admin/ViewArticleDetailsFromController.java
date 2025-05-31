package lk.ijse.StockManagement.controller.Admin;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lk.ijse.StockManagement.modelController.ArticleController;
import lk.ijse.StockManagement.util.Navigation;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ViewArticleDetailsFromController implements Initializable {
    public Text txtRef;
    public Text txtName;
    public Text txtCategory;
    public Text isCon;
    public Text txtExpir;
    public Text txtQtt;
    public Text txtMinStck;
    public Text txtDes;
    public Text txtPrix;
    public Text txtIsAct;

    public void closeOnAction(ActionEvent actionEvent) {
        Navigation.close(actionEvent);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setData();
    }

    private void setData() {
        try {
            ResultSet set = ArticleController.getArticleById(ArticleController.articleID);
            if (set.next()) {
                txtRef.setText(set.getString(1));
                txtName.setText(set.getString(2));
                txtCategory.setText(set.getString(3));
                if (set.getString(4).equals("0")) {
                    isCon.setText("No");
                } else if (set.getString(4).equals("1")) {
                    isCon.setText("Yes");
                } else {
                    System.out.println("set.getString(4): " + set.getString(4));
                    isCon.setText("Undefined");
                }


                txtExpir.setText(set.getString(5));
                txtQtt.setText(set.getString(6));
                txtMinStck.setText(set.getString(7));
                txtDes.setText(set.getString(8));
                txtPrix.setText(set.getString(9));
                if (set.getString(10).equals("0")) {
                    txtIsAct.setText("No");
                } else if (set.getString(10).equals("1")) {
                    txtIsAct.setText("Yes");
                } else {
                    System.out.println("set.getString(4): " + set.getString(4));
                    txtIsAct.setText("Undefined");
                }

            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void UpdateOnAction(ActionEvent actionEvent) throws IOException {
        ArticleController.articleID = txtRef.getText();  // Use articleID
        Navigation.swatchNavigation("UpdateArticleFrom.fxml", actionEvent);  // Navigate to article update view
    }

    public void deleteOnAction(ActionEvent actionEvent) throws IOException {
        ArticleController.articleID = txtRef.getText();
        Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.setAlwaysOnTop(true);
        alert.showAndWait();
        if (alert.getResult().equals(ButtonType.YES)) {
            try {
                boolean isDeleted = ArticleController.removeArticle(txtRef.getText());  // Use articleID for deletion
                if (isDeleted) {
                    ArticleFromController.getInstance().vBox.getChildren().clear();  // Clear the current list
                    ArticleFromController.getInstance().loadAllArticles();  // Reload the list of articles
                    Navigation.close(actionEvent);

                } else {
                    System.out.println("Article not deleted!");
                }
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
