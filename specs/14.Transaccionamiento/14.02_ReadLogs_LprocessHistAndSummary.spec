Specification Heading
=====================
Created by dramirez on 15/10/2019

This is an executable specification file which follows markdown syntax.
Every heading in this file denotes a scenario. Every bulleted point denotes a step.

* Establecer host "" port "" dir ""
* Establecer login "" user "" pass ""
* Establecer cliente
* Hash Map
* Lista Propiedades desde licencia

Scenario Heading
----------------
GIVEN
* Empresa "CAPITAL"
* Asignar Propiedad/Contrato WS "MT_SEND" "LIMSP" "CUSTOMER_PREFERENCES" "TRUE"
* Asignar Propiedad/Contrato WS "MT_SEND" "LIMSP" "PNS_APP" "mymappautomated"
* Asignar Propiedad/Contrato WS "MT_SEND" "LIMSP" "ORGANIZATION" "CAPITAL"
* Asignar Propiedad/Contrato WS "MT_SEND" "LIMSP" "INOT_MAX_CHN" "3"
* Asignar Propiedad/Contrato WS "MT_SEND" "LIMSP" "INOT_CHN_PREF" "CONTRACT"
* Forzar recarga WS

WHEN inyecta PNS (xml) al adaptador WS normalAuth
Se hace el cambio en el xml Estos son los parámetros: fname,  empresa,  ref_contract, ref_app,  clave,  clavevalor,  content
* Set XML INOT "push-inot.xml" "CAPITAL" "MT_SEND" "usercapital" "Prueba INOT "
* Insercion WS "inot" "AppAUTOMATED" "normalAuth"
* Esperar "5"

THEN
´Validar Ejecución correcta de histAndSummary analizando logs
* Establecer Instancia
* Establecer datos de acceso a AWS Tools
* Obtener ruta QA TOOLS
* ldmcontable "histAndSummary"
* Analizar logs lproces

//LIMPIAR
* Establecer login "" user "" pass ""
* Eliminar Propiedad/Contrato WS refContract"MT_SEND" refPlatform"LIMSP" property"CUSTOMER_PREFERENCES"
* Eliminar Propiedad/Contrato WS refContract"MT_SEND" refPlatform"LIMSP" property"PNS_APP"
* Eliminar Propiedad/Contrato WS refContract"MT_SEND" refPlatform"LIMSP" property"ORGANIZATION"
* Eliminar Propiedad/Contrato WS refContract"MT_SEND" refPlatform"LIMSP" property"INOT_MAX_CHN"
* Eliminar Propiedad/Contrato WS refContract"MT_SEND" refPlatform"LIMSP" property"INOT_CHN_PREF"
* Forzar recarga WS