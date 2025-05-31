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
import lk.ijse.StockManagement.modelController.InventoryController;
import lk.ijse.StockManagement.modelController.StorageController;
import lk.ijse.StockManagement.to.Article;
import lk.ijse.StockManagement.to.Item;
import lk.ijse.StockManagement.util.CrudUtil;
import lk.ijse.StockManagement.util.Navigation;
import lk.ijse.StockManagement.util.Notification;
import lk.ijse.StockManagement.util.RegexUtil;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;


public class AddArticleFromController implements Initializable {

    private static String dateNow;
    public JFXTextField txtRef;
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
    public JFXTextField txtUserName;
    public JFXTextField txtPassword;
    public JFXTextField txtQta;
    @FXML
    private ComboBox<String> comboType;
    @FXML
    private ComboBox<String> comboStorage;
    @FXML
    private ComboBox<String> comboCategorie;

    public void addOnAction(ActionEvent actionEvent) {
        String idA = getNextIDA();
        String idI = getNextIDI();
        System.out.println("idA: " + idA);
        System.out.println("idI: " + idI);
        String name = txtName.getText();
        String category = comboCategorie.getValue();
        Boolean isCons = comboType.getSelectionModel().getSelectedIndex() == 1;
        int a = isCons ? 0 : 1;
        String expirDate = (txtExpir.getValue() != null) ? txtExpir.getValue().toString() : "";
        Integer quantity = (!Objects.equals(txtQtt.getText(), "")) ? Integer.valueOf(txtQtt.getText()) : -1;
        Integer minStock = (!Objects.equals(txtMinStck.getText(), "")) ? Integer.valueOf(txtMinStck.getText()) : -1;
        String description = txtDes.getText();
        Integer price = (!Objects.equals(txtPrice.getText(), "")) ? Integer.valueOf(txtPrice.getText()) : -1;
        Boolean isActive = isAct.isSelected();
        String location = comboStorage.getValue();
        String qta = txtQta.getText();
        int b = isActive ? 1 : 0;
        if (name.isEmpty() || category.isEmpty() || quantity <= 0 || minStock <= 0 || location.isEmpty()
                || comboType.getSelectionModel().isEmpty()
                || price == -1
                || expirDate.isEmpty()
                || comboCategorie.getSelectionModel().isEmpty()
                || Objects.equals(qta, "")) {

            Notification.notificationWARNING("Missing Fields", "Please fill in required fields.");
            return;
        } else if (txtExpir.getValue().isBefore(dateNow())) {
            Notification.notificationWARNING("Date Check", "Please enter an earlier date.");
            return;
        } else if (quantity < minStock) {
            Notification.notificationWARNING("Quantity Check", "Quantity should be greater or equal to the Min Stock!");
            return;
        }
        else if(Integer.parseInt(qta) > quantity){
            Notification.notificationWARNING("Quantity Check", "Quantity added to storage should be less or equal to the Total quantity !");
            return;
        }


        try {
            int rest = quantity - Integer.parseInt(qta);

            boolean isAddedA = ArticleController.addArticle(new Article(
                    idA,
                    name,
                    category,
                    a,
                    expirDate,
                    rest,
                    minStock,
                    description,
                    Double.parseDouble(String.valueOf(price)),
                    b
            ));

            boolean isAddedI = InventoryController.addItem(new Item(
                    idI,
                    name,
                    qta,
                    location,
                    "New"
            ));


            if (isAddedA && isAddedI) {
                Notification.notification("Success", "Article added successfully.");
                Navigation.close(actionEvent);
                Navigation.swatchNavigation("ArticleFrom.fxml", actionEvent);
            } else {
                Notification.notificationWARNING("Error", "Failed to add article.");
            }
        } catch (Exception e) {

            e.printStackTrace();
            Notification.notificationWARNING("Error", "Invalid input or database error.");
        }
    }

    private String getNextIDA() {
        try {
            ResultSet set = ArticleController.getAllArticles();
            String lastID = "A000";

            while (set.next()) {
                String currentID = set.getString("reference"); // 'code' should match your column name
                if (currentID.compareTo(lastID) > 0) {
                    lastID = currentID;
                }
            }

            // Extract the numeric part and increment it
            int num = Integer.parseInt(lastID.substring(1)) + 1;
            return "A" + String.format("%03d", num);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return "A001"; // fallback if there's an error
        }
    }

    private String getNextIDI() {
        String query = "SELECT MAX(id) FROM inventory";

        try {
            ResultSet rs = CrudUtil.crudUtil(query);
            if (rs.next()) {
                int maxId = rs.getInt(1);
                return String.valueOf(maxId + 1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // If there's an error or no records, start from 1
        return "1";
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comboType.getItems().addAll("Consommable", "Non consommable");
        loadStorageNames();

        // Désactive comboCategorie au départ
        comboCategorie.setDisable(true);

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
        txtUserName.setVisible(false);
        txtPassword.setVisible(false);
    }

    private void loadStorageNames() {
        ObservableList<String> storageNames = FXCollections.observableArrayList();

        try {
            ResultSet rs = StorageController.getAllStorageNames(); // Make sure this returns ResultSet
            while (rs.next()) {
                storageNames.add(rs.getString("location_name")); // Replace "name" with your actual column name
            }
            comboStorage.setItems(storageNames);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void QttReg(KeyEvent keyEvent) {
        RegexUtil.regex(btnAdd, txtQtt, txtQtt.getText(), "[1-9]\\d*", "-fx-text-fill: white");
    }

    public void QtaReg(KeyEvent keyEvent) {
        RegexUtil.regex(btnAdd, txtQta, txtQta.getText(), "[0-9]\\d*", "-fx-text-fill: white");
    }


    public void MinStkReg(KeyEvent keyEvent) {
        RegexUtil.regex(btnAdd, txtMinStck, txtMinStck.getText(), "[1-9]\\d*", "-fx-text-fill: white");
    }

    public void PriceReg(KeyEvent keyEvent) {
        RegexUtil.regex(btnAdd, txtPrice, txtPrice.getText(), "[1-9]\\d*", "-fx-text-fill: white");
    }

    public LocalDate dateNow() {
        return LocalDate.now();
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

    public void storageOnAction(ActionEvent actionEvent){
        txtQta.setDisable(false);
    }

    public void userName(KeyEvent keyEvent) {
        RegexUtil.regex(btnAdd, txtUserName, txtUserName.getText(), "^(?!.*\\.\\.)(?!.*\\.$)[^\\W][\\w.]{0,29}$", "-fx-text-fill: white");
    }

    public void password(KeyEvent keyEvent) {
        RegexUtil.regex(btnAdd, txtPassword, txtPassword.getText(), "^(?=.{8,})(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\\s).*$", "-fx-text-fill: white");

    }

    public void close(MouseEvent mouseEvent) {
        Navigation.close(mouseEvent);
    }


}
