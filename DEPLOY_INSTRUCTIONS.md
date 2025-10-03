# 📋 Instrucciones de Despliegue - Sistema Wino

## 🏗️ Arquitectura del Sistema

```
┌──────────────────────────────────────────────────────────────┐
│  Servidor: SemiVpsWebPanel (ec2-18-188-209-94)              │
│  IP Pública: 18.188.209.94                                   │
│  Puerto Público: 80                                          │
├──────────────────────────────────────────────────────────────┤
│                                                               │
│  📱 App Móvil → http://18.188.209.94/api                    │
│  🖥️  WebPanel → http://18.188.209.94                        │
│                                                               │
│  ┌────────────────────────────────────────────┐             │
│  │ Nginx (Puerto 80)                           │             │
│  │  ├─→ / → WebPanel (React dist)             │             │
│  │  └─→ /api → Proxy → localhost:3000         │             │
│  └────────────────────────────────────────────┘             │
│                           ↓                                   │
│  ┌────────────────────────────────────────────┐             │
│  │ Backend Node.js (Puerto 3000 local)        │             │
│  │  ├─→ Express + PM2                         │             │
│  │  ├─→ SQLite Database                       │             │
│  │  └─→ CORS habilitado para app móvil       │             │
│  └────────────────────────────────────────────┘             │
│                                                               │
└──────────────────────────────────────────────────────────────┘
```

## 🚀 Pasos de Despliegue

### 1️⃣ Configurar Backend en el Servidor

```bash
# Conectar al servidor
ssh -i "Wino.pem" ubuntu@ec2-18-188-209-94.us-east-2.compute.amazonaws.com

# Ir a la carpeta del Backend
cd ~/SistemaWino/Backend

# Crear archivo .env
cat > .env << 'EOF'
EXTERNAL_PORT=3000
INTERNAL_PORT=3000
HOST=127.0.0.1
NODE_ENV=production

JWT_SECRET=haccp-secret-key-auto-generated-2024
TOKEN_EXPIRATION=24h

KITCHEN_LATITUDE=-12.0464
KITCHEN_LONGITUDE=-77.0428
GPS_RADIUS_METERS=100

DB_PATH=/home/ubuntu/SistemaWino/Backend/database/database.db
DB_DIRECTORY=/home/ubuntu/SistemaWino/Backend/database

LOG_DIRECTORY=/home/ubuntu/SistemaWino/Backend/logs
LOG_LEVEL=info

CORS_ORIGIN=*
EOF

# Verificar que PM2 esté corriendo el Backend
pm2 status

# Si no está corriendo, iniciarlo
pm2 start server.js --name wino-backend

# Guardar configuración de PM2
pm2 save
```

### 2️⃣ Configurar Nginx

```bash
# Verificar configuración de Nginx
sudo cat /etc/nginx/sites-available/wino-webpanel

# La configuración debe tener:
# location /api/ {
#     proxy_pass http://127.0.0.1:3000;
#     ...
# }

# Probar configuración
sudo nginx -t

# Recargar Nginx
sudo systemctl reload nginx
```

### 3️⃣ Configurar WebPanel

```bash
cd ~/SistemaWino/WebPanel

# Crear archivo .env
echo 'VITE_API_URL=/api' > .env
echo 'VITE_APP_TITLE=Sistema de Calidad HACCP' >> .env

# Compilar WebPanel
npm run build

# Recargar Nginx
sudo systemctl reload nginx
```

## 🌐 URLs de Acceso

### Para la Aplicación Móvil:
```
Base URL: http://ec2-18-188-209-94.us-east-2.compute.amazonaws.com/api
o
Base URL: http://18.188.209.94/api

Endpoints:
- Login: POST /api/auth/login
- Dashboard: GET /api/dashboard/hoy
- Fichado: POST /api/fichado/entrada
- etc.
```

### Para el WebPanel (Navegador):
```
URL: http://ec2-18-188-209-94.us-east-2.compute.amazonaws.com
o
URL: http://18.188.209.94
```

## 🔐 Credenciales de Prueba

**Administrador:**
- Email: admin@wino.com
- Password: admin123

**Empleado:**
- Email: empleado@wino.com
- Password: empleado123

## 🛠️ Comandos Útiles

### Ver logs del Backend:
```bash
pm2 logs wino-backend
pm2 logs wino-backend --lines 50
```

### Reiniciar Backend:
```bash
pm2 restart wino-backend
```

### Actualizar desde GitHub:
```bash
# Backend
cd ~/SistemaWino/Backend
git pull
pm2 restart wino-backend

# WebPanel
cd ~/SistemaWino/WebPanel
git pull
npm run build
sudo systemctl reload nginx
```

### Ver estado de servicios:
```bash
# PM2
pm2 status

# Nginx
sudo systemctl status nginx

# Probar endpoint local
curl http://localhost/api/health
```

## 📱 Configuración en la App Móvil

En la aplicación móvil Android, configurar la URL base del API:

```kotlin
// En el archivo de configuración de Retrofit o similar
const val BASE_URL = "http://ec2-18-188-209-94.us-east-2.compute.amazonaws.com/api/"
// o
const val BASE_URL = "http://18.188.209.94/api/"
```

## ⚠️ Notas Importantes

1. **Puerto 80 único abierto**: Todo el tráfico (app móvil y webpanel) pasa por el puerto 80
2. **Backend no accesible directamente**: El Backend en puerto 3000 solo es accesible localmente
3. **Nginx hace proxy**: Nginx redirige `/api/*` al Backend en `localhost:3000`
4. **CORS habilitado**: El Backend acepta peticiones desde cualquier origen
5. **PM2 con autoarranque**: El Backend se reinicia automáticamente al reiniciar el servidor

## 🔍 Troubleshooting

### Error de conexión desde app móvil:
```bash
# Verificar que el Backend esté corriendo
pm2 status

# Verificar logs
pm2 logs wino-backend

# Probar endpoint desde el servidor
curl http://localhost:3000/health
curl http://localhost/api/health
```

### WebPanel no carga:
```bash
# Verificar que Nginx esté corriendo
sudo systemctl status nginx

# Verificar permisos del directorio dist
ls -la ~/SistemaWino/WebPanel/dist

# Recompilar WebPanel
cd ~/SistemaWino/WebPanel
npm run build
sudo systemctl reload nginx
```

### Backend no responde:
```bash
# Ver logs en tiempo real
pm2 logs wino-backend --lines 100

# Reiniciar Backend
pm2 restart wino-backend

# Verificar archivo .env
cat ~/SistemaWino/Backend/.env
```
