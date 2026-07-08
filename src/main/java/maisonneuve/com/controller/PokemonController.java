package maisonneuve.com.controller;

import javafx.application.Platform;
import maisonneuve.com.modele.Pokemon;
import maisonneuve.com.modele.PokemonDAO;
import maisonneuve.com.service.PokedexAPI;
import maisonneuve.com.view.PokemonViewFX;

import java.io.IOException;
import java.sql.SQLException;

public class PokemonController {

    private final PokemonDAO dao = new PokemonDAO();
    private final PokedexAPI service = new PokedexAPI();
    private final PokemonViewFX viewFx;
    private Pokemon pokemonActuel;

    public PokemonController(PokemonViewFX viewFx) {
        this.viewFx = viewFx;

        // Écoute pour la touche "Entrer" pour rechercher le pokémon
        viewFx.barreRecherche.setOnAction(e -> {
            try {
                rechercherPokemon(viewFx.barreRecherche.getText());
            } catch (IOException | InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });

        // Écoute pour le btnCapturer pour capturer le pokémon
        viewFx.btnCapturer.setOnAction(e -> {
            try {
                dao.capturer(pokemonActuel);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

    }

    // Rechercher par titre ou par id sur l'API Pokedex
    public void rechercherPokemon(String recherche) throws IOException, InterruptedException {
        viewFx.barreRecherche.setDisable(true);
        viewFx.barreRecherche.clear();

        if (recherche.trim().isEmpty()) {
            viewFx.msgErreur.setText("Veuillez entrer un nom ou un ID de pokémon à rechercher.");
            viewFx.barreRecherche.setDisable(false);
            return;
        }

        final Pokemon[] p = new Pokemon[1];

        // Créer un nouveau thread (multithreading)
        Thread threadApi = new Thread(() -> {
            try {
                p[0] = service.recupererPokemon(recherche.trim().toLowerCase());
                if (p[0] == null) {
                    viewFx.msgErreur.setText("Aucun pokémon ne correspond à votre recherche.");
                    viewFx.barreRecherche.setDisable(false);
                } else {
                    this.pokemonActuel = p[0];
                    afficherCartePokemon(p[0]);
                }
                viewFx.barreRecherche.setDisable(false);
            } catch (Exception e) {
                // S'exécute lors que le thread d'arrière plan a terminé
                Platform.runLater(() -> {
                    viewFx.msgErreur.setText("Erreur lors de la requête.");
                    viewFx.barreRecherche.setDisable(false);
                });
            }
        });
        threadApi.start();
    }

    // Afficher la carte du pokémon
    public void afficherCartePokemon(Pokemon p) {

    }


    // Capturer le pokémon
}
