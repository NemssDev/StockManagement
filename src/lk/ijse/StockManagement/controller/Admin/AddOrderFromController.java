package lk.ijse.StockManagement.controller.Admin;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import lk.ijse.StockManagement.modelController.*;
import lk.ijse.StockManagement.to.Article;
import lk.ijse.StockManagement.to.Item;
import lk.ijse.StockManagement.to.Order;
import lk.ijse.StockManagement.util.CrudUtil;
import lk.ijse.StockManagement.util.Navigation;
import lk.ijse.StockManagement.util.Notification;
import lk.ijse.StockManagement.util.RegexUtil;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;


public class AddOrderFromController implements Initializable {


    public RadioButton rBtnIntOrder;
    public RadioButton rBtnExtOrder;
    public JFXTextField txtQtt;
    public JFXButton btnAdd;
    public JFXTextField txtUserName;
    public JFXTextField txtPassword;
    @FXML
    private Label lblAvailableQuantity;
    @FXML
    private Label fromStorageLabel;
    @FXML
    private Label tostrg;
    @FXML
    private Label availableQtt;
    @FXML
    private ComboBox<String> comboArticle;
    @FXML
    private ComboBox<String> comboService;
    @FXML
    private ComboBox<String> comboStorage;
    @FXML
    private ComboBox<String> comboToStorage;

    public void addOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String id;
        if (rBtnIntOrder.isSelected()) {
            id = getNextIntOrderId();
        } else {
            id = getNextExtOrderId();
        }
        String article = comboArticle.getValue();
        String service = comboService.getValue();
        String from_storage = comboStorage.getValue();
        String to_storage = comboToStorage.getValue();
        Integer quantity = Integer.valueOf(txtQtt.getText());
        String dispo = lblAvailableQuantity.getText();
        String idI = getNextInventoryId();


        if (article == null || article.isEmpty() ||
                service == null || service.isEmpty() ||
                quantity <= 0 ||
                from_storage == null || from_storage.isEmpty()) {

            Notification.notificationWARNING("Missing Fields", "Please fill in required fields.");
            return;
        }else if (Integer.valueOf(dispo)< quantity) {
            System.out.println("dispo: "+dispo);
            System.out.println("quantity: "+quantity);
            Notification.notificationWARNING("Quantity Check", "Quantity should be less or equal the available quantity");
            return;

        }

        try {
            boolean isAdded;
            if (rBtnIntOrder.isSelected()) {
                isAdded = OrderController.addInternalOrder(new Order(
                        id,
                        article,
                        service,
                        String.valueOf(quantity),
                        from_storage,
                        to_storage,
                        null
                ));
                System.out.println("Quantity to subs from original : "+String.valueOf(quantity));
                boolean isUpdated = InventoryController.updateOItem(new Item(
                        article,
                        String.valueOf(quantity),
                        from_storage
                ));
                isUpdated = InventoryController.updateNItem(new Item(
                        idI,
                        article,
                        String.valueOf(quantity),
                        to_storage,
                        "New"
                ));
            } else {
                isAdded = OrderController.addExternalOrder(new Order(
                        id,
                        article,
                        service,
                        String.valueOf(quantity),
                        from_storage,
                        null,
                        null
                ));
                boolean isUpdated = ArticleController.updateQuantity(new Article(
                        String.valueOf(quantity),
                        article
                ));

                isUpdated = InventoryController.updateNItem(new Item(
                        idI,
                        article,
                        String.valueOf(quantity),
                        from_storage,
                        "New"
                ));
            }

            if (isAdded) {
                Notification.notification("Success", "Order added successfully.");
                Navigation.close(actionEvent);
                Navigation.swatchNavigation("OrderFrom.fxml", actionEvent);
            } else {
                Notification.notificationWARNING("Error", "Failed to add order.");
            }
        } catch (Exception e) {

            e.printStackTrace();
            Notification.notificationWARNING("Error", "Invalid input or database error.");
        }
    }
    private String getNextInventoryId(){
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
    private String getNextIntOrderId() {
        try {
            ResultSet set = OrderController.getInternalOrders();
            String lastID = "I000";

            while (set.next()) {
                String currentID = set.getString("order_id"); // 'code' should match your column name
                if (currentID.compareTo(lastID) > 0) {
                    lastID = currentID;
                }
            }

            // Extract the numeric part and increment it
            int num = Integer.parseInt(lastID.substring(1)) + 1;
            return "I" + String.format("%03d", num);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return "I001"; // fallback if there's an error
        }
    }

    private String getNextExtOrderId() {
        try {
            ResultSet set = OrderController.getExternalOrders();
            String lastID = "E000";

            while (set.next()) {
                String currentID = set.getString("order_id"); // 'code' should match your column name
                if (currentID.compareTo(lastID) > 0) {
                    lastID = currentID;
                }
            }

            // Extract the numeric part and increment it
            int num = Integer.parseInt(lastID.substring(1)) + 1;
            return "E" + String.format("%03d", num);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return "E001"; // fallback if there's an error
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            ToggleGroup group = new ToggleGroup();
            rBtnIntOrder.setToggleGroup(group);
            rBtnExtOrder.setToggleGroup(group);
            ResultSet set = ArticleController.getArticleNames();
            while (set.next()) {
                comboArticle.getItems().add(set.getString("name"));
            }
            set = ServiceController.getServiceNames();
            while (set.next()) {
                comboService.getItems().add(set.getString("name"));
            }

            rBtnIntOrder.setSelected(false);
            rBtnExtOrder.setSelected(false);
            txtUserName.setVisible(false);
            txtPassword.setVisible(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

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


    public void IntOrdersOnAction(ActionEvent actionEvent) {
        if (rBtnIntOrder.isSelected()) {
            rBtnExtOrder.setSelected(false);
            comboArticle.setDisable(false);
            comboToStorage.setVisible(true);
            tostrg.setLayoutY(380);
            lblAvailableQuantity.setLayoutY(450);
            availableQtt.setLayoutY(450);
            tostrg.setVisible(true);
            fromStorageLabel.setText("From Storage: ");
            txtQtt.setLayoutY(520);
        }

    }

    public void ExtOrdersOnAction(ActionEvent actionEvent) {
        if (rBtnExtOrder.isSelected()) {
            rBtnIntOrder.setSelected(false);
            comboArticle.setDisable(false);
            comboToStorage.setVisible(false);
            fromStorageLabel.setText("To Storage: ");
            lblAvailableQuantity.setLayoutY(380);
            tostrg.setVisible(false);
            availableQtt.setLayoutY(380);
            txtQtt.setLayoutY(450);

        }
    }

    public void ArticleOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (!Objects.equals(comboArticle.getValue(), "")) {
            comboStorage.setDisable(false);
            comboStorage.getItems().clear();
            ResultSet set ;
            if(rBtnExtOrder.isSelected()){
                set = StorageController.getAllStorageNames();
                while (set.next()) {
                    comboStorage.getItems().add(set.getString("location_name"));
                }
            }
            else{
                set = InventoryController.getStorageNames(comboArticle.getValue());
                while (set.next()) {
                    comboStorage.getItems().add(set.getString("location"));
                }

            }

        }

    }

    public void StorageOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (!Objects.equals(comboStorage.getValue(), "")) {
            String selectedStorage = comboStorage.getValue();
            comboToStorage.setDisable(false);
            comboToStorage.getItems().clear();
            String selectedArticle = comboArticle.getValue();
            ResultSet set = StorageController.getAllStorageNames();
            while (set.next()) {
                if(!Objects.equals(set.getString("location_name"), comboStorage.getValue())){
                    comboToStorage.getItems().add(set.getString("location_name"));
                }
            }
            if(rBtnExtOrder.isSelected()){
                set = ArticleController.getQuantityOfArticle(selectedArticle);
            }
            else{
                set = InventoryController.getItem(selectedArticle, selectedStorage);
            }

            String quantity ="";
            if (set.next()) {
                quantity = set.getString("quantity");
            }
            lblAvailableQuantity.setVisible(true);
            lblAvailableQuantity.setText(quantity);
            txtQtt.setDisable(false);

        }

    }

    public void ToStorageOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (!Objects.equals(comboToStorage.getValue(), "")) {
            txtQtt.setDisable(false);
            String selectedArticle = comboArticle.getValue();
            String selectedStorage = comboStorage.getValue();
            ResultSet set;
            if(rBtnExtOrder.isSelected()){
                set = InventoryController.getItem(selectedArticle, selectedStorage);
            }
            else{
                set = ArticleController.getQuantityOfArticle(selectedArticle);
            }
            String quantity ="";
            if (set.next()) {
                quantity = set.getString("quantity");
            }
        }

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
