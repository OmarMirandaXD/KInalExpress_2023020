-- Crear y usar la base de datos
DROP DATABASE IF EXISTS DBKinalExpressIN5BM;
CREATE DATABASE DBKinalExpressIN5BM;
USE DBKinalExpressIN5BM;
SET GLOBAL time_zone = '-6:00';

-- Creación de tablas
CREATE TABLE Clientes(
    clienteID INT,
    nombreClientes VARCHAR(45),
    apellidosClientes VARCHAR(45),
    direccionClientes VARCHAR(128),
    NIT VARCHAR(13),
    telefonoClientes VARCHAR(13),
    correoClientes VARCHAR(128),
    PRIMARY KEY (clienteID)
);

CREATE TABLE Proveedores(
    codigoProveedor INT,
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
    numeroDocumento INT,
    fechaDocumento DATE,
    descripcion VARCHAR(60),
    totalDocumento DECIMAL(10,2),
    PRIMARY KEY (numeroDocumento)
);

CREATE TABLE TipoProducto(
    codigoTipoProducto INT,
    descripcion VARCHAR(45),
    PRIMARY KEY (codigoTipoProducto)
);

CREATE TABLE CargoDeEmpleado(
    codigoCargoEmpleado INT,
    nombreCargo VARCHAR(50),
    descripcionCargo VARCHAR(45),
    PRIMARY KEY (codigoCargoEmpleado)
);

CREATE TABLE Productos(
    codigoProducto VARCHAR(15),
    descripcionProducto VARCHAR(15),
    precioUnitario DECIMAL(10,2),
    precioDocena DECIMAL(10,2),
    precioMayor DECIMAL(10,2),
    imagenProducto VARCHAR(45),
    existencia INT,
    codigoTipoProducto INT,
    codigoProveedor INT,
    PRIMARY KEY (codigoProducto),
    FOREIGN KEY (codigoTipoProducto) REFERENCES TipoProducto(codigoTipoProducto),
    FOREIGN KEY (codigoProveedor) REFERENCES Proveedores(codigoProveedor)
);

-- Procedimientos almacenados para Clientes
DELIMITER $$

CREATE PROCEDURE sp_ListarClientes()
BEGIN
    SELECT
        c.clienteID,
        c.nombreClientes,
        c.apellidosClientes,
        c.direccionClientes,
        c.NIT,
        c.telefonoClientes,
        c.correoClientes
    FROM Clientes c;
END $$

CREATE PROCEDURE sp_AgregarClientes (
    IN _clienteID INT, 
    IN _nombreClientes VARCHAR(45), 
    IN _apellidosClientes VARCHAR(45), 
    IN _direccionClientes VARCHAR(128), 
    IN _NIT VARCHAR(13), 
    IN _telefonoClientes VARCHAR(13), 
    IN _correoClientes VARCHAR(128)
)
BEGIN
    INSERT INTO Clientes (
        clienteID, 
        nombreClientes, 
        apellidosClientes, 
        direccionClientes, 
        NIT, 
        telefonoClientes, 
        correoClientes
    ) VALUES (
        _clienteID, 
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
        c.clienteID,
        c.nombreClientes,
        c.apellidosClientes,
        c.direccionClientes,
        c.NIT,
        c.telefonoClientes,
        c.correoClientes
    FROM Clientes c
    WHERE clienteID = _clienteID;
END $$

CREATE PROCEDURE sp_eliminarClientes(IN _clienteID INT)
BEGIN
    DELETE FROM Clientes WHERE clienteID = _clienteID;
END $$

CREATE PROCEDURE sp_actualizar(
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

-- Insertar datos en Clientes
CALL sp_AgregarClientes(45, 'Juan', 'Pérez', '6ta Calle Zona 10', '1234567890123', '555123456', 'juan@gmail.com');
CALL sp_AgregarClientes(46, 'María', 'López', '7ma Avenida Zona 15', '9876543210987', '555987654', 'maria@gmail.com');
CALL sp_AgregarClientes(47, 'Carlos', 'García', '2da Calle Zona 4', '4567890123456', '555456789', 'carlos@gmail.com');
CALL sp_AgregarClientes(48, 'Laura', 'Martínez', '9na Avenida Zona 1', '7890123456789', '555789012', 'laura@gmail.com');
CALL sp_AgregarClientes(49, 'Pedro', 'Rodríguez', '3ra Calle Zona 6', '2345678901234', '555234567', 'pedro@gmail.com');
CALL sp_AgregarClientes(50, 'Ana', 'Gómez', '8va Avenida Zona 12', '5678901234567', '555567890', 'ana@gmail.com');
CALL sp_AgregarClientes(511, 'Sofía', 'Hernández', '4ta Calle Zona 8', '3456789012345', '555345678', 'sofia@gmail.com');

-- Procedimientos almacenados para Proveedores
CREATE PROCEDURE sp_AgregarProveedor (
    IN codigoProveedor INT,
    IN NitProveedor VARCHAR(10),
    IN nombresProveedor VARCHAR(60),
    IN apellidosProveedor VARCHAR(60),
    IN direccionProveedor VARCHAR(150),
    IN razonSocial VARCHAR(60),
    IN contactoPrincipal VARCHAR(100),
    IN paginaWeb VARCHAR(50),
    IN telefonoProveedor VARCHAR(13),
    IN emailProveedor VARCHAR(128)
)
BEGIN
    INSERT INTO Proveedores(
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
    )
    VALUES (
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
    );
END $$

CREATE PROCEDURE sp_ListarProveedor()
BEGIN
    SELECT
        p.codigoProveedor,
        p.NitProveedor,
        p.nombresProveedor,
        p.apellidosProveedor,
        p.direccionProveedor,
        p.razonSocial,
        p.contactoPrincipal,
        p.paginaWeb,
        p.telefonoProveedor,
        p.emailProveedor
    FROM Proveedores p;
END $$

CREATE PROCEDURE sp_editarProveedor (
    IN codigoProveedor INT,
    IN NitProveedor VARCHAR(10),
    IN nombresProveedor VARCHAR(60),
    IN apellidosProveedor VARCHAR(60),
    IN direccionProveedor VARCHAR(150),
    IN razonSocial VARCHAR(60),
    IN contactoPrincipal VARCHAR(100),
    IN paginaWeb VARCHAR(50),
    IN telefonoProveedor VARCHAR(13),
    IN emailProveedor VARCHAR(128)
)
BEGIN
    UPDATE Proveedores 
    SET
        NitProveedor = NitProveedor,
        nombresProveedor = nombresProveedor,
        apellidosProveedor = apellidosProveedor,
        direccionProveedor = direccionProveedor,
        razonSocial = razonSocial,
        contactoPrincipal = contactoPrincipal,
        paginaWeb = paginaWeb,
        telefonoProveedor = telefonoProveedor,
        emailProveedor = emailProveedor
    WHERE
        codigoProveedor = codigoProveedor;
END $$

CREATE PROCEDURE sp_eliminarProveedor(IN codigoProveedor INT)
BEGIN
    DELETE FROM Proveedores WHERE codigoProveedor = codigoProveedor;
END $$

-- Procedimiento para buscar proveedor por código
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

-- Insertar datos en Proveedores
CALL sp_AgregarProveedor(1, '1234567890', 'Proveedor1', 'Apellido1', 'Dirección1', 'Razón1', 'Contacto1', 'www.proveedor1.com', '555123456', 'proveedor1@example.com');
CALL sp_AgregarProveedor(2, '0987654321', 'Proveedor2', 'Apellido2', 'Dirección2', 'Razón2', 'Contacto2', 'www.proveedor2.com', '555987654', 'proveedor2@example.com');
CALL sp_AgregarProveedor(3, 'NIT3', 'Nombre3', 'Apellido3', 'Dirección3', 'Razón Social3', 'Contacto3', 'www.proveedor3.com', '555456789', 'proveedor3@example.com');
CALL sp_AgregarProveedor(4, 'NIT4', 'Nombre4', 'Apellido4', 'Dirección4', 'Razón Social4', 'Contacto4', 'www.proveedor4.com', '555789012', 'proveedor4@example.com');
CALL sp_AgregarProveedor(5, 'NIT5', 'Nombre5', 'Apellido5', 'Dirección5', 'Razón Social5', 'Contacto5', 'www.proveedor5.com', '555234567', 'proveedor5@example.com');

-- Procedimientos almacenados para Compras
CREATE PROCEDURE sp_AgregarCompra (
    IN _numeroDocumento INT,
    IN _fechaDocumento DATE,
    IN _descripcion VARCHAR(60),
    IN _totalDocumento DECIMAL(10,2)
)
BEGIN
    INSERT INTO Compras (
        numeroDocumento, 
        fechaDocumento, 
        descripcion, 
        totalDocumento
    )
    VALUES (
        _numeroDocumento, 
        _fechaDocumento, 
        _descripcion, 
        _totalDocumento
    );
END $$

CREATE PROCEDURE sp_ListarCompras()
BEGIN
    SELECT
        c.numeroDocumento,
        c.fechaDocumento,
        c.descripcion,
        c.totalDocumento
    FROM Compras c;
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

-- Insertar datos en Compras
CALL sp_AgregarCompra(1, '2023-01-01', 'Compra 1', 100.00);
CALL sp_AgregarCompra(2, '2023-02-01', 'Compra 2', 200.00);
CALL sp_AgregarCompra(3, '2023-03-01', 'Compra 3', 300.00);
CALL sp_AgregarCompra(4, '2023-04-01', 'Compra 4', 400.00);
CALL sp_AgregarCompra(5, '2023-05-01', 'Compra 5', 500.00);

-- Procedimientos almacenados para TipoProducto
CREATE PROCEDURE sp_AgregarTipoProducto (
    IN _codigoTipoProducto INT,
    IN _descripcion VARCHAR(45)
)
BEGIN
    INSERT INTO TipoProducto (
        codigoTipoProducto, 
        descripcion
    )
    VALUES (
        _codigoTipoProducto, 
        _descripcion
    );
END $$

CREATE PROCEDURE sp_ListarTipoProducto()
BEGIN
    SELECT
        tp.codigoTipoProducto,
        tp.descripcion
    FROM TipoProducto tp;
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

-- Procedimiento para buscar tipo de producto por código
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

-- Insertar datos en TipoProducto
CALL sp_AgregarTipoProducto(1, 'Tipo 1');
CALL sp_AgregarTipoProducto(2, 'Tipo 2');
CALL sp_AgregarTipoProducto(3, 'Tipo 3');
CALL sp_AgregarTipoProducto(4, 'Tipo 4');
CALL sp_AgregarTipoProducto(5, 'Tipo 5');

-- Procedimientos almacenados para CargoDeEmpleado
CREATE PROCEDURE sp_AgregarCargoEmpleado (
    IN _codigoCargoEmpleado INT,
    IN _nombreCargo VARCHAR(50),
    IN _descripcionCargo VARCHAR(45)
)
BEGIN
    INSERT INTO CargoDeEmpleado(
        codigoCargoEmpleado,
        nombreCargo,
        descripcionCargo
    )
    VALUES (
        _codigoCargoEmpleado,
        _nombreCargo,
        _descripcionCargo
    );
END $$

CREATE PROCEDURE sp_ListarCargoEmpleado()
BEGIN
    SELECT
        ce.codigoCargoEmpleado,
        ce.nombreCargo,
        ce.descripcionCargo
    FROM CargoDeEmpleado ce;
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

-- Insertar datos en CargoDeEmpleado
CALL sp_AgregarCargoEmpleado(1, 'Cargo 1', 'Descripción 1');
CALL sp_AgregarCargoEmpleado(2, 'Cargo 2', 'Descripción 2');
CALL sp_AgregarCargoEmpleado(3, 'Cargo 3', 'Descripción 3');
CALL sp_AgregarCargoEmpleado(4, 'Cargo 4', 'Descripción 4');
CALL sp_AgregarCargoEmpleado(5, 'Cargo 5', 'Descripción 5');

-- Procedimientos almacenados para Productos
CREATE PROCEDURE sp_AgregarProducto (
    IN _codigoProducto VARCHAR(15),
    IN _descripcionProducto VARCHAR(15),
    IN _precioUnitario DECIMAL(10,2),
    IN _precioDocena DECIMAL(10,2),
    IN _precioMayor DECIMAL(10,2),
    IN _imagenProducto VARCHAR(45),
    IN _existencia INT,
    IN _codigoTipoProducto INT,
    IN _codigoProveedor INT
)
BEGIN
    INSERT INTO Productos (
        codigoProducto,
        descripcionProducto,
        precioUnitario,
        precioDocena,
        precioMayor,
        imagenProducto,
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
        _imagenProducto,
        _existencia,
        _codigoTipoProducto,
        _codigoProveedor
    );
END $$

CREATE PROCEDURE sp_ListarProducto()
BEGIN
    SELECT
        p.codigoProducto,
        p.descripcionProducto,
        p.precioUnitario,
        p.precioDocena,
        p.precioMayor,
        p.imagenProducto,
        p.existencia,
        p.codigoTipoProducto,
        tp.descripcion AS tipoProductoDescripcion,
        p.codigoProveedor,
        pr.nombresProveedor AS proveedorNombre
    FROM Productos p
    JOIN TipoProducto tp ON p.codigoTipoProducto = tp.codigoTipoProducto
    JOIN Proveedores pr ON p.codigoProveedor = pr.codigoProveedor;
END $$

CREATE PROCEDURE sp_EditarProducto (
    IN _codigoProducto VARCHAR(15),
    IN _descripcionProducto VARCHAR(15),
    IN _precioUnitario DECIMAL(10,2),
    IN _precioDocena DECIMAL(10,2),
    IN _precioMayor DECIMAL(10,2),
    IN _imagenProducto VARCHAR(45),
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
        imagenProducto = _imagenProducto,
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

-- Insertar datos en Productos
CALL sp_AgregarProducto('P001', 'Producto 1', 10.00, 100.00, 900.00, 'imagen1.jpg', 50, 1, 1);
CALL sp_AgregarProducto('P002', 'Producto 2', 20.00, 200.00, 1800.00, 'imagen2.jpg', 60, 2, 2);
CALL sp_AgregarProducto('P003', 'Producto 3', 30.00, 300.00, 2700.00, 'imagen3.jpg', 70, 3, 3);
CALL sp_AgregarProducto('P004', 'Producto 4', 40.00, 400.00, 3600.00, 'imagen4.jpg', 80, 4, 4);

DELIMITER ;
