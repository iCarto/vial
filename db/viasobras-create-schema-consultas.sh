#!/bin/bash

config_file=$1
. $config_file

# Queries
# -------

psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/create_schema_consultas.sql

psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/preprocesar_consultas.sql

sql_query_path=`pwd`/funcions/consultas/c01-carreteras-general.sql #COPY command needs absolute path
sql_query="\COPY public.consultas_sql (sql_string) FROM '$sql_query_path' WITH DELIMITER ':'"
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname -c "$sql_query"

sql_query_path=`pwd`/funcions/consultas/c10-carreteras-anchofirme.sql #COPY command needs absolute path
sql_query="\COPY public.consultas_sql (sql_string) FROM '$sql_query_path' WITH DELIMITER ':'"
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname -c "$sql_query"

sql_query_path=`pwd`/funcions/consultas/c11-carreteras-anchofirme-menor-5.sql #COPY command needs absolute path
sql_query="\COPY public.consultas_sql (sql_string) FROM '$sql_query_path' WITH DELIMITER ':'"
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname -c "$sql_query"

sql_query_path=`pwd`/funcions/consultas/c12-carreteras-anchofirme-mayor-5-menor-7.sql #COPY command needs absolute path
sql_query="\COPY public.consultas_sql (sql_string) FROM '$sql_query_path' WITH DELIMITER ':'"
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname -c "$sql_query"

sql_query_path=`pwd`/funcions/consultas/c13-carreteras-anchofirme-mayor-7.sql #COPY command needs absolute path
sql_query="\COPY public.consultas_sql (sql_string) FROM '$sql_query_path' WITH DELIMITER ':'"
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname -c "$sql_query"

sql_query_path=`pwd`/funcions/consultas/c20-carreteras-tipofirme.sql #COPY command needs absolute path
sql_query="\COPY public.consultas_sql (sql_string) FROM '$sql_query_path' WITH DELIMITER ':'"
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname -c "$sql_query"

sql_query_path=`pwd`/funcions/consultas/c21-carreteras-tipofirme-mb.sql #COPY command needs absolute path
sql_query="\COPY public.consultas_sql (sql_string) FROM '$sql_query_path' WITH DELIMITER ':'"
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname -c "$sql_query"

sql_query_path=`pwd`/funcions/consultas/c22-carreteras-tipofirme-ts.sql #COPY command needs absolute path
sql_query="\COPY public.consultas_sql (sql_string) FROM '$sql_query_path' WITH DELIMITER ':'"
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname -c "$sql_query"

sql_query_path=`pwd`/funcions/consultas/c30-carreteras-aforos.sql #COPY command needs absolute path
sql_query="\COPY public.consultas_sql (sql_string) FROM '$sql_query_path' WITH DELIMITER ':'"
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname -c "$sql_query"

sql_query_path=`pwd`/funcions/consultas/c31-carreteras-aforos-mayor-250.sql #COPY command needs absolute path
sql_query="\COPY public.consultas_sql (sql_string) FROM '$sql_query_path' WITH DELIMITER ':'"
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname -c "$sql_query"

sql_query_path=`pwd`/funcions/consultas/c32-carreteras-aforos-mayor-500.sql #COPY command needs absolute path
sql_query="\COPY public.consultas_sql (sql_string) FROM '$sql_query_path' WITH DELIMITER ':'"
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname -c "$sql_query"

sql_query_path=`pwd`/funcions/consultas/c33-carreteras-aforos-mayor-1000.sql #COPY command needs absolute path
sql_query="\COPY public.consultas_sql (sql_string) FROM '$sql_query_path' WITH DELIMITER ':'"
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname -c "$sql_query"

sql_query_path=`pwd`/funcions/consultas/c40-carreteras-cotas.sql #COPY command needs absolute path
sql_query="\COPY public.consultas_sql (sql_string) FROM '$sql_query_path' WITH DELIMITER ':'"
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname -c "$sql_query"

psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/postprocesar_consultas.sql
