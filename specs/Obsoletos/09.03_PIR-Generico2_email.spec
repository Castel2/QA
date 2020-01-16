Prueba el Email CON y SIN plantillas.
====================================

Tags:Transaccionamiento,email,09.03_PIR-Generico2_email
Preconditions:
Metadata:ScenarioType=Auto

h1.Prueba las Estadísticas y el crédito.

LogIN, Almacen de Datos:
* Establecer host "" port "" dir ""
* Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"
* Cumplimentar Formulario y Login
* Establecer cliente
* Hash Map
* Lista Propiedades desde licencia
Escenario primero
-----------------
>> Se transacciona sin plantilla
* idioma "--"
* Menu "Diagnosis"
* Herramienta "Envíos email"
* Menu herramienta
* Titulo "PIR-Generico2_email - SIN plantillas"
* Texto "ñ Ñ < > $ € ! ' á é í ó ú à è ì ò ù"
* Parametro Variable "parametro"
* Valor Parametro "valor"
* Transaccionar EnvioEmail "testing@latinia.com" "EtiquetaEmail" " " " "

>> Ver estadísticas del Email enviado SIN plantillas
* InicioHome
* Menu "Análisis"
* Herramienta "Detalle de contenidos de transacciones de email"
* Menu herramienta
* BusquedaCriterio "email" "testing@latinia.com" "" ""

>> Se crea plantilla 'refPlantilla0' para trasaccionar después con ella
* Proposito ""
* InicioHome
* Menu "Provisión"
* Herramienta "Gestión de plantillas"
* Menu herramienta

* tipoclause "Email"
* idioma "es_ES"
* Establece Idioma
* Establece Variables "mi_variable_directa"
* Existe tipo de mensaje
* Borrar Antigua "plantilla" "refPlantilla0"
* Borrar Antigua "seccion" "0 - Encabezado"
* Borrar Antigua "seccion" "0 - Pie"
* Crear Seccion generica "0 - Encabezado" ""
* Crear Plantilla "grupo Creadas TWIST" "Mi plantilla" "refPlantilla0"
* InicioHome
* Menu herramienta
* Asignar Seccion generica "0 - Encabezado" "refPlantilla0"
* InicioHome
* Menu herramienta
* Crear Seccion generica "0 - Pie" ""
* Asignar Seccion generica "0 - Pie" "refPlantilla0"
* InicioHome
* Menu herramienta
* Crear Seccion especifica "Mi sección especifica0" "Contenido sección específica" "refPlantilla0" "forzar"
* InicioHome
* Menu herramienta
* VisualizarPlantilla "refPlantilla0"

>> Se transacciona con 'refPlantilla0'
* Menu "--"
* Herramienta "Gestión de aplicaciones"
* InicioHome
* Menu herramienta
* propiedades "wapppushemail" "CONTENT_TEMPLATE_ENABLE" "APPLICATION"
* Menu "Diagnosis"
* Herramienta "Envíos email"
* InicioHome
* Menu herramienta
* Titulo "PIR-Generico2_email - Probando plantillas de Email"
* Texto "Contenido sección específica"
* Parametro Variable "parametro"
* Valor Parametro "valor"
* Transaccionar EnvioEmail "testing@latinia.com" "EtiquetaEmail" "refPlantilla0" " "

>> Ver estadísticas del Email enviado CON plantillas
* InicioHome
* Menu "Análisis"
* Herramienta "Detalle de contenidos de transacciones de email"
* Menu herramienta
* Ver Plantilla
* InicioHome
* Menu "Provisión"
* Herramienta "Gestión de créditos"
* Menu herramienta
* Credito "GENERAL"

LogOut:
* LogOut