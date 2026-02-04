package agnes.ui;

import java.io.IOException;

import agnes.Agnes;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Agnes using FXML.
 */
public class Main extends Application {

    private Agnes agnes = new Agnes();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            fxmlLoader.<MainWindow>getController().setAgnes(agnes);  // inject the Agnes instance
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

