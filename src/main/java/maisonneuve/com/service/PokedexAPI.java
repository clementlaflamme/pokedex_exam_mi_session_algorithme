package maisonneuve.com.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import maisonneuve.com.modele.Pokemon;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class PokedexAPI {
    private static final String URL = "https://pokeapi.co/api/v2/pokemon/";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private final Map<String, String> TYPES = Map.ofEntries(
            Map.entry("normal", "normal"),
            Map.entry("fire", "feu"),
            Map.entry("water", "eau"),
            Map.entry("grass", "plante"),
            Map.entry("electric", "electrik"),
            Map.entry("ice", "glace"),
            Map.entry("fighting", "combat"),
            Map.entry("poison", "poison"),
            Map.entry("ground", "sol"),
            Map.entry("flying", "vol"),
            Map.entry("psychic", "psy"),
            Map.entry("bug", "insecte"),
            Map.entry("rock", "roche"),
            Map.entry("ghost", "spectre"),
            Map.entry("dragon", "dragon"),
            Map.entry("dark", "tenebre"),
            Map.entry("steel", "acier"),
            Map.entry("fairy", "fee")
    );

    public Pokemon recupererPokemon(String recherche) throws IOException, InterruptedException {

        HttpRequest req = HttpRequest.newBuilder(URI.create(URL + recherche)).GET().build();
        HttpResponse<String> res;

        res = client.send(req, HttpResponse.BodyHandlers.ofString());

        if (res.statusCode() == 404) {
            System.err.println("Erreur : Le Pokémon '" + recherche + "' n'existe pas.");
            return null;
        }

        JsonNode pokemon = mapper.readTree(res.body());
        Pokemon p = new Pokemon();

        p.idPokedex = Integer.parseInt(pokemon.get("id").asText());
        p.nom = pokemon.get("name").asText();
        p.typePrincipal = TYPES.get(pokemon.at("/types/0/type/name").asText());
        p.typeSecondaire = !pokemon.at("/types/1/type/name").isMissingNode() ? TYPES.get(pokemon.at("/types/1/type/name").asText()) : null;
        p.pointsVie = Integer.parseInt(pokemon.at("/stats/0/base_stat").asText());
        p.attaque = Integer.parseInt(pokemon.at("/stats/1/base_stat").asText());
        p.defense = Integer.parseInt(pokemon.at("/stats/2/base_stat").asText());
        p.vitesse = Integer.parseInt(pokemon.at("/stats/5/base_stat").asText());
        p.taille = Float.parseFloat(pokemon.get("height").asText());
        p.poids = Float.parseFloat(pokemon.get("weight").asText());
        p.imageUrl = pokemon.at("/sprites/other/official-artwork/front_default").asText();

        System.out.println(p.nom + " a été récupéré !\n" + p);

        return p;
    }
}


