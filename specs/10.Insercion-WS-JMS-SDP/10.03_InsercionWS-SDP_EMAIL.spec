InsercionWS EMAIL - Iniciando_spec: 10.03_InsercionWS-SDP_EMAIL
=================================================
Created by xruiz on 12/08/2016

Tags: InsercionWS, InsercionAdaptor

Se trata de traer a este 'spec' en Gauge, el que actualmente está en Automated.
En curso.....
Ultima actualizacion 09/02/2017

Se disponen varias inserciones al WS de SDP.
El objetivo de este escenario es poder controlar perfectamente los parametros que se inyectan en el Adaptador.


Estos son los steps denominados 'de contexto' (ver: http://getgauge.io/documentation/user/current/gauge_terminologies/contexts.html)
Estos steps se ejecutan antes de cada escenario de este spec.

* Establecer host "" port "" dir ""
* Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"
* Cumplimentar Formulario y Login
* Establecer cliente
* Hash Map
* Lista Propiedades desde licencia

Escenario primero: Insercion adaptador NormalAuth WS
----------------------------------------------------
Given Existe xml para ser inyectado y existen los roles ADAPTOR_WS, ADAPTOR_WS_PRIORITY y ADAPTOR_WS_MASSIVE en el contrato
* Menu "Provisión"
* Herramienta "Gestión de aplicaciones"
* Menu herramienta
* Roles "wapppushemail" "ADAPTOR_WS"
* Roles "wapppushemail" "ADAPTOR_WS_PRIORITY"
* Roles "wapppushemail" "ADAPTOR_WS_MASSIVE"

When inyecta EMAIL (xml) al adaptador WS normalAuth
Se hace el cambio en el xml Estos son los parámetros: fname,  empresa,  ref_contract, destinatario,  subject,  content
* Set XML EMAIL "push-email.xml" "INNOVUS" "#wapppushemail" "prueba@gmail.com" "Insercion WS " "Texto Insercion"
Se inyecta el EMAIL en el adaptador. type, refProduct, adaptor
* Insercion WS "email" "wapppushemail" "normalAuth"

Then verificamos en estadisticas que efectivamente se ha enviado el mensaje

* InicioHome
* Menu "Análisis"
* Herramienta "Detalle de contenidos de transacciones de email"
* Menu herramienta
* BusquedaCriterio "email" "prueba@gmail.com" "" "LATINIA"
* InicioHome

* LogOut

Escenario Segundo: Insercion adaptador PriorityAuth WS
------------------------------------------------------
When inyecta EMAIL (xml) al adaptador WS priorityAuth
Se hace el cambio en el xml Estos son los parámetros: fname,  empresa,  ref_contract, destinatario,  subject,  content
* Set XML EMAIL "push-email.xml" "INNOVUS" "#wapppushemail" "prueba@gmail.com" "Insercion WS " "Texto Insercion"
Se inyecta el EMAIL en el adaptador. type, refProduct, adaptor
* Insercion WS "email" "wapppushemail" "priorityAuth"

Then verificamos en estadisticas que efectivamente se ha enviado el mensaje

* InicioHome
* Menu "Análisis"
* Herramienta "Detalle de contenidos de transacciones de email"
* Menu herramienta
* BusquedaCriterio "email" "prueba@gmail.com" "" "LATINIA"
* InicioHome

* LogOut

Escenario Tercero: Insercion adaptador MassiveAuth WS
------------------------------------------------------
When inyecta EMAIL (xml) al adaptador WS massiveAuth
Se hace el cambio en el xml Estos son los parámetros: fname,  empresa,  ref_contract, destinatario,  subject,  content
* Set XML EMAIL "push-email.xml" "INNOVUS" "#wapppushemail" "prueba@gmail.com" "Insercion WS " "Texto Insercion"
Se inyecta el EMAIL en el adaptador. type, refProduct, adaptor
* Insercion WS "email" "wapppushemail" "massiveAuth"

Then verificamos en estadisticas que efectivamente se ha enviado el mensaje

* InicioHome
* Menu "Análisis"
* Herramienta "Detalle de contenidos de transacciones de email"
* Menu herramienta
* BusquedaCriterio "email" "prueba@gmail.com" "" "LATINIA"
* InicioHome

* LogOut

//Insercion mensaje mensaje malformado XML
//----------------------------------------
//String fname, String empresa, String ref_product, String ref_contract, String gsmdest, String ref_app, String clave, String clavevalor, String destinatario, String subject
//* Mensaje Malformado XML "SMS-malformed.xml" "INNOVUS" "wapppush" "#wapppush" "659000001" "" "" "" "" ""
// Mensaje Malformado XML "PNS-malformed.xml" "INNOVUS" "wtest-pns" "#wtest-pns" "" "mymappautomated0" "refuser" "liliuser" "" ""
// PushPNSunitario "PNS-malformed.xml" "INNOVUS" "wtest-pns" "#wtest-pns" "mymappautomated0" "refuser" "usercapital" "Unitario-Tiene un saldo de 88eur "
//* Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"
//* Cumplimentar Formulario y Login
//* Menu "Control"
//* Herramienta "Mensajes descartados o erróneos"
//* Menu herramienta
//* Mensajes Descartados

<- * LogOut