Genera y verifica todas las combinaciones posibles desde la herramienta WappPush (SMS)  - Iniciando_spec: 01.07_MT_SMS
======================================================================================

Tags:Transaccionamiento,sms,01.07_MT_SMS,Smoke,Regresion
Preconditions:
Metadata:ScenarioType=Auto

Explicación.-
Este escenario se supone que toda la aprovisión ya está hecha. Es decir usuario, Contratos, canales y colectores, etc...Tan solo, De manera inteligente, el propio escenario comprueba que las 'cláusulas' están creadas en el contrato.
   - G.Canales: Se asegura que las salidas 'Unknown' y 'Virtual' salen por los colectores 'Unknown' y 'Virtual' respectivamente.
   - WappPush: Se realizan diversas combinaciones de envios MT con la aplicación WappPush
   - Para los diferidos se va a las Estadísticas para comprobar que los estados son correctos, particularmente el caso del EXPIRADO

Dependencias.-
En la línea 'ComprobarClausulas', Revisar que la Plataforma dispone de los operadores allí indicados, de lo contrario no pasará.


ATENCION Para la prueba de los MT expirados, se requiere que se expiren los mensajes que llegan fuera del rango de (tsDelivery y tsDeliveryEnd) y además tampoco tienen informacion en el (tsExpire). Para ello es necesario establecer en el LConfig
                lmod-core.properties
                config/bus/core/DeliveryManager/adjustDeliveryTime = false


Estos son los steps denominados 'de contexto' (ver: http://getgauge.io/documentation/user/current/gauge_terminologies/contexts.html)
Estos steps se ejecutan antes de cada escenario de este spec.
* Establecer host "" port "" dir ""
* Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"
* Cumplimentar Formulario y Login
* Establecer cliente
* Hash Map
* Lista Propiedades desde licencia


Envio SMS desde la herramienta wapppush
---------------------------------------

Tags:wapppush,CI
Given Existen las cláusulas con diferentes canales para el envío de SMS
* Menu "Provisión"
* Herramienta "Gestión de canales"
* Menu herramienta
* Rutas "Virtual" "+000001" "SMS" "Colector Virtual"
* Menu herramienta
* Rutas "Unknown" "+000001" "SMS" "Colector Virtual"

* tipoclause "sms"
* InicioHome
* Herramienta "Gestión de contratos"
* Nombre contrato "Envíos SMS"
* Menu herramienta
* Empresa "INNOVUS"
* InicioHome
* Menu herramienta
* ComprobarClausulas "Virtual"

When se envía diferentes mensajes por los diferentes canales(clausulas)
* Menu "Diagnosis"
* Herramienta "Envíos SMS"
* Menu herramienta
* Transaccionar EnviosSMS "unknown" "Unknown ñ Ñ < > $ € ! ' á é í ó ú à è ì ò ù"
* Transaccionar EnviosSMS "virtual" "Virtual ñ Ñ < > $ € ! ' á é í ó ú à è ì ò ù"
* Transaccionar EnviosSMS Diferido "virtual" "Diferido Virtual"

Ejecuto Hist&Summary para tener estadísticas
* ldmcontable "histAndSummary"

Then se visualiza en estadisticas el estado "Entrega confirmada a usuario" para uno de los mensajes enviados
* InicioHome
* Menu "Análisis"
* Herramienta "Detalle de contenidos de transacciones"
* Menu herramienta
* Nombre contrato "Virtual"
* Ver listado "Envíos SMS"
* Ver estados "Entregado al proveedor"
* Ver estados "Entrega confirmada a usuario"
* InicioHome
* LogOut

Reenvio SMS desde el CMC
------------------------
SDP-553
Given que existe la organización y usuario
* Asignar Rol WS "lman-vcontent" "LD_PSTORAGE"
* Empresa "CAPITAL"
* Organizacion "ORG1_CAPITAL"
* Crear ORG
* refUser "userOrgCapital1"
* Crear suscriptor WS
AND el usuario tiene aprovisionadas las propiedades gsm y email
* Nueva tabla
|PROPIEDAD|VALOR|
|gsm|virtual|
|email|userOrgCapital1@test.com|
* Organizacion "ORG1_CAPITAL"
* Agregar propiedad desde WS
AND el contrato tiene asignadas las diferentes propiedades para el reenvío de SMS
* Empresa "CAPITAL"
* Asignar Propiedad/Contrato WS "MT_SEND" "LIMSP" "ORGANIZATION" "ORG1_CAPITAL"
* Asignar Propiedad/Contrato WS "MT_SEND" "LIMSP" "ENABLE_MSG_RESEND" "TRUE"
* Asignar Propiedad/Contrato WS "MT_SEND" "LIMSP" "CUSTOMER_PREFERENCES" "TRUE"
* forzarrecarga
AND se ha enviado un SMS previo
* Set XML SMS "push-sms-refuser.xml" "CAPITAL" "MT_SEND" "virtual" "InsercionWS SMS" "userOrgCapital1"
Se inyecta el SMS en el adaptador. type, refProduct, adaptor
* Insercion WS "sms" "AppAUTOMATED" "normalAuth"
* ldmcontable "histAndSummary"
* Esperar "4"
* ldmcontable "histAndSummary"
* Esperar "4"
* Comprobar Msg refContract"MT_SEND"
* Comprobar estadosWS "Entrega confirmada a usuario"
When se reenvía el SMS
* Empresa "ORG1_CAPITAL"
* Menu "Provisión"
* Herramienta "Customer Management Center"
* Menu herramienta
* Buscar "userOrgCapital1"
* reenviar mensaje "SMS"
* ldmcontable "histAndSummary"
Then deben de existir 2 mensajes enviados en las estadisticas
* Empresa "CAPITAL"
* Comprobar cantidad de mensajes cantidad"2" refContract"MT_SEND"
* Eliminar suscriptor WS "ORG1_CAPITAL" "userOrgCapital1"
* Elimina ORG "ORG1_CAPITAL"

* Eliminar Propiedad/Contrato WS refContract"MT_SEND" refPlatform"LIMSP" property"ORGANIZATION"
* Eliminar Propiedad/Contrato WS refContract"MT_SEND" refPlatform"LIMSP" property"ENABLE_MSG_RESEND"
* Eliminar Propiedad/Contrato WS refContract"MT_SEND" refPlatform"LIMSP" property"CUSTOMER_PREFERENCES"

LogOut:
* LogOut