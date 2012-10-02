#!/bin/bash

viasobras_data_orig=/home/amaneiro/03_proxectos_abertos/viasobras/datos

# # info_base
# shp2pgsql -d -g the_geom -W iso-8859-1 -s 25829 -I\
#     ${viasobras_data_orig}/info_base/Embalse_Clip_89 \
#     info_base.embalse > datos/info_base/embalse.sql
# shp2pgsql -d -g the_geom -W iso-8859-1 -s 25829 -I\
#     ${viasobras_data_orig}/info_base/municipios_lugo_text_89 \
#     info_base.municipios_lugo > datos/info_base/municipios_lugo.sql
# shp2pgsql -d -g the_geom -W iso-8859-1 -s 25829 -I\
#     ${viasobras_data_orig}/info_base/municipios_text_89 \
#     info_base.municipios > datos/info_base/municipios.sql
# shp2pgsql -d -g the_geom -W iso-8859-1 -s 25829 -I\
#     ${viasobras_data_orig}/info_base/Oceano_2_89 \
#     info_base.oceano > datos/info_base/oceano.sql
# shp2pgsql -d -g the_geom -W iso-8859-1 -s 25829 -I\
#     ${viasobras_data_orig}/info_base/Portugal_89 \
#     info_base.portugal > datos/info_base/portugal.sql
# shp2pgsql -d -g the_geom -W iso-8859-1 -s 25829 -I\
#     ${viasobras_data_orig}/info_base/Provincias_Galicia_89 \
#     info_base.provincias_galicia > datos/info_base/provincias_galicia.sql
# shp2pgsql -d -g the_geom -W iso-8859-1 -s 25829 -I\
#     ${viasobras_data_orig}/info_base/Provincias_limitrofes_89 \
#     info_base.provincias_limitrofes > datos/info_base/provincias_limitrofes.sql
# shp2pgsql -d -g the_geom -W iso-8859-1 -s 25829 -I\
#     ${viasobras_data_orig}/info_base/red_carreteras_89 \
#     info_base.red_carreteras > datos/info_base/red_carreteras.sql
# shp2pgsql -d -g the_geom -W iso-8859-1 -s 25829 -I\
#     ${viasobras_data_orig}/info_base/Rio_A_Clip_89 \
#     info_base.rio_a > datos/info_base/rio_a.sql
# shp2pgsql -d -g the_geom -W iso-8859-1 -s 25829 -I\
#     ${viasobras_data_orig}/info_base/Rio_L_Clip_89 \
#     info_base.rio_l > datos/info_base/rio_l.sql
# shp2pgsql -d -g the_geom -W iso-8859-1 -s 25829 -I\
#     ${viasobras_data_orig}/info_base/PKs_geometricos_89 \
#     info_base.pks_geometricos > datos/info_base/pks_geometricos.sql

# carreteras
shp2pgsql -d -g the_geom -W iso-8859-1 -s 25829 -I \
    ${viasobras_data_orig}/inventario/carreteras/Red_provincial_89 \
    inventario.carreteras_tmp > datos/inventario/carreteras_tmp.sql

# rampas & variantes
shp2pgsql -d -g the_geom -W iso-8859-1 -s 25829 -I \
    ${viasobras_data_orig}/inventario/carreteras/Variantes_rampas_89 \
    inventario.variantes_rampas_tmp > datos/inventario/variantes_rampas_tmp.sql

# caracteristicas
shp2pgsql -d -W iso-8859-1 \
    ${viasobras_data_orig}/inventario/inventario/ancho_plataforma.dbf \
    inventario.ancho_plataforma_tmp > datos/inventario/ancho_plataforma_tmp.sql
shp2pgsql -d -W iso-8859-1 \
    ${viasobras_data_orig}/inventario/inventario/tipo_pavimento.dbf \
    inventario.tipo_pavimento_tmp > datos/inventario/tipo_pavimento_tmp.sql
shp2pgsql -d -W iso-8859-1 \
    ${viasobras_data_orig}/inventario/inventario/cotas.dbf \
    inventario.cotas_tmp > datos/inventario/cotas_tmp.sql

# aforos
shp2pgsql -d -W iso-8859-1 \
    ${viasobras_data_orig}/inventario/aforos/aforos.dbf \
    inventario.aforos_tmp > datos/inventario/aforos_tmp.sql

# accidentes
shp2pgsql -d -W iso-8859-1 \
    ${viasobras_data_orig}/inventario/accidentes/accidentes.dbf \
    inventario.accidentes_tmp > datos/inventario/accidentes_tmp.sql
