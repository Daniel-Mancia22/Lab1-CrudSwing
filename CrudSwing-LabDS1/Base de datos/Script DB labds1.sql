create database if not exists labds1;

use labds1;

-- usuario: user-labds1
-- base datos: labds123
-- clave: labds123

create table Personas(
    id int primary key auto_increment,
    nombre varchar(200) not null,
    correo varchar(100) not null unique,
    telefono varchar(15) not null
);

-- Inserciones para poder trabajar durante el desarrollo del CRUD
insert into Personas (nombre, correo, telefono) values 
('Juan Perez', 'juan.perez@gmail.com', '+503 1234 7890'),
('Ana Gómez', 'ana.gomez@yahoo.com', '+503 0965 4321'),
('Luis Torres', 'luis.torres@hotmail.com', '+503 5436 7890'),
('María López', 'maria.lopez@outlook.com', '+503 8901 2345'),
('Carlos Martínez', 'carlos.martinez@gmail.com', '+503 4567 8901'),
('Sofía Ramírez', 'sofia.ramirez@gmail.com', '+503 7890 1256');

select *from Personas;
