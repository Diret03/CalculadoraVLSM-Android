# Calculadora VLSM para Android

[![Build Status](https://img.shields.io/badge/Build-Pasando-brightgreen.svg)](https://example.com/build-status) [![License](https://img.shields.io/badge/Licencia-MIT-yellow.svg)](https://opensource.org/licenses/MIT) ## Descripción General

Esta es una aplicación para Android diseñada para ayudar con los cálculos de Máscara de Subred de Longitud Variable (VLSM). Permite a los usuarios ingresar una red padre, especificar el número de subredes requeridas y el número de hosts necesarios para cada una, y luego calcula los detalles de la subred.

## Capturas de Pantalla

**[Espacio para la Captura de Pantalla 1: Pantalla Principal de Entrada]**
<br>
<br>
**[Espacio para la Captura de Pantalla 2: Pantalla de la Tabla de Salida]**
<br>
<br>

## Uso Básico

Aquí tienes una guía paso a paso sobre cómo usar la aplicación Calculadora VLSM:

1.  **Ingrese la Red Padre:** En el campo "Red Padre", ingrese la dirección IP de la red principal que desea subredear. Por ejemplo, `192.168.1.0`.

2.  **Ingrese el Prefijo:** En el campo "Prefijo", ingrese el prefijo CIDR de la red padre. Por ejemplo, `24`.

3.  **Especifique el Número de Subredes:** En el campo "Nro. subredes", ingrese el número total de subredes que necesita crear.

4.  **Defina los Requisitos de las Subredes:**
    * Después de ingresar el número de subredes, la aplicación mostrará una lista de filas de subredes (inicialmente 5, pero puede cambiar el número presionando el botón "Cambiar").
    * Para cada subred, debe proporcionar:
        * **Nombre:** Ingrese un nombre o identificador para la subred (por ejemplo, A, B, C o un nombre descriptivo).
        * **Tamaño host:** Ingrese el número de hosts utilizables requeridos para esta subred específica.

5.  **Calcular Subredes:** Una vez que haya ingresado la información de la red padre, el prefijo y los requisitos de host para cada subred, haga clic en el botón **"Calcular subredes"**.

6.  **Ver Resultados:** La aplicación navegará a una nueva pantalla (Actividad de Salida) donde mostrará una tabla con los detalles de cada subred calculada, incluyendo:
    * Nombre de la Subred
    * Número de Hosts Requeridos
    * Número de Hosts Asignados
    * Dirección de Subred
    * Prefijo
    * (Potencialmente más detalles como Dirección Binaria, Máscara, Rango, Broadcast en modo horizontal)

**Nota Importante:** La aplicación realizará una verificación para asegurarse de que el número total de hosts solicitados no exceda la capacidad de la red padre según el prefijo proporcionado. Si el requisito total de hosts es demasiado grande, la aplicación mostrará un mensaje de error y evitará el cálculo.

## Primeros Pasos (Para Desarrolladores)

Si deseas construir y ejecutar esta aplicación desde el código fuente:

1.  **Clona el repositorio:**
    ```bash
    git clone [https://github.com/TuNombreDeUsuario/CalculadoraVLSM-Android.git](https://github.com/TuNombreDeUsuario/CalculadoraVLSM-Android.git)
    ```
    (Reemplaza `TuNombreDeUsuario/CalculadoraVLSM-Android.git` con la URL real de tu repositorio)

2.  **Abre el proyecto en Android Studio.**

3.  **Construye y ejecuta la aplicación en un emulador o dispositivo Android.**

## Contribuciones

Si deseas contribuir a este proyecto, por favor sigue estas pautas:

1.  Haz un fork del repositorio.
2.  Crea una nueva rama para tu contribución (`git checkout -b feature/nueva-funcionalidad`).
3.  Realiza tus cambios y commitea (`git commit -am 'Añade nueva funcionalidad'`).
4.  Sube tus cambios a tu fork (`git push origin feature/nueva-funcionalidad`).
5.  Crea un nuevo Pull Request.

## Licencia

Este proyecto está licenciado bajo la [Licencia MIT](LICENSE). ## Autor

[Tu Nombre] ([Tu GitHub](https://github.com/TuNombreDeUsuario)) ```

Remember to replace the placeholder links (like the build status and license badges, the repository cloning URL, and the author information) with your actual information. Also, replace the `path/to/screenshot1.png` and `path/to/screenshot2.png` placeholders with the actual paths to your screenshots once you add them to the repository.
