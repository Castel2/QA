Mueve transacción Pull-Push virtual JMS mediante uso del 'Simulador de móvil' - Iniciando_spec: 01.05_ServiceAdaptorJMS
============================================================================

Tags:Transaccionamiento,sms,01.05_ServiceAdaptorJMS,MO,Regresion,Smoke,CI
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
Inicio,Navegacion,Almacen de Datos, Gestion de Canales:
Envio de MO por JMS
-----------------
Give existe la provisión necesaria para el envío de mensajes MO por JMS
* Hash Map
* InicioHome
* Menu "Provisión"
* Herramienta "Gestión de canales"
* Menu herramienta
* Rutas "Virtual" "+000001" "SMS" "Virtual"
* Menu herramienta
* Rutas "Unknown" "+000001" "SMS" "Virtual"
* Menu "Diagnosis"
* Herramienta "Simulador de móvil"
* Menu herramienta

When Se envía el mensaje pull-push desde el simulador móvil
* TransaccionesPullPush
|Aplicacion|Texto|Respuesta|
|jms1|ñ Ñ < > # € ! ' á é í ó ú à è ì ò ù|Servicio JMS. Servicio ofrecido por JMS normal (ñ Ñ < > # € ! ' á é í ó ú à è ì ò ù)|
|jms1 priority|Validación 3.3.15 JMS(01) ñ Ñ < > $ € ! ' á é í ó ú à è ì ò ù|Servicio JMS. Servicio ofrecido por JMS prioritario (priority.Validación 3.3.15 JMS(01) ñ Ñ < > $ € ! ' á é í ó ú à è ì ò ù)|

Then retorna la respuesta esperada (campo respuesta de la tabla anterior)


* LogOut