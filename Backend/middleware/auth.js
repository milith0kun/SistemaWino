const jwt = require('jsonwebtoken');
const { db } = require('../utils/database');

// Middleware para verificar token JWT
const authenticateToken = async (req, res, next) => {
    const authHeader = req.headers['authorization'];
    const token = authHeader && authHeader.split(' ')[1]; // Bearer TOKEN

    if (!token) {
        return res.status(401).json({
            success: false,
            error: 'Token de acceso requerido',
            message: 'Debe proporcionar un token de autenticación'
        });
    }

    try {
        // Verificar token JWT
        const user = jwt.verify(token, process.env.JWT_SECRET);

        // Verificar que el usuario aún existe y está activo
        const row = await db.get(
            'SELECT id, nombre, apellido, email, rol, cargo, area, activo FROM usuarios WHERE id = ? AND activo = 1',
            [user.id]
        );

        if (!row) {
            return res.status(403).json({
                success: false,
                error: 'Usuario no válido',
                message: 'El usuario no existe o está inactivo'
            });
        }

        // Agregar información completa del usuario al request
        req.user = {
            id: row.id,
            nombre: row.nombre,
            apellido: row.apellido,
            email: row.email,
            rol: row.rol,
            cargo: row.cargo,
            area: row.area
        };
        
        // Alias para compatibilidad
        req.usuario = req.user;

        next();
    } catch (err) {
        console.error('Error en authenticateToken:', err);
        return res.status(403).json({
            success: false,
            error: 'Token inválido',
            message: 'El token proporcionado no es válido o ha expirado'
        });
    }
};

// Middleware para verificar rol de administrador
const requireAdmin = (req, res, next) => {
    if (req.user.rol !== 'ADMIN') {
        return res.status(403).json({
            success: false,
            error: 'Acceso denegado',
            message: 'Se requieren permisos de administrador'
        });
    }
    next();
};

// Middleware opcional de autenticación (no falla si no hay token)
const optionalAuth = (req, res, next) => {
    const authHeader = req.headers['authorization'];
    const token = authHeader && authHeader.split(' ')[1];

    if (!token) {
        req.user = null;
        return next();
    }

    jwt.verify(token, process.env.JWT_SECRET, (err, user) => {
        if (err) {
            req.user = null;
        } else {
            req.user = user;
        }
        next();
    });
};

module.exports = {
    authenticateToken,
    requireAdmin,
    optionalAuth
};