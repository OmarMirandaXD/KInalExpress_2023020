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
    codigoCargoEmpleado INT,
    PRIMARY KEY (codigoEmpleado),
    FOREIGN KEY (codigoCargoEmpleado) REFERENCES CargoDeEmpleado(codigoCargoEmpleado)
);

CREATE TABLE DetalleCompra (
    codigoDetalleCompra INT NOT NULL AUTO_INCREMENT,
    costoUnitario DECIMAL(10,2),
    cantidad INT,
    codigoProducto VARCHAR(15),
    numeroDocumento INT,
    PRIMARY KEY (codigoDetalleCompra),
    FOREIGN KEY (codigoProducto) REFERENCES Productos(codigoProducto) ON DELETE CASCADE,
    FOREIGN KEY (numeroDocumento) REFERENCES Compras(numeroDocumento) ON DELETE CASCADE
);

CREATE TABLE TelefonoProveedor (
    codigoTelefonoProveedor INT NOT NULL AUTO_INCREMENT,
    numeroPrincipal VARCHAR(8),
    numeroSecundario VARCHAR(8),
    observaciones VARCHAR(45),
    codigoProveedor INT,
    PRIMARY KEY (codigoTelefonoProveedor),
    FOREIGN KEY (codigoProveedor) REFERENCES Proveedores(codigoProveedor) ON DELETE CASCADE
);

CREATE TABLE EmailProveedor (
    codigoEmailProveedor INT NOT NULL AUTO_INCREMENT,
    emailProveedor VARCHAR(50),
    descripcion VARCHAR(100),
    codigoProveedor INT,
    PRIMARY KEY (codigoEmailProveedor),
    FOREIGN KEY (codigoProveedor) REFERENCES Proveedores(codigoProveedor) ON DELETE CASCADE
);
CREATE TABLE Factura (
    numeroFactura INT NOT NULL,
    estado VARCHAR(50),
    totalFactura DECIMAL(10,2),
    fechaFactura DATE,
    clienteID INT,
    codigoEmpleado INT,
    PRIMARY KEY (numeroFactura),
    FOREIGN KEY (clienteID) REFERENCES Clientes(clienteID) ON DELETE CASCADE,
    FOREIGN KEY (codigoEmpleado) REFERENCES Empleados(codigoEmpleado) ON DELETE CASCADE
);
CREATE TABLE DetalleFactura (
    codigoDetalleFactura INT NOT NULL,
    precioUnitario DECIMAL(10,2),
    cantidad INT,
    numeroFactura INT,
    codigoProducto VARCHAR(15),
    PRIMARY KEY (codigoDetalleFactura),
    FOREIGN KEY (numeroFactura) REFERENCES Factura(numeroFactura) ON DELETE CASCADE,
    FOREIGN KEY (codigoProducto) REFERENCES Productos(codigoProducto) ON DELETE CASCADE
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
	in _clienteID INT,
	IN _NIT VARCHAR(13),
    IN _nombreClientes VARCHAR(45), 
    IN _apellidosClientes VARCHAR(45), 
    IN _direccionClientes VARCHAR(128), 
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
CREATE PROCEDURE sp_buscarCompra (
    IN _numeroDocumento INT
)
BEGIN
    SELECT
        numeroDocumento,
        fechaDocumento,
        descripcion,
        totalDocumento
    FROM Compras
    WHERE numeroDocumento = _numeroDocumento;
END $$


CREATE PROCEDURE sp_EliminarCompra(IN _numeroDocumento INT)
BEGIN
    DELETE FROM Compras WHERE numeroDocumento = _numeroDocumento;
END $$

-- Procedimientos almacenados para TipoProducto
DELIMITER $$

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

CREATE PROCEDURE sp_buscarProducto (
    IN _codigoProducto VARCHAR(15)
)
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
    FROM Productos
    WHERE codigoProducto = _codigoProducto;
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
END $$

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
        codigoCargoEmpleado
    FROM Empleados;
END $$

CREATE PROCEDURE sp_AgregarEmpleado(
	IN _codigoEmpleado  INT,
    IN _nombresEmpleado VARCHAR(50), 
    IN _apellidosEmpleado VARCHAR(50), 
    IN _sueldo DECIMAL(10,2), 
    IN _direccion VARCHAR(150), 
    IN _turno VARCHAR(15), 
    IN _codigoCargoEmpleado INT
)
BEGIN
    INSERT INTO Empleados(
        nombresEmpleado, 
        apellidosEmpleado, 
        sueldo, 
        direccion, 
        turno, 
        codigoCargoEmpleado
    ) VALUES (
        _nombresEmpleado, 
        _apellidosEmpleado, 
        _sueldo, 
        _direccion, 
        _turno, 
        _codigoCargoEmpleado
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
        codigoCargoEmpleado
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
    IN _codigoCargoEmpleado INT
)
BEGIN
    UPDATE Empleados
    SET 
        nombresEmpleado = _nombresEmpleado,
        apellidosEmpleado = _apellidosEmpleado,
        sueldo = _sueldo,
        direccion = _direccion,
        turno = _turno,
        codigoCargoEmpleado = _codigoCargoEmpleado
    WHERE
        codigoEmpleado = _codigoEmpleado;
END $$

DELIMITER ;
-- Procedimiento para agregar un detalle de compra
DELIMITER $$
CREATE PROCEDURE sp_agregarDetalleCompra(
    IN codigoDetalleCompra INT,
    IN costoUnitario DECIMAL(10,2),
    IN cantidad INT,
    IN codigoProducto VARCHAR(15),
    IN numeroDocumento INT
)
BEGIN
    INSERT INTO DetalleCompra(codigoDetalleCompra, costoUnitario, cantidad, codigoProducto, numeroDocumento)
    VALUES (codigoDetalleCompra, costoUnitario, cantidad, codigoProducto, numeroDocumento);
END $$
DELIMITER ;

-- Procedimiento para listar todos los detalles de compra
DELIMITER $$
CREATE PROCEDURE sp_listarDetalleCompra()
BEGIN
    SELECT * FROM DetalleCompra;
END $$
DELIMITER ;

-- Procedimiento para buscar un detalle de compra por su código
DELIMITER $$
CREATE PROCEDURE sp_buscarDetalleCompra(IN codigoDetalleCompra INT)
BEGIN
    SELECT * FROM DetalleCompra WHERE codigoDetalleCompra = codigoDetalleCompra;
END $$
DELIMITER ;

-- Procedimiento para actualizar un detalle de compra
DELIMITER $$
CREATE PROCEDURE sp_actualizarDetalleCompra(
    IN codigoDetalleCompra INT,
    IN costoUnitario DECIMAL(10,2),
    IN cantidad INT,
    IN codigoProducto VARCHAR(15),
    IN numeroDocumento INT
)
BEGIN
    UPDATE DetalleCompra
    SET
        costoUnitario = costoUnitario,
        cantidad = cantidad,
        codigoProducto = codigoProducto,
        numeroDocumento = numeroDocumento
    WHERE
        codigoDetalleCompra = codigoDetalleCompra;
END $$
DELIMITER ;

-- Procedimiento para eliminar un detalle de compra por su código
DELIMITER $$
CREATE PROCEDURE sp_eliminarDetalleCompra(IN codigoDetalleCompra INT)
BEGIN
    DELETE FROM DetalleCompra WHERE codigoDetalleCompra = codigoDetalleCompra;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sp_agregarTelefonoProveedor(
	IN codigoTelefonoProveedor INT,
    IN numeroPrincipal VARCHAR(8),
    IN numeroSecundario VARCHAR(8),
    IN observaciones VARCHAR(45),
    IN codigoProveedor INT
)
BEGIN
    INSERT INTO TelefonoProveedor(numeroPrincipal, numeroSecundario, observaciones, codigoProveedor)
    VALUES (numeroPrincipal, numeroSecundario, observaciones, codigoProveedor);
END $$
DELIMITER ;

-- Procedimiento para listar todos los teléfonos de proveedores
DELIMITER $$
CREATE PROCEDURE sp_ListarTelefonoProveedor()
BEGIN
    SELECT * FROM TelefonoProveedor;
END $$
DELIMITER ;

-- Procedimiento para buscar un teléfono de proveedor por su código
DELIMITER $$
CREATE PROCEDURE sp_buscarTelefonoProveedor(IN codigoTelefonoProveedor INT)
BEGIN
    SELECT * FROM TelefonoProveedor WHERE codigoTelefonoProveedor = codigoTelefonoProveedor;
END $$
DELIMITER ;

-- Procedimiento para actualizar un teléfono de proveedor
DELIMITER $$
CREATE PROCEDURE sp_actualizarTelefonoProveedor(
    IN codigoTelefonoProveedor INT,
    IN numeroPrincipal VARCHAR(8),
    IN numeroSecundario VARCHAR(8),
    IN observaciones VARCHAR(45),
    IN codigoProveedor INT
)
BEGIN
    UPDATE TelefonoProveedor
    SET
        numeroPrincipal = numeroPrincipal,
        numeroSecundario = numeroSecundario,
        observaciones = observaciones,
        codigoProveedor = codigoProveedor
    WHERE
        codigoTelefonoProveedor = codigoTelefonoProveedor;
END $$
DELIMITER ;

-- Procedimiento para eliminar un teléfono de proveedor por su código
DELIMITER $$
CREATE PROCEDURE sp_eliminarTelefonoProveedor(IN codigoTelefonoProveedor INT)
BEGIN
    DELETE FROM TelefonoProveedor WHERE codigoTelefonoProveedor = codigoTelefonoProveedor;
END $$
DELIMITER ;
-- Procedimientos almacenados para EmailProveedor
DELIMITER $$

-- Procedimiento para agregar un EmailProveedor
CREATE PROCEDURE sp_agregarEmailProveedor(
	IN _codigoEmailProveedor INT,
    IN _emailProveedor VARCHAR(50),
    IN _descripcion VARCHAR(100),
    IN _codigoProveedor INT
)
BEGIN
    INSERT INTO EmailProveedor(emailProveedor, descripcion, codigoProveedor)
    VALUES (_emailProveedor, _descripcion, _codigoProveedor);
END $$
DELIMITER ;

-- Procedimiento para listar todos los registros de EmailProveedor
DELIMITER $$
CREATE PROCEDURE sp_listarEmailProveedor()
BEGIN
    SELECT * FROM EmailProveedor;
END $$
DELIMITER ;

-- Procedimiento para buscar un EmailProveedor por su código
DELIMITER $$
CREATE PROCEDURE sp_buscarEmailProveedor(IN _codigoEmailProveedor INT)
BEGIN
    SELECT * FROM EmailProveedor WHERE codigoEmailProveedor = _codigoEmailProveedor;
END $$
DELIMITER ;

-- Procedimiento para actualizar un EmailProveedor
DELIMITER $$
CREATE PROCEDURE sp_actualizarEmailProveedor(
    IN _codigoEmailProveedor INT,
    IN _emailProveedor VARCHAR(50),
    IN _descripcion VARCHAR(100),
    IN _codigoProveedor INT
)
BEGIN
    UPDATE EmailProveedor
    SET
        emailProveedor = _emailProveedor,
        descripcion = _descripcion,
        codigoProveedor = _codigoProveedor
    WHERE
        codigoEmailProveedor = _codigoEmailProveedor;
END $$
DELIMITER ;

-- Procedimiento para eliminar un EmailProveedor por su código
DELIMITER $$
CREATE PROCEDURE sp_eliminarEmailProveedor(IN _codigoEmailProveedor INT)
BEGIN
    DELETE FROM EmailProveedor WHERE codigoEmailProveedor = _codigoEmailProveedor;
END $$
DELIMITER ;
-- Procedimientos almacenados para Factura
DELIMITER $$

-- Procedimiento para agregar una factura
CREATE PROCEDURE sp_agregarFactura(
    IN _numeroFactura INT,
    IN _estado VARCHAR(50),
    IN _totalFactura DECIMAL(10,2),
    IN _fechaFactura DATE,
    IN _clienteID INT,
    IN _codigoEmpleado INT
)
BEGIN
    INSERT INTO Factura(numeroFactura, estado, totalFactura, fechaFactura, clienteID, codigoEmpleado)
    VALUES (_numeroFactura, _estado, _totalFactura, _fechaFactura, _clienteID, _codigoEmpleado);
END $$
DELIMITER ;

-- Procedimiento para listar todas las facturas
DELIMITER $$
CREATE PROCEDURE sp_listarFactura()
BEGIN
    SELECT * FROM Factura;
END $$
DELIMITER ;

-- Procedimiento para buscar una factura por su número
DELIMITER $$
CREATE PROCEDURE sp_buscarFactura(IN _numeroFactura INT)
BEGIN
    SELECT * FROM Factura WHERE numeroFactura = _numeroFactura;
END $$
DELIMITER ;

-- Procedimiento para actualizar una factura
DELIMITER $$
CREATE PROCEDURE sp_actualizarFactura(
    IN _numeroFactura INT,
    IN _estado VARCHAR(50),
    IN _totalFactura DECIMAL(10,2),
    IN _fechaFactura DATE,
    IN _clienteID INT,
    IN _codigoEmpleado INT
)
BEGIN
    UPDATE Factura
    SET
        estado = _estado,
        totalFactura = _totalFactura,
        fechaFactura = _fechaFactura,
        clienteID = _clienteID,
        codigoEmpleado = _codigoEmpleado
    WHERE
        numeroFactura = _numeroFactura;
END $$
DELIMITER ;

-- Procedimiento para eliminar una factura por su número
DELIMITER $$
CREATE PROCEDURE sp_eliminarFactura(IN _numeroFactura INT)
BEGIN
    DELETE FROM Factura WHERE numeroFactura = _numeroFactura;
END $$
DELIMITER ;

DELIMITER $$


CREATE PROCEDURE sp_agregarDetalleFactura(
    IN _codigoDetalleFactura INT,
    IN _precioUnitario DECIMAL(10,2),
    IN _cantidad INT,
    IN _numeroFactura INT,
    IN _codigoProducto VARCHAR(15)
)
BEGIN
    INSERT INTO DetalleFactura(codigoDetalleFactura, precioUnitario, cantidad, numeroFactura, codigoProducto)
    VALUES (_codigoDetalleFactura, _precioUnitario, _cantidad, _numeroFactura, _codigoProducto);
END $$
DELIMITER ;

-- Procedimiento para listar todos los detalles de factura
DELIMITER $$
CREATE PROCEDURE sp_listarDetalleFactura()
BEGIN
    SELECT * FROM DetalleFactura;
END $$
DELIMITER ;

-- Procedimiento para buscar un detalle de factura por su código
DELIMITER $$
CREATE PROCEDURE sp_buscarDetalleFactura(IN _codigoDetalleFactura INT)
BEGIN
    SELECT * FROM DetalleFactura WHERE codigoDetalleFactura = _codigoDetalleFactura;
END $$
DELIMITER ;

-- Procedimiento para actualizar un detalle de factura
DELIMITER $$
CREATE PROCEDURE sp_actualizarDetalleFactura(
    IN _codigoDetalleFactura INT,
    IN _precioUnitario DECIMAL(10,2),
    IN _cantidad INT,
    IN _numeroFactura INT,
    IN _codigoProducto VARCHAR(15)
)
BEGIN
    UPDATE DetalleFactura
    SET
        precioUnitario = _precioUnitario,
        cantidad = _cantidad,
        numeroFactura = _numeroFactura,
        codigoProducto = _codigoProducto
    WHERE
        codigoDetalleFactura = _codigoDetalleFactura;
END $$
DELIMITER ;

-- Procedimiento para eliminar un detalle de factura por su código
DELIMITER $$
CREATE PROCEDURE sp_eliminarDetalleFactura(IN _codigoDetalleFactura INT)
BEGIN
    DELETE FROM DetalleFactura WHERE codigoDetalleFactura = _codigoDetalleFactura;
END $$
DELIMITER ;



CALL sp_AgregarClientes(1, '1234567890123', 'Juan', 'Pérez', '123 Calle Falsa', '5551234567', 'juan.perez@example.com');
CALL sp_AgregarClientes(2, '2345678901234', 'María', 'García', '456 Avenida Siempreviva', '5552345678', 'maria.garcia@example.com');
CALL sp_AgregarClientes(3, '3456789012345', 'Carlos', 'López', '789 Calle Principal', '5553456789', 'carlos.lopez@example.com');
CALL sp_AgregarClientes(4, '4567890123456', 'Ana', 'Rodríguez', '1011 Boulevard Secundario', '5554567890', 'ana.rodriguez@example.com');
CALL sp_AgregarClientes(5, '5678901234567', 'Luis', 'Martínez', '1213 Calle Tercera', '5555678901', 'luis.martinez@example.com');



CALL sp_AgregarProveedor('1234567890', 'Proveedor1', 'Apellido1', 'Dirección 1', 'Razón Social 1', 'Contacto 1', 'www.proveedor1.com', '123456789', 'proveedor1@example.com');
CALL sp_AgregarProveedor('9876543210', 'Proveedor2', 'Apellido2', 'Dirección 2', 'Razón Social 2', 'Contacto 2', 'www.proveedor2.com', '987654321', 'proveedor2@example.com');
CALL sp_AgregarProveedor('6543210987', 'Proveedor3', 'Apellido3', 'Dirección 3', 'Razón Social 3', 'Contacto 3', 'www.proveedor3.com', '654321098', 'proveedor3@example.com');
CALL sp_AgregarProveedor('3210987654', 'Proveedor4', 'Apellido4', 'Dirección 4', 'Razón Social 4', 'Contacto 4', 'www.proveedor4.com', '321098765', 'proveedor4@example.com');
CALL sp_AgregarProveedor('0987654321', 'Proveedor5', 'Apellido5', 'Dirección 5', 'Razón Social 5', 'Contacto 5', 'www.proveedor5.com', '098765432', 'proveedor5@example.com');



CALL sp_AgregarCompra('2024-06-11', 'Compra 1', 100.00);
CALL sp_AgregarCompra('2024-06-12', 'Compra 2', 200.00);
CALL sp_AgregarCompra('2024-06-13', 'Compra 3', 300.00);
CALL sp_AgregarCompra('2024-06-14', 'Compra 4', 400.00);
CALL sp_AgregarCompra('2024-06-15', 'Compra 5', 500.00);

CALL sp_AgregarTipoProducto('Tipo 1');
CALL sp_AgregarTipoProducto('Tipo 2');
CALL sp_AgregarTipoProducto('Tipo 3');
CALL sp_AgregarTipoProducto('Tipo 4');
CALL sp_AgregarTipoProducto('Tipo 5');

CALL sp_AgregarCargoEmpleado('Cargo 1', 'Descripción 1');
CALL sp_AgregarCargoEmpleado('Cargo 2', 'Descripción 2');
CALL sp_AgregarCargoEmpleado('Cargo 3', 'Descripción 3');
CALL sp_AgregarCargoEmpleado('Cargo 4', 'Descripción 4');
CALL sp_AgregarCargoEmpleado('Cargo 5', 'Descripción 5');

CALL sp_AgregarProducto('PROD001', 'Producto 1', 50.00, 45.00, 40.00, 100, 1, 1);
CALL sp_AgregarProducto('PROD002', 'Producto 2', 60.00, 55.00, 50.00, 150, 2, 2);
CALL sp_AgregarProducto('PROD003', 'Producto 3', 70.00, 65.00, 60.00, 200, 3, 3);
CALL sp_AgregarProducto('PROD004', 'Producto 4', 80.00, 75.00, 70.00, 250, 4, 4);
CALL sp_AgregarProducto('PROD005', 'Producto 5', 90.00, 85.00, 80.00, 300, 5, 5);

CALL sp_AgregarEmpleado(1, 'Juan', 'Pérez', 2000.00, 'Calle 123', 'Mañana', 1);
CALL sp_AgregarEmpleado(2, 'María', 'López', 2500.00, 'Avenida 456', 'Tarde', 2);
CALL sp_AgregarEmpleado(3, 'Pedro', 'González', 2200.00, 'Calle 789', 'Noche', 3);
CALL sp_AgregarEmpleado(4, 'Ana', 'Martínez', 2100.00, 'Carrera 456', 'Mañana', 4);
CALL sp_AgregarEmpleado(5, 'Carlos', 'Sánchez', 2300.00, 'Avenida 789', 'Tarde', 5);

CALL sp_agregarDetalleCompra(1, 10.00, 2, 'PROD001', 1);
CALL sp_agregarDetalleCompra(2, 20.00, 3, 'PROD002', 2);
CALL sp_agregarDetalleCompra(3, 30.00, 4, 'PROD003', 3);
CALL sp_agregarDetalleCompra(4, 40.00, 5, 'PROD004', 4);
CALL sp_agregarDetalleCompra(5, 50.00, 6, 'PROD005', 5);

CALL sp_agregarTelefonoProveedor(1, '12345678', '87654321', 'Teléfono principal', 1);
CALL sp_agregarTelefonoProveedor(2, '98765432', '23456789', 'Teléfono principal', 2);
CALL sp_agregarTelefonoProveedor(3, '65432109', '65432109', 'Teléfono principal', 3);
CALL sp_agregarTelefonoProveedor(4, '32109876', '32109876', 'Teléfono principal', 4);
CALL sp_agregarTelefonoProveedor(5, '09876543', '09876543', 'Teléfono principal', 5);

CALL sp_agregarEmailProveedor(1, 'proveedor1@example.com', 'Correo principal', 1);
CALL sp_agregarEmailProveedor(2, 'proveedor2@example.com', 'Correo principal', 2);
CALL sp_agregarEmailProveedor(3, 'proveedor3@example.com', 'Correo principal', 3);
CALL sp_agregarEmailProveedor(4, 'proveedor4@example.com', 'Correo principal', 4);
CALL sp_agregarEmailProveedor(5, 'proveedor5@example.com', 'Correo principal', 5);


CALL sp_agregarFactura(1, 'Pagada', 1500.00, '2024-06-11', 1, 1);
CALL sp_agregarFactura(2, 'Pendiente', 1800.00, '2024-06-11', 2, 2);
CALL sp_agregarFactura(3, 'Pagada', 2000.00, '2024-06-11', 3, 3);
CALL sp_agregarFactura(4, 'Pendiente', 2300.00, '2024-06-11', 4, 4);
CALL sp_agregarFactura(5, 'Pagada', 2500.00, '2024-06-11', 5, 5);

CALL sp_agregarDetalleFactura(1, 50.00, 2, 1, 'PROD001');
CALL sp_agregarDetalleFactura(2, 60.00, 3, 2, 'PROD002');
CALL sp_agregarDetalleFactura(3, 70.00, 4, 3, 'PROD003');
CALL sp_agregarDetalleFactura(4, 80.00, 5, 4, 'PROD004');
CALL sp_agregarDetalleFactura(5, 90.00, 6, 5, 'PROD005');

ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'pitudo37d*';
flush privileges;

select * from DetalleFactura
	join Factura on DetalleFactura.numeroFactura = Factura.numeroFactura
    join Clientes on Factura.clienteID = Clientes.clienteID
    join Productos on DetalleFactura.codigoProducto = Productos.codigoProducto
	where Factura.numeroFactura = 2;
