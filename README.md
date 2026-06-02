# 📅 Task Calendar System

## Live Demo

**Application:** LINK

> This project is actively under development. Some features are currently being improved and may not behave as expected.

---

## Overview

Task Calendar System is a web application designed to manage tasks and subtasks through an interactive calendar interface.

The project was originally developed as part of a software development diploma program and is currently being enhanced with new features, cloud deployment, and usability improvements.

Users can:

* Create, update, and delete tasks and subtasks.
* Schedule activities by date and time.
* Manage personal task planning through a calendar view.
* Access features based on role permissions (`USER`, `ADMIN`).

---

## Current Development Status

## Demo Account

USER
email: lucia@email.com
password: pwd123

ADMIN
email: ali@email.com
password: clave123

### Recently Implemented

* Migration from local MariaDB environment to PostgreSQL.
* Cloud database deployment using Neon.
* Application deployment using Render.
* Security improvements with Spring Security and BCrypt.
* Role-based access control.

### Features Under Improvement

* Weekly calendar navigation (previous/next week).
* Calendar side panels synchronization.
* Task detail visualization inside the weekly calendar.
* UI/UX refinements and responsiveness.

### Known Limitations

* Weekly calendar navigation is not fully functional yet.
* Some task details may not update correctly when selecting events from the calendar.
* Certain administrative features are intentionally restricted to ADMIN users as part of the project's role-based authorization design.

---

## Tech Stack

### Backend

* Java 17
* Spring Boot 3
* Spring Data JPA
* Hibernate

### Frontend

* Thymeleaf
* HTML5
* CSS3
* Bootstrap

### Database

* PostgreSQL (Production)
* H2 / MariaDB (Development)

### Security

* Spring Security
* BCrypt Password Encoding

### Deployment

* Render
* Neon PostgreSQL

---

## Architecture

The application follows the MVC pattern:

* Controllers handle HTTP requests.
* Services contain business logic.
* Repositories manage data persistence.
* Thymeleaf renders server-side views.
* Spring Security manages authentication and authorization.

---

## Main Features

### User Management

* User registration.
* Login and logout.
* Role-based permissions.
* Profile management.

### Task Management

* Create tasks and subtasks.
* Assign dates and schedules.
* Edit and delete entries.
* Organize activities through calendar views.

### Calendar

* Weekly planning interface.
* Time-slot based organization.
* Visual task distribution by day and hour.

### Security

* Encrypted passwords with BCrypt.
* Route protection based on roles.
* Restricted administrative functionality.

---

## Project Structure

```text
src/main/java/dgctic/core/system
├── config
├── controller
├── model
├── repository
├── service
└── utils

src/main/java/dgctic/core/security
├── config
├── model
└── service

src/main/resources
├── templates
└── static
```

## Running Locally

### Requirements

* Java 17+
* Maven 3.8+
* PostgreSQL (or H2 for development)

### Installation

```bash
git clone 
cd project-folder
mvn spring-boot:run
```

---

## Roadmap

* [ ] Complete weekly calendar navigation.
* [ ] Improve calendar event interaction.
* [ ] Add drag-and-drop scheduling.
* [ ] Improve responsive design.


---

## Author

Developed and maintained as a continuous learning and portfolio project focused on:

* Spring Boot
* Security and Roles
* Cloud Deployment
* PostgreSQL
* Full-Stack Development
