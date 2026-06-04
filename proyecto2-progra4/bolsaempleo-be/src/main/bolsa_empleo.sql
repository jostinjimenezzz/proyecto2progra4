DROP DATABASE IF EXISTS BolsaEmpleo;
CREATE DATABASE BolsaEmpleo CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE BolsaEmpleo;

/* =========================
   TABLA USUARIO
   - username:
       EMPRESA/OFERENTE -> correo
       ADMIN            -> identificación
   - rol: ADMIN | EMPRESA | OFERENTE
   ========================= */
create table Usuario (
  id bigint auto_increment,
  username varchar(120) unique not null,
  clave varchar(100) not null,        /* BCrypt */
  rol varchar(10) not null,
  habilitado tinyint(1) not null default 1,
  fecha_creacion datetime not null default current_timestamp,
  primary key(id)
);

/* =========================
   TABLA ADMIN (perfil)
   ========================= */
create table Admin (
  id bigint auto_increment,
  usuario_id bigint unique not null,
  identificacion varchar(30) unique not null,
  nombre varchar(80),
  primary key(id),
  foreign key (usuario_id) references Usuario(id)
);

/* =========================
   TABLA EMPRESA (perfil)
   - NO tiene correo (está en Usuario.username)
   - requiere aprobación
   ========================= */
create table Empresa (
  id bigint auto_increment,
  usuario_id bigint unique not null,

  nombre varchar(120) not null,
  localizacion varchar(160) not null,
  telefono varchar(30) not null,
  descripcion text,

  estado_aprobacion varchar(12) not null default 'PENDIENTE', /* PENDIENTE/APROBADO/RECHAZADO */
  fecha_registro datetime not null default current_timestamp,

  /* auditoría (recomendado) */
  fecha_aprobacion datetime null,
  aprobado_por_admin_id bigint null,

  primary key(id),
  foreign key (usuario_id) references Usuario(id),
  foreign key (aprobado_por_admin_id) references Admin(id)
);

/* =========================
   TABLA OFERENTE (perfil)
   - NO tiene correo (está en Usuario.username)
   - requiere aprobación
   - CV: ruta
   ========================= */
create table Oferente (
  id bigint auto_increment,
  usuario_id bigint unique not null,

  identificacion varchar(30) unique not null,
  nombre varchar(80) not null,
  primer_apellido varchar(80) not null,
  nacionalidad varchar(60) not null,
  telefono varchar(30) not null,
  lugar_residencia varchar(160) not null,

  estado_aprobacion varchar(12) not null default 'PENDIENTE', /* PENDIENTE/APROBADO/RECHAZADO */
  fecha_registro datetime not null default current_timestamp,

  cv_path varchar(255),
  cv_nombre_original varchar(255),
  cv_mime varchar(80),
  cv_fecha_subida datetime,

  /* auditoría (recomendado) */
  fecha_aprobacion datetime null,
  aprobado_por_admin_id bigint null,

  primary key(id),
  foreign key (usuario_id) references Usuario(id),
  foreign key (aprobado_por_admin_id) references Admin(id)
);

/* =========================
   TABLA CARACTERISTICA (jerárquica)
   ========================= */
create table Caracteristica (
  id bigint auto_increment,
  nombre varchar(120) not null,
  id_padre bigint null,
  primary key(id),
  foreign key (id_padre) references Caracteristica(id)
);

/* =========================
   TABLA PUESTO
   - PUBLICO: visible para cualquiera
   - PRIVADO: solo para oferentes registrados (en tu lógica)
   - activo: permite desactivar
   ========================= */
create table Puesto (
  id bigint auto_increment,
  empresa_id bigint not null,

  descripcion_general text not null,
  salario_ofrecido decimal(12,2),

  tipo_publicacion varchar(10) not null default 'PUBLICO', /* PUBLICO/PRIVADO */
  activo tinyint(1) not null default 1,

  fecha_registro datetime not null default current_timestamp,
  fecha_desactivacion datetime null,

  primary key(id),
  foreign key (empresa_id) references Empresa(id)
);

/* =========================
   TABLA PUESTO_CARACTERISTICA (requisitos)
   - nivel_deseado: 1..5
   ========================= */
create table PuestoCaracteristica (
  puesto_id bigint not null,
  caracteristica_id bigint not null,
  nivel_deseado int not null,
  primary key(puesto_id, caracteristica_id),
  foreign key (puesto_id) references Puesto(id),
  foreign key (caracteristica_id) references Caracteristica(id)
);

/* =========================
   TABLA OFERENTE_HABILIDAD
   - nivel: 1..5
   ========================= */
create table OferenteHabilidad (
  oferente_id bigint not null,
  caracteristica_id bigint not null,
  nivel int not null,
  primary key(oferente_id, caracteristica_id),
  foreign key (oferente_id) references Oferente(id),
  foreign key (caracteristica_id) references Caracteristica(id)
);

/* =========================================================
   DEMO DATA (3 de cada cosa)
   Usa el mismo hash para todos (clave: 111)
   ========================================================= */

SET @HASH_111 := '$2a$12$esH9VOmi2lwFmh60ZH.Zo.kn3QTJ9DlbCfo7SuTABopgSai.EiO8O';

/* =========================
   3 ADMINS
   ========================= */
insert into Usuario (username, clave, rol) values ('administrador@correo.com', @HASH_111, 'ADMIN');
set @u_admin1 := last_insert_id();
insert into Admin (usuario_id, identificacion, nombre) values (@u_admin1, 'ADMIN01', 'Administrador 1');
set @admin1 := last_insert_id();

insert into Usuario (username, clave, rol) values ('admin2@correo.com', @HASH_111, 'ADMIN');
set @u_admin2 := last_insert_id();
insert into Admin (usuario_id, identificacion, nombre) values (@u_admin2, 'ADMIN02', 'Administrador 2');
set @admin2 := last_insert_id();

insert into Usuario (username, clave, rol) values ('admin3@correo.com', @HASH_111, 'ADMIN');
set @u_admin3 := last_insert_id();
insert into Admin (usuario_id, identificacion, nombre) values (@u_admin3, 'ADMIN03', 'Administrador 3');
set @admin3 := last_insert_id();

/* =========================
   CARACTERÍSTICAS (más completas)
   ========================= */
insert into Caracteristica (nombre, id_padre) values ('Lenguajes de programación', null);
set @leng := last_insert_id();

insert into Caracteristica (nombre, id_padre) values ('Tecnologías Web', null);
set @web := last_insert_id();

insert into Caracteristica (nombre, id_padre) values ('Bases de datos', null);
set @db := last_insert_id();

/* hijos lenguajes */
insert into Caracteristica (nombre, id_padre) values ('Java', @leng);
set @c_java := last_insert_id();
insert into Caracteristica (nombre, id_padre) values ('C#', @leng);
set @c_csharp := last_insert_id();
insert into Caracteristica (nombre, id_padre) values ('Python', @leng);
set @c_python := last_insert_id();

/* hijos web */
insert into Caracteristica (nombre, id_padre) values ('HTML', @web);
set @c_html := last_insert_id();
insert into Caracteristica (nombre, id_padre) values ('CSS', @web);
set @c_css := last_insert_id();
insert into Caracteristica (nombre, id_padre) values ('JavaScript', @web);
set @c_js := last_insert_id();

/* hijos db */
insert into Caracteristica (nombre, id_padre) values ('MySQL', @db);
set @c_mysql := last_insert_id();
insert into Caracteristica (nombre, id_padre) values ('PostgreSQL', @db);
set @c_pg := last_insert_id();

/* =========================
   3 EMPRESAS (2 aprobadas + 1 pendiente)
   ========================= */
insert into Usuario (username, clave, rol) values ('empresa1@correo.com', @HASH_111, 'EMPRESA');
set @u_emp1 := last_insert_id();
insert into Empresa (usuario_id, nombre, localizacion, telefono, descripcion, estado_aprobacion, fecha_aprobacion, aprobado_por_admin_id)
values (@u_emp1, 'Empresa Uno S.A.', 'San José', '2222-1001', 'Empresa de ejemplo 1', 'APROBADO', now(), @admin1);
set @emp1 := last_insert_id();

insert into Usuario (username, clave, rol) values ('empresa2@correo.com', @HASH_111, 'EMPRESA');
set @u_emp2 := last_insert_id();
insert into Empresa (usuario_id, nombre, localizacion, telefono, descripcion, estado_aprobacion, fecha_aprobacion, aprobado_por_admin_id)
values (@u_emp2, 'Empresa Dos Ltda.', 'Heredia', '2222-1002', 'Empresa de ejemplo 2', 'APROBADO', now(), @admin1);
set @emp2 := last_insert_id();

insert into Usuario (username, clave, rol) values ('empresa3@correo.com', @HASH_111, 'EMPRESA');
set @u_emp3 := last_insert_id();
insert into Empresa (usuario_id, nombre, localizacion, telefono, descripcion, estado_aprobacion)
values (@u_emp3, 'Empresa Tres Inc.', 'Alajuela', '2222-1003', 'Empresa pendiente', 'PENDIENTE');
set @emp3 := last_insert_id();

/* =========================
   3 OFERENTES (2 aprobados + 1 pendiente)
   ========================= */
insert into Usuario (username, clave, rol) values ('oferente1@correo.com', @HASH_111, 'OFERENTE');
set @u_of1 := last_insert_id();
insert into Oferente (usuario_id, identificacion, nombre, primer_apellido, nacionalidad, telefono, lugar_residencia,
                      estado_aprobacion, fecha_aprobacion, aprobado_por_admin_id)
values (@u_of1, '1-1111-1111', 'Ana', 'Pérez', 'CR', '8888-1001', 'Heredia', 'APROBADO', now(), @admin1);
set @of1 := last_insert_id();

insert into Usuario (username, clave, rol) values ('oferente2@correo.com', @HASH_111, 'OFERENTE');
set @u_of2 := last_insert_id();
insert into Oferente (usuario_id, identificacion, nombre, primer_apellido, nacionalidad, telefono, lugar_residencia,
                      estado_aprobacion, fecha_aprobacion, aprobado_por_admin_id)
values (@u_of2, '2-2222-2222', 'Luis', 'Gómez', 'CR', '8888-1002', 'San José', 'APROBADO', now(), @admin1);
set @of2 := last_insert_id();

insert into Usuario (username, clave, rol) values ('oferente3@correo.com', @HASH_111, 'OFERENTE');
set @u_of3 := last_insert_id();
insert into Oferente (usuario_id, identificacion, nombre, primer_apellido, nacionalidad, telefono, lugar_residencia,
                      estado_aprobacion)
values (@u_of3, '3-3333-3333', 'María', 'López', 'CR', '8888-1003', 'Cartago', 'PENDIENTE');
set @of3 := last_insert_id();

/* =========================
   3 PUESTOS (para probar home top5 + buscar + privados)
   ========================= */
insert into Puesto (empresa_id, descripcion_general, salario_ofrecido, tipo_publicacion, activo)
values (@emp1, 'Desarrollador Java Jr', 1200.00, 'PUBLICO', 1);
set @p1 := last_insert_id();

insert into Puesto (empresa_id, descripcion_general, salario_ofrecido, tipo_publicacion, activo)
values (@emp1, 'QA Manual', 900.00, 'PUBLICO', 1);
set @p2 := last_insert_id();

insert into Puesto (empresa_id, descripcion_general, salario_ofrecido, tipo_publicacion, activo)
values (@emp2, 'Fullstack (privado)', 1800.00, 'PRIVADO', 1);
set @p3 := last_insert_id();

/* requisitos puestos */
insert into PuestoCaracteristica (puesto_id, caracteristica_id, nivel_deseado) values (@p1, @c_java, 3);
insert into PuestoCaracteristica (puesto_id, caracteristica_id, nivel_deseado) values (@p1, @c_mysql, 2);

insert into PuestoCaracteristica (puesto_id, caracteristica_id, nivel_deseado) values (@p2, @c_html, 2);

insert into PuestoCaracteristica (puesto_id, caracteristica_id, nivel_deseado) values (@p3, @c_java, 4);
insert into PuestoCaracteristica (puesto_id, caracteristica_id, nivel_deseado) values (@p3, @c_css, 3);
insert into PuestoCaracteristica (puesto_id, caracteristica_id, nivel_deseado) values (@p3, @c_mysql, 3);

/* =========================
   HABILIDADES OFERENTES
   ========================= */
insert into OferenteHabilidad (oferente_id, caracteristica_id, nivel) values (@of1, @c_java, 4);
insert into OferenteHabilidad (oferente_id, caracteristica_id, nivel) values (@of1, @c_mysql, 3);

insert into OferenteHabilidad (oferente_id, caracteristica_id, nivel) values (@of2, @c_java, 3);
insert into OferenteHabilidad (oferente_id, caracteristica_id, nivel) values (@of2, @c_css, 4);

/* of3 pendiente igual puede tener habilidades (si querés) */
insert into OferenteHabilidad (oferente_id, caracteristica_id, nivel) values (@of3, @c_html, 3);