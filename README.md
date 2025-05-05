# VinilosMobileG17

Proyecto Vinilos - Equipo 17

Integrantes

Estudiantes

Jhoan Sarmiento
jf.sarmiento23@uniandes.edu.coa

Diego Niño
da.ninoa1@uniandes.edu.co

Daniel Urrego
d.urregor@uniandes.edu.co

Más información y documentación en la Wiki del proyecto

## Requisitos Fundamentales

* Se requiere **JetBrains Runtime 21.0.5**.
* Es necesario **Gradle en su versión 8.7**.
* Debes tener **Android Studio (versión Android Studio Meerkat | 2024.3.1 Patch 1 ó superior)** instalado.

## Cómo construir y ejecutar la aplicación localmente

1.  **Descarga el repositorio del proyecto.**
2.  **Abre el proyecto utilizando Android Studio.**
3.  **Sincroniza el archivo `build.gradle`**: Este paso es crucial para asegurar que las versiones correctas de los sistemas se configuren en tu equipo y que el entorno esté listo para la compilación de la aplicación.
4.  **Puedes iniciar la aplicación de dos maneras:**
    * **Mediante el emulador de Android Studio**:
        * Ejecuta la aplicación a través de un emulador de Android directamente desde Android Studio.
        * Crea y selecciona el emulador deseado, y luego haz clic en el botón **Ejecutar Aplicación** (<0xF0><0x9F><0xAA><0xB6>).
    * **Utilizando un dispositivo Android físico**:
        * También tienes la opción de usar un teléfono o tableta Android real como emulador, conectándolo a tu computadora.
        * Una vez conectado, selecciónalo en Android Studio y presiona el botón **Ejecutar Aplicación** (<0xF0><0x9F><0xAA><0xB6>) para iniciar la app.
        * **Nota importante**: Para poder ejecutar en un dispositivo Android, primero debes activar las opciones de desarrollador en la configuración del dispositivo.

## Ejecución de la aplicación mediante un archivo APK

1.  **Obtén el APK**: Descarga el archivo APK de la aplicación, que se encuentra en la carpeta principal del repositorio.
2.  **Transfiere el APK a tu dispositivo**:
    * Conecta tu teléfono o tableta Android a la computadora con un cable USB y copia el archivo APK a la memoria del dispositivo.
    * Otra opción es enviar el APK al dispositivo por correo electrónico o a través de un servicio de almacenamiento en la nube.
3.  **Permite la instalación de fuentes desconocidas**:
    * En tu dispositivo, ve a **Ajustes** > **Seguridad** (o **Aplicaciones**, dependiendo del modelo).
    * Activa la opción que permite instalar aplicaciones de orígenes desconocidos.
4.  **Instala la aplicación**: Busca el archivo APK en tu dispositivo, ábrelo y sigue las instrucciones que aparezcan para instalarlo.
5.  **Inicia la aplicación**: Una vez instalada, busca el icono de la aplicación en el menú de aplicaciones de tu dispositivo y tócalo para empezar a usarla.

## Cómo ejecutar pruebas automatizadas con Espresso

1.  Asegúrate de que el proyecto esté correctamente sincronizado y que el archivo `build.gradle` esté configurado para las pruebas de Espresso.
2.  Verifica que tengas un dispositivo Android (físico o emulador) conectado y listo para recibir la ejecución de las pruebas.
3.  En Android Studio, elige el dispositivo o emulador donde quieres correr las pruebas. Puedes hacer esto en la ventana de selección de dispositivos de Android Studio.
4.  Navega hasta la carpeta de pruebas dentro de la estructura de archivos del proyecto: `src/androidTest`.
5.  Haz clic derecho sobre la clase o el paquete de pruebas que deseas ejecutar. Selecciona **Run 'nombre\_de\_la\_prueba'** para ejecutar pruebas específicas, o **Run Tests in `androidTest`** para ejecutar todas las pruebas de extremo a extremo.
6.  Android Studio mostrará una ventana con los resultados de las pruebas, donde podrás ver el progreso y el estado de cada una.
7.  Una vez finalizada la ejecución, revisa el informe completo de las pruebas en la consola de Android Studio. Este informe te mostrará detalles de cada prueba, indicando cuáles fueron exitosas y cuáles fallaron.

![image](https://github.com/user-attachments/assets/ecc4235c-bee4-4606-b0ee-4e16c5cb0718)

