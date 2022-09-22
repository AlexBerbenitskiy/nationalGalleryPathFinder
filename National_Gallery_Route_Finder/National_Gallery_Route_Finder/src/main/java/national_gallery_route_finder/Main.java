package national_gallery_route_finder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        VBox mainVB = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("main-view.fxml")));
        Scene main = new Scene(mainVB, 600, 670);
        stage.setTitle("National Gallery Route Finder");
        stage.setScene(main);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}