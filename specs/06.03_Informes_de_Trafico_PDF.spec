Comprueba la herramienta gráfica de 'Informes de tráfico PDF' - Iniciando_spec: 06.03_Informes_de_Trafico_PDF
=============================================================
Tags:Analisis,06.03_Informes_de_Trafico_PDF,Regresion,CI
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
* Menu "Análisis"
* Herramienta "Generador de informes PDF"
* Menu herramienta

Crear Informe PDF:
* Crear
* Programar
* Email
* Activar

LogOut:
* LogOut