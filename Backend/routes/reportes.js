const express = require('express');
const { db } = require('../utils/database');
const { authenticateToken, requireAdmin } = require('../middleware/auth');

const router = express.Router();

// =====================================================
// REPORTES DE PROVEEDORES CON NO CONFORMIDADES
// =====================================================
router.get('/proveedores-nc', authenticateToken, requireAdmin, async (req, res) => {
    try {
        const { mes, anio } = req.query;
        
        // Validar parámetros
        if (!mes || !anio) {
            return res.status(400).json({
                success: false,
                error: 'Se requieren los parámetros mes y anio'
            });
        }

        // Consultar proveedores con no conformidades en el mes
        const proveedores = await db.all(`
            SELECT 
                p.id,
                p.nombre_proveedor,
                p.contacto,
                p.telefono,
                COUNT(r.id) as total_entregas,
                SUM(CASE WHEN r.conforme = 0 THEN 1 ELSE 0 END) as entregas_rechazadas,
                GROUP_CONCAT(
                    CASE WHEN r.conforme = 0 
                    THEN r.fecha || ': ' || r.observaciones 
                    ELSE NULL END, 
                    ' | '
                ) as detalles_rechazos
            FROM proveedores p
            LEFT JOIN recepcion_mercaderia r ON p.id = r.proveedor_id
            WHERE strftime('%m', r.fecha) = ? AND strftime('%Y', r.fecha) = ?
            GROUP BY p.id, p.nombre_proveedor, p.contacto, p.telefono
            HAVING entregas_rechazadas > 0
            ORDER BY entregas_rechazadas DESC
        `, [mes.toString().padStart(2, '0'), anio]);

        res.json({
            success: true,
            data: proveedores,
            total: proveedores.length
        });

    } catch (error) {
        console.error('Error obteniendo reporte de proveedores:', error);
        res.status(500).json({
            success: false,
            error: 'Error al obtener reporte de proveedores'
        });
    }
});

// =====================================================
// REPORTES DE NO CONFORMIDADES GENERALES
// =====================================================
router.get('/no-conformidades', authenticateToken, requireAdmin, async (req, res) => {
    try {
        const { mes, anio } = req.query;
        
        if (!mes || !anio) {
            return res.status(400).json({
                success: false,
                error: 'Se requieren los parámetros mes y anio'
            });
        }

        // Obtener todas las no conformidades del mes
        const noConformidades = await db.all(`
            SELECT 
                r.id,
                r.fecha,
                r.hora,
                r.nombre_producto,
                p.nombre_proveedor,
                r.temperatura,
                r.conforme,
                r.observaciones,
                u.nombre || ' ' || u.apellido as responsable
            FROM recepcion_mercaderia r
            LEFT JOIN proveedores p ON r.proveedor_id = p.id
            LEFT JOIN usuarios u ON r.usuario_id = u.id
            WHERE r.conforme = 0
            AND strftime('%m', r.fecha) = ?
            AND strftime('%Y', r.fecha) = ?
            ORDER BY r.fecha DESC, r.hora DESC
        `, [mes.toString().padStart(2, '0'), anio]);

        // Estadísticas
        const estadisticas = {
            total_no_conformidades: noConformidades.length,
            por_tipo: {
                temperatura: noConformidades.filter(nc => nc.observaciones && nc.observaciones.toLowerCase().includes('temperatura')).length,
                calidad: noConformidades.filter(nc => nc.observaciones && (nc.observaciones.toLowerCase().includes('calidad') || nc.observaciones.toLowerCase().includes('aspecto'))).length,
                otros: 0
            }
        };
        estadisticas.por_tipo.otros = estadisticas.total_no_conformidades - estadisticas.por_tipo.temperatura - estadisticas.por_tipo.calidad;

        res.json({
            success: true,
            data: noConformidades,
            estadisticas
        });

    } catch (error) {
        console.error('Error obteniendo no conformidades:', error);
        res.status(500).json({
            success: false,
            error: 'Error al obtener no conformidades'
        });
    }
});

// =====================================================
// REPORTE DE ASISTENCIAS
// =====================================================
router.get('/asistencias', authenticateToken, async (req, res) => {
    try {
        const { fecha_desde, fecha_hasta, usuario_id } = req.query;

        let query = `
            SELECT 
                a.fecha,
                a.hora_entrada,
                a.hora_salida,
                a.horas_trabajadas,
                a.metodo_fichado,
                u.nombre,
                u.apellido,
                u.cargo,
                u.area
            FROM asistencia a
            INNER JOIN usuarios u ON a.usuario_id = u.id
            WHERE 1=1
        `;

        const params = [];

        if (fecha_desde) {
            query += ' AND a.fecha >= ?';
            params.push(fecha_desde);
        }

        if (fecha_hasta) {
            query += ' AND a.fecha <= ?';
            params.push(fecha_hasta);
        }

        if (usuario_id) {
            query += ' AND a.usuario_id = ?';
            params.push(usuario_id);
        }

        query += ' ORDER BY a.fecha DESC, a.hora_entrada DESC';

        const asistencias = await db.all(query, params);

        res.json({
            success: true,
            data: asistencias,
            total: asistencias.length
        });

    } catch (error) {
        console.error('Error obteniendo reporte de asistencias:', error);
        res.status(500).json({
            success: false,
            error: 'Error al obtener reporte de asistencias'
        });
    }
});

module.exports = router;
