-- =========================
-- USUARIOS
-- =========================
INSERT INTO usuarios (id, nombre, apellido, correo, password, rol)
VALUES
    (1, 'Juan', 'Pérez', 'juan.perez@example.com', '1234', 'ADMIN'),
    (2, 'Ana', 'Gómez', 'ana.gomez@example.com', '5678', 'MESERO'),
    (3, 'Carlos', 'López', 'carlos.lopez@example.com', 'abcd', 'COCINERO'),
    (4, 'María', 'Rojas', 'maria.rojas@example.com', 'efgh', 'MESERO'),
    (5, 'Luis', 'Martínez', 'luis.martinez@example.com', 'ijkl', 'ADMIN');

-- =========================
-- MESAS
-- =========================
INSERT INTO mesas (id, capacidad, estado, mesero_id)
VALUES
    (1, 4, 'LIBRE', null),
    (2, 2, 'OCUPADA', 4),
    (3, 6, 'OCUPADA', 2),
    (4, 4, 'LIBRE', null),
    (5, 8, 'OCUPADA', 2);

-- =========================
-- PRODUCTOS
-- =========================
INSERT INTO productos (id, nombre, descripcion, precio, disponible, tipo)
VALUES
    (1, 'Pizza Margarita', 'Pizza con queso y tomate', 25000, true, 'PLATO_FUERTE'),
    (2, 'Hamburguesa', 'Carne con pan artesanal', 18000, true, 'PLATO_FUERTE'),
    (3, 'Limonada Natural', 'Bebida refrescante', 7000, true, 'BEBIDA'),
    (4, 'Flan de Vainilla', 'Postre casero', 8000, true, 'POSTRE'),
    (5, 'Papas Fritas', 'Entrada crujiente', 9000, true, 'ENTRADA');

-- =========================
-- PEDIDOS
-- =========================
INSERT INTO pedidos (id, mesa_id, fecha_hora, estado, total)
VALUES
    (1, 2, '2025-10-13 12:00:00', 'REGISTRADO', 32000),
    (2, 3, '2025-10-13 12:30:00', 'EN_PREPARACION', 25000),
    (3, 5, '2025-10-13 13:00:00', 'COMPLETADO', 45000),
    (4, 1, '2025-10-13 13:30:00', 'CANCELADO', 20000),
    (5, 4, '2025-10-13 14:00:00', 'REGISTRADO', 28000);

-- =========================
-- DETALLE PEDIDOS
-- =========================
INSERT INTO pedidos_detallados (id, cantidad, precio_unitario, precio_total, pedido_id, producto_id)
VALUES
    (1, 1, 25000, 25000, 1, 1),
    (2, 1, 7000, 7000, 1, 3),
    (3, 2, 9000, 18000, 2, 5),
    (4, 1, 18000, 18000, 3, 2),
    (5, 1, 8000, 8000, 3, 4);