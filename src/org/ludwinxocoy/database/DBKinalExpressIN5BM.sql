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

DELIMITER $$



-- Insertar datos de prueba
INSERT INTO Clientes (nombreClientes, apellidosClientes, direccionClientes, NIT, telefonoClientes, correoClientes) VALUES
('Juan', 'Pérez', '123 Calle Falsa', '1234567890123', '555-1234', 'juan.perez@example.com'),
('María', 'Gómez', '456 Avenida Siempre Viva', '9876543210987', '555-5678', 'maria.gomez@example.com');

-- Insertar datos en TipoProducto
INSERT INTO TipoProducto (descripcion) VALUES
('Electrónica'),
('Ropa'),
('Alimentos');

-- Insertar datos en CargoDeEmpleado
INSERT INTO CargoDeEmpleado (nombreCargo, descripcionCargo) VALUES
('Gerente', 'Responsable de la gestión general de la empresa'),
('Vendedor', 'Encargado de la venta de productos');

-- Insertar datos en Proveedores
INSERT INTO Proveedores (NitProveedor, nombresProveedor, apellidosProveedor, direccionProveedor, razonSocial, contactoPrincipal, paginaWeb, telefonoProveedor, emailProveedor) VALUES
('1234567890', 'Carlos', 'Ramírez', '789 Calle Principal', 'Proveedor ABC', 'Carlos Ramírez', 'www.proveedorabc.com', '555-8765', 'carlos.ramirez@example.com'),
('9876543210', 'Laura', 'Martínez', '321 Avenida Central', 'Proveedor XYZ', 'Laura Martínez', 'www.proveedorxyz.com', '555-4321', 'laura.martinez@example.com');

-- Insertar datos en Compras
INSERT INTO Compras (fechaDocumento, descripcion, totalDocumento) VALUES
('2023-05-01', 'Compra de materiales', 1500.00),
('2023-06-15', 'Compra de equipos', 3200.50);

-- Insertar datos en Productos
INSERT INTO Productos (codigoProducto, descripcionProducto, precioUnitario, precioDocena, precioMayor, existencia, codigoTipoProducto, codigoProveedor) VALUES
('P001', 'Televisor LED', 500.00, 4800.00, 4500.00, 50, 1, 1),
('P002', 'Camisa', 20.00, 220.00, 200.00, 100, 2, 2);

-- Insertar datos en Empleados
INSERT INTO Empleados (nombresEmpleado, apellidosEmpleado, sueldo, direccion, turno, codigoCargoEmpleado) VALUES
('Luis', 'González', 1500.50, 'Calle Principal 123', 'Diurno', 1),
('Ana', 'Martínez', 1200.75, 'Avenida Central 456', 'Nocturno', 2);

-- Insertar datos en DetalleCompra
INSERT INTO DetalleCompra (codigoDetalleCompra, costoUnitario, cantidad, codigoProducto, numeroDocumento) VALUES 
(1, 10.50, 3, 'P001', 1),
(2, 15.75, 2, 'P002', 2);

-- Insertar datos de prueba en TelefonoProveedor
INSERT INTO TelefonoProveedor (numeroPrincipal, numeroSecundario, observaciones, codigoProveedor) VALUES
('5551234', '5555678', 'Teléfono principal de contacto', 1),
('5558765', '5554321', 'Teléfono secundario de contacto', 2);

-- Insertar datos de prueba en EmailProveedor
INSERT INTO EmailProveedor (emailProveedor, descripcion, codigoProveedor) VALUES
('contacto@proveedorabc.com', 'Correo principal del proveedor ABC', 1),
('info@proveedorxyz.com', 'Correo secundario del proveedor XYZ', 2);

-- Insertar datos de prueba en Factura
INSERT INTO Factura (numeroFactura, estado, totalFactura, fechaFactura, clienteID, codigoEmpleado) VALUES
(1, 'Pagada', 150.00, '2024-05-15', 1, 1),
(2, 'Pendiente', 200.00, '2024-05-20', 2, 2);

-- Insertar datos de prueba en DetalleFactura
INSERT INTO DetalleFactura (codigoDetalleFactura, precioUnitario, cantidad, numeroFactura, codigoProducto) VALUES
(1, 50.00, 2, 1, 'P001'),
(2, 30.00, 3, 2, 'P002');

delimiter $$

create trigger tr_PrecioProductos_After_Insert
after insert on DetalleCompra
for each row 
begin
    declare total decimal(10,2);
    declare cantidad int;

    set total = NEW.costoUnitario * NEW.cantidad;

    update Productos
    set precioUnitario = total * 0.40,
        precioDocena  = total * 0.35 * 12,
        precioMayor = total * 0.25
    where Productos.codigoProducto = NEW.codigoProducto;

    update Productos
    set Productos.existencia = Productos.existencia - NEW.cantidad
    where Productos.codigoProducto = NEW.codigoProducto;

end $$
delimiter ;

delimiter $$

create trigger tr_TotalDocumento_After_Insert
after insert on DetalleCompra
for each row
begin
    declare total decimal(10,2);

    select sum(costoUnitario * cantidad) into total from DetalleCompra 
    where numeroDocumento = NEW.numeroDocumento;

    update Compras 
        set totalDocumento = total 
    where numeroDocumento = NEW.numeroDocumento;
end $$
delimiter ;

delimiter $$

create trigger tr_PrecioUnitario_After_Upd
after update on DetalleCompra
for each row
begin

    declare precioP decimal(10,2);

    set precioP = (select precioUnitario from Productos where codigoProducto = NEW.codigoProducto);

    update DetalleFactura
    set DetalleFactura.precioUnitario = precioP
    where DetalleFactura.codigoProducto = NEW.codigoProducto;
end $$
delimiter ;


delimiter $$

create trigger tr_TotalFactura_Aftr_U
after update on DetalleFactura
for each row
begin
    declare totalFactura decimal(10,2);

    select sum(precioUnitario * cantidad) into totalFactura from DetalleFactura
    where numeroFactura = NEW.numeroFactura;

    update Factura
        set Factura.totalFactura = totalFactura
    where Factura.numeroFactura = NEW.numeroFactura;
end $$
delimiter ;

