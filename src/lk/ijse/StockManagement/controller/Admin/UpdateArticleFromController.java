package lk.ijse.StockManagement.controller.Admin;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import lk.ijse.StockManagement.modelController.ArticleController;
import lk.ijse.StockManagement.to.Article;
import lk.ijse.StockManagement.util.Navigation;
import lk.ijse.StockManagement.util.Notification;
import lk.ijse.StockManagement.util.RegexUtil;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

public class UpdateArticleFromController implements Initializable {
    private static String dateNow;
    public JFXTextField txtRef;
    public JFXTextField txtArticleID;
    public JFXTextField txtName;
    public JFXTextField txtCat;
    public CheckBox isCon;
    public JFXDatePicker txtExpir;
    public JFXTextField txtQtt;
    public JFXTextField txtMinStck;
    public JFXTextField txtDes;
    public JFXTextField txtPrice;
    public CheckBox isAct;
    public JFXButton btnAdd;
    public JFXButton btn;
    @FXML
    private ComboBox<String> comboType;
    @FXML
    private ComboBox<String> comboCategorie;

    public void updateOnAction(ActionEvent actionEvent) {
        String id = txtRef.getText(); // Assuming `txtRef` holds the article code (ID)
        String name = txtName.getText();
        String category = comboCategorie.getValue();
        Boolean isCons = comboType.getSelectionModel().getSelectedIndex() == 1;
        int a = isCons ? 0 : 1;
        String expirDate = txtExpir.getValue().toString();
        Integer quantity = Integer.valueOf(txtQtt.getText());
        Integer minStock = Integer.valueOf(txtMinStck.getText());
        String description = txtDes.getText();
        Integer price = Integer.valueOf(txtPrice.getText());
        Boolean isActive = isAct.isSelected();
        int b = isActive ? 1 : 0;

        System.out.println("======== ARTICLE DATA ========");
        System.out.println("ID (Reference): " + id);
        System.out.println("Name: " + name);
        System.out.println("Category: " + category);
        System.out.println("Is Consommable: " + isCons + " (Stored as " + a + ")");
        System.out.println("Expiration Date: " + expirDate);
        System.out.println("Quantity: " + quantity);
        System.out.println("Min Stock: " + minStock);
        System.out.println("Description: " + description);
        System.out.println("Price: " + price);
        System.out.println("Is Active: " + isActive + " (Stored as " + b + ")");
        System.out.println("================================");


        if (name.isEmpty() || id.isEmpty() || category.isEmpty()) {
            Notification.notificationWARNING("Missing Fields", "Please fill in required fields.");
            return;
        }

        try {
            boolean isUpdated = ArticleController.updateArticle(new Article(
                    id,
                    name,
                    category,
                    a,
                    expirDate,
                    quantity,
                    minStock,
                    description,
                    Double.parseDouble(String.valueOf(price)),
                    b
            ));

            if (isUpdated) {
                Notification.notification("Success", "Article updated successfully.");
                Navigation.close(actionEvent);
            } else {
                Notification.notificationWARNING("Error", "Failed to update article.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Notification.notificationWARNING("Error", "Invalid input or database error.");
        }
    }


    private void setData() {
        ObservableList<String> consommableCategories = FXCollections.observableArrayList("Bureautique",
                "Informatique",
                "Technologie",
                "Tirage",
                "Nettoyage",
                "Entretient",
                "Jardin",
                "Divers");
        ObservableList<String> nonConsommableCategories = FXCollections.observableArrayList("Meuble",
                "Informatique",
                "Technologie",
                "Divers");

        try {
            // Assuming txtRef.getText() holds the reference you are searching for
            String searchReference = ArticleController.articleID;
            System.out.println("Reference in txtRef: " + searchReference);

            ResultSet set = ArticleController.getAllArticles(); // Or get by reference if needed

            boolean found = false;  // Flag to check if the reference is found

            while (set.next()) {  // Loop through all the rows
                String reference = set.getString("reference");

                // Check if the current row's reference matches the search reference
                if (reference.equals(searchReference)) {
                    found = true;

                    // Debugging: Print the fetched values (optional)
                    System.out.println("Found Article with Reference: " + reference);
                    System.out.println("Name: " + set.getString("name"));
                    System.out.println("Category: " + set.getString("category"));
                    System.out.println("Is Consumable: " + set.getInt("is_consumable"));
                    System.out.println("Expiration Date: " + set.getDate("expiration_date"));
                    System.out.println("Quantity: " + set.getInt("quantity"));
                    System.out.println("Min Stock: " + set.getInt("min_stock"));
                    System.out.println("Designation: " + set.getString("designation"));
                    System.out.println("Price: " + set.getLong("prix_achat"));
                    System.out.println("Is Active: " + set.getInt("is_active"));

                    // Set the data for this article into UI components
                    txtRef.setText(reference);  // 1. reference (primary key)
                    txtName.setText(set.getString("name"));  // 2. name
                    int isCons = set.getInt("is_consumable");  // 4. is_consumable
                    if (isCons == 0) {
                        comboType.setValue("Non consommable");
                    } else {
                        comboType.setValue("Consommable");
                    }
                    if (isCons == 1) {
                        comboCategorie.setItems(consommableCategories);
                    } else {
                        comboCategorie.setItems(nonConsommableCategories);
                    }
                    comboCategorie.setValue(set.getString("category"));  // 3. category (nullable)


                    Date date = set.getDate("expiration_date");  // 5. expiration_date
                    if (date != null) txtExpir.setValue(((java.sql.Date) date).toLocalDate());

                    txtQtt.setText(String.valueOf(set.getInt("quantity")));  // 6. quantity
                    txtMinStck.setText(String.valueOf(set.getInt("min_stock")));  // 7. min_stock
                    txtDes.setText(set.getString("designation"));  // 8. designation
                    txtPrice.setText(String.valueOf(set.getLong("prix_achat")));  // 9. prix_achat

                    int isActive = set.getInt("is_active");  // 10. is_active
                    isAct.setSelected(isActive == 1);

                    break;  // Exit the loop once the reference is found
                }
            }

            // If the reference wasn't found
            if (!found) {
                Notification.notificationWARNING("Not Found", "Article with the given reference does not exist.");
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Notification.notificationWARNING("Error", "Failed to load article data.");
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comboType.getItems().addAll("Consommable", "Non consommable");

        // Désactive comboCategorie au départ
        comboCategorie.setDisable(false);

        // Listener sur comboType
        comboType.setOnAction(e -> {
            String selectedType = comboType.getValue();
            comboCategorie.setDisable(false);
            comboCategorie.getItems().clear();

            if ("Consommable".equals(selectedType)) {
                comboCategorie.getItems().addAll("Bureautique",
                        "Informatique",
                        "Technologie",
                        "Tirage",
                        "Nettoyage",
                        "Entretient",
                        "Jardin",
                        "Divers"
                );
            } else if ("Non consommable".equals(selectedType)) {
                comboCategorie.getItems().addAll("Meuble",
                        "Informatique",
                        "Technologie",
                        "Divers"
                );
            }
        });

        if (ArticleController.articleID != null) {
            setData();
        } else {
            System.out.println("No article ID found.");
        }

    }


    public void close(MouseEvent mouseEvent) {
        Navigation.close(mouseEvent);
    }


    public void QttReg(KeyEvent keyEvent) {
        RegexUtil.regex(btnAdd, txtQtt, txtQtt.getText(), "[1-9]\\d*", "-fx-text-fill: white");
    }

    public void MinStkReg(KeyEvent keyEvent) {
        RegexUtil.regex(btnAdd, txtMinStck, txtMinStck.getText(), "[1-9]\\d*", "-fx-text-fill: white");
    }

    public void PriceReg(KeyEvent keyEvent) {
        RegexUtil.regex(btnAdd, txtPrice, txtPrice.getText(), "[1-9]\\d*", "-fx-text-fill: white");
    }

    private String dateNow() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(new Date());
    }

    public void getDateOnAction(ActionEvent actionEvent) {
        // Get the date from the TextField (DatePicker)
        LocalDate selectedDate = txtExpir.getValue();

        // Get the current date (today)
        LocalDate nowDate = LocalDate.now();

        // Print current date for debugging
        System.out.println("Current date: " + nowDate);

        // Check if selected date is after the current date
        if (selectedDate != null && selectedDate.isAfter(nowDate)) {
            System.out.println("Selected date is in the future.");
        } else {
            System.out.println("Selected date is not in the future.");
        }
    }

}
