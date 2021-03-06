SELECT c.codigo AS "Código (LU-P)", \
       c.denominacion AS "Denominación", \
       COALESCE(c.categoria, '') AS "Categoría" \
FROM inventario.carreteras AS c \
[[WHERE]] \
ORDER BY c.categoria, c.codigo;
