INSERT INTO Rol (nombre_rol) VALUES ('Administrador');
INSERT INTO Rol (nombre_rol) VALUES ('Miembro');
INSERT INTO Rol (nombre_rol) VALUES ('Visitante');

INSERT INTO Usuarios (cedula, nombre_completo, id_rol, direccion, password, telefono, email)
VALUES (1001, 'Admin General', 1, 'Oficina Central', '123', '3201234567', 'admin@coworking.com');