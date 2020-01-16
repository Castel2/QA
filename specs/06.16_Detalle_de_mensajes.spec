Detalle de Mensajes - Iniciando_spec: Detalle_de_mensajes
=========================================================
Created by amartinez on 17/07/2019

This is an executable specification file which follows markdown syntax.
Every heading in this file denotes a scenario. Every bulleted point denotes a step.

* Establecer host "" port "" dir ""
* Establecer login "" user "" pass ""
* Establecer cliente
* Hash Map
* Lista Propiedades desde licencia

Escenario primero: Verificación GUI Detalle de mensajes
-------------------------------------------------------
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
* ldmcontable "histAndSummary"

THEN
* refproduct "AppAUTOMATED"
* Nombre contrato "AppAUTOMATED Automated"

* Establecer login "CAPITAL" user "USER1" pass "USER1"
* Cumplimentar Formulario y Login
* InicioHome
* Menu "Herramientas"
* Herramienta "Detalle de mensajes Automated"
* Menu herramienta
* Verificar tipo Mensajes
* Filtro Básico filtrar por tipo Mensaje "SMS"
* Filtro Básico filtrar por tipo Mensaje "EMAIL"
* Filtro avanzado Filtrar por tipo de mensaje "PNS"
* Ver detalle de Mensaje Notificación tipoMensaje "EMAIL" proveedor"EMail-SMTP" canal"comercial@latinia.com" destinatario"usercapital@latinia.com"
* ver detalle contenido mensaje tipoMensaje"EMAIL"
* ver detalle estados mensaje tipoMEnsaje"EMAIL"
* Filtro Avanzado filtrar mensaje por ID de Transacción

* Ver detalle de Mensaje Notificación tipoMensaje "SMS" proveedor"Virtual" canal"+000001" destinatario"+34659000001"
* ver detalle contenido mensaje tipoMensaje"SMS"
* ver detalle estados mensaje tipoMEnsaje"SMS"
* Filtro Avanzado filtrar mensaje por ID de Transacción

* Ver detalle de Mensaje Notificación tipoMensaje "PNS" proveedor"Google" canal"" destinatario"usercapital"
* ver detalle contenido mensaje tipoMensaje"PNS"
* ver detalle estados mensaje tipoMEnsaje"PNS"
* Filtro Avanzado filtrar mensaje por ID de Transacción

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