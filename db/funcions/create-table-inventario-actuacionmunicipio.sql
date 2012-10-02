
DROP TABLE IF EXISTS inventario.actuacion_municipio;
CREATE TABLE inventario.actuacion_municipio(
       id SERIAL,
       codigo_actuacion text,
       codigo_municipio text,
       CONSTRAINT pk_actuacion_municipio PRIMARY KEY (codigo_municipio, codigo_actuacion),
       FOREIGN KEY(codigo_actuacion) REFERENCES inventario.actuaciones (codigo_actuacion)
               ON DELETE CASCADE
               ON UPDATE CASCADE
);
