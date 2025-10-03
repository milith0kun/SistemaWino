# 🎉 SISTEMA WINO - CONFIGURACIÓN COMPLETA Y VERIFICADA

## ✅ **Estado Actual del Sistema**

### 📅 Fecha: 3 de octubre de 2025
### 🚀 Estado: **100% OPERATIVO**

---

## 🌐 **ARQUITECTURA COMPLETA DEL SISTEMA**

```
┌─────────────────────────────────────────────────────────────────┐
│                    APLICACIÓN MÓVIL ANDROID                      │
│                                                                  │
│  URL: http://ec2-18-188-209-94.us-east-2.compute.amazonaws.com/api/
│  Estado: ✅ Conectada y funcionando                             │
│  Ubicación: Cusco, Perú (-13.505883, -72.0098174)              │
└──────────────────────────┬──────────────────────────────────────┘
                           │
                           │ HTTP Requests
                           │
┌──────────────────────────▼──────────────────────────────────────┐
│                      PANEL WEB (WebPanel)                        │
│                                                                  │
│  URL: http://ec2-18-188-209-94.us-east-2.compute.amazonaws.com │
│  API: /api (proxy a Backend)                                    │
│  Estado: ✅ Funcionando                                         │
└──────────────────────────┬──────────────────────────────────────┘
                           │
                           │ HTTP Requests
                           │
┌──────────────────────────▼──────────────────────────────────────┐
│              AWS EC2: ec2-18-188-209-94                         │
│              (SemiVpsWebPanel)                                   │
│                                                                  │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │                 Nginx (Puerto 80)                          │ │
│  │                                                             │ │
│  │  /        → WebPanel (archivos estáticos)                 │ │
│  │  /api/*  → Backend (localhost:3000)                       │ │
│  │             rewrite: quita /api antes de proxy             │ │
│  └──────────────────────────────┬──────────────────────────────┘ │
│                                 │                                │
│  ┌──────────────────────────────▼─────────────────────────────┐ │
│  │       Backend Node.js + Express                            │ │
│  │       localhost:3000 (PM2: wino-backend)                   │ │
│  │                                                             │ │
│  │  Coordenadas GPS Cocina:                                   │ │
│  │    Latitud:  -13.505883 (Cusco, Perú)                     │ │
│  │    Longitud: -72.0098174                                   │ │
│  │    Radio:    100 metros                                    │ │
│  │                                                             │ │
│  │  ┌────────────┐  ┌────────────┐  ┌────────────┐          │ │
│  │  │ /auth/*    │  │ /dashboard │  │ /fichado/* │          │ │
│  │  │ /haccp/*   │  │ /tiempo-   │  │            │          │ │
│  │  │            │  │  real      │  │            │          │ │
│  │  └────────────┘  └────────────┘  └────────────┘          │ │
│  │                         │                                  │ │
│  │                         ▼                                  │ │
│  │           ┌─────────────────────────┐                     │ │
│  │           │  SQLite Database        │                     │ │
│  │           │  database.db            │                     │ │
│  │           │                         │                     │ │
│  │           │  - usuarios             │                     │ │
│  │           │  - asistencia           │                     │ │
│  │           │  - codigos_qr_locales   │                     │ │
│  │           └─────────────────────────┘                     │ │
│  └───────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────┘
```

---

## 📱 **APLICACIÓN MÓVIL**

### URLs Configuradas

#### NetworkConfig.kt
```kotlin
val ENVIRONMENTS = mapOf(
    "aws_production" to "http://ec2-18-188-209-94.us-east-2.compute.amazonaws.com/api/",
    "emulator" to "http://10.0.2.2:3000/"
)
const val DEFAULT_BASE_URL = "http://ec2-18-188-209-94.us-east-2.compute.amazonaws.com/api/"
```

#### NetworkModule.kt
```kotlin
private const val DEFAULT_BASE_URL = "http://ec2-18-188-209-94.us-east-2.compute.amazonaws.com/api/"
```

#### AutoNetworkDetector.kt
```kotlin
private const val AWS_PRODUCTION_URL = "http://ec2-18-188-209-94.us-east-2.compute.amazonaws.com/api/"
```

#### ProductionConfig.kt
```kotlin
const val SERVER_IP = "ec2-18-188-209-94.us-east-2.compute.amazonaws.com"
const val USE_HTTPS = false  // Puerto 80 HTTP
```

### Estado de Conexión
- ✅ Login exitoso
- ✅ Token JWT funcionando
- ✅ Dashboard cargando datos
- ✅ Historial accesible
- ✅ GPS detectando ubicación

---

## 🌐 **PANEL WEB (WebPanel)**

### Configuración

#### .env
```properties
VITE_API_URL=/api
VITE_APP_TITLE=Sistema de Calidad HACCP
```

#### src/services/api.js
```javascript
const API_URL = import.meta.env.VITE_API_URL || '/api';
// Axios configurado con interceptors para JWT
```

### Estado
- ✅ Accesible en http://ec2-18-188-209-94.us-east-2.compute.amazonaws.com
- ✅ Login funcionando
- ✅ Dashboard cargando
- ✅ Endpoints respondiendo correctamente

---

## 🖥️ **BACKEND (Node.js + Express)**

### Configuración GPS (Backend/.env)

```properties
# CONFIGURACIÓN GPS (Cusco, Perú - Para pruebas)
KITCHEN_LATITUDE=-13.505883
KITCHEN_LONGITUDE=-72.0098174
GPS_RADIUS_METERS=100
```

### Configuración del Servidor

```properties
EXTERNAL_PORT=3000
HOST=127.0.0.1
NODE_ENV=production

JWT_SECRET=haccp-secret-key-auto-generated-2024
TOKEN_EXPIRATION=24h

DB_PATH=/home/ubuntu/SistemaWino/Backend/database/database.db
LOG_DIRECTORY=/home/ubuntu/SistemaWino/Backend/logs

CORS_ORIGIN=*
```

### Endpoints Principales

| Endpoint | Método | Descripción | Estado |
|----------|--------|-------------|--------|
| `/auth/login` | POST | Autenticación | ✅ |
| `/auth/verify` | GET | Verificar token | ✅ |
| `/dashboard/hoy` | GET | Dashboard del día | ✅ |
| `/dashboard/admin` | GET | Dashboard admin | ✅ |
| `/fichado/entrada` | POST | Registrar entrada | ✅ |
| `/fichado/salida` | POST | Registrar salida | ✅ |
| `/fichado/historial` | GET | Historial de fichajes | ✅ |
| `/haccp/*` | GET/POST | Formularios HACCP | ✅ |

### Estado
- ✅ Corriendo en PM2 (proceso: wino-backend)
- ✅ Puerto 3000 (localhost only)
- ✅ Base de datos SQLite operativa
- ✅ Validación GPS configurada para Cusco

---

## 🔐 **CREDENCIALES DE ACCESO**

### Usuario Administrador
```
Email:    admin@hotel.com
Password: admin123
Rol:      ADMIN
Nombre:   Administrador Sistema
```

### Usuario Empleado (Pruebas)
```
Email:    empleado@hotel.com
Password: empleado123
Rol:      EMPLEADO
Nombre:   Empleado Prueba
```

---

## 📍 **CONFIGURACIÓN GPS**

### Coordenadas de la Cocina (Actualizadas)
```
Latitud:  -13.505883
Longitud: -72.0098174
Ubicación: Cusco, Perú
Radio:    100 metros
```

### Validación GPS
- ✅ Calcula distancia usando fórmula de Haversine
- ✅ Permite fichado solo dentro de 100m del punto configurado
- ✅ Rechaza fichados fuera de rango con mensaje descriptivo
- ✅ Incluye coordenadas del usuario y cocina en respuesta

---

## 🗄️ **BASE DE DATOS**

### Ubicación
```
/home/ubuntu/SistemaWino/Backend/database/database.db
```

### Tablas Principales

#### usuarios
```sql
- id: INTEGER PRIMARY KEY
- nombre: TEXT
- apellido: TEXT
- email: TEXT UNIQUE
- password: TEXT (hashed con bcrypt)
- rol: TEXT ('ADMIN', 'EMPLEADO')
- cargo: TEXT
- area: TEXT
- activo: BOOLEAN
- fecha_creacion: DATETIME
```

#### asistencia
```sql
- id: INTEGER PRIMARY KEY
- usuario_id: INTEGER
- fecha: DATE
- hora_entrada: TIME
- hora_salida: TIME
- latitud: REAL
- longitud: REAL
- latitud_salida: REAL
- longitud_salida: REAL
- ubicacion_valida: BOOLEAN
- codigo_qr: TEXT
- metodo_fichado: TEXT ('MANUAL', 'GPS', 'QR')
- observaciones: TEXT
- timestamp_creacion: DATETIME
```

---

## 🚀 **COMANDOS ÚTILES**

### Gestión del Servidor

```bash
# SSH al servidor
ssh -i wino.pem ubuntu@ec2-18-188-209-94.us-east-2.compute.amazonaws.com

# Estado del backend
pm2 status

# Ver logs en tiempo real
pm2 logs wino-backend

# Ver últimos errores
pm2 logs wino-backend --err --lines 50 --nostream

# Reiniciar backend
pm2 restart wino-backend

# Reiniciar con nuevas variables de entorno
pm2 restart wino-backend --update-env

# Actualizar código desde GitHub
cd ~/SistemaWino && git pull && pm2 restart wino-backend --update-env
```

### Gestión de la Base de Datos

```bash
# Ver usuarios
sqlite3 ~/SistemaWino/Backend/database/database.db \
  "SELECT id, nombre, apellido, email, rol FROM usuarios;"

# Ver fichajes de hoy
sqlite3 ~/SistemaWino/Backend/database/database.db \
  "SELECT * FROM asistencia WHERE fecha = date('now');"

# Backup de base de datos
cp ~/SistemaWino/Backend/database/database.db \
   ~/SistemaWino/Backend/database/database.db.backup-$(date +%Y%m%d)
```

### Nginx

```bash
# Estado de Nginx
sudo systemctl status nginx

# Reiniciar Nginx
sudo systemctl restart nginx

# Ver logs de error
sudo tail -f /var/log/nginx/error.log
```

---

## 📲 **INSTALACIÓN DE LA APP**

### Desde el Computador

```bash
# Verificar dispositivo conectado
adb devices

# Compilar APK
cd "Sistema de Calidad"
.\gradlew assembleDebug

# Instalar en dispositivo
adb install -r app/build/outputs/apk/debug/app-debug.apk

# Abrir la app
adb shell am start -n com.example.sistemadecalidad/.MainActivity

# Ver logs de la app
adb logcat | Select-String "sistemadecalidad|FichadoViewModel|GPS"
```

---

## ✅ **VERIFICACIÓN COMPLETA**

### Lista de Chequeo

- [x] **Servidor AWS** corriendo y accesible
- [x] **Backend** funcionando en PM2
- [x] **Nginx** configurado correctamente
- [x] **Base de datos** con esquema completo
- [x] **WebPanel** desplegado y funcionando
- [x] **App Móvil** compilada e instalada
- [x] **URLs** actualizadas en todos los componentes
- [x] **Coordenadas GPS** configuradas para ubicación actual
- [x] **Autenticación** funcionando en app y WebPanel
- [x] **Dashboard** cargando datos correctamente
- [x] **Validación GPS** operativa

### Flujo de Fichado Verificado

1. ✅ Usuario abre la app
2. ✅ Inicia sesión → Obtiene token JWT
3. ✅ Dashboard carga estado de fichado del día
4. ✅ GPS detecta ubicación del usuario
5. ✅ Usuario presiona "MARCAR ENTRADA"
6. ✅ App envía coordenadas al backend
7. ✅ Backend valida si está dentro del rango (100m)
   - ✅ **Dentro del rango**: Registra fichado exitoso
   - ✅ **Fuera del rango**: Rechaza con mensaje descriptivo
8. ✅ App muestra resultado al usuario

---

## 🎯 **PRÓXIMOS PASOS**

### Para Pruebas en Producción

1. **Cambiar coordenadas GPS** a la ubicación real del restaurante/hotel:
   ```bash
   # Editar Backend/.env en el servidor
   KITCHEN_LATITUDE=-12.xxxx  # Coordenadas reales
   KITCHEN_LONGITUDE=-77.xxxx
   ```

2. **Ajustar radio GPS** si es necesario:
   ```bash
   GPS_RADIUS_METERS=100  # Cambiar según necesidad
   ```

3. **Crear usuarios reales** para los empleados

4. **Configurar HTTPS** (opcional pero recomendado):
   - Obtener certificado SSL (Let's Encrypt)
   - Configurar Nginx para HTTPS
   - Actualizar URLs en la app

---

## 📞 **INFORMACIÓN DE SOPORTE**

### Enlaces Importantes
- **WebPanel**: http://ec2-18-188-209-94.us-east-2.compute.amazonaws.com
- **API**: http://ec2-18-188-209-94.us-east-2.compute.amazonaws.com/api/
- **GitHub**: https://github.com/milith0kun/SistemaWino.git

### Servidor AWS
- **IP**: 18.188.209.94
- **Host**: ec2-18-188-209-94.us-east-2.compute.amazonaws.com
- **Región**: us-east-2 (Ohio)
- **Puerto HTTP**: 80

### Contacto Técnico
- **Usuario SSH**: ubuntu
- **Key**: wino.pem

---

**Última Actualización**: 2025-10-03  
**Versión**: 1.0.0  
**Estado**: ✅ Sistema 100% Operativo y Listo para Uso
