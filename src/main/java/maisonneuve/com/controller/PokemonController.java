package maisonneuve.com.controller;

import maisonneuve.com.modele.Pokemon;
import maisonneuve.com.modele.PokemonDAO;
import maisonneuve.com.service.PokedexAPI;
import maisonneuve.com.view.PokemonViewFX;

import java.io.IOException;

public class PokemonController {

    private final PokemonDAO dao = new PokemonDAO();
    private final PokedexAPI service = new PokedexAPI();
    private final PokemonViewFX viewFx;

    public PokemonController(PokemonViewFX viewFx) {
        this.viewFx = viewFx;

        /*viewFx.btnCapturer.setOnAction(e -> {

        });*/

    }

    // Rechercher par titre ou par id sur l'API Pokedex
    public void rechercherPokemon(String recherche) throws IOException, InterruptedException {

        if (recherche.trim().isEmpty()) {
            viewFx.msgErreur.setText("Veuillez entrer un nom ou un ID de pokémon à rechercher.");
            return;
        }

        Pokemon p = service.recupererPokemon(recherche.trim().toLowerCase());

        if (p == null) {
            viewFx.msgErreur.setText("Aucun pokémon ne correspond à votre recherche.");
            return;
        }

        afficherCartePokemon(p);

    }


    // Afficher la carte du pokémon
    public void afficherCartePokemon(Pokemon p) {

    }


    // Capturer le pokémon
}
