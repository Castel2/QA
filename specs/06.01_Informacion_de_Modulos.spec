Herramienta gráfica de Información de Módulos - Iniciando_spec: 06.01_Informacion_de_Modulos
=============================================
Tags:Analisis,06.01_Informacion_de_Modulos,Smoke

Estos son los steps denominados 'de contexto' (ver: http://getgauge.io/documentation/user/current/gauge_terminologies/contexts.html)
Estos steps se ejecutan antes de cada escenario de este spec.
* Establecer host "" port "" dir ""
* Establecer login "" user "" pass ""
* Cumplimentar Formulario y Login
* Establecer cliente
* Hash Map
* Lista Propiedades desde licencia
Escenario primero
-----------------
* Menu "Control" 
* Herramienta "Información de módulos"
* Menu herramienta
* Item1_AdaptorInJMSCollector
* Atras
* Item2_Core
* Atras
* ExportTodo
* Export_busutil
* Esperar "3"

LogOut:
* LogOut