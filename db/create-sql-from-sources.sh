#!/bin/bash

# ToDo: see how to filter previously the shapes with ogr2ogr

path_data=/home/amaneiro/03_proxectos_abertos/viasobras/piloto

# info_base
shp2pgsql -d -g the_geom -W iso-8859-1 -s 23029 -I ${path_data}/Limites_POL/Concellos info_base.concellos > datos/info_base/concellos.sql

# inventario
shp2pgsql -d -g the_geom -W iso-8859-1 -s 23029 -I ${path_data}/Vias inventario.carreteras > datos/inventario/carreteras.sql
shp2pgsql -d -W iso-8859-1 ${path_data}/accidentes.dbf inventario.accidentes > datos/inventario/accidentes.sql
shp2pgsql -d -W iso-8859-1 ${path_data}/aforos.dbf inventario.aforos > datos/inventario/aforos.sql
shp2pgsql -d -W iso-8859-1 ${path_data}/inventario.dbf inventario.inventario > datos/inventario/inventario.sql
