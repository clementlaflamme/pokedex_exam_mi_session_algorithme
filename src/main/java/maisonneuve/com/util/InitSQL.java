package maisonneuve.com.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.util.stream.Collectors;

public class InitSQL {
    public static boolean executerInitSQL() throws IOException {

        try (InputStream is = InitSQL.class.getResourceAsStream("/sql/sqlDump.sql")) {

            if (is == null) {
                System.err.println("Erreur : Le fichier sqlDump.sql est introuvable dans les ressources.");
                return false;
            }

            String sql = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));

            try (Connection co = Connexion.getConnexion();
                 Statement stmt = co.createStatement()) {
                stmt.execute(sql);
                System.out.println("Base de donnée chargée !");
                return true;

            } catch (Exception e) {
                System.err.println("Erreur : " + e.getMessage());
                return false;
            }
        }
    }
}
