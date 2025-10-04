const express = require('express');
const { db } = require('../utils/database');
const { authenticateToken, requireAdmin } = require('../middleware/auth');

const router = express.Router();

// =====================================================
// OBTENER LOGS DE AUDITORÍA
// =====================================================
router.get('/logs', authenticateToken, requireAdmin, async (req, res) => {
    try {
        const { fecha_desde, fecha_hasta, usuario_id, accion, modulo } = req.query;

        let query = `
            SELECT 
                l.id,
                l.fecha,
                l.hora,
                l.usuario_id,
                u.nombre || ' ' || u.apellido as usuario_nombre,
                u.cargo,
                u.area,
                l.accion,
                l.modulo,
                l.detalles,
                l.ip_address
            FROM logs_auditoria l
            LEFT JOIN usuarios u ON l.usuario_id = u.id
            WHERE 1=1
        `;

        const params = [];

        if (fecha_desde) {
            query += ' AND l.fecha >= ?';
            params.push(fecha_desde);
        }

        if (fecha_hasta) {
            query += ' AND l.fecha <= ?';
            params.push(fecha_hasta);
        }

        if (usuario_id) {
            query += ' AND l.usuario_id = ?';
            params.push(usuario_id);
        }

        if (accion) {
            query += ' AND l.accion = ?';
            params.push(accion);
        }

        if (modulo) {
            query += ' AND l.modulo = ?';
            params.push(modulo);
        }

        query += ' ORDER BY l.fecha DESC, l.hora DESC LIMIT 500';

        const logs = await db.all(query, params);

        res.json({
            success: true,
            data: logs,
            total: logs.length
        });

    } catch (error) {
        console.error('Error obteniendo logs de auditoría:', error);
        
        // Si la tabla no existe, crear una respuesta vacía
        if (error.message && error.message.includes('no such table')) {
            return res.json({
                success: true,
                data: [],
                total: 0,
                message: 'La tabla de auditoría aún no ha sido creada'
            });
        }

        res.status(500).json({
            success: false,
            error: 'Error al obtener logs de auditoría'
        });
    }
});

// =====================================================
// REGISTRAR LOG DE AUDITORÍA
// =====================================================
router.post('/log', authenticateToken, async (req, res) => {
    try {
        const { accion, modulo, detalles } = req.body;
        const usuario_id = req.user.id;
        const ip_address = req.ip || req.connection.remoteAddress;

        const fecha = new Date().toISOString().split('T')[0];
        const hora = new Date().toTimeString().split(' ')[0];

        await db.run(`
            INSERT INTO logs_auditoria 
            (fecha, hora, usuario_id, accion, modulo, detalles, ip_address)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        `, [fecha, hora, usuario_id, accion, modulo, detalles, ip_address]);

        res.json({
            success: true,
            message: 'Log registrado exitosamente'
        });

    } catch (error) {
        console.error('Error registrando log:', error);
        
        // No fallar si la tabla no existe
        if (error.message && error.message.includes('no such table')) {
            return res.json({
                success: true,
                message: 'Log no registrado - tabla no creada aún'
            });
        }

        res.status(500).json({
            success: false,
            error: 'Error al registrar log'
        });
    }
});

module.exports = router;
