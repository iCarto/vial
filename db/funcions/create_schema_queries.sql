DROP SCHEMA IF EXISTS queries CASCADE;
CREATE SCHEMA queries;

DROP TABLE IF EXISTS queries.queries;
CREATE TABLE queries.queries(
       codigo character varying(5) NOT NULL,
       descripcion character varying(250),
       consulta character varying(1500),
       haswhere character varying(2),
       titulo character varying(250),
       subtitulo character varying(250),
       CONSTRAINT pk_consultas PRIMARY KEY (codigo)
);

INSERT INTO queries.queries (codigo, descripcion, consulta, haswhere, titulo)
       VALUES ('C1',
       'Nro de tramos, por tipo de pavimento',
       'SELECT tipopavime AS "Tipo de pavimento", COUNT(tipopavime) AS "Nro de tramos" FROM inventario.tipo_pavimento [[WHERE]] GROUP BY tipopavime ORDER BY tipopavime',
       'NO',
       'Nro de tramos, por tipo de pavimento');
INSERT INTO queries.queries (codigo, descripcion, consulta, haswhere, titulo)
       VALUES ('C2',
       'Nro de tramos, por ancho de plataforma',
       'SELECT ancho_plataforma AS "Ancho de plataforma", COUNT(ancho_plataforma) AS "Nro de tramos" FROM inventario.ancho_plataforma [[WHERE]] GROUP BY ancho_plataforma ORDER BY ancho_plataforma',
       'NO',
       'Nro de tramos, por ancho de plataforma');
