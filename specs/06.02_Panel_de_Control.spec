Herramienta gráfica 'Panel de Control' - Iniciando_spec: 06.02_Panel_de_Control.spec
======================================
Tags:Analisis,06.02_Panel_de_Control,Regresion,CI
Preconditions:
Metadata:ScenarioType=Auto

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
* InicioHome
* Menu "Control"
* Herramienta "Panel de control"
* Menu herramienta

Panel de Control:
* General
* Logs
* Generar_Exportacion "Mis logs Automated" "Export logs automaticos Automated"
* Generar_Exportacion actual "Logs Automated actual" "Export actual logs automaticos Automated"
* LogDown
/* Bajar descargas generadas
* Conectividad
* BBDD "ver"

LogOut:
* LogOut
