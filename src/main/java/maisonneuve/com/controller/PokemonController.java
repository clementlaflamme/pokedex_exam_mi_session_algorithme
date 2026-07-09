package maisonneuve.com.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import maisonneuve.com.modele.Pokemon;
import maisonneuve.com.modele.PokemonDAO;
import maisonneuve.com.service.PokedexAPI;
import maisonneuve.com.view.PokemonViewFX;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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
                            afficherListeCapture();
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
                        afficherListeCapture();
                    } catch (SQLException ex) {
                        viewFx.msgErreur.setText("Erreur lors de la capture. Avez-vous déjà capturé ce Pokémon ?");
                    }
                }
            } catch (SQLException ex) {
                viewFx.msgErreur.setText("Erreur de connexion avec la base de données.");
            }
        });

        // Définir ce qui est affiché dans la liste des Pokémons capturés
        viewFx.listePokemonsCaptures.setCellFactory(listView -> new javafx.scene.control.ListCell<Pokemon>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(Pokemon pokemon, boolean empty) {
                super.updateItem(pokemon, empty);

                if (empty || pokemon == null) {
                    setText(null);
                    setGraphic(null);
                } else {

                    // Initialisation du cadre de l'image
                    imageView.setFitWidth(40);
                    imageView.setFitHeight(40);
                    imageView.setPreserveRatio(true);

                    // Les composants qui sont affichés dans la liste
                    setText(premiereLettreEnMaj(pokemon.nom));

                    // Si l'image est valide, on l'affiche
                    if (pokemon.imageUrl != null && !pokemon.imageUrl.isEmpty()) {
                        Image img = new Image(pokemon.imageUrl, 40, 40, true, true, true);
                        imageView.setImage(img);

                        setGraphic(imageView);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });

        viewFx.listePokemonsCaptures.getSelectionModel().selectedItemProperty().addListener((obs, ancien, nouveau) -> {
            if (nouveau != null) {
                afficherCartePokemon(nouveau);
                pokemonActuel = nouveau;
            }
        });

    }


    public void demarrer() {
        afficherListeCapture();
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
        Image image = new Image(p.imageUrl, 200, 0, true, true, true);
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

    public void afficherListeCapture() {
        Thread thread = new Thread(() -> {
            try {
                List<Pokemon> pokemonsCaptures = dao.lister();

                if (pokemonsCaptures == null || pokemonsCaptures.isEmpty()) {
                    return;
                }

                Platform.runLater(() -> {
                    viewFx.listePokemonsCaptures.setItems(FXCollections.observableArrayList(pokemonsCaptures));
                });
            } catch (SQLException e) {
                Platform.runLater(() -> {
                    System.err.println("Erreur lors de l'affichage de la liste des pokémons capturés : " + e.getMessage());
                    viewFx.msgErreur.setText("Erreur lors de l'affichage de la liste des pokémons capturés.");
                });
            }
        });
        thread.start();
    }
}
