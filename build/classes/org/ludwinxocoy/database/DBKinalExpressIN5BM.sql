-- Crear y usar la base de datos
DROP DATABASE IF EXISTS DBKinalExpressIN5BM;
CREATE DATABASE DBKinalExpressIN5BM;
USE DBKinalExpressIN5BM;
SET GLOBAL time_zone = '-6:00';

-- Creación de tablas
CREATE TABLE Clientes(
    clienteID INT AUTO_INCREMENT,
    nombreClientes VARCHAR(45),
    apellidosClientes VARCHAR(45),
    direccionClientes VARCHAR(128),
    NIT VARCHAR(13),
    telefonoClientes VARCHAR(13),
    correoClientes VARCHAR(128),
    PRIMARY KEY (clienteID)
);

CREATE TABLE Proveedores(
    codigoProveedor INT AUTO_INCREMENT,
    NitProveedor VARCHAR(10),
    nombresProveedor VARCHAR(60),
    apellidosProveedor VARCHAR(60),
    direccionProveedor VARCHAR(150),
    razonSocial VARCHAR(60),
    contactoPrincipal VARCHAR(100),
    paginaWeb VARCHAR(50),
    telefonoProveedor VARCHAR(13),
    emailProveedor VARCHAR(128),
    PRIMARY KEY (codigoProveedor)
);

CREATE TABLE Compras(
    numeroDocumento INT AUTO_INCREMENT,
    fechaDocumento DATE,
    descripcion VARCHAR(60),
    totalDocumento DECIMAL(10,2),
    PRIMARY KEY (numeroDocumento)
);

CREATE TABLE TipoProducto(
    codigoTipoProducto INT AUTO_INCREMENT,
    descripcion VARCHAR(45),
    PRIMARY KEY (codigoTipoProducto)
);

CREATE TABLE CargoDeEmpleado(
    codigoCargoEmpleado INT AUTO_INCREMENT,
    nombreCargo VARCHAR(140),
    descripcionCargo VARCHAR(145),
    PRIMARY KEY (codigoCargoEmpleado)
);

CREATE TABLE Productos(
    codigoProducto VARCHAR(15),
    descripcionProducto VARCHAR(45),
    precioUnitario DECIMAL(10,2),
    precioDocena DECIMAL(10,2),
    precioMayor DECIMAL(10,2),
    existencia INT,
    codigoTipoProducto INT,
    codigoProveedor INT,
    PRIMARY KEY (codigoProducto),
    FOREIGN KEY (codigoTipoProducto) REFERENCES TipoProducto(codigoTipoProducto),
    FOREIGN KEY (codigoProveedor) REFERENCES Proveedores(codigoProveedor)
);
CREATE TABLE Empleados(
    codigoEmpleado INT AUTO_INCREMENT,
    nombresEmpleado VARCHAR(50),
    apellidosEmpleado VARCHAR(50),
    sueldo DECIMAL(10,2),
    direccion VARCHAR(150),
    turno VARCHAR(15),
    CargoEmpleado_codigoCargoEmpleado INT,
    PRIMARY KEY (codigoEmpleado),
    FOREIGN KEY (CargoEmpleado_codigoCargoEmpleado) REFERENCES CargoDeEmpleado(codigoCargoEmpleado)
);

-- Procedimientos almacenados para Clientes
DELIMITER $$

CREATE PROCEDURE sp_ListarClientes()
BEGIN
    SELECT
        clienteID,
        nombreClientes,
        apellidosClientes,
        direccionClientes,
        NIT,
        telefonoClientes,
        correoClientes
    FROM Clientes;
END $$

CREATE PROCEDURE sp_AgregarClientes (
    IN _nombreClientes VARCHAR(45), 
    IN _apellidosClientes VARCHAR(45), 
    IN _direccionClientes VARCHAR(128), 
    IN _NIT VARCHAR(13), 
    IN _telefonoClientes VARCHAR(13), 
    IN _correoClientes VARCHAR(128)
)
BEGIN
    INSERT INTO Clientes (
        nombreClientes, 
        apellidosClientes, 
        direccionClientes, 
        NIT, 
        telefonoClientes, 
        correoClientes
    ) VALUES (
        _nombreClientes, 
        _apellidosClientes, 
        _direccionClientes, 
        _NIT, 
        _telefonoClientes, 
        _correoClientes
    );
END $$

CREATE PROCEDURE sp_buscarClientes(IN _clienteID INT)
BEGIN
    SELECT
        clienteID,
        nombreClientes,
        apellidosClientes,
        direccionClientes,
        NIT,
        telefonoClientes,
        correoClientes
    FROM Clientes
    WHERE clienteID = _clienteID;
END $$

CREATE PROCEDURE sp_eliminarClientes(IN _clienteID INT)
BEGIN
    DELETE FROM Clientes WHERE clienteID = _clienteID;
END $$

CREATE PROCEDURE sp_actualizarClientes(
    IN _clienteID INT, 
    IN _nombreClientes VARCHAR(45), 
    IN _apellidosClientes VARCHAR(45), 
    IN _direccionClientes VARCHAR(128), 
    IN _NIT VARCHAR(13), 
    IN _telefonoClientes VARCHAR(13), 
    IN _correoClientes VARCHAR(128)
)
BEGIN
    UPDATE Clientes
    SET 
        nombreClientes = _nombreClientes,
        apellidosClientes = _apellidosClientes,
        direccionClientes = _direccionClientes,
        NIT = _NIT,
        telefonoClientes = _telefonoClientes,
        correoClientes = _correoClientes
    WHERE
        clienteID = _clienteID;
END $$

-- Procedimientos almacenados para Proveedores
CREATE PROCEDURE sp_AgregarProveedor (
    IN _NitProveedor VARCHAR(10),
    IN _nombresProveedor VARCHAR(60),
    IN _apellidosProveedor VARCHAR(60),
    IN _direccionProveedor VARCHAR(150),
    IN _razonSocial VARCHAR(60),
    IN _contactoPrincipal VARCHAR(100),
    IN _paginaWeb VARCHAR(50),
    IN _telefonoProveedor VARCHAR(13),
    IN _emailProveedor VARCHAR(128)
)
BEGIN
    INSERT INTO Proveedores(
        NitProveedor, 
        nombresProveedor, 
        apellidosProveedor, 
        direccionProveedor, 
        razonSocial, 
        contactoPrincipal, 
        paginaWeb,
        telefonoProveedor,
        emailProveedor
    )
    VALUES (
        _NitProveedor, 
        _nombresProveedor, 
        _apellidosProveedor, 
        _direccionProveedor, 
        _razonSocial, 
        _contactoPrincipal, 
        _paginaWeb,
        _telefonoProveedor,
        _emailProveedor
    );
END $$

CREATE PROCEDURE sp_ListarProveedor()
BEGIN
    SELECT
        codigoProveedor,
        NitProveedor,
        nombresProveedor,
        apellidosProveedor,
        direccionProveedor,
        razonSocial,
        contactoPrincipal,
        paginaWeb,
        telefonoProveedor,
        emailProveedor
    FROM Proveedores;
END $$

CREATE PROCEDURE sp_editarProveedor (
    IN _codigoProveedor INT,
    IN _NitProveedor VARCHAR(10),
    IN _nombresProveedor VARCHAR(60),
    IN _apellidosProveedor VARCHAR(60),
    IN _direccionProveedor VARCHAR(150),
    IN _razonSocial VARCHAR(60),
    IN _contactoPrincipal VARCHAR(100),
    IN _paginaWeb VARCHAR(50),
    IN _telefonoProveedor VARCHAR(13),
    IN _emailProveedor VARCHAR(128)
)
BEGIN
    UPDATE Proveedores 
    SET
        NitProveedor = _NitProveedor,
        nombresProveedor = _nombresProveedor,
        apellidosProveedor = _apellidosProveedor,
        direccionProveedor = _direccionProveedor,
        razonSocial = _razonSocial,
        contactoPrincipal = _contactoPrincipal,
        paginaWeb = _paginaWeb,
        telefonoProveedor = _telefonoProveedor,
        emailProveedor = _emailProveedor
    WHERE
        codigoProveedor = _codigoProveedor;
END $$

CREATE PROCEDURE sp_eliminarProveedor(IN _codigoProveedor INT)
BEGIN
    DELETE FROM Proveedores WHERE codigoProveedor = _codigoProveedor;
END $$

CREATE PROCEDURE sp_buscarProveedor (
    IN _codigoProveedor INT
)
BEGIN
    SELECT
        codigoProveedor,
        NitProveedor,
        nombresProveedor,
        apellidosProveedor,
        direccionProveedor,
        razonSocial,
        contactoPrincipal,
        paginaWeb,
        telefonoProveedor,
        emailProveedor
    FROM Proveedores
    WHERE codigoProveedor = _codigoProveedor;
END $$

-- Procedimientos almacenados para Compras
CREATE PROCEDURE sp_AgregarCompra (
    IN _fechaDocumento DATE,
    IN _descripcion VARCHAR(60),
    IN _totalDocumento DECIMAL(10,2)
)
BEGIN
    INSERT INTO Compras (
        fechaDocumento, 
        descripcion, 
        totalDocumento
    )
    VALUES (
        _fechaDocumento, 
        _descripcion, 
        _totalDocumento
    );
END $$

CREATE PROCEDURE sp_ListarCompras()
BEGIN
    SELECT
        numeroDocumento,
        fechaDocumento,
        descripcion,
        totalDocumento
    FROM Compras;
END $$

CREATE PROCEDURE sp_EditarCompra (
    IN _numeroDocumento INT,
    IN _fechaDocumento DATE,
    IN _descripcion VARCHAR(60),
    IN _totalDocumento DECIMAL(10,2)
)
BEGIN
    UPDATE Compras
    SET
        fechaDocumento = _fechaDocumento,
        descripcion = _descripcion,
        totalDocumento = _totalDocumento
    WHERE
        numeroDocumento = _numeroDocumento;
END $$

CREATE PROCEDURE sp_EliminarCompra(IN _numeroDocumento INT)
BEGIN
    DELETE FROM Compras WHERE numeroDocumento = _numeroDocumento;
END $$

-- Procedimientos almacenados para TipoProducto
CREATE PROCEDURE sp_AgregarTipoProducto (
    IN _descripcion VARCHAR(45)
)
BEGIN
    INSERT INTO TipoProducto (
        descripcion
    )
    VALUES (
        _descripcion
    );
END $$

CREATE PROCEDURE sp_ListarTipoProducto()
BEGIN
    SELECT
        codigoTipoProducto,
        descripcion
    FROM TipoProducto;
END $$

CREATE PROCEDURE sp_EditarTipoProducto (
    IN _codigoTipoProducto INT,
    IN _descripcion VARCHAR(45)
)
BEGIN
    UPDATE TipoProducto
    SET
        descripcion = _descripcion
    WHERE
        codigoTipoProducto = _codigoTipoProducto;
END $$

CREATE PROCEDURE sp_EliminarTipoProducto(IN _codigoTipoProducto INT)
BEGIN
    DELETE FROM TipoProducto WHERE codigoTipoProducto = _codigoTipoProducto;
END $$

CREATE PROCEDURE sp_buscarTipoProducto (
    IN _codigoTipoProducto INT
)
BEGIN
    SELECT
        codigoTipoProducto,
        descripcion
    FROM TipoProducto
    WHERE codigoTipoProducto = _codigoTipoProducto;
END $$

-- Procedimientos almacenados para CargoDeEmpleado
CREATE PROCEDURE sp_AgregarCargoEmpleado (
    IN _nombreCargo VARCHAR(50),
    IN _descripcionCargo VARCHAR(45)
)
BEGIN
    INSERT INTO CargoDeEmpleado(
        nombreCargo,
        descripcionCargo
    )
    VALUES (
        _nombreCargo,
        _descripcionCargo
    );
END $$

CREATE PROCEDURE sp_ListarCargoEmpleado()
BEGIN
    SELECT
        codigoCargoEmpleado,
        nombreCargo,
        descripcionCargo
    FROM CargoDeEmpleado;
END $$

CREATE PROCEDURE sp_EditarCargoEmpleado (
    IN _codigoCargoEmpleado INT,
    IN _nombreCargo VARCHAR(50),
    IN _descripcionCargo VARCHAR(45)
)
BEGIN
    UPDATE CargoDeEmpleado
    SET
        nombreCargo = _nombreCargo,
        descripcionCargo = _descripcionCargo
    WHERE
        codigoCargoEmpleado = _codigoCargoEmpleado;
END $$

CREATE PROCEDURE sp_EliminarCargoEmpleado(IN _codigoCargoEmpleado INT)
BEGIN
    DELETE FROM CargoDeEmpleado WHERE codigoCargoEmpleado = _codigoCargoEmpleado;
END $$

CREATE PROCEDURE sp_buscarCargoEmpleado (
    IN _codigoCargoEmpleado INT
)
BEGIN
    SELECT
        codigoCargoEmpleado,
        nombreCargo,
        descripcionCargo
    FROM CargoDeEmpleado
    WHERE codigoCargoEmpleado = _codigoCargoEmpleado;
END $$

-- Procedimientos almacenados para Productos
CREATE PROCEDURE sp_AgregarProducto (
    IN _codigoProducto VARCHAR(15),
    IN _descripcionProducto VARCHAR(45),
    IN _precioUnitario DECIMAL(10,2),
    IN _precioDocena DECIMAL(10,2),
    IN _precioMayor DECIMAL(10,2),
    IN _existencia INT,
    IN _codigoTipoProducto INT,
    IN _codigoProveedor INT
)
BEGIN
    INSERT INTO Productos(
        codigoProducto,
        descripcionProducto,
        precioUnitario,
        precioDocena,
        precioMayor,
        existencia,
        codigoTipoProducto,
        codigoProveedor
    )
    VALUES (
        _codigoProducto,
        _descripcionProducto,
        _precioUnitario,
        _precioDocena,
        _precioMayor,
        _existencia,
        _codigoTipoProducto,
        _codigoProveedor
    );
END $$

CREATE PROCEDURE sp_ListarProductos()
BEGIN
    SELECT
        codigoProducto,
        descripcionProducto,
        precioUnitario,
        precioDocena,
        precioMayor,
        existencia,
        codigoTipoProducto,
        codigoProveedor
    FROM Productos;
END;


CREATE PROCEDURE sp_EditarProducto (
    IN _codigoProducto VARCHAR(15),
    IN _descripcionProducto VARCHAR(45),
    IN _precioUnitario DECIMAL(10,2),
    IN _precioDocena DECIMAL(10,2),
    IN _precioMayor DECIMAL(10,2),
    IN _existencia INT,
    IN _codigoTipoProducto INT,
    IN _codigoProveedor INT
)
BEGIN
    UPDATE Productos
    SET
        descripcionProducto = _descripcionProducto,
        precioUnitario = _precioUnitario,
        precioDocena = _precioDocena,
        precioMayor = _precioMayor,
        existencia = _existencia,
        codigoTipoProducto = _codigoTipoProducto,
        codigoProveedor = _codigoProveedor
    WHERE
        codigoProducto = _codigoProducto;
END $$

CREATE PROCEDURE sp_EliminarProducto(IN _codigoProducto VARCHAR(15))
BEGIN
    DELETE FROM Productos WHERE codigoProducto = _codigoProducto;
END $$

DELIMITER ;
DELIMITER $$

CREATE PROCEDURE sp_ListarEmpleados()
BEGIN
    SELECT
        codigoEmpleado,
        nombresEmpleado,
        apellidosEmpleado,
        sueldo,
        direccion,
        turno,
        CargoEmpleado_codigoCargoEmpleado
    FROM Empleados;
END $$

CREATE PROCEDURE sp_AgregarEmpleado(
    IN _nombresEmpleado VARCHAR(50), 
    IN _apellidosEmpleado VARCHAR(50), 
    IN _sueldo DECIMAL(10,2), 
    IN _direccion VARCHAR(150), 
    IN _turno VARCHAR(15), 
    IN _CargoEmpleado_codigoCargoEmpleado INT
)
BEGIN
    INSERT INTO Empleados(
        nombresEmpleado, 
        apellidosEmpleado, 
        sueldo, 
        direccion, 
        turno, 
        CargoEmpleado_codigoCargoEmpleado
    ) VALUES (
        _nombresEmpleado, 
        _apellidosEmpleado, 
        _sueldo, 
        _direccion, 
        _turno, 
        _CargoEmpleado_codigoCargoEmpleado
    );
END $$

CREATE PROCEDURE sp_BuscarEmpleado(IN _codigoEmpleado INT)
BEGIN
    SELECT
        codigoEmpleado,
        nombresEmpleado,
        apellidosEmpleado,
        sueldo,
        direccion,
        turno,
        CargoEmpleado_codigoCargoEmpleado
    FROM Empleados
    WHERE codigoEmpleado = _codigoEmpleado;
END $$

CREATE PROCEDURE sp_EliminarEmpleado(IN _codigoEmpleado INT)
BEGIN
    DELETE FROM Empleados WHERE codigoEmpleado = _codigoEmpleado;
END $$

CREATE PROCEDURE sp_ActualizarEmpleado(
    IN _codigoEmpleado INT, 
    IN _nombresEmpleado VARCHAR(50), 
    IN _apellidosEmpleado VARCHAR(50), 
    IN _sueldo DECIMAL(10,2), 
    IN _direccion VARCHAR(150), 
    IN _turno VARCHAR(15), 
    IN _CargoEmpleado_codigoCargoEmpleado INT
)
BEGIN
    UPDATE Empleados
    SET 
        nombresEmpleado = _nombresEmpleado,
        apellidosEmpleado = _apellidosEmpleado,
        sueldo = _sueldo,
        direccion = _direccion,
        turno = _turno,
        CargoEmpleado_codigoCargoEmpleado = _CargoEmpleado_codigoCargoEmpleado
    WHERE
        codigoEmpleado = _codigoEmpleado;
END $$

DELIMITER ;

-- Insertar datos de prueba
INSERT INTO Clientes (nombreClientes, apellidosClientes, direccionClientes, NIT, telefonoClientes, correoClientes) VALUES
('Juan', 'Pérez', '123 Calle Falsa', '1234567890123', '555-1234', 'juan.perez@example.com'),
('María', 'Gómez', '456 Avenida Siempre Viva', '9876543210987', '555-5678', 'maria.gomez@example.com');

INSERT INTO Proveedores (NitProveedor, nombresProveedor, apellidosProveedor, direccionProveedor, razonSocial, contactoPrincipal, paginaWeb, telefonoProveedor, emailProveedor) VALUES
('1234567890', 'Carlos', 'Ramírez', '789 Calle Principal', 'Proveedor ABC', 'Carlos Ramírez', 'www.proveedorabc.com', '555-8765', 'carlos.ramirez@example.com'),
('9876543210', 'Laura', 'Martínez', '321 Avenida Central', 'Proveedor XYZ', 'Laura Martínez', 'www.proveedorxyz.com', '555-4321', 'laura.martinez@example.com');

INSERT INTO Compras (fechaDocumento, descripcion, totalDocumento) VALUES
('2023-05-01', 'Compra de materiales', 1500.00),
('2023-06-15', 'Compra de equipos', 3200.50);

INSERT INTO TipoProducto (descripcion) VALUES
('Electrónica'),
('Ropa'),
('Alimentos');

INSERT INTO CargoDeEmpleado (nombreCargo, descripcionCargo) VALUES
('Gerente', 'Responsable de la gestión general de la empresa'),
('Vendedor', 'Encargado de la venta de productos');

INSERT INTO Productos (codigoProducto, descripcionProducto, precioUnitario, precioDocena, precioMayor, existencia, codigoTipoProducto, codigoProveedor) VALUES
('P001', 'Televisor LED', 500.00, 4800.00, 4500.00, 50, 1, 1),
('P002', 'Camisa', 20.00, 220.00, 200.00, 100, 2, 2);

INSERT INTO Empleados (nombresEmpleado, apellidosEmpleado, sueldo, direccion, turno, CargoEmpleado_codigoCargoEmpleado) VALUES
('Luis', 'González', 1500.50, 'Calle Principal 123', 'Diurno', 1),
('Ana', 'Martínez', 1200.75, 'Avenida Central 456', 'Nocturno', 2);
