// Configuración simplificada para el servidor HACCP

const config = {
  server: {
    port: process.env.PORT || 3000,
    host: process.env.HOST || '0.0.0.0',
    publicIP: process.env.PUBLIC_IP || null
  },
  database: {
    path: process.env.DB_PATH || './database/database.db',
    directory: process.env.DB_DIRECTORY || './database'
  },
  jwt: {
    secret: process.env.JWT_SECRET || 'haccp-wino-secret-key-2024',
    expiresIn: process.env.JWT_EXPIRES_IN || '24h'
  },
  gps: {
    enabled: process.env.GPS_VALIDATION_ENABLED === 'true',
    maxDistance: parseInt(process.env.GPS_MAX_DISTANCE) || 100,
    latitude: parseFloat(process.env.LOCATION_LATITUDE) || null,
    longitude: parseFloat(process.env.LOCATION_LONGITUDE) || null
  }
};

const displayConfig = () => {
  console.log('\n🔧 Configuración del Servidor:');
  console.log('================================');
  console.log(`Puerto: ${config.server.port}`);
  console.log(`Host: ${config.server.host}`);
  console.log(`Base de datos: ${config.database.path}`);
  console.log(`JWT expiración: ${config.jwt.expiresIn}`);
  console.log(`Validación GPS: ${config.gps.enabled ? 'Activada' : 'Desactivada'}`);
  if (config.gps.enabled) {
    console.log(`  - Distancia máxima: ${config.gps.maxDistance}m`);
    console.log(`  - Ubicación: ${config.gps.latitude}, ${config.gps.longitude}`);
  }
  console.log('================================\n');
};

module.exports = { config, displayConfig };
