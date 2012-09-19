SELECT numero AS "Código (LU-P)", \
       denominacion AS "Denominación", \
       longitud AS "Longitud", \
       categoria AS "Categoría" \
 FROM inventario.carreteras \
 ORDER BY numero, categoria;
