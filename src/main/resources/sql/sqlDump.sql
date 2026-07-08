DROP TABLE IF EXISTS pokemons;
DROP TYPE IF EXISTS type_pokemon CASCADE;

CREATE TYPE type_pokemon AS ENUM (
    'normal',
    'fire',
    'water',
    'grass',
    'electric',
    'ice',
    'fighting',
    'poison',
    'ground',
    'flying',
    'psychic',
    'bug',
    'rock',
    'ghost',
    'dragon',
    'dark',
    'steel',
    'fairy'
);

CREATE TABLE pokemons
(
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    id_pokedex      INT           NOT NULL,
    nom             VARCHAR(100)  NOT NULL,
    type_principal  type_pokemon  NOT NULL,
    type_secondaire type_pokemon,
    points_vie      INT           NOT NULL,
    taille          DECIMAL(6, 2) NOT NULL,
    poids           DECIMAL(6, 2) NOT NULL,
    image_url       TEXT          NOT NULL,
    capture_le       TIMESTAMPTZ DEFAULT NOW(),

    CONSTRAINT pv_positif CHECK ( points_vie > 0 ),
    CONSTRAINT taille_positive CHECK ( taille > 0 ),
    CONSTRAINT poids_positif CHECK ( taille > 0 )
);

INSERT INTO pokemons (id_pokedex, nom, type_principal, points_vie, taille, poids, image_url) VALUES (
    143, 'snorlax', 'normal', 160, 21, 4600, 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/143.png'
);
INSERT INTO pokemons (id_pokedex, nom, type_principal, type_secondaire, points_vie, taille, poids, image_url) VALUES (
    145, 'zapdos', 'electric', 'flying', 90, 16, 526, 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/145.png'
);



