#!/bin/bash

# ToDo: see how to filter previously the shapes with ogr2ogr

path_data=/home/amaneiro/03_proxectos_abertos/viasobras/piloto/datos

# info_base
shp2pgsql -d -g the_geom -W iso-8859-1 -s 23029 -I\
    ${path_data}/info_base/concellos/Concellos info_base.concellos > datos/info_base/concellos.sql
shp2pgsql -d -g the_geom -W iso-8859-1 -s 23029 -I \
    ${path_data}/inventario/pKs info_base.pks  > datos/info_base/pks.sql
shp2pgsql -d -g the_geom -W iso-8859-1 -s 23029 -I\
    ${path_data}/info_base/nucleos/Nuc_INE_99_09 info_base.nucleos > datos/info_base/nucleos.sql
shp2pgsql -d -g the_geom -W iso-8859-1 -s 23029 -I \
    ${path_data}/info_base/rede_carreteras/Vias_Provinciales_datos info_base.rede_carreteras > datos/info_base/rede_carreteras.sql

# inventario
shp2pgsql -d -g the_geom -W iso-8859-1 -s 23029 -I\
    ${path_data}/inventario/Vias inventario.carreteras > datos/inventario/carreteras.sql
shp2pgsql -d -W iso-8859-1 \
    ${path_data}/inventario/accidentes.dbf inventario.accidentes > datos/inventario/accidentes.sql
shp2pgsql -d -W iso-8859-1 \
    ${path_data}/inventario/aforos.dbf inventario.aforos > datos/inventario/aforos.sql
shp2pgsql -d -W iso-8859-1 \
    ${path_data}/inventario/inventario.dbf inventario.inventario > datos/inventario/inventario.sql
