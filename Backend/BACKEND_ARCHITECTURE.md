# 🏨 SISTEMA HACCP - ARQUITECTURA BACKEND COMPLETA

## 📋 ÍNDICE
1. [Resumen Ejecutivo](#resumen-ejecutivo)
2. [Arquitectura del Sistema](#arquitectura-del-sistema)
3. [Base de Datos SQLite](#base-de-datos-sqlite)
4. [API REST - Endpoints Completos](#api-rest---endpoints-completos)
5. [Autenticación y Seguridad](#autenticación-y-seguridad)
6. [Modelos de Datos](#modelos-de-datos)
7. [Lógica de Negocio](#lógica-de-negocio)
8. [Panel Web Administrativo - Especificaciones](#panel-web-administrativo---especificaciones)
9. [Exportación de Datos](#exportación-de-datos)
10. [Deployment](#deployment)

---

## 🎯 RESUMEN EJECUTIVO

### **Sistema Completo de Gestión HACCP para Hotel/Restaurant**

**Stack Tecnológico:**
- **Backend**: Node.js + Express.js
- **Base de Datos**: SQLite3
- **Autenticación**: JWT (JSON Web Tokens)
- **Hosting**: AWS EC2 (Ubuntu)
- **IP Servidor**: `18.220.8.226`
- **Puerto**: `3000`
- **Proceso Manager**: PM2 (proceso: `wino-backend`)

**Funcionalidades Principales:**
1. ✅ **Asistencia y Fichado**: Control de entrada/salida con GPS validation
2. ✅ **Formularios HACCP**: 6 controles de calidad alimentaria
3. ✅ **Dashboard**: Resumen de asistencias y controles
4. ✅ **Historial**: Consulta de registros pasados
5. ✅ **Gestión de Usuarios**: ADMIN, SUPERVISOR, COCINERO, EMPLEADO

---

## 🏗️ ARQUITECTURA DEL SISTEMA

```
┌─────────────────────────────────────────────────────────────┐
│                    APLICACIÓN ANDROID                        │
│              (Cliente Móvil - Kotlin/Compose)                │
└────────────────────┬────────────────────────────────────────┘
                     │ HTTPS
                     │ JWT Token
                     ▼
┌─────────────────────────────────────────────────────────────┐
│                  BACKEND API REST                            │
│              (Node.js + Express.js)                          │
│                                                              │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐   │
│  │  Auth    │  │ Fichado  │  │  HACCP   │  │Dashboard │   │
│  │  Routes  │  │  Routes  │  │  Routes  │  │  Routes  │   │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘   │
│                                                              │
│  ┌──────────────────────────────────────────────────┐      │
│  │          Middleware de Autenticación             │      │
│  │  - JWT Verification                              │      │
│  │  - GPS Validation                                │      │
│  │  - Role-Based Access Control                     │      │
│  └──────────────────────────────────────────────────┘      │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────────┐
│                  BASE DE DATOS SQLite                        │
│                   (database.db)                              │
│                                                              │
│  📊 TABLAS PRINCIPALES (16 tablas):                         │
│  - usuarios                                                  │
│  - asistencia                                                │
│  - control_recepcion_mercaderia                             │
│  - control_coccion                                           │
│  - control_lavado_desinfeccion_frutas                       │
│  - control_lavado_manos                                      │
│  - control_temperatura_camaras                              │
│  - proveedores                                               │
│  - productos                                                 │
│  - camaras_frigorificas                                     │
│  - turnos                                                    │
│  - codigos_qr_locales                                       │
│  - auditoria_logs                                            │
│  + 7 vistas para reportes                                    │
└─────────────────────────────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────────┐
│              PANEL WEB ADMINISTRATIVO                        │
│           (React.js - A DESARROLLAR)                         │
│                                                              │
│  📈 Visualización en Tiempo Real                            │
│  📥 Exportación a Excel                                      │
│  👥 Gestión de Usuarios                                      │
│  📊 Reportes y Analytics                                     │
└─────────────────────────────────────────────────────────────┘
```

---

## 🗄️ BASE DE DATOS SQLITE

### **Archivo**: `BasedeDatos.sql` (adjunto completo)

### **Módulos de la Base de Datos:**

#### **MÓDULO 1: Usuarios y Autenticación**
```sql
CREATE TABLE usuarios (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,  -- bcrypt hashed
    rol VARCHAR(20) CHECK(rol IN ('ADMIN', 'SUPERVISOR', 'COCINERO', 'EMPLEADO')),
    cargo VARCHAR(100),
    area VARCHAR(50) CHECK(area IN ('COCINA', 'SALON', 'ADMINISTRACION', 'ALMACEN')),
    activo BOOLEAN DEFAULT 1,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

**Roles del Sistema:**
- `ADMIN`: Acceso total, gestión de usuarios
- `SUPERVISOR`: Supervisión de controles HACCP, aprobación de registros
- `COCINERO`: Registro de controles de cocción y temperatura
- `EMPLEADO`: Fichado, lavado de manos

#### **MÓDULO 2: Asistencia y Fichado**
```sql
CREATE TABLE asistencia (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    usuario_id INTEGER NOT NULL,
    fecha DATE NOT NULL,
    hora_entrada TIME,
    hora_salida TIME,
    horas_trabajadas DECIMAL(5,2),
    turno_id INTEGER,
    latitud DECIMAL(10,8),
    longitud DECIMAL(11,8),
    ubicacion_valida BOOLEAN DEFAULT 0,
    metodo_fichado VARCHAR(20) CHECK(metodo_fichado IN ('MANUAL', 'GPS', 'QR')),
    estado VARCHAR(20) CHECK(estado IN ('PUNTUAL', 'TARDANZA', 'FALTA')),
    observaciones TEXT
);
```

**Validación GPS:**
- Punto válido guardado en `config-app-universal.js`
- Radio de tolerancia: 100 metros
- Se valida en middleware `requireGPSValidation`

#### **MÓDULO 3: Control de Recepción de Mercadería**
```sql
CREATE TABLE control_recepcion_mercaderia (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    mes INTEGER NOT NULL,
    anio INTEGER NOT NULL,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    
    -- TIPO: 'FRUTAS_VERDURAS' o 'ABARROTES'
    tipo_control VARCHAR(50) NOT NULL,
    
    -- DATOS DE RECEPCIÓN
    proveedor_id INTEGER NOT NULL,
    nombre_proveedor VARCHAR(200),
    producto_id INTEGER NOT NULL,
    nombre_producto VARCHAR(100),
    cantidad_solicitada DECIMAL(10,2),
    peso_unidad_recibido DECIMAL(10,2) NOT NULL,
    unidad_medida VARCHAR(20) NOT NULL,
    
    -- CAMPOS ESPECÍFICOS PARA FRUTAS/VERDURAS
    estado_producto VARCHAR(20) CHECK(estado_producto IN ('EXCELENTE', 'REGULAR', 'PESIMO')),
    conformidad_integridad_producto VARCHAR(20),
    
    -- CAMPOS ESPECÍFICOS PARA ABARROTES
    registro_sanitario_vigente BOOLEAN,
    fecha_vencimiento_producto DATE,
    evaluacion_vencimiento VARCHAR(20),
    conformidad_empaque_primario VARCHAR(20),
    
    -- EVALUACIONES COMUNES (C = CONFORME, NC = NO CONFORME)
    uniforme_completo VARCHAR(2) CHECK(uniforme_completo IN ('C', 'NC')),
    transporte_adecuado VARCHAR(2),
    puntualidad VARCHAR(2),
    
    -- RESPONSABLES
    responsable_registro_id INTEGER NOT NULL,  -- Usuario que llena el formulario
    responsable_registro_nombre VARCHAR(200),
    responsable_supervision_id INTEGER NOT NULL,  -- Supervisor del turno
    responsable_supervision_nombre VARCHAR(200),
    
    -- OBSERVACIONES
    observaciones TEXT,
    accion_correctiva TEXT,
    producto_rechazado BOOLEAN DEFAULT 0
);
```

**⚠️ IMPORTANTE - Diferencia entre responsables:**
- **responsable_registro**: Usuario logueado que llena el formulario (EMPLEADO/COCINERO)
- **responsable_supervision**: Supervisor seleccionado que verifica el control (SUPERVISOR)

#### **MÓDULO 4: Control de Cocción**
```sql
CREATE TABLE control_coccion (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    mes INTEGER, anio INTEGER, dia INTEGER, fecha DATE, hora TIME,
    
    producto_cocinar VARCHAR(50) CHECK(producto_cocinar IN ('POLLO', 'CARNE', 'PESCADO', 'HAMBURGUESA', 'PIZZA')),
    proceso_coccion VARCHAR(1) CHECK(proceso_coccion IN ('H', 'P', 'C')),  -- H:Horno, P:Plancha, C:Cocina
    temperatura_coccion DECIMAL(5,2) NOT NULL,  -- Debe ser > 80°C
    tiempo_coccion_minutos INTEGER NOT NULL,
    conformidad VARCHAR(2) CHECK(conformidad IN ('C', 'NC')),  -- C si temp > 80°C
    accion_correctiva TEXT,
    responsable_id INTEGER NOT NULL,
    responsable_nombre VARCHAR(200)
);
```

#### **MÓDULO 5: Control de Lavado de Frutas/Verduras**
```sql
CREATE TABLE control_lavado_desinfeccion_frutas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    mes INTEGER, anio INTEGER, dia INTEGER, fecha DATE, hora TIME,
    
    producto_quimico VARCHAR(50) NOT NULL,
    concentracion_producto DECIMAL(5,2) NOT NULL,
    nombre_fruta_verdura VARCHAR(100) NOT NULL,
    
    -- CONFORMIDADES (C/NC)
    lavado_agua_potable VARCHAR(2),
    desinfeccion_producto_quimico VARCHAR(2),
    concentracion_correcta VARCHAR(2),
    tiempo_desinfeccion_minutos INTEGER CHECK(tiempo_desinfeccion_minutos BETWEEN 0 AND 10),
    
    acciones_correctivas TEXT,
    supervisor_id INTEGER NOT NULL,
    supervisor_nombre VARCHAR(200)
);
```

#### **MÓDULO 6: Control de Lavado de Manos**
```sql
CREATE TABLE control_lavado_manos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    mes INTEGER, anio INTEGER, fecha DATE, hora TIME,
    
    area_estacion VARCHAR(20) CHECK(area_estacion IN ('COCINA', 'SALON')),
    turno VARCHAR(20) CHECK(turno IN ('MAÑANA', 'TARDE', 'NOCHE')),
    
    empleado_id INTEGER NOT NULL,  -- Empleado que se lava las manos (seleccionado de dropdown)
    nombres_apellidos VARCHAR(200),
    firma TEXT,
    
    procedimiento_correcto VARCHAR(2) CHECK(procedimiento_correcto IN ('C', 'NC')),
    accion_correctiva TEXT,
    
    supervisor_id INTEGER,  -- Supervisor del turno (opcional)
    supervisor_nombre VARCHAR(200)
);
```

**⚠️ CAMBIO IMPORTANTE:**
- El `empleado_id` YA NO es automáticamente el usuario logueado
- Se selecciona manualmente de un dropdown de empleados del área
- Permite que un supervisor registre lavados de varios empleados

#### **MÓDULO 7: Control de Temperatura de Cámaras**
```sql
CREATE TABLE control_temperatura_camaras (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    mes INTEGER, anio INTEGER, dia INTEGER, fecha DATE,
    
    camara_id INTEGER NOT NULL,
    
    -- TURNO MAÑANA (08:00)
    hora_manana TIME DEFAULT '08:00',
    temperatura_manana DECIMAL(5,2),
    responsable_manana_id INTEGER,
    responsable_manana_nombre VARCHAR(200),
    conformidad_manana VARCHAR(2),
    
    -- TURNO TARDE (16:00)
    hora_tarde TIME DEFAULT '16:00',
    temperatura_tarde DECIMAL(5,2),
    responsable_tarde_id INTEGER,
    responsable_tarde_nombre VARCHAR(200),
    conformidad_tarde VARCHAR(2),
    
    acciones_correctivas TEXT,
    supervisor_id INTEGER,
    supervisor_nombre VARCHAR(200),
    
    UNIQUE(camara_id, fecha)  -- Un registro por cámara por día
);

CREATE TABLE camaras_frigorificas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre VARCHAR(50) NOT NULL,
    tipo VARCHAR(20) CHECK(tipo IN ('REFRIGERACION', 'CONGELACION')),
    numero_camara INTEGER NOT NULL,
    temperatura_minima DECIMAL(5,2),  -- REFRIGERACIÓN: 1-4°C, CONGELACIÓN: < -18°C
    temperatura_maxima DECIMAL(5,2),
    ubicacion VARCHAR(100),
    activo BOOLEAN DEFAULT 1
);
```

#### **MÓDULO 8: Auditoría**
```sql
CREATE TABLE auditoria_logs (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    usuario_id INTEGER,
    usuario_nombre VARCHAR(200),
    accion VARCHAR(50) NOT NULL,  -- INSERT, UPDATE, DELETE
    tabla_afectada VARCHAR(50) NOT NULL,
    registro_id INTEGER,
    datos_anteriores TEXT,  -- JSON
    datos_nuevos TEXT,  -- JSON
    ip_address VARCHAR(45),
    timestamp_accion DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

### **Vistas para Reportes:**

La base de datos incluye 7 vistas predefinidas:
1. `vista_asistencia_diaria`
2. `vista_recepciones_frutas_verduras`
3. `vista_recepciones_abarrotes`
4. `vista_control_coccion`
5. `vista_temperatura_camaras`
6. `vista_lavado_manos`
7. `vista_lavado_frutas`
8. `vista_resumen_no_conformidades`

---

## 🌐 API REST - ENDPOINTS COMPLETOS

### **Base URL**: `http://18.220.8.226:3000/api`

### **1. AUTENTICACIÓN** (`/auth`)

#### **POST `/auth/login`**
Autenticación de usuario

**Request Body:**
```json
{
  "email": "admin@hotel.com",
  "password": "password123"
}
```

**Response (200):**
```json
{
  "success": true,
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "usuario": {
    "id": 1,
    "nombre": "Administrador",
    "apellido": "Sistema",
    "email": "admin@hotel.com",
    "rol": "ADMIN",
    "cargo": "Administrador General",
    "area": "ADMINISTRACION"
  }
}
```

#### **GET `/auth/verify`**
Verificar token válido (requiere Bearer token)

**Headers:**
```
Authorization: Bearer <token>
```

**Response (200):**
```json
{
  "valid": true,
  "usuario": { ... }
}
```

#### **POST `/auth/refresh`**
Refrescar token (requiere token válido)

#### **POST `/auth/logout`**
Cerrar sesión

---

### **2. FICHADO/ASISTENCIA** (`/fichado`)

#### **POST `/fichado/entrada`**
Registrar entrada (requiere GPS validation)

**Request Body:**
```json
{
  "latitud": -12.0464,
  "longitud": -77.0428,
  "metodo": "GPS"
}
```

**Response (201):**
```json
{
  "success": true,
  "message": "Entrada registrada correctamente",
  "data": {
    "id": 123,
    "usuario_id": 1,
    "fecha": "2024-03-15",
    "hora_entrada": "08:00:00",
    "ubicacion_valida": true,
    "estado": "PUNTUAL"
  }
}
```

#### **POST `/fichado/salida`**
Registrar salida

**Request Body:**
```json
{
  "latitud": -12.0464,
  "longitud": -77.0428,
  "metodo": "GPS"
}
```

#### **GET `/fichado/historial?mes=3&anio=2024`**
Obtener historial de asistencias

**Response (200):**
```json
{
  "success": true,
  "data": [
    {
      "id": 123,
      "fecha": "2024-03-15",
      "hora_entrada": "08:00:00",
      "hora_salida": "17:00:00",
      "horas_trabajadas": 9.0,
      "estado": "PUNTUAL",
      "turno": "MAÑANA"
    }
  ]
}
```

#### **GET `/fichado/estado-hoy`**
Obtener estado actual del día (si ya fichó entrada/salida)

---

### **3. FORMULARIOS HACCP** (`/haccp`)

#### **POST `/haccp/recepcion-mercaderia`**
Registrar recepción de frutas/verduras

**Request Body:**
```json
{
  "mes": 3,
  "anio": 2024,
  "fecha": "2024-03-15",
  "hora": "10:30",
  "tipo_control": "FRUTAS_VERDURAS",
  "nombre_proveedor": "Distribuidora San Jorge",
  "nombre_producto": "Manzanas",
  "cantidad_solicitada": "50",
  "peso_unidad_recibido": 48.5,
  "unidad_medida": "KG",
  "estado_producto": "EXCELENTE",
  "conformidad_integridad_producto": "EXCELENTE",
  "uniforme_completo": "C",
  "transporte_adecuado": "C",
  "puntualidad": "C",
  "observaciones": "Producto en buen estado",
  "accion_correctiva": null,
  "producto_rechazado": false,
  "supervisor_id": 2
}
```

**Response (201):**
```json
{
  "success": true,
  "message": "Recepción de mercadería registrada correctamente",
  "data": {
    "id": 45,
    "fecha": "2024-03-15"
  }
}
```

#### **POST `/haccp/recepcion-abarrotes`**
Registrar recepción de abarrotes

**Request Body:**
```json
{
  "mes": 3,
  "anio": 2024,
  "fecha": "2024-03-15",
  "hora": "11:00",
  "nombreProveedor": "Abarrotes del Norte",
  "nombreProducto": "Arroz Superior",
  "cantidadSolicitada": "100",
  "registroSanitarioVigente": true,
  "evaluacionVencimiento": "EXCELENTE",
  "conformidadEmpaque": "EXCELENTE",
  "uniformeCompleto": "C",
  "transporteAdecuado": "C",
  "puntualidad": "C",
  "observaciones": null,
  "accionCorrectiva": null,
  "supervisorId": 2
}
```

#### **POST `/haccp/control-coccion`**
Registrar control de cocción

**Request Body:**
```json
{
  "mes": 3,
  "anio": 2024,
  "dia": 15,
  "fecha": "2024-03-15",
  "hora": "13:00",
  "producto_cocinar": "POLLO",
  "proceso_coccion": "H",
  "temperatura_coccion": 85.5,
  "tiempo_coccion_minutos": 45,
  "conformidad": "C",
  "accion_correctiva": null
}
```

#### **POST `/haccp/lavado-frutas`**
Registrar lavado y desinfección de frutas

**Request Body:**
```json
{
  "mes": 3,
  "anio": 2024,
  "dia": 15,
  "fecha": "2024-03-15",
  "hora": "09:00",
  "producto_quimico": "Cloro",
  "concentracion_producto": 200,
  "nombre_fruta_verdura": "Lechugas",
  "lavado_agua_potable": "C",
  "desinfeccion_producto_quimico": "C",
  "concentracion_correcta": "C",
  "tiempo_desinfeccion_minutos": 5,
  "acciones_correctivas": null,
  "supervisor_id": 2
}
```

#### **POST `/haccp/lavado-manos`**
Registrar lavado de manos

**Request Body:**
```json
{
  "mes": 3,
  "anio": 2024,
  "fecha": "2024-03-15",
  "hora": "08:30",
  "area_estacion": "COCINA",
  "turno": "MAÑANA",
  "empleado_id": 4,
  "nombres_apellidos": "Pedro López",
  "procedimiento_correcto": "C",
  "accion_correctiva": null,
  "supervisor_id": 2
}
```

#### **POST `/haccp/temperatura-camaras`**
Registrar temperatura de cámaras

**Request Body:**
```json
{
  "mes": 3,
  "anio": 2024,
  "dia": 15,
  "fecha": "2024-03-15",
  "camara_id": 1,
  "turno": "MAÑANA",
  "temperatura": 2.5,
  "hora": "08:00",
  "conformidad": "C",
  "acciones_correctivas": null,
  "supervisor_id": 2
}
```

#### **GET `/haccp/camaras`**
Obtener lista de cámaras frigoríficas

**Response (200):**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "nombre": "REFRIGERACIÓN 1",
      "tipo": "REFRIGERACION",
      "numero_camara": 1,
      "temperatura_minima": 1.0,
      "temperatura_maxima": 4.0
    },
    {
      "id": 3,
      "nombre": "CONGELACIÓN 1",
      "tipo": "CONGELACION",
      "numero_camara": 1,
      "temperatura_minima": -25.0,
      "temperatura_maxima": -18.0
    }
  ]
}
```

#### **GET `/haccp/empleados?area=COCINA`**
Obtener empleados (filtrado opcional por área)

**Response (200):**
```json
{
  "success": true,
  "data": [
    {
      "id": 3,
      "nombre": "María",
      "apellido": "García",
      "cargo": "Cocinero Principal",
      "area": "COCINA",
      "rol": "COCINERO"
    },
    {
      "id": 4,
      "nombre": "Pedro",
      "apellido": "López",
      "cargo": "Ayudante de Cocina",
      "area": "COCINA",
      "rol": "EMPLEADO"
    }
  ]
}
```

#### **GET `/haccp/supervisores?area=COCINA`**
Obtener supervisores activos (filtrado opcional por área)

---

### **4. DASHBOARD** (`/dashboard`)

#### **GET `/dashboard/hoy`**
Resumen del dashboard para el día de hoy

**Response (200):**
```json
{
  "success": true,
  "data": {
    "fecha": "2024-03-15",
    "estadoFichado": {
      "fichado": true,
      "horaEntrada": "08:00:00",
      "horaSalida": null,
      "estado": "PUNTUAL"
    },
    "resumen": {
      "asistenciaHoy": 45,
      "controlesHaccp": 23,
      "noConformidades": 2
    }
  }
}
```

#### **GET `/dashboard/resumen?mes=3&anio=2024`**
Resumen mensual

#### **GET `/dashboard/admin`**
Dashboard administrativo (solo ADMIN)

---

### **5. TIEMPO REAL** (`/tiempo-real`)

#### **GET `/tiempo-real/ahora`**
Obtener fecha y hora del servidor

**Response (200):**
```json
{
  "success": true,
  "data": {
    "fecha": "2024-03-15",
    "hora": "14:30:00",
    "timestamp": 1710518400000,
    "zonaHoraria": "America/Lima"
  }
}
```

---

## 🔐 AUTENTICACIÓN Y SEGURIDAD

### **JWT (JSON Web Tokens)**

**Secret Key**: Almacenada en variable de entorno `JWT_SECRET`

**Token Structure:**
```json
{
  "id": 1,
  "email": "admin@hotel.com",
  "rol": "ADMIN",
  "area": "ADMINISTRACION",
  "iat": 1710518400,
  "exp": 1710604800
}
```

**Expiración**: 24 horas

### **Middleware de Autenticación**

```javascript
// middleware/auth.js

// Verificar token JWT
const authenticateToken = (req, res, next) => {
  const authHeader = req.headers['authorization'];
  const token = authHeader && authHeader.split(' ')[1];
  
  if (!token) {
    return res.status(401).json({ 
      success: false, 
      error: 'Token no proporcionado' 
    });
  }
  
  jwt.verify(token, process.env.JWT_SECRET, (err, usuario) => {
    if (err) {
      return res.status(403).json({ 
        success: false, 
        error: 'Token inválido o expirado' 
      });
    }
    req.usuario = usuario;
    next();
  });
};

// Requerir rol ADMIN
const requireAdmin = (req, res, next) => {
  if (req.usuario.rol !== 'ADMIN') {
    return res.status(403).json({ 
      success: false, 
      error: 'Acceso denegado. Se requiere rol ADMIN' 
    });
  }
  next();
};

// Validación GPS
const requireGPSValidation = (validar) => {
  return (req, res, next) => {
    if (!validar) return next();
    
    const { latitud, longitud } = req.body;
    const config = require('../config-app-universal');
    
    // Validar coordenadas dentro del radio permitido
    const isValid = validateGPSCoordinates(
      latitud, 
      longitud, 
      config.gpsValidation.latitud, 
      config.gpsValidation.longitud, 
      config.gpsValidation.radioMetros
    );
    
    if (!isValid) {
      return res.status(403).json({ 
        success: false, 
        error: 'Ubicación fuera del rango permitido' 
      });
    }
    next();
  };
};
```

### **Configuración GPS**

Archivo: `config-app-universal.js`

```javascript
module.exports = {
  gpsValidation: {
    enabled: true,
    latitud: -12.0464,
    longitud: -77.0428,
    radioMetros: 100,
    mensaje: "Debes estar en el Hotel para fichar"
  }
};
```

---

## 📦 MODELOS DE DATOS

### **Usuario**
```typescript
interface Usuario {
  id: number;
  nombre: string;
  apellido: string;
  email: string;
  password: string;  // bcrypt hashed
  rol: 'ADMIN' | 'SUPERVISOR' | 'COCINERO' | 'EMPLEADO';
  cargo: string;
  area: 'COCINA' | 'SALON' | 'ADMINISTRACION' | 'ALMACEN';
  activo: boolean;
  fecha_creacion: Date;
}
```

### **Asistencia**
```typescript
interface Asistencia {
  id: number;
  usuario_id: number;
  fecha: Date;
  hora_entrada: Time;
  hora_salida: Time | null;
  horas_trabajadas: number | null;
  turno_id: number;
  latitud: number;
  longitud: number;
  ubicacion_valida: boolean;
  metodo_fichado: 'MANUAL' | 'GPS' | 'QR';
  estado: 'PUNTUAL' | 'TARDANZA' | 'FALTA';
  observaciones: string | null;
}
```

### **Control Recepción Mercadería**
```typescript
interface ControlRecepcionMercaderia {
  id: number;
  mes: number;
  anio: number;
  fecha: Date;
  hora: Time;
  tipo_control: 'FRUTAS_VERDURAS' | 'ABARROTES';
  proveedor_id: number;
  nombre_proveedor: string;
  producto_id: number;
  nombre_producto: string;
  cantidad_solicitada: number | null;
  peso_unidad_recibido: number;
  unidad_medida: string;
  
  // Frutas/Verduras
  estado_producto: 'EXCELENTE' | 'REGULAR' | 'PESIMO' | null;
  conformidad_integridad_producto: 'EXCELENTE' | 'REGULAR' | 'PESIMO' | null;
  
  // Abarrotes
  registro_sanitario_vigente: boolean | null;
  fecha_vencimiento_producto: Date | null;
  evaluacion_vencimiento: 'EXCELENTE' | 'REGULAR' | 'PESIMO' | null;
  conformidad_empaque_primario: 'EXCELENTE' | 'REGULAR' | 'PESIMO' | null;
  
  // Comunes
  uniforme_completo: 'C' | 'NC';
  transporte_adecuado: 'C' | 'NC';
  puntualidad: 'C' | 'NC';
  
  responsable_registro_id: number;
  responsable_registro_nombre: string;
  responsable_supervision_id: number;
  responsable_supervision_nombre: string;
  
  observaciones: string | null;
  accion_correctiva: string | null;
  producto_rechazado: boolean;
}
```

### **Control Cocción**
```typescript
interface ControlCoccion {
  id: number;
  mes: number;
  anio: number;
  dia: number;
  fecha: Date;
  hora: Time;
  producto_cocinar: 'POLLO' | 'CARNE' | 'PESCADO' | 'HAMBURGUESA' | 'PIZZA';
  proceso_coccion: 'H' | 'P' | 'C';  // Horno, Plancha, Cocina
  temperatura_coccion: number;
  tiempo_coccion_minutos: number;
  conformidad: 'C' | 'NC';  // C si temp > 80°C
  accion_correctiva: string | null;
  responsable_id: number;
  responsable_nombre: string;
}
```

### **Control Temperatura Cámaras**
```typescript
interface ControlTemperaturaCamaras {
  id: number;
  mes: number;
  anio: number;
  dia: number;
  fecha: Date;
  camara_id: number;
  
  // Turno Mañana
  hora_manana: Time;
  temperatura_manana: number | null;
  responsable_manana_id: number | null;
  responsable_manana_nombre: string | null;
  conformidad_manana: 'C' | 'NC' | null;
  
  // Turno Tarde
  hora_tarde: Time;
  temperatura_tarde: number | null;
  responsable_tarde_id: number | null;
  responsable_tarde_nombre: string | null;
  conformidad_tarde: 'C' | 'NC' | null;
  
  acciones_correctivas: string | null;
  supervisor_id: number | null;
  supervisor_nombre: string | null;
}
```

---

## 🎨 PANEL WEB ADMINISTRATIVO - ESPECIFICACIONES

### **Objetivo**
Crear un panel web para que el ADMIN pueda:
1. Visualizar todos los datos en tiempo real
2. Exportar reportes a Excel
3. Gestionar usuarios
4. Ver estadísticas y gráficos
5. Auditar registros

### **Tecnologías Recomendadas**
- **Frontend**: React.js + TypeScript
- **UI Library**: Material-UI o Ant Design
- **State Management**: Redux Toolkit o Zustand
- **Charts**: Recharts o Chart.js
- **Export Excel**: xlsx o exceljs
- **HTTP Client**: Axios

### **Estructura de Páginas**

#### **1. Login** (`/login`)
- Formulario de login (email + password)
- Llamar a `POST /auth/login`
- Guardar token en localStorage
- Redirigir a dashboard

#### **2. Dashboard Principal** (`/dashboard`)
**Widgets:**
- Asistencia hoy: Total empleados presentes
- Controles HACCP hoy: Total formularios completados
- No conformidades del mes: Gráfico de barras
- Productos rechazados esta semana: Lista con detalles
- Gráfico de asistencias del mes: Línea temporal

**Datos:**
- GET `/dashboard/admin`
- GET `/dashboard/resumen?mes=X&anio=Y`

#### **3. Asistencias** (`/asistencias`)
**Funcionalidades:**
- Tabla con todas las asistencias
- Filtros: Fecha (desde-hasta), Usuario, Estado (PUNTUAL/TARDANZA/FALTA)
- Botón "Exportar a Excel"
- Detalle de cada asistencia: Hora entrada, salida, ubicación GPS

**Datos:**
- GET `/fichado/historial?mes=X&anio=Y`

**Columnas:**
| Fecha | Usuario | Cargo | Área | Entrada | Salida | Horas | Estado | Ubicación |

#### **4. Formularios HACCP** (`/haccp`)

**Submódulos:**

##### **4.1. Recepción de Mercadería** (`/haccp/recepcion-mercaderia`)
- Tabla con todos los registros
- Filtros: Fecha, Tipo (Frutas/Abarrotes), Proveedor, Producto Rechazado
- Columnas: Fecha, Proveedor, Producto, Cantidad, Conformidad, Rechazado
- Botón "Exportar a Excel"
- Vista detalle: Modal con todos los campos

**Datos:**
- GET `/haccp/recepcion-mercaderia?mes=X&anio=Y`

##### **4.2. Control de Cocción** (`/haccp/control-coccion`)
- Tabla con registros
- Filtros: Fecha, Producto, Conformidad
- Columnas: Fecha, Producto, Proceso, Temperatura, Tiempo, Conformidad
- Highlight en rojo si Conformidad = NC

**Datos:**
- GET `/haccp/control-coccion?mes=X&anio=Y`

##### **4.3. Lavado de Frutas** (`/haccp/lavado-frutas`)
- Tabla con registros
- Filtros: Fecha, Fruta/Verdura
- Columnas: Fecha, Producto, Químico, Concentración, Conformidades

**Datos:**
- GET `/haccp/lavado-frutas?mes=X&anio=Y`

##### **4.4. Lavado de Manos** (`/haccp/lavado-manos`)
- Tabla con registros
- Filtros: Fecha, Área, Turno, Empleado
- Columnas: Fecha, Empleado, Área, Turno, Conformidad

**Datos:**
- GET `/haccp/lavado-manos?mes=X&anio=Y`

##### **4.5. Temperatura de Cámaras** (`/haccp/temperatura-camaras`)
- Tabla con registros
- Filtros: Fecha, Cámara
- Columnas: Fecha, Cámara, Temp Mañana, Temp Tarde, Conformidad
- Gráfico de líneas: Temperaturas de la semana

**Datos:**
- GET `/haccp/temperatura-camaras?mes=X&anio=Y`
- GET `/haccp/camaras`

#### **5. Gestión de Usuarios** (`/usuarios`)
**Funcionalidades:**
- Tabla con todos los usuarios
- CRUD completo: Crear, Editar, Eliminar (desactivar)
- Filtros: Rol, Área, Activo/Inactivo
- Cambiar contraseña

**Datos:**
- GET `/usuarios` (nuevo endpoint a crear)
- POST `/usuarios` (crear usuario)
- PUT `/usuarios/:id` (actualizar)
- DELETE `/usuarios/:id` (desactivar)

#### **6. Reportes y Estadísticas** (`/reportes`)
**Secciones:**

##### **6.1. Resumen de No Conformidades**
- Usar vista `vista_resumen_no_conformidades`
- Gráfico de barras por tipo de control
- Tabla con detalles

##### **6.2. Proveedores con Más No Conformidades**
- Consulta custom:
```sql
SELECT nombre_proveedor, COUNT(*) as total_entregas,
       SUM(CASE WHEN uniforme_completo = 'NC' THEN 1 ELSE 0 END) as sin_uniforme,
       SUM(CASE WHEN producto_rechazado = 1 THEN 1 ELSE 0 END) as rechazados
FROM control_recepcion_mercaderia
WHERE anio = 2024
GROUP BY proveedor_id, nombre_proveedor
ORDER BY rechazados DESC
```

##### **6.3. Empleados con Más No Conformidades (Lavado de Manos)**
- Filtrar por mes
- Gráfico de torta o barras

##### **6.4. Temperaturas Fuera de Rango**
- Alertas de cámaras con conformidad NC
- Historial de la última semana

#### **7. Auditoría** (`/auditoria`)
- Tabla con logs de `auditoria_logs`
- Filtros: Usuario, Acción, Tabla, Fecha
- Columnas: Fecha, Usuario, Acción, Tabla, Registro ID, Datos

**Datos:**
- GET `/auditoria/logs?fecha_desde=X&fecha_hasta=Y`

---

## 📊 EXPORTACIÓN DE DATOS

### **Funcionalidad de Exportación a Excel**

**Librería recomendada**: `xlsx` o `exceljs`

**Ejemplo de implementación:**

```javascript
import * as XLSX from 'xlsx';

const exportarAsistencias = (datos) => {
  // Transformar datos
  const datosExcel = datos.map(a => ({
    'Fecha': a.fecha,
    'Usuario': `${a.nombre} ${a.apellido}`,
    'Cargo': a.cargo,
    'Área': a.area,
    'Hora Entrada': a.hora_entrada,
    'Hora Salida': a.hora_salida || 'N/A',
    'Horas Trabajadas': a.horas_trabajadas || 0,
    'Estado': a.estado,
    'Turno': a.turno
  }));
  
  // Crear worksheet y workbook
  const ws = XLSX.utils.json_to_sheet(datosExcel);
  const wb = XLSX.utils.book_new();
  XLSX.utils.book_append_sheet(wb, ws, 'Asistencias');
  
  // Guardar archivo
  const fecha = new Date().toISOString().split('T')[0];
  XLSX.writeFile(wb, `asistencias_${fecha}.xlsx`);
};
```

### **Formatos de Excel a Generar:**

1. **Asistencias por Mes**
   - Columnas: Fecha, Usuario, Cargo, Área, Entrada, Salida, Horas, Estado
   - Formato: `asistencias_YYYY-MM.xlsx`

2. **Recepción de Mercadería**
   - Columnas: Fecha, Proveedor, Producto, Cantidad, Conformidad, Rechazado, Observaciones
   - Formato: `recepcion_mercaderia_YYYY-MM.xlsx`

3. **Control de Cocción**
   - Columnas: Fecha, Producto, Temperatura, Tiempo, Conformidad, Responsable
   - Formato: `control_coccion_YYYY-MM.xlsx`

4. **Temperatura de Cámaras**
   - Columnas: Fecha, Cámara, Temp Mañana, Temp Tarde, Conformidad
   - Formato: `temperatura_camaras_YYYY-MM.xlsx`

5. **Lavado de Manos**
   - Columnas: Fecha, Empleado, Área, Turno, Conformidad
   - Formato: `lavado_manos_YYYY-MM.xlsx`

6. **Resumen de No Conformidades**
   - Columnas: Tipo Control, Total Registros, No Conformidades, % NC
   - Formato: `resumen_nc_YYYY-MM.xlsx`

---

## 🚀 DEPLOYMENT

### **Servidor Actual**
- **Provider**: AWS EC2
- **OS**: Ubuntu 22.04 LTS
- **IP Pública**: `18.220.8.226`
- **Puerto**: `3000`
- **Process Manager**: PM2

### **Estructura de Archivos en Servidor**

```
/home/ubuntu/Servidor-Wino-/
├── server.js
├── package.json
├── config-app-universal.js
├── database/
│   └── database.db
├── routes/
│   ├── auth.js
│   ├── fichado.js
│   ├── haccp.js
│   ├── dashboard.js
│   └── tiempo-real.js
├── middleware/
│   └── auth.js
├── utils/
│   ├── database.js
│   └── gpsValidation.js
└── logs/
    ├── access.log
    └── error.log
```

### **Comandos PM2**

```bash
# Ver estado
pm2 status

# Reiniciar backend
pm2 restart wino-backend

# Ver logs
pm2 logs wino-backend

# Monitorear
pm2 monit
```

### **Variables de Entorno**

Archivo `.env` (no versionado):
```
NODE_ENV=production
PORT=3000
JWT_SECRET=tu_secret_key_aqui
DB_PATH=./database/database.db
```

### **Actualizar Backend**

```bash
# 1. Conectar a servidor
ssh -i edmil-key.pem ubuntu@18.220.8.226

# 2. Ir a directorio
cd /home/ubuntu/Servidor-Wino-

# 3. Hacer backup de base de datos
cp database/database.db database/database_backup_$(date +%Y%m%d).db

# 4. Subir archivos nuevos (desde local)
scp -i edmil-key.pem archivo.js ubuntu@18.220.8.226:/home/ubuntu/Servidor-Wino-/routes/

# 5. Reiniciar PM2
pm2 restart wino-backend

# 6. Verificar logs
pm2 logs wino-backend --lines 50
```

---

## 📝 RESUMEN PARA DESARROLLO DEL PANEL WEB

### **Paso 1: Setup Inicial**
1. Crear proyecto React con TypeScript
2. Instalar dependencias:
```bash
npm install axios react-router-dom @mui/material @emotion/react @emotion/styled
npm install xlsx recharts date-fns
npm install --save-dev @types/react @types/react-dom
```

### **Paso 2: Configurar API Client**
```typescript
// src/api/apiClient.ts
import axios from 'axios';

const API_BASE_URL = 'http://18.220.8.226:3000/api';

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json'
  }
});

// Interceptor para agregar token
apiClient.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default apiClient;
```

### **Paso 3: Servicios por Módulo**
```typescript
// src/services/asistenciaService.ts
import apiClient from '../api/apiClient';

export const obtenerHistorialAsistencias = async (mes: number, anio: number) => {
  const response = await apiClient.get(`/fichado/historial?mes=${mes}&anio=${anio}`);
  return response.data.data;
};

// src/services/haccpService.ts
export const obtenerRecepcionMercaderia = async (mes: number, anio: number) => {
  const response = await apiClient.get(`/haccp/recepcion-mercaderia?mes=${mes}&anio=${anio}`);
  return response.data.data;
};

// ... más servicios
```

### **Paso 4: Componentes Principales**

**Login:**
```typescript
// src/pages/Login.tsx
const Login = () => {
  const handleLogin = async (email: string, password: string) => {
    const response = await apiClient.post('/auth/login', { email, password });
    localStorage.setItem('token', response.data.token);
    navigate('/dashboard');
  };
};
```

**Dashboard:**
```typescript
// src/pages/Dashboard.tsx
const Dashboard = () => {
  const [datos, setDatos] = useState(null);
  
  useEffect(() => {
    const cargarDatos = async () => {
      const res = await apiClient.get('/dashboard/admin');
      setDatos(res.data.data);
    };
    cargarDatos();
  }, []);
  
  return (
    <Grid container spacing={3}>
      <Grid item xs={12} md={3}>
        <StatCard title="Asistencia Hoy" value={datos?.asistenciaHoy} />
      </Grid>
      {/* Más widgets */}
    </Grid>
  );
};
```

**Tabla de Asistencias con Exportación:**
```typescript
// src/pages/Asistencias.tsx
import * as XLSX from 'xlsx';

const Asistencias = () => {
  const [asistencias, setAsistencias] = useState([]);
  
  const exportarExcel = () => {
    const datos = asistencias.map(a => ({
      'Fecha': a.fecha,
      'Usuario': `${a.nombre} ${a.apellido}`,
      'Entrada': a.hora_entrada,
      'Salida': a.hora_salida,
      'Estado': a.estado
    }));
    
    const ws = XLSX.utils.json_to_sheet(datos);
    const wb = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb, ws, 'Asistencias');
    XLSX.writeFile(wb, 'asistencias.xlsx');
  };
  
  return (
    <>
      <Button onClick={exportarExcel}>Exportar a Excel</Button>
      <DataGrid rows={asistencias} columns={columnas} />
    </>
  );
};
```

### **Paso 5: Rutas**
```typescript
// src/App.tsx
import { BrowserRouter, Routes, Route } from 'react-router-dom';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/asistencias" element={<Asistencias />} />
        <Route path="/haccp/recepcion-mercaderia" element={<RecepcionMercaderia />} />
        <Route path="/haccp/control-coccion" element={<ControlCoccion />} />
        <Route path="/haccp/lavado-frutas" element={<LavadoFrutas />} />
        <Route path="/haccp/lavado-manos" element={<LavadoManos />} />
        <Route path="/haccp/temperatura-camaras" element={<TemperaturaCamaras />} />
        <Route path="/usuarios" element={<Usuarios />} />
        <Route path="/reportes" element={<Reportes />} />
        <Route path="/auditoria" element={<Auditoria />} />
      </Routes>
    </BrowserRouter>
  );
}
```

---

## 🎯 CHECKLIST PARA IA DESARROLLADORA

### **Pre-requisitos**
- [ ] Leer y entender `BasedeDatos.sql` completo
- [ ] Revisar todos los endpoints en este documento
- [ ] Entender sistema de autenticación JWT
- [ ] Conocer roles: ADMIN, SUPERVISOR, COCINERO, EMPLEADO

### **Desarrollo**
- [ ] Setup proyecto React + TypeScript
- [ ] Configurar axios con interceptor de autenticación
- [ ] Crear servicios para cada módulo (asistencia, haccp, usuarios, dashboard)
- [ ] Implementar Login y manejo de sesiones
- [ ] Crear Dashboard principal con widgets
- [ ] Implementar página de Asistencias con filtros y exportación
- [ ] Implementar las 5 páginas de formularios HACCP
- [ ] Crear CRUD de usuarios (solo para ADMIN)
- [ ] Implementar página de Reportes con gráficos
- [ ] Implementar página de Auditoría
- [ ] Agregar funcionalidad de exportación a Excel en todas las tablas
- [ ] Implementar actualización en tiempo real (polling o websockets)
- [ ] Añadir validaciones y manejo de errores
- [ ] Crear loading states y spinners
- [ ] Diseño responsive (mobile-first)

### **Testing**
- [ ] Probar login con usuarios de diferentes roles
- [ ] Verificar que ADMIN vea todo, SUPERVISOR solo su área
- [ ] Probar exportación de Excel en cada módulo
- [ ] Verificar filtros de fecha funcionan correctamente
- [ ] Probar navegación entre páginas
- [ ] Verificar que token expirado redirija a login

### **Deployment**
- [ ] Build de producción
- [ ] Configurar nginx como reverse proxy
- [ ] SSL certificate (Let's Encrypt)
- [ ] Monitoreo y logs

---

## 📞 CONTACTO Y SOPORTE

**Servidor Backend**: `http://18.220.8.226:80`  
**Archivo Base de Datos**: `/home/ubuntu/Servidor-Wino-/database/database.db`  
**Process Manager**: PM2 (proceso: `wino-backend`)

**Documentos de Referencia:**
1. `BasedeDatos.sql` - Esquema completo de base de datos
2. `BACKEND_ARCHITECTURE.md` - Este documento
3. `Implemntacio_backend.md` - Detalles de implementación

---

**Última Actualización**: 2 de octubre de 2025  
**Versión**: 1.0.0  
**Estado**: ✅ Completamente Funcional y Documentado
