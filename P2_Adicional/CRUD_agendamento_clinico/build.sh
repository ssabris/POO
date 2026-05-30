#!/bin/bash
# =============================================================
#  build.sh  –  Compila e executa o Sistema Clínico
#  Requer: Java 21+ e Maven 3.6+ instalados
# =============================================================
set -e

echo "==== Compilando com Maven ===="
mvn package -q

echo ""
echo "==== Executando ===="
java -jar target/clinica.jar
