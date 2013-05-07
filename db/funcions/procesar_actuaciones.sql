BEGIN;

-- definition
DROP TABLE IF EXISTS inventario.actuaciones;
CREATE TABLE inventario.actuaciones(
       gid SERIAL,
       codigo_actuacion text UNIQUE,
       codigo_carretera text,
       pk_inicial float,
       pk_final float,
       longitud integer,
       tipo text,
       accidente_tipo text,
       accidente_descripcion text,
       accidente_fecha date,
       accidente_observaciones text,
       autorizacion_tipo text,
       autorizacion_peticionario text,
       autorizacion_beneficiario text,
       autorizacion_fecha_solicitud date,
       autorizacion_fecha_autorizacion date,
       autorizacion_importe_tasas float,
       autorizacion_importe_aval float,
       autorizacion_observaciones text,
       conservacion_tipo text,
       conservacion_zona_ano text,
       conservacion_titulo text,
       conservacion_descripcion text,
       conservacion_importe_proyecto float,
       conservacion_fecha_inicio date,
       conservacion_fecha_fin date,
       conservacion_fecha_consignacion_solicitud date,
       conservacion_fecha_consignacion_aprobacion date,
       conservacion_fecha_aprobacion_junta date,
       conservacion_fecha_adjudicacion date,
       conservacion_fecha_replanteo date,
       conservacion_fecha_recepcion date,
       conservacion_fecha_informe_devolucion_garantia date,
       conservacion_contratista_nombre text,
       conservacion_contratista_cif text,
       conservacion_observaciones text,
       conservacion_propuesta_tipo text,
       conservacion_propuesta_zona_ano text,
       conservacion_propuesta_titulo text,
       conservacion_propuesta_descripcion text,
       conservacion_propuesta_importe_proyecto float,
       conservacion_propuesta_fecha_consignacion_solicitud date,
       conservacion_propuesta_fecha_consignacion_aprobacion date,
       conservacion_propuesta_fecha_aprobacion_junta date,
       conservacion_propuesta_fecha_adjudicacion date,
       conservacion_propuesta_fecha_replanteo date,
       conservacion_propuesta_fecha_recepcion date,
       conservacion_propuesta_fecha_informe_devolucion_garantia date,
       conservacion_propuesta_contratista_nombre text,
       conservacion_propuesta_contratista_cif text,
       conservacion_propuesta_observaciones text,
       construccion_tipo text,
       construccion_titulo text,
       construccion_descripcion text,
       construccion_contratista_nombre text,
       construccion_contratista_cif text,
       construccion_importe_proyecto float,
       construccion_importe_adjudicacion float,
       construccion_fecha_consignacion_solicitud date,
       construccion_fecha_consignacion_aprobacion date,
       construccion_fecha_aprobacion_junta date,
       construccion_fecha_adjudicacion date,
       construccion_fecha_replanteo date,
       construccion_fecha_recepcion date,
       construccion_fecha_informe_devolucion_garantia date,
       construccion_observaciones text,
       denuncia_tipo text,
       denuncia_denunciante text,
       denuncia_fecha_denuncia date,
       denuncia_fecha_resolucion date,
       denuncia_importe_sancion float,
       denuncia_observaciones text,
       expropiacion_titulo text,
       expropiacion_fecha_justiprecio date,
       expropiacion_observaciones text,
       incidencia_tipo text,
       incidencia_contratista_nombre text,
       incidencia_contratista_cif text,
       incidencia_contacto text,
       incidencia_comunicante text,
       incidencia_lugar text,
       incidencia_actuaciones text,
       incidencia_deteccion_motivo text,
       incidencia_deteccion_fecha date,
       incidencia_deteccion_hora text,
       incidencia_resolucion_motivo text,
       incidencia_resolucion_codigo text,
       incidencia_resolucion_fecha date,
       incidencia_aviso_1 text,
       incidencia_aviso_2 text,
       incidencia_aviso_3 text,
       incidencia_observaciones text,
       inventario_tipo text,
       inventario_margen text,
       inventario_ano text,
       inventario_utm_x float,
       inventario_utm_y float,
       inventario_utm_z float,
       inventario_observaciones text,
       otros_fecha date,
       otros_observaciones text,
       transferencia_tipo text,
       transferencia_organismo text,
       transferencia_fecha_inicio date,
       transferencia_fecha_fin date,
       transferencia_observaciones text,
       travesia_nucleo_poblacion text,
       travesia_aceras text,
       travesia_arcenes text,
       travesia_tipo_suelo text,
       travesia_ancho_calzada float,
       travesia_ancho_acera_izquierda float,
       travesia_ancho_acera_derecha float,
       travesia_ancho_acera_izquierda_pki float,
       travesia_ancho_acera_izquierda_pkf float,
       travesia_ancho_acera_derecha_pki float,
       travesia_ancho_acera_derecha_pkf float,
       travesia_ancho_arcen_izquierda float,
       travesia_ancho_arcen_derecha float,
       travesia_ancho_arcen_izquierda_pki float,
       travesia_ancho_arcen_derecha_pki float,
       travesia_ancho_arcen_izquierda_pkf float,
       travesia_ancho_arcen_derecha_pkf float,
       travesia_pavimento_izquierda text,
       travesia_pavimento_derecha text,
       travesia_observaciones text,
       vialidad_codigo text,
       vialidad_peticionario text,
       vialidad_beneficiario text,
       vialidad_nro_registro text,
       vialidad_cif text,
       vialidad_fecha_peticion date,
       vialidad_fecha_inicio date,
       vialidad_fecha_fin date,
       vialidad_observaciones text,
       CONSTRAINT pk_actuaciones PRIMARY KEY(gid),
       FOREIGN KEY (codigo_carretera) REFERENCES inventario.carreteras (numero)
               ON DELETE CASCADE
               ON UPDATE CASCADE
);

-- linear referencing
SELECT AddGeometryColumn('inventario', 'actuaciones', 'the_geom', '25829', 'MULTILINESTRINGM', 3);
ALTER TABLE inventario.actuaciones DROP CONSTRAINT enforce_geotype_the_geom;
ALTER TABLE inventario.actuaciones DROP CONSTRAINT enforce_dims_the_geom;
SELECT inventario.update_geom_line_all('inventario', 'actuaciones');

-- indexes
CREATE INDEX actuaciones_the_geom
       ON inventario.actuaciones USING GIST(the_geom);
CREATE INDEX actuaciones_codigo_carretera
       ON inventario.actuaciones USING BTREE(codigo_carretera);

-- triggers
DROP TRIGGER IF EXISTS update_geom_actuaciones ON inventario.actuaciones;
CREATE TRIGGER update_geom_actuaciones
       BEFORE UPDATE OR INSERT
       ON inventario.actuaciones FOR EACH ROW
       EXECUTE PROCEDURE inventario.update_geom_line_on_pk_change();

DROP TRIGGER IF EXISTS update_longitud_actuacion ON inventario.actuaciones;
CREATE TRIGGER update_longitud_actuacion
       BEFORE UPDATE OR INSERT
       ON inventario.actuaciones FOR EACH ROW
       EXECUTE PROCEDURE inventario.update_longitud();

COMMIT;
