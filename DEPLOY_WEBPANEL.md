# Guía de Despliegue WebPanel en Servidor 13.59.31.255

## 📋 Información del Proyecto
- **Servidor IP**: 13.59.31.255
- **Usuario**: ec2-user (Amazon Linux)
- **Clave SSH**: edmil-key.pem (ubicada en la raíz del proyecto)
- **Repositorio**: https://github.com/milith0kun/Servidor-Wino-.git
- **Rama**: WebPanel
- **Puerto**: 5173 (desarrollo) / 80 o 443 (producción)

---

## 🔑 Paso 1: Conectarse al Servidor

### Desde Windows (PowerShell):
```powershell
# Navegar a la carpeta donde está la clave
cd "d:\Programacion Fuera de la U\AppWino"

# Conectarse al servidor usando SSH
ssh -i edmil-key.pem ec2-user@13.59.31.255
```

### Solución de Problemas de Permisos (si es necesario):
Si aparece un error de permisos en Windows:
```powershell
# Dar permisos correctos al archivo .pem
icacls edmil-key.pem /inheritance:r
icacls edmil-key.pem /grant:r "%USERNAME%:(R)"
```

---

## 📦 Paso 2: Instalar Dependencias en el Servidor

Una vez conectado al servidor, instala Node.js y Git:

```bash
# Actualizar paquetes del sistema
sudo yum update -y

# Instalar Node.js (versión 18 LTS recomendada)
curl -sL https://rpm.nodesource.com/setup_18.x | sudo bash -
sudo yum install -y nodejs

# Verificar instalación
node --version
npm --version

# Instalar Git si no está instalado
sudo yum install -y git

# Verificar Git
git --version
```

---

## 🚀 Paso 3: Clonar el Repositorio y Configurar el Proyecto

```bash
# Crear directorio para el proyecto
cd /home/ec2-user
mkdir -p projects
cd projects

# Clonar solo la rama WebPanel
git clone -b WebPanel https://github.com/milith0kun/Servidor-Wino-.git webpanel
cd webpanel

# Verificar que estamos en la rama correcta
git branch

# Navegar a la carpeta WebPanel
cd WebPanel

# Instalar dependencias
npm install

# Crear archivo .env para configuración
nano .env
```

Contenido del archivo `.env`:
```env
VITE_API_URL=http://TU_IP_BACKEND:3001/api
```

---

## ⚙️ Paso 4: Configurar Nginx como Proxy Reverso (Producción)

### Instalar Nginx:
```bash
sudo yum install -y nginx
```

### Configurar Nginx:
```bash
sudo nano /etc/nginx/conf.d/webpanel.conf
```

Contenido del archivo de configuración:
```nginx
server {
    listen 80;
    server_name 13.59.31.255;

    # Logs
    access_log /var/log/nginx/webpanel_access.log;
    error_log /var/log/nginx/webpanel_error.log;

    location / {
        proxy_pass http://localhost:5173;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_cache_bypass $http_upgrade;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

### Iniciar Nginx:
```bash
# Verificar configuración
sudo nginx -t

# Iniciar y habilitar Nginx
sudo systemctl start nginx
sudo systemctl enable nginx

# Verificar estado
sudo systemctl status nginx
```

---

## 🏃 Paso 5: Ejecutar el Proyecto

### Modo Desarrollo (para pruebas):
```bash
cd /home/ec2-user/projects/webpanel/WebPanel
npm run dev
```

### Modo Producción con PM2:
```bash
# Instalar PM2 globalmente
sudo npm install -g pm2

# Primero hacer build del proyecto
npm run build

# Instalar serve para servir archivos estáticos
sudo npm install -g serve

# Iniciar con PM2
pm2 start "serve -s dist -l 5173" --name webpanel

# Guardar configuración PM2
pm2 save

# Configurar PM2 para iniciar al arrancar el servidor
pm2 startup
# (Ejecuta el comando que PM2 te muestra)

# Ver logs
pm2 logs webpanel

# Ver estado
pm2 status
```

---

## 🔥 Paso 6: Configurar Firewall

```bash
# Abrir puerto 80 (HTTP)
sudo firewall-cmd --permanent --add-service=http

# Abrir puerto 443 (HTTPS) - opcional
sudo firewall-cmd --permanent --add-service=https

# Abrir puerto 5173 (desarrollo) - opcional
sudo firewall-cmd --permanent --add-port=5173/tcp

# Recargar firewall
sudo firewall-cmd --reload

# Verificar reglas
sudo firewall-cmd --list-all
```

**IMPORTANTE**: También debes configurar el Security Group en AWS para permitir tráfico en los puertos 80, 443 y 5173 (si es necesario).

---

## 🔄 Paso 7: Actualizar el Proyecto

Para actualizar cuando hagas cambios:

```bash
cd /home/ec2-user/projects/webpanel
git pull origin WebPanel
cd WebPanel
npm install
npm run build
pm2 restart webpanel
```

---

## 📝 Comandos Útiles

### PM2:
```bash
pm2 list                  # Lista todos los procesos
pm2 logs webpanel         # Ver logs en tiempo real
pm2 restart webpanel      # Reiniciar aplicación
pm2 stop webpanel         # Detener aplicación
pm2 delete webpanel       # Eliminar proceso
pm2 monit                 # Monitor interactivo
```

### Nginx:
```bash
sudo systemctl status nginx    # Estado del servicio
sudo systemctl restart nginx   # Reiniciar Nginx
sudo nginx -t                  # Verificar configuración
sudo tail -f /var/log/nginx/webpanel_error.log   # Ver logs de errores
```

### Git:
```bash
git status                # Ver estado
git log --oneline         # Ver historial
git branch -a             # Ver todas las ramas
git pull                  # Actualizar código
```

---

## 🌐 Acceder a la Aplicación

Una vez configurado, accede a:
- **Desarrollo**: http://13.59.31.255:5173
- **Producción**: http://13.59.31.255

---

## 🐛 Solución de Problemas

### Error de permisos de PM2:
```bash
pm2 kill
pm2 flush
pm2 start "serve -s dist -l 5173" --name webpanel
```

### Error de Nginx:
```bash
sudo nginx -t
sudo tail -f /var/log/nginx/error.log
```

### Puerto ocupado:
```bash
# Ver qué proceso usa el puerto 5173
sudo lsof -i :5173

# Matar el proceso si es necesario
sudo kill -9 <PID>
```

### Problemas de conexión:
1. Verificar Security Groups en AWS Console
2. Verificar firewall: `sudo firewall-cmd --list-all`
3. Verificar que el servicio esté corriendo: `pm2 status`

---

## 📚 Referencias Adicionales

- [Documentación de Vite](https://vitejs.dev/)
- [Documentación de PM2](https://pm2.keymetrics.io/)
- [Documentación de Nginx](https://nginx.org/en/docs/)

---

## ✅ Checklist de Despliegue

- [ ] Conectado al servidor vía SSH
- [ ] Node.js y npm instalados
- [ ] Repositorio clonado
- [ ] Dependencias instaladas
- [ ] Archivo .env configurado
- [ ] Nginx instalado y configurado
- [ ] Firewall configurado
- [ ] Security Group de AWS configurado
- [ ] PM2 instalado y proceso iniciado
- [ ] Aplicación accesible desde el navegador

---

**Fecha de última actualización**: 3 de octubre de 2025
