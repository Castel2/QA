InsercionWS SMS - Iniciando_spec: 10.05_InsercionWS-SDP_SMS
=====================
Created by amartinez on 8/02/2017

Tags: InsercionWS, InsercionAdaptor

Se disponen varias inserciones al WS de SDP.

* Establecer host "" port "" dir ""
* Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"
* Cumplimentar Formulario y Login
* Establecer cliente
* Hash Map
* Lista Propiedades desde licencia
Escenario primero: Insercion adaptador NormalAuth WS
----------------------------------------------------
Given que existen los roles ADAPTOR_WS, ADAPTOR_WS_PRIORITY y ADAPTOR_WS_MASSIVE en el contrato
* Menu "Provisión"
* Herramienta "Gestión de aplicaciones"
* Menu herramienta
* Roles "wapppush" "ADAPTOR_WS"
* Roles "wapppush" "ADAPTOR_WS_PRIORITY"
* Roles "wapppush" "ADAPTOR_WS_MASSIVE"

When inyecta SMS (xml) al adaptador WS normalAuth
Se hace el cambio en el xml Estos son los parámetros: fname,  empresa,  ref_contract, gsm,  content
* Set XML SMS "push-sms.xml" "INNOVUS" "#wapppush" "virtual" "InsercionWS SMS" ""
Se inyecta el SMS en el adaptador. type, refProduct, adaptor
* Insercion WS "sms" "wapppush" "normalAuth"

Then verificamos en estadisticas que efectivamente se ha enviado el mensaje
* InicioHome
* Menu "Análisis"
* Herramienta "Detalle de contenidos de transacciones"
* Menu herramienta
* BusquedaCriterio "telefono" "virtual" "" ""
* InicioHome

*LogOut

Escenario segundo: Insercion adaptador PriorityAuth WS
------------------------------------------------------
Given Existe xml para ser inyectado
When inyecta SMS (xml) al adaptador WS priorityAuth
Se hace el cambio en el xml Estos son los parámetros: fname,  empresa,  ref_contract,  gsm,  content
* Set XML SMS "push-sms.xml" "INNOVUS" "#wapppush" "virtual" "InsercionWS SMS" ""
Se inyecta el SMS en el adaptador. type, refProduct, adaptor
* Insercion WS "sms" "wapppush" "priorityAuth"

Then verificamos en estadisticas que efectivamente se ha enviado el mensaje

* Menu "Análisis"
* Herramienta "Detalle de contenidos de transacciones"
* Menu herramienta
* BusquedaCriterio "telefono" "virtual" "" ""
* InicioHome
*LogOut

Escenario tercero: Insercion adaptador MassiveAuth WS
-----------------------------------------------------
When inyecta SMS (xml) al adaptador WS massiveAuth
Se hace el cambio en el xml Estos son los parámetros: fname,  empresa,  ref_contract, gsm,  content
* Set XML SMS "push-sms.xml" "INNOVUS" "#wapppush" "virtual" "InsercionWS SMS" ""
Se inyecta el SMS en el adaptador. type, refProduct, adaptor
* Insercion WS "sms" "wapppush" "massiveAuth"

Then verificamos en estadisticas que efectivamente se ha enviado el mensaje

* Menu "Análisis"
* Herramienta "Detalle de contenidos de transacciones"
* Menu herramienta
* BusquedaCriterio "telefono" "virtual" "" ""
* InicioHome

*LogOut