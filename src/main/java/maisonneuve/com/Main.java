package maisonneuve.com;

import maisonneuve.com.modele.Pokemon;
import maisonneuve.com.modele.PokemonDAO;
import maisonneuve.com.service.PokedexAPI;
import maisonneuve.com.util.InitSQL;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    static void main(String[] args) throws IOException, InterruptedException, SQLException {

        // Initialiser la base de donnée
        InitSQL.executerInitSQL();

        MainFX.main(args);

        //Tests
       /* PokedexAPI api = new PokedexAPI();
        Pokemon p = api.recupererPokemon("pikachu");
        PokemonDAO dao = new PokemonDAO();
        dao.capturer(p);*/
    }
}
