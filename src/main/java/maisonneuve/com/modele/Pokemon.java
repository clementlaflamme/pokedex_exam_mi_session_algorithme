package maisonneuve.com.modele;

public class Pokemon {
    public String id;
    public Integer idPokedex;
    public String nom;
    public String typePrincipal;
    public String typeSecondaire;
    public Integer pointsVie;
    public Integer attaque;
    public Integer defense;
    public Integer vitesse;
    public Float taille;
    public Float poids;
    public String imageUrl;
    public String captureLe;

    // constructeur vide
    public Pokemon() {}

    @Override
    public String toString() {
        return "Pokemon{" +
                "id='" + id + '\'' +
                ", idPokedex=" + idPokedex +
                ", nom='" + nom + '\'' +
                ", typePrincipal='" + typePrincipal + '\'' +
                ", typeSecondaire='" + typeSecondaire + '\'' +
                ", pointsVie=" + pointsVie +
                ", attaque=" + attaque +
                ", defense=" + defense +
                ", vitesse=" + vitesse +
                ", taille=" + taille +
                ", poids=" + poids +
                ", imageUrl='" + imageUrl + '\'' +
                ", captureLe='" + captureLe + '\'' +
                '}';
    }
}
