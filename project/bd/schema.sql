CREATE DATABASE coworking_db;
USE coworking_db;

CREATE TABLE Rol (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre_rol VARCHAR(50) NOT NULL
);

CREATE TABLE Usuarios (
    cedula INT PRIMARY KEY,
    nombre_completo VARCHAR(100) NOT NULL,
    id_rol INT NOT NULL,
    direccion VARCHAR(150),
    password VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    email VARCHAR(100) UNIQUE,
    FOREIGN KEY (id_rol) REFERENCES Rol(id_rol)
);

CREATE TABLE Recursos (
    id_recurso INT AUTO_INCREMENT PRIMARY KEY,
    tipo_recurso ENUM('NO_ESPECIFICADO', 'OFICINA_PRIVADA', 'ESCRITORIO_FIJO', 'SALA_REUNIONES', 'AUDITORIO', 'ZONA_COMUN', 'RECURSO_TECNOLOGICO') DEFAULT 'NO_ESPECIFICADO',
    nombre_recurso VARCHAR(100) NOT NULL,
    valor_hora DECIMAL(10,2),
    fecha_inicio_tarifa DATE,
    fecha_fin_tarifa DATE,
    estado ENUM('activo', 'inactivo') DEFAULT 'activo',
    descripcion TEXT
);

CREATE TABLE Reservas (
    id_reserva INT AUTO_INCREMENT PRIMARY KEY,
    fecha_inicio DATE NOT NULL,
    hora_inicio TIME NOT NULL,
    fecha_fin DATE NOT NULL,
    hora_fin TIME NOT NULL,
    id_recurso INT NOT NULL,
    estado ENUM('pendiente', 'confirmada', 'cancelada') DEFAULT 'pendiente',
    usuario_reserva INT NOT NULL,
    FOREIGN KEY (id_recurso) REFERENCES Recursos(id_recurso),
    FOREIGN KEY (usuario_reserva) REFERENCES Usuarios(cedula)
);

CREATE TABLE Pagos (
    id_pago INT AUTO_INCREMENT PRIMARY KEY,
    id_reserva INT NOT NULL,
    monto DECIMAL(10,2) NOT NULL,
    fecha_pago DATE NOT NULL,
    metodo_pago VARCHAR(50),
    FOREIGN KEY (id_reserva) REFERENCES Reservas(id_reserva)
);

CREATE TABLE Notificaciones (
    id_notificacion INT AUTO_INCREMENT PRIMARY KEY,
    cedula_usuario INT NOT NULL,
    mensaje VARCHAR(500) NOT NULL,
    fecha_envio DATETIME NOT NULL,
    tipo_notificacion ENUM('CONFIRMACION', 'RECORDATORIO', 'ALERTA_VENCIMIENTO', 'GENERAL') NOT NULL DEFAULT 'GENERAL',
    estado ENUM('PENDIENTE', 'ENVIADO', 'LEIDO') DEFAULT 'PENDIENTE',
    FOREIGN KEY (cedula_usuario) REFERENCES Usuarios(cedula)
);


CREATE TABLE Reportes (
    id_reporte INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    descripcion VARCHAR(500),
    fecha_generacion DATE NOT NULL,
    tipo_reporte ENUM('OCUPACION', 'INGRESOS', 'USO_POR_USUARIO', 'RESERVAS', 'GENERAL' ) NOT NULL,
    total_registros INT,
    monto_total DECIMAL(10,2)
);

