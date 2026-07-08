package maisonneuve.com.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import maisonneuve.com.modele.Pokemon;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PokedexAPI {
    private static final String URL = "https://pokeapi.co/api/v2/pokemon/";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public Pokemon recupererPokemon(String recherche) throws IOException, InterruptedException {

        HttpRequest req = HttpRequest.newBuilder(URI.create(URL + recherche)).GET().build();
        HttpResponse<String> res;

        try {
            res = client.send(req, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        JsonNode pokemon = mapper.readTree(res.body());
        Pokemon p = new Pokemon();

        p.id = pokemon.get("id").asText();
        p.idPokedex = Integer.valueOf(pokemon.get("id_pokedex").asText());
        p.nom = pokemon.get("nom").asText();
        p.typePrincipal = pokemon.get("id").asText();
        p.typeSecondaire = pokemon.get("id").asText();
        p.pointsVie = Integer.valueOf(pokemon.get("points_vie").asText());
        p.taille = Float.valueOf(pokemon.get("taille").asText());
        p.poids = Float.valueOf(pokemon.get("poids").asText());
        p.imageUrl = pokemon.get("image_url").asText();
        p.captureLe = pokemon.get("capture_le").asText();

        return p;
    }
}


