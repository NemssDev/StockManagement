package lk.ijse.StockManagement.controller.Admin;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lk.ijse.StockManagement.controller.Admin.bar.OrderDetailBar;
import lk.ijse.StockManagement.controller.User.StoreFromController;
import lk.ijse.StockManagement.modelController.OrderController;
import lk.ijse.StockManagement.util.Navigation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class OrderFromController implements Initializable {
    private static OrderFromController instance;
    public VBox vBox;
    public Text txtTodayAttendance;
    public JFXTextField search;
    public JFXDatePicker selectDate;
    public JFXRadioButton rBtnAllOrders;
    public JFXRadioButton rBtnIntOrders;
    public JFXRadioButton rBtnExtOrders;
    public Text or;
    public Text txtCategoryDate;
    public Text txtOptionTime;
    public ScrollPane scrollPane;
    public Text txtallOrders;

    public AnchorPane anchorpane;

    ArrayList<String> month = new ArrayList<>();

    public OrderFromController() {
        instance = this;
    }

    public static OrderFromController getInstance() {
        return instance;
    }

    // Load all orders from OrderController
    public void loadAllOrders() {
        vBox.getChildren().clear();
        try {
            List<String> orders = OrderController.loadAllOrders();
            txtallOrders.setText(String.valueOf(orders.size()));

            for (String reference : orders) {
                loadOrderData(reference);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void loadOrderData(String orderReference) {
        try {
            FXMLLoader loader = new FXMLLoader(StoreFromController.class.getResource("/lk/ijse/StockManagement/view/bar/OrderDetailBar.fxml"));
            Parent root = loader.load();
            lk.ijse.StockManagement.controller.Admin.bar.OrderDetailBar controller = loader.getController();
            controller.setData(orderReference);
            vBox.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadAllId() {

        vBox.getChildren().clear();
        try {

            ResultSet ids = OrderController.loadAllOrderIds();

            while (ids.next()) {
                System.out.println("id");
                LoadOrderData(ids.getString(1));
            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void loadIntId() {

        vBox.getChildren().clear();
        try {

            ResultSet ids = OrderController.loadIntOrderIds();

            while (ids.next()) {
                System.out.println("id");
                LoadOrderData(ids.getString(1));
            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void loadExtId() {

        vBox.getChildren().clear();
        try {

            ResultSet ids = OrderController.loadExtOrderIds();

            while (ids.next()) {
                System.out.println("id");
                LoadOrderData(ids.getString(1));
            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void LoadOrderData(String id) {

        try {
            System.out.println(id);
            FXMLLoader loader = new FXMLLoader(StoreFromController.class.getResource("/lk/ijse/StockManagement/view/bar/OrderDetailBar.fxml"));
            Parent root = loader.load();
            OrderDetailBar controller = loader.getController();
            controller.setData(id);
            vBox.getChildren().add(root);
        } catch (IOException e) {
        }

    }

    public void initialize(URL location, ResourceBundle resources) {

        scrollPane.lookup(".scroll-pane").setStyle("-fx-background-color: transparent");
        setSelectMonth();

        loadAllOrders(); // Load on init
        rBtnAllOrders.setSelected(true);
        if (rBtnAllOrders.isSelected()) {
            or.setVisible(false);
            selectDate.setVisible(false);
            selectDate.setDisable(true);
        }
    }

    public void searchOnKeyReleased(KeyEvent keyEvent) {
        vBox.getChildren().clear();
        try {
            if (search.getText().isEmpty()) {
                loadAllOrders();
            } else {
                List<String> matchedOrders = OrderController.searchOrders(search.getText());
                for (String reference : matchedOrders) {
                    loadOrderData(reference);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void backOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.swatchNavigation("AdminDashBoardForm.fxml", actionEvent);
    }

    public void newOrderOnAction(ActionEvent actionEvent) throws IOException {
        Navigation.popupNavigation("AddOrderFrom.fxml");


    }

    public void update(ActionEvent actionEvent) throws IOException {
        loadAllOrders();

    }

    public void imprimer(ActionEvent actionEvent) throws IOException {
        loadAllOrders();

    }

    /*public void allOrdersOnAction(ActionEvent actionEvent) {
        if (rBtnAllOrders.isSelected()) {
            loadAllOrders();
        }
    }*/

    public void allOrdersOnAction(ActionEvent actionEvent) {
        if (rBtnAllOrders.isSelected()) {
            rBtnExtOrders.setSelected(false);
            rBtnIntOrders.setSelected(false);
            loadAllId();
        } else {
            or.setVisible(true);
            selectDate.setVisible(true);
        }
    }

    public void IntOrdersOnAction(ActionEvent actionEvent) {
        if (rBtnIntOrders.isSelected()) {
            rBtnExtOrders.setSelected(false);
            rBtnAllOrders.setSelected(false);
            loadIntId();
        } else {
            or.setVisible(true);
            selectDate.setVisible(true);
        }
    }

    public void ExtOrdersOnAction(ActionEvent actionEvent) {
        if (rBtnExtOrders.isSelected()) {
            rBtnAllOrders.setSelected(false);
            rBtnIntOrders.setSelected(false);
            loadExtId();
        } else {
            or.setVisible(true);
            selectDate.setVisible(true);
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


    @FXML
    private VBox orderListContainer; // Container that holds order detail items

    @FXML
    private void handleImprimerButtonClick() {
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream("AllOrders.pdf"));
            document.open();
            document.add(new Paragraph("Liste des commandes"));
            document.add(new Paragraph(" "));

            for (Node node : orderListContainer.getChildren()) {
                FXMLLoader loader = new FXMLLoader();
                loader.setRoot(node);
                loader.setControllerFactory(param -> null); // avoids controller override

                // get controller manually from the node’s properties
                OrderDetailBar controller = (OrderDetailBar) node.getProperties().get("controller");

                if (controller != null) {
                    document.add(new Paragraph("ID Commande: " + controller.getOrderID()));
                    document.add(new Paragraph("Article: " + controller.getArticle()));
                    document.add(new Paragraph("Service: " + controller.getService()));
                    document.add(new Paragraph("Quantité: " + controller.getQuantity()));
                    document.add(new Paragraph("Stockage: " + controller.getStorage()));
                    document.add(new Paragraph("Date: " + controller.getDate()));
                    document.add(new Paragraph(" "));
                }
            }

            document.close();
            System.out.println("PDF exporté avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
