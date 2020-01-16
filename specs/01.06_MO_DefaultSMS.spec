Verifica la funcionalidad transaccional de DefaultSMS así como también la herramienta gráfica DefaultSMS - Iniciando_spec: 01.06_MO_DefaultSMS
========================================================================================================

Tags:Transaccionamiento,sms,01.06_MO_DefaultSMS,MO,Smoke,Regresion,CI
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
* Hash Map
* InicioHome
* Menu "Provisión"
* Herramienta "Gestión de canales"
* Menu herramienta
* Rutas "Virtual" "+000001" "SMS" "Virtual"
* Menu herramienta
* Rutas "Unknown" "+000001" "SMS" "Virtual"
* Menu "Provisión"
* Herramienta "Recepciones no identificadas"
* Menu herramienta


* Transaccionar MO DefaultSMS "Default_CualquierCosa"


* LogOut