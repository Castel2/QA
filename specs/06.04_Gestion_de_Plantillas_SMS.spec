Prueba las plantillas SMS a nivel GUI, no se prueba la funcionalidad del BUS - Iniciando_spec: 06.04_Gestion_de_Plantillas_SMS
========================================================================
Tags:Provision,06.04_Gestion_de_Plantillas_SMS,Regresion,CI
Preconditions:

Estos son los steps denominados 'de contexto' (ver: http://getgauge.io/documentation/user/current/gauge_terminologies/contexts.html)
Estos steps se ejecutan antes de cada escenario de este spec.
* Establecer host "" port "" dir ""
* Establecer login "" user "" pass ""
* Cumplimentar Formulario y Login
* Establecer cliente
* Hash Map
* Lista Propiedades desde licencia
Inicio,Navegacion,Almacen de Datos:
Escenario primero
-----------------
* InicioHome
* Proposito ""
* Menu "Provisión"
* Herramienta "Gestión de plantillas"
* Menu herramienta

* tipoclause "SMS"
* idioma "es_ES"
* Establece Idioma
* Establece Variables "mi_variable_directa"
* Existe tipo de mensaje
* Borrar Antigua "plantilla" "refPlantilla0"
* Borrar Antigua "seccion" "0 - Encabezado"
* Borrar Antigua "seccion" "0 - Pie"
* Crear Seccion generica "0 - Encabezado" ""
* Crear Plantilla "grupo Creadas AUTOMATED" "Mi plantilla" "refPlantilla0"
* InicioHome
* Menu herramienta
* Asignar Seccion generica "0 - Encabezado" "refPlantilla0"
* InicioHome
* Menu herramienta
* Crear Seccion generica "0 - Pie" ""
* Asignar Seccion generica "0 - Pie" "refPlantilla0"
* InicioHome
* Menu herramienta
* Crear Seccion especifica "Mi sección especifica0" "Contenido sección específica" "refPlantilla0" "forzarNO"
* InicioHome
* Menu herramienta
* VisualizarPlantilla "refPlantilla0"
* Borrar Antigua "plantilla" "refPlantilla0"
* Borrar Antigua "seccion" "0 - Encabezado"
* Borrar Antigua "seccion" "0 - Pie"

LogOut:
* LogOut