#!/bin/bash

dropdb -h localhost -p 5432 -U postgres vias_obras;
createdb -h localhost -p 5432 -U postgres -O postgres -T template_postgis vias_obras;

# Info base
psql -h localhost -p 5432 -U postgres vias_obras < funcions/create_schema_infobase.sql
psql -h localhost -p 5432 -U postgres vias_obras < datos/info_base/concellos.sql

# Inventario
psql -h localhost -p 5432 -U postgres vias_obras < funcions/create_schema_inventario.sql
psql -h localhost -p 5432 -U postgres vias_obras < datos/inventario/carreteras.sql
psql -h localhost -p 5432 -U postgres vias_obras < datos/inventario/accidentes.sql
psql -h localhost -p 5432 -U postgres vias_obras < datos/inventario/aforos.sql
psql -h localhost -p 5432 -U postgres vias_obras < datos/inventario/inventario.sql

# Post-procesado to create final tables
psql -h localhost -p 5432 -U postgres vias_obras < funcions/procesar_aforos.sql
psql -h localhost -p 5432 -U postgres vias_obras < funcions/procesar_inventario.sql

# Linear referencing: calibrate road, event points & dynamic segmentation
psql -h localhost -p 5432 -U postgres vias_obras < funcions/calibrate_carreteras.sql
psql -h localhost -p 5432 -U postgres vias_obras < funcions/create_accidentes_event_points.sql
psql -h localhost -p 5432 -U postgres vias_obras < funcions/create_aforos_event_points.sql
psql -h localhost -p 5432 -U postgres vias_obras < funcions/create_dynamic_segments_from_inventario.sql

