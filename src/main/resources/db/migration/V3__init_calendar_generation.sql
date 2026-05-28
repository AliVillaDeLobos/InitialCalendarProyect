DROP FUNCTION IF EXISTS public.generar_calendario_semanal_viejo;

CREATE OR REPLACE FUNCTION public.generar_calendario_semanal_viejo(
    anio_inicio INT,
    anio_fin INT
)
RETURNS VOID
LANGUAGE plpgsql
AS $$

DECLARE
fecha_actual DATE;
    semana_numero INT;
    anio_actual INT;
    fecha_inicio_semana DATE;
    fecha_fin_semana DATE;
    dia_loop INT;
    id_semana_insertada INT;
    fecha_dia DATE;

BEGIN

    fecha_actual := make_date(anio_inicio, 1, 1);

    WHILE EXTRACT(YEAR FROM fecha_actual) <= anio_fin LOOP

        -- mover a lunes
        fecha_actual := fecha_actual + ((8 - EXTRACT(DOW FROM fecha_actual))::INT % 7);

        fecha_inicio_semana := fecha_actual;
        fecha_fin_semana := fecha_actual + INTERVAL '6 days';
        semana_numero := EXTRACT(WEEK FROM fecha_actual);
        anio_actual := EXTRACT(YEAR FROM fecha_actual);

        -- INSERT SEMANA (requiere UNIQUE en (numero_semana, anio))
INSERT INTO semanas (numero_semana, anio, fecha_inicio, fecha_fin)
VALUES (semana_numero, anio_actual, fecha_inicio_semana, fecha_fin_semana)
    ON CONFLICT (numero_semana, anio) DO NOTHING;

-- OBTENER ID CORRECTO
SELECT id_semana INTO id_semana_insertada
FROM semanas
WHERE numero_semana = semana_numero
  AND anio = anio_actual
    LIMIT 1;

-- GENERAR DÍAS
dia_loop := 0;

        WHILE dia_loop < 7 LOOP

            fecha_dia := fecha_inicio_semana + dia_loop;

INSERT INTO dias (id_semana, fecha, nombre_dia)
VALUES (
           id_semana_insertada,
           fecha_dia,
           CASE EXTRACT(DOW FROM fecha_dia)
               WHEN 1 THEN 'LUNES'
               WHEN 2 THEN 'MARTES'
               WHEN 3 THEN 'MIERCOLES'
               WHEN 4 THEN 'JUEVES'
               WHEN 5 THEN 'VIERNES'
               WHEN 6 THEN 'SABADO'
               WHEN 0 THEN 'DOMINGO'
               END
       )
    ON CONFLICT (fecha) DO NOTHING;

dia_loop := dia_loop + 1;

END LOOP;

        fecha_actual := fecha_actual + INTERVAL '7 days';

END LOOP;

END;
$$;

-- EJECUCIÓN
SELECT public.generar_calendario_semanal_viejo(2026, 2040);