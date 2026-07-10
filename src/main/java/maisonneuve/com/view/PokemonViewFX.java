package maisonneuve.com.view;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import maisonneuve.com.modele.Pokemon;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

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
    public Label atq;
    public Label def;
    public Label vit;
    public Label statPv;
    public ProgressBar barrePv;
    public Label statAtq;
    public ProgressBar barreAtq;
    public Label statDef;
    public ProgressBar barreDef;
    public Label statVit;
    public ProgressBar barreVit;
    public Label messageStatut;
    public Button btnCapturer;
    public Label titreListe;
    public ListView<Pokemon> listePokemonsCaptures;
    public ImageView imageViewAsh;
    public Separator ligneSeparation;
    public StackPane stackPaneBtnCapturer;

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
        alert.setHeight(250);

        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
        Platform.exit();
    }


    public PokemonViewFX() {

        // Zone du haut : En-tête et barre de recherche
        titre = new Label("P O K É D E X");
        titre.getStyleClass().add("titre");

        sousTitre = new Label("Recherchez tous vos pokémons préférés !");
        sousTitre.getStyleClass().add("sous-titre");
        VBox.setMargin(sousTitre, new Insets(-10, 0, 0, 0));

        barreRecherche = new TextField();
        barreRecherche.getStyleClass().add("barre-recherche");

        msgErreur = new Label();
        msgErreur.getStyleClass().add("message-erreur");
        VBox haut = new VBox(5, titre, sousTitre, barreRecherche, msgErreur);
        haut.setAlignment(Pos.TOP_CENTER);
        haut.getStyleClass().add("boite-haut");

        // Zone centrale : carte du pokémon
        image = new ImageView();
        image.setFitWidth(175);
        image.setFitHeight(175);
        image.getStyleClass().add("image-pokemon");
        HBox cadreImage = new HBox(image);
        cadreImage.getStyleClass().add("cadre-image");
        cadreImage.setAlignment(Pos.CENTER);


        nomPokemon = new Label("Ex : Pikachu");
        nomPokemon.getStyleClass().add("nom-pokemon");
        idPokemon = new Label("Ex : #25");
        idPokemon.getStyleClass().add("id-pokemon");
        HBox nomId = new HBox(15, nomPokemon, idPokemon);
        nomId.setAlignment(Pos.BASELINE_CENTER);

        VBox carte = new VBox(8, cadreImage, nomId);
        carte.setAlignment(Pos.TOP_CENTER);


        // Types (1 ou 2)
        type1 = new Label("Électrique");
        type2 = new Label("Vol");
        HBox types = new HBox(25, type1, type2);
        types.setAlignment(Pos.CENTER);

        // Ligne de séparation
        ligneSeparation = new Separator();

        // Poids et taille
        poidsPokemon = new Label("Ex : 24 kg");
        poids = new Label("POIDS :");
        poids.setStyle("-fx-text-fill: grey;");
        taillePokemon = new Label("Ex: 50cm");
        taille = new Label("TAILLE :");
        taille.setStyle("-fx-text-fill: grey;");

        // Statistiques
        statistiques = new Label("Statistiques de base");
        statistiques.getStyleClass().add("titre-statistiques");
        pv = new Label("PV :");
        pv.setStyle("-fx-text-fill: grey;");


        atq = new Label("ATQ :");
        atq.setStyle("-fx-text-fill: grey;");


        def = new Label("DEF :");
        def.setStyle("-fx-text-fill: grey;");


        vit = new Label("VIT :");
        vit.setStyle("-fx-text-fill: grey;");

        statPv = new Label("Ex: 5");
        barrePv = new ProgressBar(0.5);
        statAtq = new Label("Ex: 5");
        barreAtq = new ProgressBar(0.5);
        statDef = new Label("Ex: 5");
        barreDef = new ProgressBar(0.5);
        statVit = new Label("Ex: 5");
        barreVit = new ProgressBar(0.5);

        barrePv.setMinSize(150, 15);
        barrePv.setPrefSize(150, 15);
        barrePv.setMaxSize(150, 15);

        barreAtq.setMinSize(150, 15);
        barreAtq.setPrefSize(150, 15);
        barreAtq.setMaxSize(150, 15);

        barreDef.setMinSize(150, 15);
        barreDef.setPrefSize(150, 15);
        barreDef.setMaxSize(150, 15);

        barreVit.setMinSize(150, 15);
        barreVit.setPrefSize(150, 15);
        barreVit.setMaxSize(150, 15);


        // Colonne 1
        HBox boitePoids = new HBox(2, poids, poidsPokemon);
        boitePoids.setAlignment(Pos.BASELINE_CENTER);
        
        HBox boitePv = new HBox(2, pv, statPv);
        boitePv.setAlignment(Pos.BASELINE_CENTER);
        
        HBox boiteAtq = new HBox(2, atq, statAtq);
        boiteAtq.setAlignment(Pos.BASELINE_CENTER);

        // Colonne 2
        HBox boiteTaille = new HBox(2, taille, taillePokemon);
        boiteTaille.setAlignment(Pos.BASELINE_CENTER);
        
        HBox boiteVit = new HBox(2, vit, statVit);
        boiteVit.setAlignment(Pos.BASELINE_CENTER);
        
        HBox boiteDef = new HBox(2, def, statDef);
        boiteDef.setAlignment(Pos.BASELINE_CENTER);

        VBox col1 = new VBox(5, boitePoids, boitePv, barrePv, boiteAtq, barreAtq);
        VBox col2 = new VBox(5, boiteTaille, boiteVit, barreVit, boiteDef, barreDef);
        HBox stats = new HBox(16, col1, col2);
        stats.setAlignment(Pos.CENTER);

        // Vbox pour la carte
        VBox carteStats = new VBox(10, carte, types, ligneSeparation, statistiques, stats);
        carteStats.getStyleClass().add("carte-pokemon");
        carteStats.setAlignment(Pos.CENTER);
        VBox.setMargin(carteStats, new Insets(15, 15, 5, 15 ));

        // Bouton capturer
        btnCapturer = new Button("Capturer !?");
        btnCapturer.getStyleClass().add("btn-capturer");
        stackPaneBtnCapturer = new StackPane(btnCapturer);
        HBox conteneurBouton = new HBox(stackPaneBtnCapturer);
        conteneurBouton.setAlignment(Pos.CENTER);
        btnCapturer.setMinHeight(40);
        btnCapturer.setPrefHeight(40);
        btnCapturer.setMaxHeight(40);


        //Image de Ash
        String cheminImageAsh = getClass().getResource("/images/ashCut.png").toExternalForm();
        Image imageAsh = new Image(cheminImageAsh);
        imageViewAsh = new ImageView(imageAsh);
        imageViewAsh.setFitWidth(150);
        imageViewAsh.setPreserveRatio(true);

        // Liste des pokémons capturés
        titreListe = new Label("Liste des Pokémons capturés");
        listePokemonsCaptures = new ListView<>();
        listePokemonsCaptures.setFixedCellSize(40);
        listePokemonsCaptures.setPrefHeight(250);
        listePokemonsCaptures.setMaxHeight(250);
        HBox cadreListe = new HBox(listePokemonsCaptures);
        cadreListe.setAlignment(Pos.CENTER);

        // Hbox Image Ash et liste pokémons
        HBox imgListe = new HBox(4, imageViewAsh, cadreListe);
        HBox.setHgrow(listePokemonsCaptures, Priority.ALWAYS);
        imgListe.setAlignment(Pos.BOTTOM_CENTER);
        VBox.setMargin(imgListe, new Insets(0, 15, 0, 15));
        VBox.setVgrow(imgListe, Priority.ALWAYS);

        // Assemblage
        VBox centre = new VBox(2, carteStats, conteneurBouton, imgListe);

        // Zone du bas
        messageStatut = new Label("Bienvenue dans votre Pokédex !");
        messageStatut.getStyleClass().add("statut");
        messageStatut.setAlignment(Pos.CENTER);

        racine = new BorderPane();
        racine.setTop(haut);
        racine.setCenter(centre);
        racine.setBottom(messageStatut);
        //BorderPane.setAlignment(messageStatut, Pos.BOTTOM_CENTER);
        barreRecherche.maxWidthProperty().bind(racine.widthProperty().multiply(0.50));
        ligneSeparation.maxWidthProperty().bind(racine.widthProperty().multiply(0.75));
        cadreImage.maxWidthProperty().bind(racine.widthProperty().multiply(0.50));
        btnCapturer.prefWidthProperty().bind(carte.widthProperty());
        listePokemonsCaptures.prefWidthProperty().bind(carte.widthProperty());
    }

    public void animationImageApparition(ImageView image) {
        image.setOpacity(0.0);
        FadeTransition fade = new FadeTransition(Duration.seconds(1.5), image);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();
    }

    public void animationBoutonGrosseur(StackPane stackBouton) {
        ScaleTransition zoomIn = new ScaleTransition(Duration.seconds(0.2), stackBouton);
        zoomIn.setToX(1.1);
        zoomIn.setToY(1.1);

        ScaleTransition zoomOut = new ScaleTransition(Duration.seconds(0.2), stackBouton);
        zoomOut.setToX(1.06);
        zoomOut.setToY(1.06);

        stackBouton.setOnMouseEntered(event -> {
            zoomOut.stop(); // on stop d'abord pour arreter l'animation si elle est en train de jouer
            zoomIn.play();
        });

        stackBouton.setOnMouseExited(event -> {
            zoomIn.stop();
            zoomOut.play();
        });

    }

    public void animationDeclencherBalayage(StackPane stackBouton, Region bouton) {
        double largeur = bouton.getWidth();
        double hauteur = bouton.getHeight();
        double hauteurForme = hauteur + 300;

        // la forme blanche c'est le shine qui passe sur le bouton
        Rectangle formeBlanche = new Rectangle(60, hauteurForme);
        formeBlanche.setFill(Color.web("#FFFFFF", 0.65));

        // rotation à partir d'un nouveau pivot au centre de la forme pour éviter des problèmes de positionnement
        // utiliser simplement .setRotate effectuerait un pivot à partir du coin supérieur gauche
        Rotate r = new Rotate(30, 20, hauteurForme / 2);
        formeBlanche.getTransforms().add(r);

        formeBlanche.setMouseTransparent(true); // pour ne pas bloquer les clics
        formeBlanche.setManaged(false); // empeche le hbox de se resizer a l'apparition de la forme

        // création d'un masque de la taille du bouton avec les mêmes valeurs de border-radius
        Rectangle masque = new Rectangle(largeur, hauteur);
        masque.setArcWidth(25);
        masque.setArcHeight(25);

        // appliquer le masque et envoyer la forme blanche dans le StackPane
        stackBouton.setClip(masque);
        stackBouton.getChildren().add(formeBlanche);

        // animation de gauche à droite
        formeBlanche.setTranslateX(-largeur - 80); // Départ caché à gauche
        formeBlanche.setTranslateY(-hauteurForme / 2 );

        TranslateTransition tt = new TranslateTransition(Duration.seconds(.7), formeBlanche);
        tt.setToX(largeur + 80); // Arrivée cachée à droite

        tt.setOnFinished(e -> stackBouton.getChildren().remove(formeBlanche));

        tt.play();
    }

    public Parent getRoot() {return racine;}

}
