package maisonneuve.com.controller;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import maisonneuve.com.modele.Pokemon;
import maisonneuve.com.modele.PokemonDAO;
import maisonneuve.com.service.PokedexAPI;
import maisonneuve.com.view.PokemonViewFX;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class PokemonController {

    private final PokemonDAO dao = new PokemonDAO();
    private final PokedexAPI service = new PokedexAPI();
    private final PokemonViewFX viewFx;
    private Pokemon pokemonActuel;

    public PokemonController(PokemonViewFX viewFx) {
        this.viewFx = viewFx;

        // Écoute pour la touche "Entrer" pour rechercher le pokémon
        viewFx.barreRecherche.setOnAction(e -> {
            rechercherPokemon(viewFx.barreRecherche.getText());
        });

        // Écoute pour le btnCapturer pour capturer le pokémon
        // btnCapturer affiche Relacher et permet de relacher un pokémon si il existe déjà dans la BD
        viewFx.btnCapturer.setOnAction(e -> {
            try {
                List<Pokemon> tous = dao.lister();
                boolean pokemonDejaCapture = false;
                for (Pokemon p : tous) {
                    if (p.idPokedex.equals(pokemonActuel.idPokedex)) {
                        pokemonDejaCapture = true;
                        break;
                    }
                }

                if (pokemonDejaCapture) {
                    // affichage du popup de confirmation avant d'agir sur la BD
                    String messageRelache = "Êtes-vous sûr de vouloir relâcher " + premiereLettreEnMaj(pokemonActuel.nom) + "?";
                    boolean confirmer = viewFx.afficherConfirmation("Confirmer la relâche", messageRelache);
                    if (confirmer) {
                        try {
                            dao.relacher(pokemonActuel);
                            viewFx.msgErreur.setText(null);
                            viewFx.messageStatut.setText("Le Pokémon " + pokemonActuel.nom + " a été relâché");
                            viewFx.btnCapturer.setText("Capturer");
                        } catch (SQLException ex) {
                            viewFx.msgErreur.setText("Erreur lors de la relâche. Vous n'avez pas ce Pokémon");
                        }
                    } else {
                            viewFx.messageStatut.setText("Relâche annulée.");
                    }
                } else {
                    try {
                        dao.capturer(pokemonActuel);
                        viewFx.msgErreur.setText(null);
                        viewFx.messageStatut.setText("Le pokémon " + pokemonActuel.nom + " a été capturé");
                        viewFx.btnCapturer.setText("Relâcher");
                    } catch (SQLException ex) {
                        viewFx.msgErreur.setText("Erreur lors de la capture. Avez-vous déjà capturé ce Pokémon ?");
                    }
                }
            } catch (SQLException ex) {
                viewFx.msgErreur.setText("Erreur de connexion avec la base de données.");
            }
        });
    }

    // Rechercher par titre ou par id sur l'API Pokedex
    public void rechercherPokemon(String recherche) {
        viewFx.barreRecherche.setDisable(true);
        viewFx.msgErreur.setText(null);

        String rechercheNettoyee = recherche.trim().toLowerCase();

        if (rechercheNettoyee.isEmpty()) {
            viewFx.msgErreur.setText("Veuillez entrer un nom ou un ID de Pokémon à rechercher.");
            viewFx.barreRecherche.setDisable(false);
            return;
        }

        final Pokemon[] p = new Pokemon[1];

        Thread threadApi = new Thread(() -> {
            try {
                p[0] = service.recupererPokemon(rechercheNettoyee);

                Platform.runLater(() -> {
                    if (p[0] == null) {
                        viewFx.msgErreur.setText("Aucun Pokémon ne correspond à votre recherche.");
                        viewFx.barreRecherche.setDisable(false);
                    } else {
                        this.pokemonActuel = p[0];
                        afficherCartePokemon(p[0]);
                        viewFx.messageStatut.setText("Le pokémon " + premiereLettreEnMaj(p[0].nom) + " a été trouvé !");
                        viewFx.barreRecherche.setDisable(false);
                        viewFx.barreRecherche.clear();
                    }
                });

            } catch (Exception e) {
                Platform.runLater(() -> {
                    viewFx.msgErreur.setText("Erreur lors de la requête.");
                    viewFx.barreRecherche.setDisable(false);
                });
            }
        });

        threadApi.start();
    }

    public String premiereLettreEnMaj(String texte) {
        if (texte == null || texte.isEmpty()) {
            return texte;
        }
        return texte.substring(0, 1).toUpperCase() + texte.substring(1);
    }

    // Afficher la carte du pokémon
    public void afficherCartePokemon(Pokemon p) {
        Image image = new Image(p.imageUrl);
        viewFx.image.setImage(image);
        viewFx.image.getStyleClass().add("image");

        viewFx.nomPokemon.setText(premiereLettreEnMaj(p.nom));
        viewFx.nomPokemon.getStyleClass().add("nom");

        viewFx.idPokemon.setText("#" + p.idPokedex);
        viewFx.idPokemon.getStyleClass().add("numPokedex");

        viewFx.type1.setText(premiereLettreEnMaj(p.typePrincipal));
        String classeCss = p.typePrincipal;
        viewFx.type1.getStyleClass().setAll(classeCss, "classLabel");
        if (p.typeSecondaire != null) {
            viewFx.type2.setText(premiereLettreEnMaj(p.typeSecondaire));
            classeCss = p.typeSecondaire;
            viewFx.type2.getStyleClass().setAll(classeCss, "classLabel");
            viewFx.type2.setVisible(true);
        } else {
            viewFx.type2.setVisible(false);
        }
        viewFx.poidsPokemon.setText(p.poids / 10 + " kg");
        viewFx.poidsPokemon.getStyleClass().add("stats");
        viewFx.taillePokemon.setText(p.taille * 10 + " cm");
        viewFx.taillePokemon.getStyleClass().add("stats");

        viewFx.statPv.setText(String.valueOf(p.pointsVie));
        double progresPv = p.pointsVie / 255.0;
        viewFx.barrePv.setProgress(progresPv);
        viewFx.statPv.getStyleClass().add("stats");

        viewFx.statAtk.setText(String.valueOf(p.attaque));
        double progresAtk = p.attaque / 200.0;
        viewFx.barreAtk.setProgress(progresAtk);
        viewFx.statAtk.getStyleClass().add("stats");

        viewFx.statDef.setText(String.valueOf(p.defense));
        double progresDef = p.defense / 255.0;
        viewFx.barreDef.setProgress(progresDef);
        viewFx.statDef.getStyleClass().add("stats");

        viewFx.statSpd.setText(String.valueOf(p.vitesse));
        double progresSpd = p.vitesse / 200.0;
        viewFx.barreSpd.setProgress(progresSpd);
        viewFx.statSpd.getStyleClass().add("stats");

    }
}
