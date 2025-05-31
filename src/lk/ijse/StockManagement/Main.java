package lk.ijse.StockManagement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("/lk/ijse/theGym/view/LoginFrom.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/lk/ijse/StockManagement/view/LoginFrom.fxml"));
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/lk/ijse/StockManagement/view/assets/image/icon/logo.png")));
        primaryStage.setTitle("Stock Management");
        primaryStage.setScene(new Scene(root));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
}
