#!/bin/bash

# Require having a template with PostGIS support (ie: template_postgis)

config_file=$1
. $config_file

# Setting permisions to read only user
SCHEMAS=(consultas elle info_base inventario public)
TABLES=(consultas.aforos consultas.consultas consultas.consultas_actuaciones elle._map elle._map_overview elle._map_overview_style elle._map_style info_base.embalse info_base.municipios info_base.municipios_lugo info_base.oceano info_base.portugal info_base.provincias_galicia info_base.provincias_limitrofes info_base.red_carreteras info_base.rio_a info_base.rio_l inventario.accidentes inventario.actuacion_municipio inventario.actuaciones inventario.aforos inventario.ancho_plataforma inventario.carretera_municipio inventario.carreteras inventario.carreteras_lugo inventario.cotas inventario.municipio_codigo inventario.pks_1000 inventario.rampas inventario.tipo_pavimento inventario.variantes public.geometry_columns public.spatial_ref_sys)

for i in "${SCHEMAS[@]}"; do
    psql -h $viasobras_server -U $viasobras_superuser -p $viasobras_port -c "grant usage on schema $i to $viasobras_user_lectura" $viasobras_dbname;
done

for i in "${TABLES[@]}"; do
    psql -h $viasobras_server -U $viasobras_superuser -p $viasobras_port -c "grant select on $i to $viasobras_user_lectura" $viasobras_dbname;
done

psql -h $viasobras_server -p $viasobras_port -U $viasobras_superuser \
    $viasobras_dbname -c "VACUUM ANALYZE;"
