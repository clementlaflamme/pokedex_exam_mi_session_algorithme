package maisonneuve.com.modele;

import maisonneuve.com.util.Connexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class PokemonDAO {
    public void capturer(Pokemon p) throws SQLException {
        String sql =
            "INSERT INTO pokemons"
            +"(id_pokedex, nom, type_principal, type_secondaire, points_vie, taille, poids, image_url) "
            +"VALUES(?, ?, ?::type_pokemon, ?::type_pokemon, ?, ?, ?, ?) "
            +"ON CONFLICT (id_pokedex) DO UPDATE SET "
            +"nom=EXCLUDED.nom";


        try(Connection co = Connexion.getConnexion();
            PreparedStatement ps = co.prepareStatement(sql)){

            ps.setInt(1, p.idPokedex);
            ps.setString(2, p.nom);
            ps.setString(3, p.typePrincipal);

            // permet au type_secondaire d'être NULL
            if (p.typeSecondaire == null) {
                ps.setNull(4, Types.VARCHAR);
            } else {
                ps.setString(4, p.typeSecondaire);
            }

            ps.setInt(5, p.pointsVie);
            ps.setFloat(6, p.taille);
            ps.setFloat(7, p.poids);
            ps.setString(8, p.imageUrl);

            ps.executeUpdate();

            System.out.println(p.nom + " a été capturé ! ");
        }
    }

    public List<Pokemon> lister() throws SQLException{
        String sql = "SELECT * FROM pokemons ORDER BY id_pokedex ASC";
        List<Pokemon> tous = new ArrayList<>();

        try(Connection co = Connexion.getConnexion();
            Statement st = co.createStatement();
            ResultSet rs = st.executeQuery(sql)) {

            while(rs.next()) {
                Pokemon p = new Pokemon();
                p.id = rs.getString("id");
                p.idPokedex = rs.getInt("id_pokedex");
                p.nom = rs.getString("nom");
                p.typePrincipal = rs.getString("type_principal");
                p.typeSecondaire = rs.getString("type_secondaire");
                p.pointsVie = rs.getInt("points_vie");
                p.taille = rs.getFloat("taille");
                p.poids = rs.getFloat("poids");
                p.imageUrl = rs.getString("image_url");
                p.captureLe = rs.getString("capture_le");
                tous.add(p);
            }

        }
        return tous;
    }

    public void relacher(Pokemon p) throws SQLException {
        String sql =
                "DELETE FROM pokemons WHERE nom = ?";

        try(Connection co = Connexion.getConnexion();
            PreparedStatement ps = co.prepareStatement(sql)){

            ps.setString(1, p.nom);

            ps.executeUpdate();

            System.out.println(p.nom + " a été relâché ! ");
        }
    }

}
