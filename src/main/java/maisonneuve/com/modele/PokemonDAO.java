package maisonneuve.com.modele;

import maisonneuve.com.util.Connexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class PokemonDAO {
    public void sauvegarder(Pokemon p) throws SQLException {
        String sql =
            "INSERT INTO pokemons"
            +"(id, id_pokedex, nom, type_principal, type_secondaire, points_vie, taille, poids, image_url) "
            +"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?) "
            +"ON CONFLICT (id) DO UPDATE SET "
            +"nom=EXCLUDED.nom";


        try(Connection co = Connexion.getConnexion();
            PreparedStatement ps = co.prepareStatement(sql)){

            ps.setString(1, p.id);
            ps.setInt(2, p.id_pokedex);
            ps.setString(3, p.nom);
            ps.setString(4, p.type_principal);

            // permet au type_secondaire d'être NULL
            if (p.type_secondaire == null) {
                ps.setNull(5, Types.VARCHAR);
            } else {
                ps.setString(5, p.type_secondaire);
            }

            ps.setInt(6, p.points_vie);
            ps.setFloat(7, p.taille);
            ps.setFloat(8, p.poids);
            ps.setString(9, p.image_url);

            ps.executeUpdate();
        }
    }

    public List<Pokemon> lister() throws SQLException{
        String sql = "SELECT * from pokemons ORDER BY id_pokedex ASC";
        List<Pokemon> tous = new ArrayList<>();

        try(Connection co = Connexion.getConnexion();
            Statement st = co.createStatement();
            ResultSet rs = st.executeQuery(sql)) {

            while(rs.next()) {
                Pokemon p = new Pokemon();
                p.id = rs.getString("id");
                p.id_pokedex = rs.getInt("id_pokedex");
                p.nom = rs.getString("nom");
                p.type_principal = rs.getString("type_principal");
                p.type_secondaire = rs.getString("type_secondaire");
                p.points_vie = rs.getInt("points_vie");
                p.taille = rs.getFloat("taille");
                p.poids = rs.getFloat("poids");
                p.image_url = rs.getString("image_url");
                p.capture_le = rs.getString("capture_le");
                tous.add(p);
            }

        }
        return tous;
    }
}
