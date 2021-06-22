import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class ApplicationMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        TabPane root = FXMLLoader.load(getClass().getResource("./FX/Main.fxml"));
        primaryStage.setTitle("Acasa");
        primaryStage.setScene(new Scene(root,root.getPrefWidth(), root.getPrefHeight()));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
