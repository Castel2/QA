Specification Heading - Iniciando_spec: 09.04_PNS-Properties
=====================
Tags: 09.04_PNS-Properties
Created by amartinez on 7/09/2017

This is an executable specification file which follows markdown syntax.
Every heading in this file denotes a scenario. Every bulleted point denotes a step.
     
* Establecer host "" port "" dir ""
* Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"
* Cumplimentar Formulario y Login
* Establecer cliente
* Hash Map
* Lista Propiedades desde licencia
Escenario primero
-----------------
Se prueba la propiedad aplicacion/contrato "ORGANIZATION"
release 395-3.1.0
Voy a innovus y creo dos organizaciones asociadas a CAPITAL
* Menu "Provisión"
* Herramienta "Gestión de empresas"
* Menu herramienta
* Crear Organizacion "COMPANY1" "CAPITAL"
* Crear Organizacion "COMPANY2" "CAPITAL"
* LogOut
Desde CAPITAL creamos una MApp asociada a company2
* Establecer login "CAPITAL" user "USER1" pass "USER1"
* Cumplimentar Formulario y Login
* InicioHome
* Menu "--"
* Herramienta "Gestión de M-Apps Automated"
* Menu herramienta
* Proposito "provision"
* PNS_MApp_Manager "application" "company2"
* LogOut
vuelvo a INNOVUS y cambio la propiedad Aplicacion/contrato ORGANIZATION=COMPANY1, y enviamos una pns al usuario1 asociado a company2, el mensaje se descarta inmediatamente porque el usuario esta asociado a una organizacion diferente a la que se encuentra en el contrato
* Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"
* Cumplimentar Formulario y Login
* InicioHome
* Proposito "provision"
* Empresa "company2"
* Herramienta "Customer Management Center"
* Menu herramienta
* CrearSuscriptor "usuario1" "--"
* MApps "usuario1" "application0"
* InicioHome
* Empresa "CAPITAL"
* Herramienta "Gestión de contratos"
* Menu herramienta
* ConProps "Envíos de PushNots Automated" "ORGANIZATION" "COMPANY1"
* Menu herramienta
* ConProps "Envíos de PushNots Automated" "CUSTOMER_PREFERENCES" "TRUE"
* Menu herramienta
* ConProps "Envíos de PushNots Automated" "PNS_APP" "application0"
* forzarrecarga
* Set XML PNSunitario "push-pns-unitario.xml" "CAPITAL" "MT_PNS" "application0" "refuser" "usuario1" "Prueba PNS con propiedad ORGANIZATION"
* Insercion WS "pns" "wtest-pns" "normalAuth"
* LogOut
Voy a CAPITAL y verifico que el mensaje se haya descartado, no debe aparecer en estadisticas
* Establecer login "CAPITAL" user "USER1" pass "USER1"
* Cumplimentar Formulario y Login
* InicioHome
* Menu "--"
* Herramienta "Detalle de transacciones Automated"
* Menu herramienta
* BusquedaCriterio "#refUser" "usuario1" "noaparecer" "company2"
* LogOut
Vuelvo a INNOVUS y hago el cambio en la propiedad ORGANIZATION = COMPANY2, a esta organización es a la que esta asociado el usuario1, se envia nuevamente el mensaje y este no se debe descartar
* Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"
* Cumplimentar Formulario y Login
* InicioHome
* Herramienta "Gestión de contratos"
* Menu herramienta
* ConProps "Envíos de PushNots Automated" "ORGANIZATION" "COMPANY2"
* forzarrecarga
* Set XML PNSunitario "push-pns-unitario.xml" "CAPITAL" "MT_PNS" "application0" "refuser" "usuario1" "Prueba PNS con propiedad ORGAIZATION"
* Insercion WS "pns" "wtest-pns" "normalAuth"
* LogOut
Voy a CAPITAL y verifico en Detalle de transacciones que efectivamente se envia el mensaje
* Establecer login "CAPITAL" user "USER1" pass "USER1"
* Cumplimentar Formulario y Login
* InicioHome
* Menu "--"
* Herramienta "Detalle de transacciones Automated"
* Menu herramienta
* BusquedaCriterio "#refUser" "usuario1" "" "company2"
* Ver estados "Entrega confirmada a usuario"
* LogOut

release 395-3.2.0
Se verifica que exista el contrato, de no ser así se crea y se agregan las clausulas pns y smsMT
* Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"
* Cumplimentar Formulario y Login
* Empresa "CAPITAL"
* tipoclause "sms"
* Menu "Provisión"
* Herramienta "Gestión de contratos"
* Nombre contrato "AppAUTOMATED"
* Menu herramienta
* ComprobarClausulas "Virtual"
 ComprobarClausulas "Movistar-ES"
* InicioHome
* forzarrecarga
* Herramienta "Gestión de contratos"
* Menu herramienta
* ConProps "AppAUTOMATED Automated" "ORGANIZATION" "CAPITAL"
* Menu herramienta
* ConProps "AppAUTOMATED Automated" "CUSTOMER_PREFERENCES" "TRUE"
* Menu herramienta
* ConProps "AppAUTOMATED Automated" "INOT_MAX_CHN" "4"
* Menu herramienta
* ConProps "AppAUTOMATED Automated" "INOT_CHN_PREF" "CONTRACT"
* Menu herramienta
* ConProps "AppAUTOMATED Automated" "PNS_APP" "*"
* Menu herramienta
* ConProps "AppAUTOMATED Automated" "PNS_DOWNLOADABLE" "TRUE"
* Menu herramienta
* ConProps "AppAUTOMATED Automated" "PNS_CONFIDENTIAL" "TRUE"
* Menu herramienta
* ConProps "AppAUTOMATED Automated" "PNS_CONFIDENTIAL_NONE" "NEXT_CHANNEL"
* forzarrecarga
* InicioHome
* Proposito "provision"
* Herramienta "Customer Management Center"
* Menu herramienta
* Agregar Suscriptor "usuario2"
* Delete Mapp from User "usuario2"
* MApps "usuario2" "myMAppAutomated0"
* MApps "usuario2" "myMAppAutomated0"
* Eliminar Propiedad "usuario2" "gsm"
*forzarrecarga
* LogOut
* Login RGTester
* Set XML INOT "push-inot.xml" "CAPITAL" "MT_SEND" "usuario2" "Prueba INOT CONFIDENTIAL"
* Insercion JMS "inot" "AppAUTOMATED" "normalAuth"
verificar en descartados
* Cumplimentar Formulario y Login
* Menu "Control"
* Herramienta "Mensajes descartados o erróneos"
* Menu herramienta
* Buscar descartados por el idMsg "Prueba inot"
* InicioHome
establecer propiedad gsm
* Menu "Provisión"
* Herramienta "Customer Management Center"
* Menu herramienta
* Establecer Propiedad "usuario2" "gsm" "609000001"
* Obtener UUID "CAPITAL" "usuario2" "0"
* Establecer Mapp Confidencial "CAPITAL" "usuario2" "true"
* Verificar confidencial TRUE "CAPITAL" "usuario2" "uuid"
enviar pns
* Set XML PNSunitario "push-pns-unitario.xml" "CAPITAL" "MT_SEND" "myMAppAutomated0" "refuser" "usuario2" "Prueba PNS CONFIDENTIAL"
* Insercion WS "pns" "AppAUTOMATED" "normalAuth"
verificar que se envia al uuid confidencial
* LogOut
Voy a CAPITAL y verifico en Detalle de transacciones que efectivamente se envia el mensaje
* Establecer login "CAPITAL" user "USER1" pass "USER1"
* Cumplimentar Formulario y Login
* InicioHome
* Menu "--"
* Herramienta "Detalle de transacciones Automated"
* Menu herramienta
* BusquedaCriterio "#refUser" "usuario2" "" "CAPITAL"
* Ver estados "Entrega confirmada a usuario"
* Verificar UUID
* LogOut
enviar inot
* Login RGTester
* Set XML INOT "push-inot.xml" "CAPITAL" "MT_SEND" "usuario2" "Prueba INOT CONFIDENTIAL2"
* Insercion JMS "inot" "AppAUTOMATED" "normalAuth"
verificar que sale por el uuid confidential y sms
* Establecer login "CAPITAL" user "USER1" pass "USER1"
* Cumplimentar Formulario y Login
* InicioHome
* Menu "--"
* Herramienta "Detalle de transacciones Automated"
* Menu herramienta
* BusquedaCriterio "#refUser" "usuario2" "" "CAPITAL"
* Nombre contrato "+34609000001"
* Ver estados "Entrega confirmada a usuario"
* Obtener UUID "CAPITAL" "usuario2" "1"
* Establecer Mapp Confidencial "CAPITAL" "usuario2" "true"
* Verificar respuesta uuid FALSE "CAPITAL" "usuario2"
enviar pns
* Set XML PNSunitario "push-pns-unitario.xml" "CAPITAL" "MT_SEND" "myMAppAutomated0" "refuser" "usuario2" "Prueba PNS CONFIDENTIAL2"
* Insercion WS "pns" "AppAUTOMATED" "normalAuth"
verificar que se envia al uuid confidencial
* InicioHome
* Menu "--"
* Herramienta "Detalle de transacciones Automated"
* Menu herramienta
* BusquedaCriterio "#refUser" "usuario2" "" "CAPITAL"
* Ver estados "Entrega confirmada a usuario"
* Verificar UUID
* LogOut
PNS_DOWNLOADABLE
* Establecer login "CAPITAL" user "USER1" pass "USER1"
* Cumplimentar Formulario y Login
* Set XML PNSunitario "push-pns-unitario.xml" "CAPITAL" "MT_SEND" "myMAppAutomated0" "refuser" "usuario2" "Prueba PNS_DOWNLOADABLE = TRUE"
* Insercion WS "pns" "AppAUTOMATED" "normalAuth"
* Menu "--"
* Herramienta "Detalle de transacciones Automated"
* Menu herramienta
* BusquedaCriterio "#refUser" "usuario2" "" "CAPITAL"
* Ver estados "Entrega confirmada a usuario"
* Obtener IdMsg
* Obtener contenido PNS "CAPITAL" "myMAppAutomated0" "usuario2"
* LogOut
* Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"
* Cumplimentar Formulario y Login
* Menu "Provisión"
* Herramienta "Gestión de contratos"
* Menu herramienta
* ConProps "AppAUTOMATED Automated" "PNS_DOWNLOADABLE" "FALSE"
* valor propiedad "PNS_DOWNLOADABLE" "FALSE"
* forzarrecarga
* LogOut
* Establecer login "CAPITAL" user "USER1" pass "USER1"
* Cumplimentar Formulario y Login
* Set XML PNSunitario "push-pns-unitario.xml" "CAPITAL" "MT_SEND" "myMAppAutomated0" "refuser" "usuario2" "Prueba PNS_DOWNLOADABLE =FALSE"
* Insercion WS "pns" "AppAUTOMATED" "normalAuth"
* Menu "--"
* Herramienta "Detalle de transacciones Automated"
* Menu herramienta
* BusquedaCriterio "#refUser" "usuario2" "" "CAPITAL"
* Ver estados "Entrega confirmada a usuario"
* Obtener IdMsg
* Obtener contenido PNS "CAPITAL" "myMAppAutomated0" "usuario2"
* LogOut

setUserSomeProperties
* Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"
* Cumplimentar Formulario y Login
* Empresa "CAPITAL"
* refUser "usuario2"
* Menu "Provisión"
* Herramienta "Customer Management Center"
* Menu herramienta
* Eliminar Propiedad "usuario2" "gsm"
* Eliminar Propiedad "usuario2" "cclist"
* Organizacion "CAPITAL"
* Obtener Propiedades desde Usuario
* Existen Propiedades
* Nueva tabla
|PROPIEDAD|VALOR|
|gsm|609000008|
* Agregar propiedad desde WS
* Obtener Propiedades desde Usuario
* Existen Propiedades
* Nueva tabla
|PROPIEDAD|VALOR|
|cclist|1234,5678,2468|
* Agregar propiedad desde WS
* Obtener Propiedades desde Usuario
* Existen Propiedades
* Nueva tabla
|PROPIEDAD|VALOR|
|cclist|5678|
* Agregar propiedad desde WS
* Obtener Propiedades desde Usuario
* Existen Propiedades
* LogOut

* Eliminar Propiedad/Contrato WS refContract"MT_SEND" refPlatform"LIMSP" property"CUSTOMER_PREFERENCES"
* Eliminar Propiedad/Contrato WS refContract"MT_SEND" refPlatform"LIMSP" property"PNS_APP"
* Eliminar Propiedad/Contrato WS refContract"MT_SEND" refPlatform"LIMSP" property"ORGANIZATION"
* Eliminar Propiedad/Contrato WS refContract"MT_SEND" refPlatform"LIMSP" property"INOT_MAX_CHN"
* Eliminar Propiedad/Contrato WS refContract"MT_SEND" refPlatform"LIMSP" property"INOT_CHN_PREF"
* Eliminar Propiedad/Contrato WS refContract"MT_SEND" refPlatform"LIMSP" property"PNS_DOWNLOADABLE"
* Eliminar Propiedad/Contrato WS refContract"MT_SEND" refPlatform"LIMSP" property"PNS_CONFIDENTIAL"
* Eliminar Propiedad/Contrato WS refContract"MT_SEND" refPlatform"LIMSP" property"PNS_CONFIDENTIAL_NONE"


