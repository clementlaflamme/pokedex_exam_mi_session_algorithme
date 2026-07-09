package maisonneuve.com;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import maisonneuve.com.controller.PokemonController;
import maisonneuve.com.view.PokemonViewFX;

public class MainFX extends Application {

    @Override
    public void start(Stage stage) {
        PokemonViewFX view = new PokemonViewFX();
        PokemonController ctrl = new PokemonController(view);

        Scene scene = new Scene(view.getRoot(), 900, 500);

        String css = MainFX.class.getResource("/style.css").toExternalForm();
        scene.getStylesheets().add(css);

        stage.setTitle("Pokédex - Recherchez vos pokémons préférés");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
