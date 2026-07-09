package maisonneuve.com.view;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
    public Label statAtk;
    public Label statDef;
    public Label statSpd;
    public Label messageStatut;
    public Button btnCapturer;

    // demande de confirmation lors de la libération d'un Pokémon
    public boolean afficherConfirmation(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> resultat = alert.showAndWait();
        return resultat.isPresent() && resultat.get() == ButtonType.OK;
    }

    public PokemonViewFX() {

        // Zone du haut : En-tête et barre de recherche
        titre = new Label("Pokédex");
        sousTitre = new Label("Recherchez tous vos pokémons préférés !");
        barreRecherche = new TextField();
        msgErreur = new Label();
        VBox haut = new VBox(8, titre, sousTitre, barreRecherche, msgErreur);

        // Zone centrale : carte du pokémon
        image = new ImageView();
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
        taillePokemon = new Label("Ex: 5m");
        taille = new Label("Taille");

        VBox colPoids = new VBox(3, poids, poidsPokemon);
        VBox colTaille = new VBox(3, taille, taillePokemon);
        HBox poidsTaille = new HBox(2, colPoids, colTaille);

        // Statistiques
        statistiques = new Label("Statistiques de base");
        pv = new Label("PV");
        atk = new Label("ATK");
        def = new Label("DEF");
        spd = new Label("SPD");

        statPv = new Label("Ex: 5");
        statAtk = new Label("Ex: 5");
        statDef = new Label("Ex: 5");
        statSpd = new Label("Ex: 5");

        VBox colLabels = new VBox(4, pv, atk, def, spd);
        VBox colStats = new VBox(4, statPv, statAtk, statDef, statSpd);
        HBox stats = new HBox(2, colLabels, colStats);

        // Bouton capturer
        btnCapturer = new Button("Capturer");

        // Asemblage
        VBox centre = new VBox(20, carte, types, poidsTaille, stats, btnCapturer);



        // Zone du bas
        messageStatut = new Label("Ex: Pikachu chargé avec succès !");

        racine = new BorderPane();
        racine.setTop(haut);
        racine.setCenter(centre);
        racine.setBottom(messageStatut);
    }

    public Parent getRoot() {return racine;}
}
