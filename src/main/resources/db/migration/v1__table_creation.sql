DROP TABLE IF EXISTS hora CASCADE;
DROP TABLE IF EXISTS subtarea_eliminada CASCADE;
DROP TABLE IF EXISTS dia_subtarea CASCADE;
DROP TABLE IF EXISTS periodo_tarea CASCADE;
DROP TABLE IF EXISTS subtareas CASCADE;
DROP TABLE IF EXISTS dias CASCADE;
DROP TABLE IF EXISTS semanas CASCADE;
DROP TABLE IF EXISTS tareas CASCADE;
DROP TABLE IF EXISTS clase_tarea CASCADE;
DROP TABLE IF EXISTS usuarios_rol CASCADE;
DROP TABLE IF EXISTS rol CASCADE;
DROP TABLE IF EXISTS usuarios CASCADE;
DROP TABLE IF EXISTS descripciones CASCADE;


-- Usuarios
CREATE TABLE usuarios (
                          id_usuario INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                          password_hash VARCHAR(255) NOT NULL,
                          nombre VARCHAR(50) NOT NULL,
                          apellido_paterno VARCHAR(100) NOT NULL,
                          apellido_materno VARCHAR(100) NOT NULL,
                          correo VARCHAR(100) NOT NULL UNIQUE
);

-- Rol
CREATE TABLE rol (
                     id_rol INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                     nombre_rol VARCHAR(50) NOT NULL
);

-- Usarios Y Rol
CREATE TABLE usuarios_rol (
                              id_usuarios_rol INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                              id_usuario  INTEGER NOT NULL,
                              id_rol INTEGER NOT NULL,
                              FOREIGN KEY (id_usuario) REFERENCES usuarios (id_usuario) ON DELETE CASCADE,
                              FOREIGN KEY (id_rol) REFERENCES rol(id_rol) ON DELETE CASCADE,
                              CONSTRAINT uq_usuario_rol UNIQUE(id_usuario, id_rol)
);


-- Clases de tarea
CREATE TABLE clase_tarea (
                             id_clase_tarea INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                             id_usuario INTEGER NOT NULL,
                             color VARCHAR(50) NOT NULL ,
                             nombre VARCHAR(50) NOT NULL ,
                             FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
                             CONSTRAINT uq_usuario_color UNIQUE(id_usuario, color),
                             CONSTRAINT uq_usuario_nombre UNIQUE(id_usuario, nombre)
);

-- Tareas
CREATE TABLE tareas (
                        id_tarea INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                        id_clase_tarea INTEGER NOT NULL,
                        nombre VARCHAR(50) NOT NULL,
                        FOREIGN KEY (id_clase_tarea) REFERENCES clase_tarea(id_clase_tarea) ON DELETE CASCADE
);

-- Descripciones
CREATE TABLE descripciones (
                               id_descripcion INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                               descripcion TEXT
);

-- Semanas
CREATE TABLE semanas (
                         id_semana INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                         numero_semana INTEGER NOT NULL,
                         anio INTEGER NOT NULL,
                         fecha_inicio DATE NOT NULL,
                         fecha_fin DATE NOT NULL
);

-- Días
CREATE TABLE dias (
                      id_dia INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                      id_semana INTEGER NOT NULL,
                      fecha DATE NOT NULL,
                      nombre_dia VARCHAR(50) NOT NULL,
                      FOREIGN KEY (id_semana) REFERENCES semanas(id_semana) ON DELETE CASCADE
);

-- Subtareas
CREATE TABLE subtareas (
                           id_subtarea INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                           id_tarea INTEGER NOT NULL,
                           id_descripcion INTEGER,
                           nombre VARCHAR(100) NOT NULL,
                           estado BOOLEAN DEFAULT FALSE NOT NULL,
                           fecha_creacion DATE DEFAULT CURRENT_DATE,
                           eliminada BOOLEAN DEFAULT FALSE NOT NULL,
                           FOREIGN KEY (id_tarea) REFERENCES tareas(id_tarea) ON DELETE CASCADE,
                           FOREIGN KEY (id_descripcion) REFERENCES descripciones(id_descripcion)ON DELETE SET NULL
);

-- Cambie los ENUM por VARCHAR para mayor flexibilidad en BD y lo manejo desde la Java con JPA
-- Periodo de las tareas
CREATE TABLE periodo_tarea (
                               id_periodo_tarea INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                               id_tarea INTEGER UNIQUE NOT NULL,
                               estado VARCHAR(50) NOT NULL
                                   CHECK (estado IN ('PENDIENTE','EN_PROGRESO','COMPLETADO')),
                               fecha_creacion DATE NOT NULL,
                               fecha_fin DATE NOT NULL,
                               FOREIGN KEY (id_tarea) REFERENCES tareas(id_tarea) ON DELETE CASCADE
);

-- Relación subtarea - día
CREATE TABLE dia_subtarea (
                              id_dia_subtarea INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                              id_subtarea INTEGER NOT NULL,
                              estado VARCHAR(50) NOT NULL
                                  CHECK (estado IN ('PENDIENTE','EN_PROGRESO','COMPLETADO')),
                              id_dia INTEGER NOT NULL,
                              FOREIGN KEY (id_subtarea) REFERENCES subtareas(id_subtarea) ON DELETE CASCADE,
                              FOREIGN KEY (id_dia) REFERENCES dias(id_dia) ON DELETE CASCADE
);

CREATE TABLE subtarea_eliminada (
                                    id_subtarea_eliminada INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                    id_subtarea INTEGER NOT NULL UNIQUE,
                                    mensaje TEXT NOT NULL,
                                    fecha_eliminacion DATE DEFAULT CURRENT_DATE,
                                    FOREIGN KEY (id_subtarea) REFERENCES subtareas(id_subtarea) ON DELETE CASCADE
);

--La hora del dia a la que se realiza la subtarea
CREATE TABLE hora (
                      id_hora INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                      id_dia_subtarea INTEGER NOT NULL,
                      id_dia INTEGER NOT NULL,
                      hora INTEGER NOT NULL CHECK (hora BETWEEN 0 AND 23),
                      FOREIGN KEY (id_dia_subtarea) REFERENCES dia_subtarea(id_dia_subtarea) ON DELETE CASCADE,
                      FOREIGN KEY (id_dia) REFERENCES dias(id_dia) ON DELETE CASCADE,
                      CONSTRAINT uq_dia_hora UNIQUE(id_dia, hora)
);


-- Insertar usuarios de ejemplo
INSERT INTO usuarios (password_hash, nombre, apellido_paterno, apellido_materno, correo)
VALUES
    ('$2a$14$TYFsAko3eE9ruASJnWQqUelMxYYGyWdZfuiG56MepK7Vt0jvSKo3u', 'Lucia', 'Garcia', 'Lopez', 'lucia@email.com'),
    ('$2a$14$zspJeCMX7toJ8dOTjY15euC1hj7BhhBgp/htiVlbOKWzUNvdm7/hq', 'Pedro', 'Martinez', 'Soto', 'pedro@email.com'),
    ('$2a$14$f8Qbij70ZNjnBSH0WPpYJOY1qAELvNzVFvhOlOCijvZhhHtywoqMi', 'Ali', 'Villalobos', ' ', 'ali@gmail.com');

INSERT INTO rol (nombre_rol)
VALUES
    ('ADMIN'),
    ('USER');

INSERT INTO usuarios_rol (id_usuario, id_rol)
VALUES
    (1, 2),
    (2, 2),
    (3, 1);

-- Insertar clases de tareas
INSERT INTO clase_tarea (id_usuario,color, nombre)
VALUES
    (1,'#BF0A2E', 'Estudio'),
    (1,'#4BAE24', 'Salud'),
    (2,'#0A19BF', 'Hobbies');

-- Insertar tareas
INSERT INTO tareas (id_clase_tarea,  nombre)
VALUES
    (1, 'Preparar examen matematicas'),
    (2, 'Entrenamiento semanal');

-- Descripciones
INSERT INTO descripciones (descripcion)
VALUES
    ('Resolver ejercicios del capitulo 5'),
    ('Correr 5km tres veces por semana');

-- Subtareas
INSERT INTO subtareas (id_tarea, id_descripcion, nombre, fecha_creacion)
VALUES
    (1, 1, 'Practicar integrales', '2025-08-12'),
    (2, 2, 'Sesion de running', '2025-08-13');

-- Insertar en periodo_tarea
INSERT INTO periodo_tarea (id_tarea, estado, fecha_creacion, fecha_fin)
SELECT id_tarea, 'PENDIENTE', '2025-08-01', '2025-12-31'
FROM tareas;