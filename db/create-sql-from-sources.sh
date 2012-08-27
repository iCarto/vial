#!/bin/bash

viasobras_data_orig=/home/amaneiro/03_proxectos_abertos/viasobras/datos

# info_base
shp2pgsql -d -g the_geom -W iso-8859-1 -s 23029 -I\
    ${viasobras_data_orig}/info_base/contorno/Oceano \
    info_base.oceano > datos/info_base/oceano.sql
shp2pgsql -d -g the_geom -W iso-8859-1 -s 23029 -I\
    ${viasobras_data_orig}/info_base/contorno/Portugal \
    info_base.portugal > datos/info_base/portugal.sql
shp2pgsql -d -g the_geom -W iso-8859-1 -s 23029 -I\
    ${viasobras_data_orig}/info_base/contorno/Provincias_Galicia \
    info_base.provincias_galicia > datos/info_base/provincias_galicia.sql
shp2pgsql -d -g the_geom -W iso-8859-1 -s 23029 -I\
    ${viasobras_data_orig}/info_base/contorno/Provincias_limitrofes \
    info_base.provincias_limitrofes > datos/info_base/provincias_limitrofes.sql
shp2pgsql -d -g the_geom -W iso-8859-1 -s 23029 -I\
    ${viasobras_data_orig}/info_base/municipios/municipios_lugo_text \
    info_base.concellos > datos/info_base/concellos.sql
shp2pgsql -d -g the_geom -W iso-8859-1 -s 23029 -I\
    ${viasobras_data_orig}/info_base/nucleos/Nuc_INE_99_09 \
    info_base.nucleos > datos/info_base/nucleos.sql

# inventario
shp2pgsql -d -W iso-8859-1 \
    ${viasobras_data_orig}/inventario/accidentes/accidentes_2006_2012.dbf \
    inventario.accidentes > datos/inventario/accidentes.sql
shp2pgsql -d -W iso-8859-1 \
    ${viasobras_data_orig}/inventario/aforos/aforos.dbf \
    inventario.aforos > datos/inventario/aforos.sql
shp2pgsql -d -g the_geom -W iso-8859-1 -s 23029 -I \
    ${viasobras_data_orig}/inventario/red_carreteras/Red_provincial \
    inventario.red_carreteras_tmp > datos/inventario/red_carreteras_tmp.sql
shp2pgsql -d -W iso-8859-1 \
    ${viasobras_data_orig}/inventario/inventario/ancho_plataforma.dbf \
    inventario.ancho_plataforma_tmp > datos/inventario/ancho_plataforma_tmp.sql
shp2pgsql -d -W iso-8859-1 \
    ${viasobras_data_orig}/inventario/inventario/tipo_pavimento.dbf \
    inventario.tipo_pavimento_tmp > datos/inventario/tipo_pavimento_tmp.sql
