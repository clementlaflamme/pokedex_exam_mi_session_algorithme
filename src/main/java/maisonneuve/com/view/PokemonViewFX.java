package maisonneuve.com.view;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import maisonneuve.com.modele.Pokemon;

import java.util.Optional;

public class PokemonViewFX {

    public Label titre;
    public Label sousTitre;
    public TextField barreRecherche;
    public Label msgErreur;
    public BorderPane racine;
    public ImageView image;
    public Label nomPokemon;
    public Label idPokemon;
    public Label type1;
    public Label type2;
    public Label poidsPokemon;
    public Label poids;
    public Label taillePokemon;
    public Label taille;
    public Label statistiques;
    public Label pv;
    public Label atk;
    public Label def;
    public Label spd;
    public Label statPv;
    public ProgressBar barrePv;
    public Label statAtk;
    public ProgressBar barreAtk;
    public Label statDef;
    public ProgressBar barreDef;
    public Label statSpd;
    public ProgressBar barreSpd;
    public Label messageStatut;
    public Button btnCapturer;
    public Label titreListe;
    public ListView<Pokemon> listePokemonsCaptures;

    // demande de confirmation lors de la libération d'un Pokémon
    public boolean afficherConfirmation(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> resultat = alert.showAndWait();
        return resultat.isPresent() && resultat.get() == ButtonType.OK;
    }

    public void afficherErreurCritique(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Erreur !");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
        Platform.exit();
    }


    public PokemonViewFX() {

        // Zone du haut : En-tête et barre de recherche
        titre = new Label("P O K É D E X");
        titre.getStyleClass().add("nom");

        // test CSS
        //titre.getStyleClass().add("test");


        sousTitre = new Label("Recherchez tous vos pokémons préférés !");
        sousTitre.getStyleClass().add("subtitle");
        barreRecherche = new TextField();
        barreRecherche.getStyleClass().add("borderRad");

        msgErreur = new Label();
        msgErreur.getStyleClass().add("subtitle");
        VBox haut = new VBox(8, titre, sousTitre, barreRecherche, msgErreur);

        // Zone centrale : carte du pokémon
        image = new ImageView();
        image.setFitWidth(200);
        image.setFitHeight(200);

        nomPokemon = new Label("Ex : Pikachu");
        idPokemon = new Label("Ex : #25");
        VBox carte = new VBox(8, image, nomPokemon, idPokemon);

        // Types (1 ou 2)
        type1 = new Label("Électrique");
        type2 = new Label("Vol");
        HBox types = new HBox(2, type1, type2);

        // Poids et taille
        poidsPokemon = new Label("Ex : 24 kg");
        poids = new Label("Poids");
        poids.getStyleClass().add("subtitle");
        taillePokemon = new Label("Ex: 50cm");
        taille = new Label("Taille");
        taille.getStyleClass().add("subtitle");

        VBox colPoids = new VBox(3, poids, poidsPokemon);
        VBox colTaille = new VBox(3, taille, taillePokemon);
        HBox poidsTaille = new HBox(2, colPoids, colTaille);

        // Statistiques
        statistiques = new Label("Statistiques de base");
        pv = new Label("PV");
        pv.setStyle("-fx-text-fill: grey;");


        atk = new Label("ATK");
        atk.setStyle("-fx-text-fill: grey;");


        def = new Label("DEF");
        def.setStyle("-fx-text-fill: grey;");


        spd = new Label("SPD");
        spd.setStyle("-fx-text-fill: grey;");

        statPv = new Label("Ex: 5");
        barrePv = new ProgressBar(0.5);
        statAtk = new Label("Ex: 5");
        barreAtk = new ProgressBar(0.5);
        statDef = new Label("Ex: 5");
        barreDef = new ProgressBar(0.5);
        statSpd = new Label("Ex: 5");
        barreSpd = new ProgressBar(0.5);

        VBox colLabels = new VBox(4, pv, atk, def, spd);
        VBox colProgressBars = new VBox(4, barrePv, barreAtk, barreDef, barreSpd);
        VBox colStats = new VBox(4, statPv, statAtk, statDef, statSpd);
        HBox stats = new HBox(8, colLabels, colProgressBars, colStats);

        // Bouton capturer
        btnCapturer = new Button("Capturer");

        // Liste des pokémons capturés
        titreListe = new Label("Liste des Pokémons capturés");
        listePokemonsCaptures = new ListView<>();

        // Assemblage
        VBox centre = new VBox(20, carte, types, poidsTaille, stats, btnCapturer, listePokemonsCaptures);



        // Zone du bas
        messageStatut = new Label("Ex: Pikachu chargé avec succès !");

        racine = new BorderPane();
        racine.setTop(haut);
        racine.setCenter(centre);
        racine.setBottom(messageStatut);
    }

    public Parent getRoot() {return racine;}
}
