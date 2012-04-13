#!/bin/bash

dropdb -h localhost -p 5432 -U postgres vias_obras;
createdb -h localhost -p 5432 -U postgres -O postgres -T template_postgis vias_obras;

# Info base
psql -h localhost -p 5432 -U postgres vias_obras < create-schema-infobase.sql
psql -h localhost -p 5432 -U postgres vias_obras < datos/info_base/concellos.sql

# Inventario
psql -h localhost -p 5432 -U postgres vias_obras < create-schema-inventario.sql
psql -h localhost -p 5432 -U postgres vias_obras < datos/inventario/carreteras.sql
psql -h localhost -p 5432 -U postgres vias_obras < datos/inventario/accidentes.sql
psql -h localhost -p 5432 -U postgres vias_obras < datos/inventario/aforos.sql
psql -h localhost -p 5432 -U postgres vias_obras < datos/inventario/inventario.sql

# Linear referencing: calibrate road, event points & dynamic segmentation
#psql -h localhost -p 5432 -U postgres vias_obras < calibrar_carreteras.sql
psql -h localhost -p 5432 -U postgres vias_obras < create_event_points.sql
psql -h localhost -p 5432 -U postgres vias_obras < create_segments_dynamically.sql
