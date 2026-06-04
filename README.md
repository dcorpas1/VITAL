# VITAL

**Aplicación Inteligente de Planificación Integral y Automatizada de Salud y Bienestar Personalizado**

Proyecto de Fin de Grado — Ciclo Formativo de Grado Superior en Desarrollo de Aplicaciones Multiplataforma (DAM)
I.E.S. Juan de la Cierva · Curso 2025/2026

**Autores:** Jorge Trives Villapalos y Daniel Corpas Jiménez

---

## ¿Qué es VITAL?

VITAL es una aplicación Android nativa que automatiza la planificación de salud y bienestar. A partir de un único formulario inicial (onboarding), la aplicación genera de forma automática un plan mensual completo de 30 días que incluye:

- **Menús diarios** (desayuno, comida y cena) adaptados a las calorías y macronutrientes del usuario.
- **Rutinas de entrenamiento** según el material disponible (gimnasio o casa) y el objetivo.
- **Hábitos diarios** complementarios.

El usuario no necesita planificar nada manualmente: el motor de cálculo de la app (Motor VITAL) calcula sus necesidades nutricionales con la fórmula de Mifflin-St Jeor y construye el plan combinando esos datos con un catálogo de ejercicios y alimentos.

---

## Tecnologías utilizadas

| Componente | Tecnología |
|---|---|
| Lenguaje | Kotlin |
| Interfaz de usuario | Jetpack Compose |
| Arquitectura | Clean Architecture + MVVM |
| Inyección de dependencias | Hilt |
| Autenticación | Firebase Authentication |
| Base de datos | Cloud Firestore |
| Navegación | Navigation Compose |
| Asincronía | Kotlin Coroutines |

La aplicación se organiza en tres capas siguiendo Clean Architecture:

- **Presentación:** Jetpack Compose, ViewModels (MVVM) y estados reactivos con StateFlow.
- **Dominio:** Kotlin puro, sin dependencias de Android ni Firebase. Contiene los casos de uso (Motor VITAL, generación del plan) y las entidades.
- **Datos:** Firebase SDK y repositorios que implementan las interfaces definidas en el dominio.

---

## Cómo instalar el APK (para probar la app)

Si solo quieres instalar la aplicación en un móvil Android para probarla, no necesitas Android Studio. Sigue estos pasos:

### Requisitos
- Un dispositivo Android con **versión 8.0 (Oreo) o superior**.
- Conexión a internet (la app sincroniza los datos con Firebase).

### Pasos

1. **Descarga el APK** desde la carpeta [`APK/`](./APK) de este repositorio. Pulsa sobre el fichero `VITAL.apk` y luego en el botón de descarga (**Download**).

2. **Pasa el APK al móvil** (si lo has descargado en el ordenador): por cable USB, correo, o subiéndolo a Google Drive y abriéndolo desde el móvil.

3. **Permite la instalación de apps de orígenes desconocidos.** Al abrir el APK, Android te avisará de que la app no viene de Google Play. Pulsa en **Ajustes** y activa el permiso **"Permitir desde esta fuente"** (o "Instalar apps desconocidas") para la aplicación desde la que estás abriendo el APK (normalmente Archivos o Chrome).

4. **Instala.** Vuelve atrás, pulsa **Instalar** y espera a que termine.

5. **Abre VITAL**, regístrate con un correo y una contraseña, completa el formulario de onboarding y la app generará tu plan mensual automáticamente.

> **Nota:** este es un APK de depuración (*debug*), destinado a pruebas. No está firmado para distribución en Google Play.

---

## Cómo abrir el proyecto en Android Studio

Si quieres ver o ejecutar el código fuente:

### Requisitos previos
- **Android Studio** Ladybug (2024.2) o superior.
- **Android SDK** API 26 o superior.
- El fichero `google-services.json` ya está incluido en `app/` para conectar con Firebase.

### Pasos

1. Clona el repositorio:
   ```bash
   git clone https://github.com/dcorpas1/VITAL.git
   ```
2. Abre el proyecto en Android Studio.
3. Sincroniza Gradle (el IDE lo solicita automáticamente al abrir).
4. Conecta un dispositivo Android o lanza un emulador.
5. Pulsa **Run** (▶️) para compilar y ejecutar.

### Cargar el catálogo de Firestore (opcional)

El catálogo de ejercicios y alimentos se precarga mediante un script de Node.js. Si necesitas cargarlo desde cero:

1. Sitúate en la carpeta que contiene `seed.js` y `service-account.json`.
2. Ejecuta:
   ```bash
   npm install firebase-admin
   node seed.js
   ```

---

## Estructura del repositorio

```
VITAL/
├── APK/            → APK ejecutable para instalar y probar la app
├── app/            → Código fuente de la aplicación Android
├── gradle/         → Configuración de Gradle (version catalog)
├── imagenes/       → Diagramas y capturas del proyecto
└── README.md       → Este documento
```

---

## Funcionalidades principales

- Registro y autenticación con Firebase.
- Onboarding guiado multi-paso con validaciones.
- Motor VITAL: cálculo de TMB, TDEE y macronutrientes (fórmula Mifflin-St Jeor).
- Generación automática del plan mensual de 30 días.
- Calendario mensual interactivo con detalle por día.
- Marcar días como completados.
- Seguimiento del progreso (historial de peso y racha de días).
- Formulario de personalización avanzada.
- Regeneración del plan e intercambio dinámico de comidas.

---

*VITAL — Proyecto de Fin de Grado — Jorge Trives & Daniel Corpas — 2026*
