DROP DATABASE IF EXISTS vias_obras;
CREATE DATABASE vias_obras;

-- ESQUEMA DOMINIOS
DROP SCHEMA IF EXISTS dominios;
CREATE SCHEMA dominios;

DROP TABLE IF EXISTS dominios.municipios;
CREATE TABLE dominios.municipios{
       codigo_ine_municipio NUMERIC(2),
       nome VARCHAR(50),
       CONSTRAINT pk_municipios PRIMARY KEY(codigo_ine_municipio)
}; -- TODO insertar valores: codigos INE + nome

DROP TABLE IF EXISTS dominios.topografia_tipo;
CREATE TABLE dominios.topografia_tipos{
       id_tipo NUMERIC(1),
       nome_tipo VARCHAR(10),
       CONSTRAINT pk_topografia_tipo PRIMARY KEY (id_tipo)
}; -- TODO insertar valores: A, O, 0, MA, LL, Ll

DROP TABLE IF EXISTS dominios.suelo_tipo;
CREATE TABLE dominios.suelo_tipos{
       id_tipo NUMERIC(1),
       nome_tipo VARCHAR(10),
       CONSTRAINT pk_suelo_tipo PRIMARY KEY (id_tipo)
}; -- TODO insertar valores: A, A - G, G, P

DROP TABLE IF EXISTS dominios.estado;
CREATE TABLE dominios.estados{
       id_estado NUMERIC(1),
       nome_estado VARCHAR(10),
       CONSTRAINT pk_estado PRIMARY KEY (id_estado)
}; -- TODO insertar valores: B, M

DROP TABLE IF EXISTS dominios.pavimento_tipo;
CREATE TABLE dominios.pavimento_tipos{
       id_tipo NUMERIC(1),
       nome_tipo VARCHAR(10),
       CONSTRAINT pk_pavimento_tipo PRIMARY KEY (id_tipo)
}; -- TODO insertar valores: B, M, AAC, AAF, ...

DROP TABLE IF EXISTS dominios.bionda_tipo;
CREATE TABLE dominios.bionda_tipos{
       id_tipo NUMERIC(1),
       nome_tipo VARCHAR(10),
       CONSTRAINT pk_bionda_tipo PRIMARY KEY (id_tipo)
}; -- TODO insertar valores: curva, recto

-- TODO: definir si interesa
DROP TABLE IF EXISTS dominios.cuneta_tipo;
CREATE TABLE dominios.cuneta_tipos{
       id_tipo NUMERIC(1),
       nome_tipo VARCHAR(10),
       CONSTRAINT pk_cuneta_tipo PRIMARY KEY (id_tipo)
}; -- TODO insertar valores: tierra, hormigon

DROP TABLE IF EXISTS dominios.sinais_tipos;
CREATE TABLE dominios.sinais_tipos{
       id_tipo NUMERIC(3),
       nome_tipo VARCHAR(20),
       CONSTRAINT pk_sinais_tipo PRIMARY KEY (id_tipo),
}; -- TODO insertar valores: velocidad maxima, entrada poblado, ...

-- ESQUEMA INVENTARIO
DROP SCHEMA IF EXISTS inventario;
CREATE SCHEMA inventario;

DROP TABLE IF EXISTS inventario.carreteras;
CREATE TABLE inventario.carreteras{
       id_carretera NUMERIC(4),
       nome VARCHAR(50),
       lonxitude DOUBLE PRECISION,
       orixe VARCHAR(50),
       fin VARCHAR(50),
       CONSTRAINT pk_carreteras PRIMARY KEY (id_carretera)
};

DROP TABLE IF EXISTS inventario.tramos_intermunicipais;
CREATE TABLE inventario.tramos_intermunicipais{
       id_carretera NUMERIC(4)
       id_intermunicipal NUMERIC(3)
       codigo_ine_municipio -- ver dominios.municipio
       pk_orixe NUMERIC(3,2)
       pk_fin NUMERIC(3,2)
       lonxitude DOUBLE PRECISION,
       topografia NUMERIC(1), -- ver dominios.topografia_tipo
       tipo_suelo NUMERIC(1), -- ver dominios.suelo_tipo
       cota_maxima NUMERIC(5),
       incorporacion_ano NUMERIC(4)
       incorporacion_lonxitude DOUBLE PRECISION,
       estado_drenaxe TEXT, -- TODO: non ten datos
       observacions TEXT,
       CONSTRAINT pk_tramos_intermunicipais PRIMARY KEY(id_carretera, id_intermunicipal)
};

DROP TABLE IF EXISTS inventario.tramos_intramunicipais;
CREATE TABLE inventario.tramos_intramunicipais{
       id_carretera NUMERIC(4),
       id_intermunicipal NUMERIC(3),
       id_intramunicipal NUMERIC(3),
       pk_orixe NUMERIC(3,2),
       pk_fin NUMERIC(3,2),
       lonxitude DOUBLE PRECISION,
       ancho_plataforma DOUBLE PRECISION,
       ancho_calzada DOUBLE PRECISION,
       pavimento_tipo NUMERIC(1), -- ver dominios.pavimento_tipo
       pavimento_ano NUMERIC(4),
       pavimento_estado NUMERIC(1), -- ver dominios.estado
       bionda_esquerda_clase NUMERIC(1), -- ver dominios.bionda_tipo
       bionda_dereita_clase NUMERIC(1), -- ver dominios.bionda_tipo
       cuneta_esquerda_tipo NUMERIC(1), -- TODO: definir si interesa
       cuneta_dereita_tipo NUMERIC(1), -- TODO: definir si interesa
       pintura_borde BOOLEAN,
       pintura_eixo BOOLEAN,
       CONSTRAINT pk_tramos_intramunicipais PRIMARY KEY(gid),
       CONSTRAINT unique_id_tramo UNIQUE(id_carretera, id_intermunicipal, id_intramunicipal)
};

DROP TABLE IF EXISTS inventario.imds;
CREATE TABLE inventario.imds{
       id_carretera NUMERIC(4), -- TODO: chequear a que se relaciona
       id_intermunicipal NUMERIC(3), -- chequear a que se relaciona
       id_intramunicipal NUMERIC(3), -- chequear a que se relaciona
       imd NUMERIC(5),
       ano NUMERIC(4),
       pk NUMERIC(3,2),
       CONSTRAINT pk_imds PRIMARY KEY(id_carretera, id_intermunicipal, id_intramunicipal)
};

DROP TABLE IF EXISTS inventario.sinais;
CREATE TABLE inventario.sinais{
       id_carretera NUMERIC(4), -- TODO: chequear a que se relaciona
       id_intermunicipal NUMERIC(3), -- chequear a que se relaciona
       id_intramunicipal NUMERIC(3), -- chequear a que se relaciona
       pk NUMERIC(3,2),
       tipo_sinal NUMERIC(3),
       CONSTRAINT pk_sinais PRIMARY KEY (id_carretera, id_intermunicipal, id_intramunicipal)
};

DROP TABLE IF EXISTS inventario.sinais_tipo_ponton;
CREATE TABLE inventario.sinais_tipo_ponton{
       luz DOUBLE PRECISION,
       altura DOUBLE PRECISION,
       material TEXT -- TODO chequear: non hai valores
} INHERITS (sinais);

DROP TABLE IF EXISTS inventario.sinais_tipo_ponte;
CREATE TABLE inventario.sinais_tipo_ponte{
       luz DOUBLE PRECISION,
       altura DOUBLE PRECISION,
       lonxitude DOUBLE PRECISION
} INHERITS (sinais);

DROP TABLE IF EXISTS inventario.sinais_tipo_espello;
CREATE TABLE inventario.sinais_tipo_espello{
       marxe TEXT
} INHERITS (sinais);
