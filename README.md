# 📅 Proyecto Calendario

## Descripción
Este proyecto es una aplicación web de calendario que permite gestionar tareas y subtareas por día y hora, con funcionalidades de asignación de roles de usuario y control de accesos. Está desarrollado con **Spring Boot**, **Thymeleaf** y **JPA/Hibernate**.

Permite:
- Crear, modificar y eliminar tareas y subtareas.
- Asignar tareas a días y horarios específicos.
- Gestionar usuarios con roles (`USER`, `ADMIN`).
- Visualizar un calendario interactivo con las subtareas ubicadas en el día y hora correspondiente.

---

## Tecnologías
- **Backend:** Java 17, Spring Boot 3.x
- **Frontend:** Thymeleaf, HTML5, CSS3, Bootstrap
- **Base de datos:** H2 (desarrollo) / MySQL (producción)
- **Seguridad:** Spring Security, BCrypt
- **Control de versiones:** Git

---

## Funcionalidades principales

### Gestión de usuarios
- Registro de nuevos usuarios.
- Inicio de sesión y cierre de sesión.
- Roles y permisos (`USER`, `ADMIN`).

### Gestión de tareas y subtareas
- Crear tareas y subtareas asociadas a días específicos.
- Asignar horarios a las subtareas.
- Editar y eliminar tareas y subtareas.

### Visualización de calendario
- Vista semanal o mensual.
- Subtareas visibles en el día y hora correspondiente.
- Colores o indicadores según prioridad o estado de la tarea.

### Seguridad
- Encriptación de contraseñas con BCrypt.
- Control de acceso a rutas según rol.

---

## Estructura del proyecto

src/main/java/dgctic/core/system
├── config # Configuraciones de la aplicación
├── controller # Controladores web
├── model # Entidades JPA
├── repository # Repositorios Spring Data JPA
├── service # Lógica de negocio
└── utils # Utilidades

src/main/resources
├── templates # Plantillas Thymeleaf
└── static # CSS, JS, imágenes

src/main/java/dgctic/core/security
├── config # Configuraciones de seguridad
├── model # UserDetails
├── service # Implementación de seguridad


---

## Requisitos
- Java 17+
- Maven 3.8+
- Base de datos (H2 para desarrollo, MySQL/PostgreSQL para producción)

---

## Uso
1. Regístrate como usuario.
2. Accede al calendario para crear tus tareas y subtareas.
3. Administra tareas con fechas y horas específicas.

---

## Licencia
MIT License © AliV16
