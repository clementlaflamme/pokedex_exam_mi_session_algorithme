package maisonneuve.com.view;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PokemonViewFX {

    Label titre;
    Label sousTitre;
    TextField barreRecherche;
    BorderPane racine;
    ImageView image;
    Label nomPokemon;
    Label idPokemon;
    Label type1;
    Label type2;
    Label poidsPokemon;
    Label poids;
    Label taillePokemon;
    Label taille;
    Label statistiques;
    Label pv;
    Label atk;
    Label def;
    Label spd;
    Label statPv;
    Label statAtk;
    Label statDef;
    Label statSpd;
    Label messageStatut;

    public PokemonViewFX() {

        // Zone du haut : En-tête et barre de recherche
        titre = new Label("Pokédex");
        sousTitre = new Label("Recherchez tous vos pokémons préférés !");
        barreRecherche = new TextField();
        VBox haut = new VBox(6, titre, sousTitre, barreRecherche);

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

        VBox centre = new VBox(20, carte, types, poidsTaille, stats);

        // Zone du bas
        messageStatut = new Label("Ex: Pikachu chargé avec succès !");

        racine = new BorderPane();
        racine.setTop(haut);
        racine.setCenter(centre);
        racine.setBottom(messageStatut);
    }

    public Parent getRoot() {return racine;}
}
