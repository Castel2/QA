Estadísticas de trafico - Iniciando_spec: Estadisticas_de _trafico
==================================================================
Created by amartinez on 17/07/2019

This is an executable specification file which follows markdown syntax.
Every heading in this file denotes a scenario. Every bulleted point denotes a step.

* Establecer host "" port "" dir ""
* Establecer login "" user "" pass ""
* Establecer cliente
* Hash Map
* Lista Propiedades desde licencia

Escenario primero:
----------------
GIVEN
* Empresa "CAPITAL"
* Asignar Propiedad/Contrato WS "MT_SEND" "LIMSP" "CUSTOMER_PREFERENCES" "TRUE"
* Asignar Propiedad/Contrato WS "MT_SEND" "LIMSP" "PNS_APP" "mymappautomated"
* Asignar Propiedad/Contrato WS "MT_SEND" "LIMSP" "ORGANIZATION" "CAPITAL"
* Asignar Propiedad/Contrato WS "MT_SEND" "LIMSP" "INOT_MAX_CHN" "4"
* Asignar Propiedad/Contrato WS "MT_SEND" "LIMSP" "INOT_CHN_PREF" "CONTRACT"
* Forzar recarga WS

WHEN inyecta PNS (xml) al adaptador WS normalAuth
Se hace el cambio en el xml Estos son los parámetros: fname,  empresa,  ref_contract, ref_app,  clave,  clavevalor,  content
* Set XML INOT "push-inot.xml" "CAPITAL" "MT_SEND" "usercapital" "Prueba INOT "
* Insercion WS "inot" "AppAUTOMATED" "normalAuth"

* Establecer login "CAPITAL" user "USER1" pass "USER1"
* Cumplimentar Formulario y Login
* InicioHome
* Menu "Herramientas"
* Herramienta "Detalle de mensajes Automated"
* Menu herramienta
* Esperar "20"
* Verificar tipo Mensajes
* ldmcontable "histAndSummary"
* InicioHome
* Herramienta "Estadísticas de tráfico Automated"
* Menu herramienta
* Número de mensajes privios al envío
* Set XML INOT "push-inot.xml" "CAPITAL" "MT_SEND" "usercapital" "Prueba INOT "
* Insercion WS "inot" "AppAUTOMATED" "normalAuth"
* Esperar "5"
* ldmcontable "histAndSummary"
* InicioHome
* Esperar "3"
* Menu herramienta
* Esperar "5"

THEN se verifican los elementos gráficos de la pestaña "tráfico por servicios"
* Verificar tráfico por servicio
* Esperar "5"
* InicioHome
* LogOut

//LIMPIAR
* Establecer login "" user "" pass ""
* Eliminar Propiedad/Contrato WS refContract"MT_SEND" refPlatform"LIMSP" property"CUSTOMER_PREFERENCES"
* Eliminar Propiedad/Contrato WS refContract"MT_SEND" refPlatform"LIMSP" property"PNS_APP"
* Eliminar Propiedad/Contrato WS refContract"MT_SEND" refPlatform"LIMSP" property"ORGANIZATION"
* Eliminar Propiedad/Contrato WS refContract"MT_SEND" refPlatform"LIMSP" property"INOT_MAX_CHN"
* Eliminar Propiedad/Contrato WS refContract"MT_SEND" refPlatform"LIMSP" property"INOT_CHN_PREF"
* Forzar recarga WS
