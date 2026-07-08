package maisonneuve.com;

import maisonneuve.com.util.InitSQL;

import java.io.IOException;

public class Main {
    static void main() throws IOException {

        // Initialiser la base de donnée
        InitSQL.executerInitSQL();

    }
}
