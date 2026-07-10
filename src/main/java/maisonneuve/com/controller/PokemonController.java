package maisonneuve.com.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
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
    private boolean pokemonDejaCapture;

    public PokemonController(PokemonViewFX viewFx) {
        this.viewFx = viewFx;

        // Écoute pour la touche "Entrer" pour rechercher le pokémon
        viewFx.barreRecherche.setOnAction((_) -> rechercherPokemon(viewFx.barreRecherche.getText()));

        // Écoute pour le btnCapturer pour capturer le pokémon
        // btnCapturer affiche Relacher et permet de relacher un pokémon si il existe déjà dans la BD
        viewFx.btnCapturer.setOnAction(_ -> {
            if (pokemonDejaCapture) {
                // Relâcher un Pokémon avec popup de confirmation
                String messageRelache = "Êtes-vous sûr de vouloir relâcher " + premiereLettreEnMaj(pokemonActuel.nom) + "?";
                boolean confirmer = viewFx.afficherConfirmation("Confirmer la relâche", messageRelache);

                if (confirmer) {
                    Thread threadRelache = new Thread(() -> {
                        try {
                            dao.relacher(pokemonActuel);

                            Platform.runLater(() -> {
                                viewFx.msgErreur.setText(null);
                                viewFx.messageStatut.setText("Le Pokémon " + premiereLettreEnMaj(pokemonActuel.nom) + " a été relâché ! ✅");
                                viewFx.btnCapturer.setText("Capturer!?");
                                pokemonDejaCapture = false;
                                afficherListeCapture();
                            });
                        } catch (SQLException ex) {
                            Platform.runLater(() -> {
                                if (estUneErreurDeConnexion(ex)) {
                                    viewFx.afficherErreurCritique("La connexion avec le serveur PostgreSQL a été perdue. Fermeture de l'application.");
                                } else {
                                    viewFx.msgErreur.setText("Erreur lors de la relâche. Vous n'avez pas ce Pokémon");
                                }
                            });
                        }
                    });
                    threadRelache.start();
                } else {
                    viewFx.messageStatut.setText("Relâche annulée.");
                }

            } else {
                // Capturer un Pokémon
                Thread threadCapture = new Thread(() -> {
                    try {
                        dao.capturer(pokemonActuel);

                        Platform.runLater(() -> {
                            viewFx.msgErreur.setText(null);
                            viewFx.messageStatut.setText("Le pokémon " + premiereLettreEnMaj(pokemonActuel.nom) + " a été capturé ! ✅");
                            viewFx.btnCapturer.setText("Relâcher!?");
                            pokemonDejaCapture = true;
                            afficherListeCapture();
                        });
                    } catch (SQLException ex) {
                        Platform.runLater(() -> {
                            if (estUneErreurDeConnexion(ex)) {
                                viewFx.afficherErreurCritique("La connexion avec le serveur PostgreSQL a été perdue. Fermeture de l'application.");
                            } else {
                                viewFx.msgErreur.setText("Erreur lors de la capture. Avez-vous déjà capturé ce Pokémon ?");
                            }
                        });
                    }
                });
                threadCapture.start();
            }
        });

        // Définir ce qui est affiché dans la liste des Pokémons capturés
        viewFx.listePokemonsCaptures.setCellFactory((_) -> new javafx.scene.control.ListCell<>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(Pokemon pokemon, boolean empty) {
                super.updateItem(pokemon, empty);

                if (empty || pokemon == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(null);

                    imageView.setFitWidth(40);
                    imageView.setFitHeight(40);
                    imageView.setPreserveRatio(true);

                    if (pokemon.imageUrl != null && !pokemon.imageUrl.isEmpty()) {
                        Image img = new Image(pokemon.imageUrl, 40, 40, true, true, true);
                        imageView.setImage(img);
                    } else {
                        imageView.setImage(null);
                    }

                    Label nomLabel = new Label(premiereLettreEnMaj(pokemon.nom));
                    Label idLabel = new Label("#" + String.valueOf(pokemon.idPokedex));

                    nomLabel.getStyleClass().add("cellule-nom");
                    idLabel.getStyleClass().add("cellule-id");

                    BorderPane conteneurCellule = new BorderPane();

                    conteneurCellule.setLeft(imageView);
                    conteneurCellule.setCenter(nomLabel);
                    conteneurCellule.setRight(idLabel);


                    BorderPane.setAlignment(imageView, Pos.CENTER_LEFT);
                    BorderPane.setAlignment(nomLabel, Pos.CENTER);
                    BorderPane.setAlignment(idLabel, Pos.CENTER_RIGHT);

                    setGraphic(conteneurCellule);
                }
            }
        });

        // Écoute pour la sélection sur la liste des Pokémons capturés, puis affiche sa carte
        viewFx.listePokemonsCaptures.getSelectionModel().selectedItemProperty().addListener((_, _, nouveau) -> {
            if (nouveau != null) {
                afficherCartePokemon(nouveau);
            }
        });

    }

    public void demarrer(Stage stage) throws IOException {
        chargerPokemonInitial(stage);
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
                        viewFx.msgErreur.setText("Aucun Pokémon ne correspond à votre recherche : '" + rechercheNettoyee + "'.");
                        viewFx.barreRecherche.setDisable(false);
                        viewFx.barreRecherche.clear();
                    } else {
                        afficherCartePokemon(p[0]);
                        viewFx.messageStatut.setText("Le Pokémon " + premiereLettreEnMaj(p[0].nom) + " a été trouvé ! ✅");
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
        pokemonActuel = p;

        Image image = new Image(p.imageUrl, 200, 0, true, true, true);
        viewFx.image.setImage(image);
        viewFx.image.getStyleClass().add("image");

        viewFx.nomPokemon.setText(premiereLettreEnMaj(p.nom));
        viewFx.nomPokemon.getStyleClass().add("nom");

        viewFx.idPokemon.setText("#" + p.idPokedex);
        viewFx.idPokemon.getStyleClass().add("numPokedex");

        viewFx.type1.setText(premiereLettreEnMaj(p.typePrincipal));
        String classeCss = p.typePrincipal;
        viewFx.type1.getStyleClass().setAll(classeCss, "pilule-type");
        if (p.typeSecondaire != null) {
            viewFx.type2.setText(premiereLettreEnMaj(p.typeSecondaire));
            classeCss = p.typeSecondaire;
            viewFx.type2.getStyleClass().setAll(classeCss, "pilule-type");
            viewFx.type2.setVisible(true);
            viewFx.type2.setManaged(true);
        } else {
            viewFx.type2.setVisible(false);
            viewFx.type2.setManaged(false);
        }
        viewFx.poidsPokemon.setText(p.poids / 10 + "kg");
        viewFx.poidsPokemon.getStyleClass().add("stats");
        viewFx.taillePokemon.setText(p.taille * 10 + "cm");
        viewFx.taillePokemon.getStyleClass().add("stats");

        viewFx.statPv.setText(String.valueOf(p.pointsVie));
        double progresPv = p.pointsVie / 255.0;
        viewFx.barrePv.setProgress(progresPv);
        viewFx.statPv.getStyleClass().add("stats");

        viewFx.statAtq.setText(String.valueOf(p.attaque));
        double progresAtq = p.attaque / 200.0;
        viewFx.barreAtq.setProgress(progresAtq);
        viewFx.statAtq.getStyleClass().add("stats");

        viewFx.statDef.setText(String.valueOf(p.defense));
        double progresDef = p.defense / 255.0;
        viewFx.barreDef.setProgress(progresDef);
        viewFx.statDef.getStyleClass().add("stats");

        viewFx.statVit.setText(String.valueOf(p.vitesse));
        double progresVit = p.vitesse / 200.0;
        viewFx.barreVit.setProgress(progresVit);
        viewFx.statVit.getStyleClass().add("stats");

        Thread thread = new Thread(() -> {
            try {
                boolean estCapture = dao.estDejaCapture(p.idPokedex);

                Platform.runLater(() -> {
                    pokemonDejaCapture = estCapture;
                    if (!estCapture) {
                        viewFx.btnCapturer.setText("Capturer !?");
                    } else {
                        viewFx.btnCapturer.setText("Relâcher !?");
                    }
                });
            } catch (SQLException ex) {
                Platform.runLater(() -> {
                    if (estUneErreurDeConnexion(ex)) {
                        viewFx.afficherErreurCritique("La connexion avec le serveur PostgreSQL a été perdue. Fermeture de l'application.");
                    } else {
                        viewFx.msgErreur.setText("Erreur lors de la vérification du Pokémon déjà capturé");
                    }
                });
            }
        });
        thread.start();
    }

    public void afficherListeCapture() {
        Thread thread = new Thread(() -> {
            try {
                List<Pokemon> pokemonsCaptures = dao.lister();

                Platform.runLater(() -> {
                    if (pokemonsCaptures == null || pokemonsCaptures.isEmpty()) {
                        List<Pokemon> lignesVides = new ArrayList<>();
                        for (int i = 0; i < 6; i++) {
                            lignesVides.add(null);
                        }
                        viewFx.listePokemonsCaptures.setItems(FXCollections.observableArrayList(lignesVides));
                    } else {
                        viewFx.listePokemonsCaptures.setItems(FXCollections.observableArrayList(pokemonsCaptures));
                    }
                });

            } catch (SQLException ex) {
                Platform.runLater(() -> {
                    if (estUneErreurDeConnexion(ex)) {
                        viewFx.afficherErreurCritique("La connexion avec le serveur PostgreSQL a été perdue. Fermeture de l'application.");
                    } else {
                        viewFx.msgErreur.setText("Erreur lors de l'affichage de la liste des pokémons capturés.");
                    }
                });
            }
        });
        thread.start();
    }

    public void chargerPokemonInitial(Stage stage) {

        // Pas de thread ici, car c'est cette fonction qui afficher la fenêtre
        try {
            List<Pokemon> pokemons = dao.lister();

            // Si la base de donnée est vide, on charge Pikachu par défaut
            if (pokemons == null || pokemons.isEmpty()) {
                Pokemon pikachu = new Pokemon();
                pikachu.idPokedex = 25;
                pikachu.nom = "pikachu";
                pikachu.typePrincipal = "electrik";
                pikachu.pointsVie = 35;
                pikachu.attaque = 55;
                pikachu.defense = 40;
                pikachu.vitesse = 90;
                pikachu.taille = 4.0f;
                pikachu.poids = 60.0F;
                pikachu.imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/25.png";
                afficherCartePokemon(pikachu);
            } else {
                // Si la BD contient un pokémon, on affiche le premier
                afficherCartePokemon(pokemons.getFirst());
            }

            // On démarre la fenêtre ici pour éviter le clignotement
            stage.show();

        } catch (SQLException ex) {
            if (estUneErreurDeConnexion(ex)) {
                viewFx.afficherErreurCritique("Échec lors de la connexion à la base de donnée. Vérifiez qu'elle est bien active sur localhost:5432/exam_pokedex et que vos informations correspondent avec celles dans util/Connexion, puis réessayez.");
                Platform.exit();
            } else {
                viewFx.msgErreur.setText("Erreur lors de l'affichage de la liste des pokémons capturés.");
            }

        }
    }

    public boolean estUneErreurDeConnexion(SQLException ex) {
        String state = ex.getSQLState();
        if (state != null) {
            // Si l'état commence par "08", la connexion avec la base de donnée est perdue
            return state.startsWith("08");
        }
        return false;
    }
}
