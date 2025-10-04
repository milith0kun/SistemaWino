# ✅ VERIFICACIÓN DE CONEXIÓN APK ↔ BACKEND

## 📊 RESUMEN DE ENDPOINTS

### ✅ FORMULARIOS HACCP - ESTADO DE CONEXIÓN

| # | Formulario | Endpoint APK | Endpoint Backend | Estado |
|---|------------|--------------|------------------|--------|
| 1 | **Recepción Mercadería** | `POST /haccp/recepcion-mercaderia` | `POST /haccp/recepcion-mercaderia` | ✅ CONECTADO |
| 2 | **Recepción Abarrotes** | `POST /haccp/recepcion-abarrotes` | `POST /haccp/recepcion-abarrotes` | ✅ CONECTADO |
| 3 | **Control Cocción** | `POST /haccp/control-coccion` | `POST /haccp/control-coccion` | ✅ CONECTADO |
| 4 | **Lavado Frutas** | `POST /haccp/lavado-frutas` | `POST /haccp/lavado-frutas` | ✅ CONECTADO |
| 5 | **Lavado Manos** | `POST /haccp/lavado-manos` | `POST /haccp/lavado-manos` | ✅ CONECTADO |
| 6 | **Temperatura Cámaras** | `POST /haccp/temperatura-camaras` | `POST /haccp/temperatura-camaras` | ✅ CONECTADO |

### ✅ OTROS ENDPOINTS

| Categoría | Endpoint APK | Endpoint Backend | Estado |
|-----------|--------------|------------------|--------|
| **Autenticación** | `POST /auth/login` | `POST /api/auth/login` | ⚠️ REVISAR |
| **Verificar Token** | `GET /auth/verify` | `GET /api/auth/verify` | ⚠️ REVISAR |
| **Fichado Entrada** | `POST /fichado/entrada` | `POST /api/fichado/entrada` | ⚠️ REVISAR |
| **Fichado Salida** | `POST /fichado/salida` | `POST /api/fichado/salida` | ⚠️ REVISAR |
| **Dashboard Hoy** | `GET /dashboard/hoy` | `GET /api/dashboard/hoy` | ⚠️ REVISAR |
| **Health Check** | `GET /health` | `GET /health` | ✅ CONECTADO |

---

## ⚠️ PROBLEMA DETECTADO

### **Prefijo `/api` faltante en el APK**

**Backend** (servidor.js):
```javascript
app.use('/api/auth', require('./routes/auth'));
app.use('/api/fichado', require('./routes/fichado'));
app.use('/api/dashboard', require('./routes/dashboard'));
app.use('/api/haccp', require('./routes/haccp'));
```

**APK** (ApiService.kt):
```kotlin
@POST("auth/login")  // ❌ Falta /api
@POST("fichado/entrada")  // ❌ Falta /api
@POST("haccp/lavado-manos")  // ❌ Falta /api
```

### 🔧 SOLUCIÓN

El APK debe incluir el prefijo `/api` en todas las rutas, O el `baseUrl` de Retrofit debe incluirlo.

**Opción 1: Actualizar baseUrl en NetworkModule** (RECOMENDADO)
```kotlin
// Antes:
baseUrl = "http://ec2-18-188-209-94.us-east-2.compute.amazonaws.com/"

// Después:
baseUrl = "http://ec2-18-188-209-94.us-east-2.compute.amazonaws.com/api/"
```

**Opción 2: Agregar /api a cada endpoint**
```kotlin
@POST("api/auth/login")
@POST("api/fichado/entrada")
// etc...
```

---

## 📋 ESTADO ACTUAL

### ✅ LO QUE FUNCIONA:
- Health check (`/health`)
- Posiblemente los formularios HACCP si se probaron antes del cambio de `/api`

### ❌ LO QUE PUEDE NO FUNCIONAR:
- Login
- Fichado
- Dashboard
- Todos los endpoints que requieren autenticación

---

## 🎯 ACCIÓN REQUERIDA

1. **Actualizar baseUrl en NetworkModule.kt**
2. **Recompilar APK**
3. **Probar todos los formularios**

---

## 📝 ARCHIVOS A MODIFICAR

### 1️⃣ `NetworkModule.kt`
```kotlin
Buscar: baseUrl = ProductionConfig.getServerUrl()
Cambiar a: baseUrl = ProductionConfig.getServerUrl() + "api/"
```

O actualizar `ProductionConfig.kt`:
```kotlin
fun getServerUrl(): String {
    val protocol = if (USE_HTTPS) "https" else "http"
    val url = if (SERVER_PORT.isNotBlank()) {
        "$protocol://$SERVER_IP:$SERVER_PORT/"
    } else {
        "$protocol://$SERVER_IP/"
    }
    return url + "api/"  // Agregar esto
}
```

---

## ✅ DESPUÉS DE LA CORRECCIÓN

Todos los endpoints quedarán:
- APK: `baseUrl + "auth/login"` = `http://servidor/api/auth/login` ✅
- Backend: `/api/auth/login` ✅

**MATCH PERFECTO** ✅

---

## 🧪 PRUEBAS POST-CORRECCIÓN

1. ✅ Login
2. ✅ Fichado entrada
3. ✅ Fichado salida
4. ✅ Dashboard
5. ✅ Recepción Mercadería
6. ✅ Control Cocción
7. ✅ Lavado Frutas
8. ✅ **Lavado Manos** (recién corregido)
9. ✅ Temperatura Cámaras

---

**CONCLUSIÓN**: Necesitas agregar `/api/` al baseUrl para que todo funcione correctamente.
