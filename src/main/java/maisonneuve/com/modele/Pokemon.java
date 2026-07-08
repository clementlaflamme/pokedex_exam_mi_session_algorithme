package maisonneuve.com.modele;

public class Pokemon {
    public String id;
    public Integer idPokedex;
    public String nom;
    public String typePrincipal;
    public String typeSecondaire;
    public Integer pointsVie;
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
                ", taille=" + taille +
                ", poids=" + poids +
                ", imageUrl='" + imageUrl + '\'' +
                ", captureLe='" + captureLe + '\'' +
                '}';
    }
}
