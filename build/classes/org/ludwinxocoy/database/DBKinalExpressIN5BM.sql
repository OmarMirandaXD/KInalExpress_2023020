drop database if exists DBKinalExpressIN5BM;

create database DBKinalExpressIN5BM;

use DBKinalExpressIN5BM;
set global time_zone = '-6:00';

create table Clientes(
	clienteID int ,
    nombreClientes varchar (45),
    apellidosClientes varchar (45),
    direccionClientes varchar (128),
    NIT varchar (13),
    telefonoClientes varchar (13),
    correoClientes varchar (128),
    primary key PK_ClienteID(clienteID)
);

create table Proveedores(
	codigoProveedor int,
    NitProveedor varchar (10),
    nombresProveedor varchar (60),
    apellidosProveedor varchar (60),
    direccionProveedor varchar (150),
    razonSocial varchar (60),
    contactoPrincipal varchar (100),
    paginaWeb varchar(50),
    primary key PK_CodigoProveedor(codigoProveedor)
);

create table Compras(
	numeroDocumento int,
    fechaDocumento date,
	descripcion varchar(60),
    totalDocumento decimal(10,2),
    primary key PK_NumeroDocumento(numeroDocumento)
);

create table TipoProducto(
	codigoTipoProducto int,
    descripcion varchar(45),
    primary key PK_CodigoTipoProducto(codigoTipoProducto)
);

create table CargoDeEmpleado(
	codigoCargoEmpleado int,
    nombreCargo varchar(50),
    descripcionCargo varchar (45),
	primary key PK_CodigoCargoEmpleado(codigoCargoEmpleado)
);

delimiter $$

create procedure sp_ListarClientes ()
begin 
	select
    c.clienteID,
    c.nombreClientes,
    c.apellidosClientes,
    c.direccionClientes,
    c.NIT,
    c.telefonoClientes,
    c.correoClientes
    from clientes c;
end $$        
delimiter ;

call sp_ListarClientes;

delimiter $$

create procedure sp_AgregarClientes (in _clienteID int, in _nombreClientes varchar (45), in _apellidosClientes varchar(45), in _direccionClientes varchar(128), in _NIT varchar(13), in _telefonoClientes varchar (13), in _correoClientes varchar(128))
begin 
	insert into Clientes (Clientes.clienteID, Clientes.nombreClientes, Clientes.apellidosClientes, Clientes.direccionClientes, Clientes.NIT, Clientes.telefonoClientes, Clientes.correoClientes)
		values (_clienteID, _nombreClientes, _apellidosClientes, _direccionClientes, _NIT, _telefonoClientes,_correoClientes);
end $$        
delimiter ;

CALL sp_AgregarClientes(45, 'Juan', 'Pérez', '6ta Calle Zona 10', '1234567890123', '555123456', 'juan@gmail.com');
CALL sp_AgregarClientes(46, 'María', 'López', '7ma Avenida Zona 15', '9876543210987', '555987654', 'maria@gmail.com');
CALL sp_AgregarClientes(47, 'Carlos', 'García', '2da Calle Zona 4', '4567890123456', '555456789', 'carlos@gmail.com');
CALL sp_AgregarClientes(48, 'Laura', 'Martínez', '9na Avenida Zona 1', '7890123456789', '555789012', 'laura@gmail.com');
CALL sp_AgregarClientes(49, 'Pedro', 'Rodríguez', '3ra Calle Zona 6', '2345678901234', '555234567', 'pedro@gmail.com');
CALL sp_AgregarClientes(50, 'Ana', 'Gómez', '8va Avenida Zona 12', '5678901234567', '555567890', 'ana@gmail.com');
CALL sp_AgregarClientes(511, 'Sofía', 'Hernández', '4ta Calle Zona 8', '3456789012345', '555345678', 'sofia@gmail.com');
delimiter $$

create procedure sp_buscarClientes (in _clienteID int)
begin 
	select
    c.clienteID,
    c.nombreClientes,
    c.apellidosClientes,
    c.direccionClientes,
    c.NIT,
    c.telefonoClientes,
    c.correoClientes
    from clientes c
    where clienteID = _clienteID;
end $$        
delimiter ;

call sp_buscarClientes(1);

delimiter $$

create procedure sp_eliminarClientes (in _clienteID int)
begin 
	delete from clientes where clienteID = _clienteID;
end $$
delimiter ;

call sp_eliminarClientes(1);

delimiter $$

create procedure sp_actualizar  (in _clienteID int, in _nombreClientes varchar (45), in _apellidosClientes varchar(45), in _direccionClientes varchar(128), in _NIT varchar(13), in _telefonoClientes varchar (13), in _correoClientes varchar(128))
begin 
	update Clientes
    set nombreClientes = _nombreClientes,
    apellidosClientes = _apellidosClientes,
    direccionClientes = _direccionClientes,
    nit = _nit,
    telefonoClientes = _telefonoClientes,
    correoClientes = _correoClientes
    where
    clienteID = _clienteID;
end $$
delimiter ;

CALL sp_AgregarClientes(51, 'Sofía', 'Hernández', '4ta Calle Zona 8', '3456789012345', '555345678', 'sofia@gmail.com');
select * from Clientes;

delimiter $$

CREATE PROCEDURE sp_AgregarProveedor (
    IN codigoProveedor INT,
    IN NitProveedor VARCHAR(10),
    IN nombresProveedor VARCHAR(60),
    IN apellidosProveedor VARCHAR(60),
    IN direccionProveedor VARCHAR(150),
    IN razonSocial VARCHAR(60),
    IN contactoPrincipal VARCHAR(100),
    IN paginaWeb VARCHAR(50)
)
BEGIN
    INSERT INTO Proveedores(codigoProveedor, NitProveedor, nombresProveedor, apellidosProveedor, direccionProveedor, razonSocial, contactoPrincipal, paginaWeb)
    VALUES (codigoProveedor, NITProveedor, nombresProveedor, apellidosProveedor, direccionProveedor, razonSocial, contactoPrincipal, paginaWeb);
END $$

DELIMITER ;

CALL sp_AgregarProveedor(5, 'NIT5', 'Nombre5', 'Apellido5', 'Dirección5', 'Razón Social5', 'Contacto5', 'www.proveedor5.com');
CALL sp_AgregarProveedor(6, 'NIT6', 'Nombre6', 'Apellido6', 'Dirección6', 'Razón Social6', 'Contacto6', 'www.proveedor6.com');
CALL sp_AgregarProveedor(7, 'NIT7', 'Nombre7', 'Apellido7', 'Dirección7', 'Razón Social7', 'Contacto7', 'www.proveedor7.com');
CALL sp_AgregarProveedor(8, 'NIT8', 'Nombre8', 'Apellido8', 'Dirección8', 'Razón Social8', 'Contacto8', 'www.proveedor8.com');
CALL sp_AgregarProveedor(9, 'NIT9', 'Nombre9', 'Apellido9', 'Dirección9', 'Razón Social9', 'Contacto9', 'www.proveedor9.com');
CALL sp_AgregarProveedor(110, 'NIT10', 'Nombre10', 'Apellido10', 'Dirección10', 'Razón Social10', 'Contacto10', 'www.proveedor10.com');
CALL sp_AgregarProveedor(11, 'NIT11', 'Nombre11', 'Apellido11', 'Dirección11', 'Razón Social11', 'Contacto11', 'www.proveedor11.com');

DELIMITER $$

CREATE PROCEDURE sp_ListarProveedor()
BEGIN
    SELECT
        C.codigoProveedor,
        C.NitProveedor,
        C.nombresProveedor,
        C.apellidosProveedor,
        C.direccionProveedor,
        C.razonSocial,
        C.contactoPrincipal,
        C.paginaWeb
    FROM
        Proveedores C;
END $$

DELIMITER ;

CALL sp_ListarProveedor();

DELIMITER $$

CREATE PROCEDURE sp_editarProveedor (
    IN codigoProveedor INT,
    IN NitProveedor VARCHAR(10),
    IN nombresProveedor VARCHAR(60),
    IN apellidosProveedor VARCHAR(60),
    IN direccionProveedor VARCHAR(150),
    IN razonSocial VARCHAR(60),
    IN contactoPrincipal VARCHAR(100),
    IN paginaWeb VARCHAR(50)
)
BEGIN
    UPDATE Proveedores SET
        NitProveedor = NitProveedor,
        Proveedores.nombresProveedor = nombresProveedor,
        Proveedores.apellidosProveedor = apellidosProveedor,
        Proveedores.direccionProveedor = direccionProveedor,
        Proveedores.razonSocial = razonSocial,
        Proveedores.contactoPrincipal = contactoPrincipal,
        Proveedores.paginaWeb = paginaWeb
    WHERE
        Proveedores.codigoProveedor = codigoProveedor;
END $$

DELIMITER ;

CALL sp_AgregarProveedor(10, 'NIT10', 'Nombre10', 'Apellido10', 'Dirección10', 'Razón Social10', 'Contacto10', 'www.proveedor10.com');

DELIMITER $$

CREATE PROCEDURE sp_eliminarProveedor (codigoProveedor INT)
BEGIN
    DELETE FROM Proveedores WHERE codigoProveedor = codigoProveedor;
END $$

DELIMITER ;

DELIMITER $$

CREATE PROCEDURE sp_AgregarCompra (
    IN _numeroDocumento INT,
    IN _fechaDocumento DATE,
    IN _descripcion VARCHAR(60),
    IN _totalDocumento DECIMAL(10,2)
)
BEGIN
    INSERT INTO Compras(numeroDocumento, fechaDocumento, descripcion, totalDocumento)
    VALUES (_numeroDocumento, _fechaDocumento, _descripcion, _totalDocumento);
END $$

DELIMITER ;

CALL sp_AgregarCompra(3, '2024-05-07', 'Compra de materiales de construcción', 800.00);
CALL sp_AgregarCompra(4, '2024-05-06', 'Compra de mobiliario de oficina', 1200.00);
CALL sp_AgregarCompra(5, '2024-05-05', 'Compra de material informático', 1000.00);
CALL sp_AgregarCompra(6, '2024-05-04', 'Compra de suministros de limpieza', 600.00);
CALL sp_AgregarCompra(7, '2024-05-03', 'Compra de herramientas de trabajo', 1500.00);
CALL sp_AgregarCompra(8, '2024-05-02', 'Compra de equipos de seguridad', 2000.00);
CALL sp_AgregarCompra(9, '2024-05-01', 'Compra de materia prima', 700.00);

DELIMITER $$

CREATE PROCEDURE sp_ListarCompras()
BEGIN
    SELECT
        C.numeroDocumento,
        C.fechaDocumento,
        C.descripcion,
        C.totalDocumento
    FROM
        Compras C;
END $$

DELIMITER ;

CALL sp_ListarCompras();

DELIMITER $$

CREATE PROCEDURE sp_EditarCompra (
    IN _numeroDocumento INT,
    IN _fechaDocumento DATE,
    IN _descripcion VARCHAR(60),
    IN _totalDocumento DECIMAL(10,2)
)
BEGIN
    UPDATE Compras SET
        fechaDocumento = _fechaDocumento,
        descripcion = _descripcion,
        totalDocumento = _totalDocumento
    WHERE
        numeroDocumento = _numeroDocumento;
END $$

DELIMITER ;

CALL sp_EditarCompra(1, '2024-05-10', 'Compra de materiales actualizada', 600.00);

DELIMITER $$

CREATE PROCEDURE sp_EliminarCompra (_numeroDocumento INT)
BEGIN
    DELETE FROM Compras WHERE numeroDocumento = _numeroDocumento;
END $$

DELIMITER ;

CALL sp_EliminarCompra(2);

DELIMITER $$

CREATE PROCEDURE sp_AgregarTipoProducto (
    IN _codigoTipoProducto INT,
    IN _descripcion VARCHAR(45)
)
BEGIN
    INSERT INTO TipoProducto(codigoTipoProducto, descripcion)
    VALUES (_codigoTipoProducto, _descripcion);
END $$

DELIMITER ;

DELIMITER $$

CALL sp_AgregarTipoProducto(3, 'Ropa');
CALL sp_AgregarTipoProducto(4, 'Calzado');
CALL sp_AgregarTipoProducto(5, 'Electrónica');
CALL sp_AgregarTipoProducto(6, 'Hogar');
CALL sp_AgregarTipoProducto(7, 'Juguetes');
CALL sp_AgregarTipoProducto(8, 'Alimentos');
CALL sp_AgregarTipoProducto(9, 'Bebidas');
DELIMITER $$

CREATE PROCEDURE sp_ListarTipoProducto()
BEGIN
    SELECT
        T.codigoTipoProducto,
        T.descripcion
    FROM
        TipoProducto T;
END $$

DELIMITER ;

CALL sp_ListarTipoProducto();

DELIMITER $$

CREATE PROCEDURE sp_EditarTipoProducto (
    IN _codigoTipoProducto INT,
    IN _descripcion VARCHAR(45)
)
BEGIN
    UPDATE TipoProducto SET
        descripcion = _descripcion
    WHERE
        codigoTipoProducto = _codigoTipoProducto;
END $$

DELIMITER ;

CALL sp_EditarTipoProducto(1, 'Electrodomésticos actualizados');

DELIMITER $$

CREATE PROCEDURE sp_EliminarTipoProducto (_codigoTipoProducto INT)
BEGIN
    DELETE FROM TipoProducto WHERE codigoTipoProducto = _codigoTipoProducto;
END $$

DELIMITER ;

CALL sp_EliminarTipoProducto(2);

DELIMITER $$

CREATE PROCEDURE sp_AgregarCargoEmpleado (
    IN _codigoCargoEmpleado INT,
    IN _nombreCargo VARCHAR(50),
    IN _descripcionCargo VARCHAR(45)
)
BEGIN
    INSERT INTO CargoDeEmpleado(codigoCargoEmpleado, nombreCargo, descripcionCargo)
    VALUES (_codigoCargoEmpleado, _nombreCargo, _descripcionCargo);
END $$

DELIMITER ;

CALL sp_AgregarCargoEmpleado(3, 'Contador', 'Encargado de contabilidad');
CALL sp_AgregarCargoEmpleado(4, 'Recepcionista', 'Encargado de atención al cliente');
CALL sp_AgregarCargoEmpleado(5, 'Técnico', 'Encargado de mantenimiento');
CALL sp_AgregarCargoEmpleado(6, 'Chofer', 'Encargado de transporte');
CALL sp_AgregarCargoEmpleado(7, 'Auxiliar', 'Encargado de apoyo administrativo');
CALL sp_AgregarCargoEmpleado(8, 'Supervisor', 'Encargado de supervisar operaciones');
CALL sp_AgregarCargoEmpleado(9, 'Analista', 'Encargado de análisis de datos');

DELIMITER $$

CREATE PROCEDURE sp_ListarCargoEmpleado()
BEGIN
    SELECT
        CE.codigoCargoEmpleado,
        CE.nombreCargo,
        CE.descripcionCargo
    FROM
        CargoDeEmpleado CE;
END $$

DELIMITER ;

CALL sp_ListarCargoEmpleado();

DELIMITER $$

CREATE PROCEDURE sp_EditarCargoEmpleado (
    IN _codigoCargoEmpleado INT,
    IN _nombreCargo VARCHAR(50),
    IN _descripcionCargo VARCHAR(45)
)
BEGIN
    UPDATE CargoDeEmpleado SET
        nombreCargo = _nombreCargo,
        descripcionCargo = _descripcionCargo
    WHERE
        codigoCargoEmpleado = _codigoCargoEmpleado;
END $$

DELIMITER ;

CALL sp_EditarCargoEmpleado(1, 'Gerente de ventas', 'Encargado de supervisar las ventas');

DELIMITER $$

CREATE PROCEDURE sp_EliminarCargoEmpleado (_codigoCargoEmpleado INT)
BEGIN
    DELETE FROM CargoDeEmpleado WHERE codigoCargoEmpleado = _codigoCargoEmpleado;
END $$

DELIMITER ;

CALL sp_EliminarCargoEmpleado(2);

DELIMITER ;