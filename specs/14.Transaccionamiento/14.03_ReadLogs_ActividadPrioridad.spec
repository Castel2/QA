Specification Heading
=====================
Created by ycastelblanco on 7/01/2020

This is an executable specification file which follows markdown syntax.
Every heading in this file denotes a scenario. Every bulleted point denotes a step.

* Establecer host "" port "" dir ""
* Establecer login "" user "" pass ""
* Establecer cliente
* Hash Map
* Lista Propiedades desde licencia

Scenario Prioridad 7
----------------
GIVEN
* Empresa "CAPITAL"
* Asignar Propiedad/Contrato WS "MT_SEND" "LIMSP" "CUSTOMER_PREFERENCES" "TRUE"
* Asignar Propiedad/Contrato WS "MT_SEND" "LIMSP" "PNS_APP" "mymappautomated"
* Asignar Propiedad/Contrato WS "MT_SEND" "LIMSP" "ORGANIZATION" "CAPITAL"
* Asignar Propiedad/Contrato WS "MT_SEND" "LIMSP" "INOT_MAX_CHN" "3"
* Asignar Propiedad/Contrato WS "MT_SEND" "LIMSP" "INOT_CHN_PREF" "CONTRACT"
* Asignar Propiedad/Contrato WS "MT_SEND" "LIMSP" "PRIORITY" "7"
* Forzar recarga WS

WHEN se inyecta PNS y emal (xml) al adaptador WS normalAuth
* Set XML INOT "push-inot.xml" "CAPITAL" "MT_SEND" "usercapital" "Prueba INOT "
* Insercion WS "inot" "AppAUTOMATED" "normalAuth"
* Esperar "10"

THEN
Validar la prioridad asignada a los mensajes con los logs de actividad
* Establecer Instancia
* Establecer datos de acceso a AWS Tools
* Obtener ruta QA TOOLS
* Analizar logs actividad "prioridad"

//LIMPIAR
* Establecer login "" user "" pass ""
* Eliminar Propiedad/Contrato WS refContract"MT_SEND" refPlatform"LIMSP" property"CUSTOMER_PREFERENCES"
* Eliminar Propiedad/Contrato WS refContract"MT_SEND" refPlatform"LIMSP" property"PNS_APP"
* Eliminar Propiedad/Contrato WS refContract"MT_SEND" refPlatform"LIMSP" property"ORGANIZATION"
* Eliminar Propiedad/Contrato WS refContract"MT_SEND" refPlatform"LIMSP" property"INOT_MAX_CHN"
* Eliminar Propiedad/Contrato WS refContract"MT_SEND" refPlatform"LIMSP" property"INOT_CHN_PREF"
* Eliminar Propiedad/Contrato WS refContract"MT_SEND" refPlatform"LIMSP" property"PRIORITY"
* Forzar recarga WS

Scenario prioridad 2
----------------
GIVEN
* Empresa "CAPITAL"
* Asignar Propiedad/Contrato WS "MT_SEND" "LIMSP" "CUSTOMER_PREFERENCES" "TRUE"
* Asignar Propiedad/Contrato WS "MT_SEND" "LIMSP" "PNS_APP" "mymappautomated"
* Asignar Propiedad/Contrato WS "MT_SEND" "LIMSP" "ORGANIZATION" "CAPITAL"
* Asignar Propiedad/Contrato WS "MT_SEND" "LIMSP" "INOT_MAX_CHN" "3"
* Asignar Propiedad/Contrato WS "MT_SEND" "LIMSP" "INOT_CHN_PREF" "CONTRACT"
* Asignar Propiedad/Contrato WS "MT_SEND" "LIMSP" "PRIORITY" "2"
* Forzar recarga WS

WHEN se inyecta PNS y emal (xml) al adaptador WS normalAuth
* Set XML INOT "push-inot.xml" "CAPITAL" "MT_SEND" "usercapital" "Prueba INOT "
* Insercion WS "inot" "AppAUTOMATED" "normalAuth"
* Esperar "10"

THEN
Validar la prioridad asignada a los mensajes con los logs de actividad
* Establecer Instancia
* Establecer datos de acceso a AWS Tools
* Obtener ruta QA TOOLS
* Analizar logs actividad "prioridad"

//LIMPIAR
* Establecer login "" user "" pass ""
* Eliminar Propiedad/Contrato WS refContract"MT_SEND" refPlatform"LIMSP" property"CUSTOMER_PREFERENCES"
* Eliminar Propiedad/Contrato WS refContract"MT_SEND" refPlatform"LIMSP" property"PNS_APP"
* Eliminar Propiedad/Contrato WS refContract"MT_SEND" refPlatform"LIMSP" property"ORGANIZATION"
* Eliminar Propiedad/Contrato WS refContract"MT_SEND" refPlatform"LIMSP" property"INOT_MAX_CHN"
* Eliminar Propiedad/Contrato WS refContract"MT_SEND" refPlatform"LIMSP" property"INOT_CHN_PREF"
* Eliminar Propiedad/Contrato WS refContract"MT_SEND" refPlatform"LIMSP" property"PRIORITY"
* Forzar recarga WS