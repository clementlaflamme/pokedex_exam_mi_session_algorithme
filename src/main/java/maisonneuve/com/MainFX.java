package maisonneuve.com;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import maisonneuve.com.controller.PokemonController;
import maisonneuve.com.view.PokemonViewFX;

import java.io.IOException;
import java.sql.SQLException;

public class MainFX extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        PokemonViewFX view = new PokemonViewFX();
        PokemonController ctrl = new PokemonController(view);
        ctrl.demarrer();

        Scene scene = new Scene(view.getRoot(), 900, 1000);

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
