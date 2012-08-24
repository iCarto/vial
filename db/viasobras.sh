#!/bin/bash

usage() {
    echo
    echo "`basename $0` -c config_file -s schema_to_deploy"
    echo
    echo "-c: config file"
    echo "-s: schema to deploy (elle, inventario, queries). 'all' will recreate the whole database"
    exit -1
}

while getopts ":c:s:" opt; do
  case $opt in
    c)
      #If config file exists, load it; else, exit.
      if [ -e $OPTARG ]
      then
          echo "LOG: config file="$OPTARG
          config_file=$OPTARG
      else
          echo "ERROR: file "$OPTARG" not exist"
          usage
      fi
      ;;
    s)
      #Use all (recreate the whole database) if schema is not set
      if [ -z $OPTARG ]
      then
          schema=all
      else
          schema=$OPTARG
      fi
          echo "LOG: schema="$schema
      ;;
    \?)
      echo "ERROR: Option" $OPTARG "not available"
      usage
      ;;
    :)
      echo "ERROR: Option" $OPTARG "requires an argument"
      usage
      ;;
  esac
done

#Check schema & config_file are set
if [ -z $schema ]
then
    echo "ERROR: schema not set"
    usage
elif [ -z $config_file ]
then
    echo "ERROR: config file not set"
    usage
fi

if [ $schema == "all" ]
then

    echo "LOG: drop & create database"
    ./viasobras-create-db.sh $config_file
    ./viasobras-create-schema-elle.sh $config_file
    ./viasobras-create-schema-infobase.sh $config_file
    ./viasobras-create-schema-queries.sh $config_file
    ./viasobras-create-schema-inventario.sh $config_file

elif [ $schema == "elle" ]
then
    echo "LOG: drop & create schema ELLE"
    ./viasobras-create-schema-elle.sh $config_file

elif [ $schema == "infobase" ]
then
    echo "LOG: drop & create schema INFOBASE"
    ./viasobras-create-schema-infobase.sh $config_file

elif [ $schema == "inventario" ]
then
    echo "LOG: drop & create schema INVENTARIO"
    ./viasobras-create-schema-inventario.sh $config_file

elif [ $schema == "queries" ]
then
    echo "LOG: drop & create schema QUERIES"
    ./viasobras-create-schema-queries.sh $config_file

fi
