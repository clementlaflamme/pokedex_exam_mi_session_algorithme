package maisonneuve.com;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import maisonneuve.com.controller.PokemonController;
import maisonneuve.com.view.PokemonViewFX;

import java.io.IOException;

public class MainFX extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        PokemonViewFX view = new PokemonViewFX();
        PokemonController ctrl = new PokemonController(view);

        Scene scene = new Scene(view.getRoot(), 540, 1080);

        scene.getStylesheets().add("https://fonts.googleapis.com/css2?family=Arimo:ital,wght@0,400..700;1,400..700&family=Carter+One&family=Roboto:ital,wght@0,100..900;1,100..900&display=swap");

        String css = MainFX.class.getResource("/css/style.css").toExternalForm();
        scene.getStylesheets().add(css);

        stage.setTitle("Pokédex - Recherchez vos pokémons préférés");
        stage.setScene(scene);

        ctrl.demarrer(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
