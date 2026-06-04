# Proyecto 2 - Progra 4 — Bolsa de Empleo

## Estructura

```
Proyecto2-Progra4/
├── bolsaempleo-be/   ← Backend Spring Boot (puerto 8080)
└── bolsaempleo-fe/   ← Frontend React + Vite (puerto 5173)
```

---

## Requisitos previos

- Java 21
- Maven
- Node.js 18+
- MySQL corriendo con la base de datos `bolsaempleo`

---

## 1. Base de datos

Importar el SQL en MySQL:

```sql
-- En MySQL Workbench o consola:
SOURCE bolsaempleo-be/src/main/bolsa_empleo.sql;
```

Verificar que `application.properties` tiene tus credenciales:
```
bolsaempleo-be/src/main/resources/application.properties
```
Por defecto usa `root / root` en `localhost:3306/bolsaempleo`.

---

## 2. Correr el Backend

```bash
cd bolsaempleo-be
mvn spring-boot:run
```

Queda corriendo en: http://localhost:8080

---

## 3. Correr el Frontend

En otra terminal:

```bash
cd bolsaempleo-fe
npm install
npm run dev
```

Queda corriendo en: http://localhost:5173

---

## Autenticación JWT

- Endpoint login: `POST /api/auth/login`
- Registro empresa: `POST /api/auth/registro/empresa`
- Registro oferente: `POST /api/auth/registro/oferente`
- Perfil autenticado: `GET /api/usuarios/me`

El frontend guarda el token JWT en `localStorage` y lo envía en el header de autenticación de cada request.

---

## Producción (servidor único)

El backend sirve el frontend compilado desde `src/main/resources/static/`.

Scripts:

```bash
./build.sh   # build frontend + empaquetar backend
./start.sh   # levantar backend en 8080 (sirve SPA y API)
./dev.sh     # levantar backend y frontend en modo desarrollo
```

---

## Páginas públicas (sin login)

| URL | Descripción |
|-----|-------------|
| http://localhost:5173/ | Inicio — últimos 5 puestos |
| http://localhost:5173/puestos/buscar | Buscar por características |
| http://localhost:5173/puestos/detalle/:id | Detalle de un puesto |
| http://localhost:5173/registro/empresa | Registro de empresa |
| http://localhost:5173/registro/oferente | Registro de oferente |
| http://localhost:5173/login | Login |
