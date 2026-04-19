# MisHoras

App Android de registro de horas trabajadas por proyecto. Funciona sin internet.
Los datos se guardan dentro de la app (no dependen del navegador).

---

## ¿Cómo obtener el APK?

No hace falta instalar nada en tu PC. GitHub compila el APK por vos, gratis,
en la nube. Solo tenés que seguir estos pasos **una vez**:

### 1) Crear una cuenta en GitHub (5 min)

1. Entrá a **https://github.com/signup** desde el celular o la PC.
2. Poné tu email, una contraseña y un nombre de usuario.
3. Confirmá tu email desde el link que te mandan.

Es gratis y no te van a pedir tarjeta.

### 2) Crear un repositorio nuevo

1. Una vez logueado, tocá el `+` arriba a la derecha → **New repository**.
2. En **Repository name** escribí: `MisHoras`.
3. Dejá **Public** marcado (así GitHub Actions es gratis con más minutos).
4. **NO** marques "Add a README file" ni nada. Dejá todo vacío.
5. Tocá **Create repository**.

### 3) Subir los archivos

Ahora GitHub te muestra una página medio vacía con instrucciones técnicas.
Ignoralas y hacé esto:

1. Mirá cerca del centro/arriba y tocá el link que dice
   **"uploading an existing file"** (o andá directo a:
   `https://github.com/TU_USUARIO/MisHoras/upload/main`).
2. **Importante**: tenés que subir el CONTENIDO de la carpeta `MisHoras`,
   no la carpeta en sí. Es decir: abrí la carpeta `MisHoras` en tu
   explorador/archivos, seleccioná TODO lo de adentro (incluyendo la
   carpeta `.github` que empieza con punto, la carpeta `app`, y los
   archivos `build.gradle`, `settings.gradle`, etc.) y arrastralos a la
   caja de GitHub.
3. Tip si venís del celular: es más fácil desde una PC. Si solo tenés
   celular, descomprimí el zip con alguna app de archivos y subí uno por
   uno (o de a varios).
4. Abajo, donde dice **Commit changes**, tocá el botón verde
   **"Commit changes"**. Esperá a que termine de subir.

### 4) Esperar que GitHub compile el APK (~2-3 min)

Apenas terminás de subir los archivos, GitHub empieza a compilar solo.

1. En tu repositorio, tocá la pestaña **Actions** (arriba).
2. Vas a ver un item llamado "Compilar APK" con un circulito amarillo
   (compilando) o verde (listo).
3. Esperá hasta que aparezca el tilde verde ✓. Tarda 2-3 minutos la
   primera vez.

### 5) Descargar el APK

1. Andá a la pestaña **Releases** (a la derecha, o en
   `https://github.com/TU_USUARIO/MisHoras/releases`).
2. Vas a ver **MisHoras build 1**. Tocalo.
3. Abajo, en **Assets**, tocá **MisHoras.apk**. Se descarga al cel.

### 6) Instalar el APK en el celular

1. Abrí el archivo `MisHoras.apk` desde la carpeta Descargas.
2. Android te va a decir que "no se permite instalar apps desconocidas".
   Esto es normal. Tocá **Configuración** → activá el permiso para el
   navegador/explorador que estés usando → volvé atrás.
3. Tocá **Instalar**. Listo.
4. El ícono del reloj aparece en tu pantalla de apps. Abrí, usá, cerrá.
   Como cualquier app común.

---

## ¿Y si quiero hacer un cambio al HTML más adelante?

1. En tu repo de GitHub, entrá a la carpeta
   `app/src/main/assets/` → tocá `index.html`.
2. Tocá el iconito del lápiz (arriba a la derecha) → editá el HTML
   directo desde la web → tocá **Commit changes**.
3. GitHub vuelve a compilar automáticamente y genera un nuevo release
   (build 2, 3, etc). Bajás el nuevo APK de **Releases** e instalás
   encima del anterior. **Los datos NO se pierden** al actualizar
   (solo se pierden si desinstalás).

---

## Preguntas frecuentes

**¿Necesito internet para usar la app?**
No. Solo para compilarla la primera vez. Después funciona offline.

**¿Los datos se pueden perder?**
Solo si desinstalás la app. Borrar caché de Chrome ya no les afecta.
Además Android hace backup automático a Google Drive si tenés el backup
de apps activado. Y seguís teniendo el botón de backup `.json` dentro
de la app para mayor seguridad.

**¿Hay publicidad?**
No. El APK es 100% tuyo, sin publicidad, sin tracking, sin nada.

**¿Por qué dice "app de origen desconocido" al instalar?**
Porque el APK está firmado con una clave de desarrollo (no está subido
a la Play Store). Es normal y seguro — el APK lo generaste vos desde
tu propio código. Una vez instalado, funciona como cualquier otra app.

**¿Puedo cambiar el nombre o el ícono?**
Sí:
- Nombre: editá `app/src/main/res/values/strings.xml`.
- Ícono: reemplazá `app/src/main/res/drawable/ic_launcher_foreground.xml`.

---

## Soporte

Si algo falla en la compilación, abrí la pestaña **Actions**, entrá al
job en rojo, y copiá el error. Con eso se puede diagnosticar qué pasó.
