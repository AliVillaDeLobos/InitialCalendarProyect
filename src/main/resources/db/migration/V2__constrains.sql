ALTER TABLE semanas
    ADD CONSTRAINT uq_semanas_numero_anio UNIQUE (numero_semana, anio);

ALTER TABLE dias
    ADD CONSTRAINT uq_dias_fecha UNIQUE (fecha);

ALTER TABLE dia_subtarea
    ADD CONSTRAINT uq_subtarea_dia UNIQUE (id_subtarea, id_dia);