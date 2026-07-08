package maisonneuve.com.modele;

public class Pokemon {
    public String id;
    public Integer id_pokedex;
    public String nom;
    public String type_principal;
    public String type_secondaire;
    public Integer points_vie;
    public Float taille;
    public Float poids;
    public String image_url;
    public String capture_le;

    // constructeur vide
    public Pokemon() {}

    @Override
    public String toString() {
        return "Pokemon{" +
                "id='" + id + '\'' +
                ", id_pokedex=" + id_pokedex +
                ", nom='" + nom + '\'' +
                ", type_principal='" + type_principal + '\'' +
                ", type_secondaire='" + type_secondaire + '\'' +
                ", points_vie=" + points_vie +
                ", taille=" + taille +
                ", poids=" + poids +
                ", image_url='" + image_url + '\'' +
                ", capture_le='" + capture_le + '\'' +
                '}';
    }
}
