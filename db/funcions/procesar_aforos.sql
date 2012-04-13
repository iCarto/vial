DROP TABLE IF EXISTS inventario.imds;
CREATE TABLE inventario.imds (LIKE inventario.aforos);
ALTER TABLE inventario.imds DROP COLUMN imd0;
ALTER TABLE inventario.imds DROP COLUMN fecha2;
ALTER TABLE inventario.imds DROP COLUMN a_oimd0;
ALTER TABLE inventario.imds DROP COLUMN imd0pk;
ALTER TABLE inventario.imds DROP COLUMN imd1;
ALTER TABLE inventario.imds DROP COLUMN fecha3;
ALTER TABLE inventario.imds DROP COLUMN a_oimd1;
ALTER TABLE inventario.imds DROP COLUMN imd1pk;
ALTER TABLE inventario.imds DROP COLUMN imd2;
ALTER TABLE inventario.imds DROP COLUMN fecha4;
ALTER TABLE inventario.imds DROP COLUMN a_oimd2;
ALTER TABLE inventario.imds DROP COLUMN imd2pk;
ALTER TABLE inventario.imds DROP COLUMN imd3;
ALTER TABLE inventario.imds DROP COLUMN fecha5;
ALTER TABLE inventario.imds DROP COLUMN a_oimd3;
ALTER TABLE inventario.imds DROP COLUMN imd3pk;

INSERT INTO inventario.imds (
       SELECT gid,
              municipio, numeroinve, tramo, nombredela, longitudmu, longitudto,
              imd, fecha, a_o, pk,
              observacio
       FROM inventario.aforos
);

INSERT INTO inventario.imds (
       SELECT nextval('inventario.aforos_gid_seq'),
              municipio, numeroinve, tramo, nombredela, longitudmu, longitudto,
              imd0 AS imd, fecha2 AS fecha, a_oimd0 AS a_o, imd0pk AS pk,
              observacio
       FROM inventario.aforos
);

INSERT INTO inventario.imds (
       SELECT nextval('inventario.aforos_gid_seq'),
              municipio, numeroinve, tramo, nombredela, longitudmu, longitudto,
              imd1 AS imd, fecha3 AS fecha, a_oimd1 AS a_o, imd1pk AS pk,
              observacio
       FROM inventario.aforos
);

INSERT INTO inventario.imds (
       SELECT nextval('inventario.aforos_gid_seq'),
              municipio, numeroinve, tramo, nombredela, longitudmu, longitudto,
              imd2 AS imd, fecha4 AS fecha, a_oimd2 AS a_o, imd2pk AS pk,
              observacio
       FROM inventario.aforos
);

INSERT INTO inventario.imds (
       SELECT nextval('inventario.aforos_gid_seq'),
              municipio, numeroinve, tramo, nombredela, longitudmu, longitudto,
              imd3 AS imd, fecha5 AS fecha, a_oimd3 AS a_o, imd3pk AS pk,
              observacio
       FROM inventario.aforos
);

DELETE FROM inventario.imds
       WHERE imd = '0' AND a_o = '0' AND pk = '0';

DROP TABLE IF EXISTS inventario.aforos;
ALTER TABLE inventario.imds RENAME TO aforos;
