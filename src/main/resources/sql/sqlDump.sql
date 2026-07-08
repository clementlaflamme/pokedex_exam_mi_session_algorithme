DROP TABLE IF EXISTS pokemons;
DROP TYPE IF EXISTS type_pokemon CASCADE;

CREATE TYPE type_pokemon AS ENUM (
    'normal',
    'feu',
    'eau',
    'plante',
    'electrik',
    'glace',
    'combat',
    'poison',
    'sol',
    'vol',
    'psy',
    'insecte',
    'roche',
    'spectre',
    'dragon',
    'tenebre',
    'acier',
    'fee'
);

CREATE TABLE pokemons
(
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    id_pokedex      INT           NOT NULL,
    nom             VARCHAR(100)  NOT NULL,
    type_principal  type_pokemon  NOT NULL,
    type_secondaire type_pokemon,
    points_vie      INT           NOT NULL,
    attaque         INT           NOT NULL,
    defense         INT           NOT NULL,
    vitesse         INT           NOT NULL,
    taille          DECIMAL(6, 2) NOT NULL,
    poids           DECIMAL(6, 2) NOT NULL,
    image_url       TEXT          NOT NULL,
    capture_le      TIMESTAMPTZ      DEFAULT NOW(),

    CONSTRAINT uq_id_pokedex UNIQUE (id_pokedex),
    CONSTRAINT pv_positif CHECK ( points_vie > 0 ),
    CONSTRAINT taille_positive CHECK ( taille > 0 ),
    CONSTRAINT poids_positif CHECK ( taille > 0 )
);

INSERT INTO pokemons (id_pokedex, nom, type_principal, points_vie, attaque, defense, vitesse, taille, poids, image_url)
VALUES (143, 'snorlax', 'normal', 160, 110, 65, 30, 21, 4600,
        'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/143.png');
INSERT INTO pokemons (id_pokedex, nom, type_principal, type_secondaire, points_vie, attaque, defense, vitesse, taille,
                      poids, image_url)
VALUES (145, 'zapdos', 'electrik', 'vol', 90, 90, 85, 100, 16, 526,
        'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/145.png');