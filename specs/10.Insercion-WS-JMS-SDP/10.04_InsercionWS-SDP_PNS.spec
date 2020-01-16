InsercionWS PNS - Iniciando_spec: 10.04_InsercionWS-SDP_PNS
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
* Roles "wtest-pns" "ADAPTOR_WS"
* Roles "wtest-pns" "ADAPTOR_WS_PRIORITY"
* Roles "wtest-pns" "ADAPTOR_WS_MASSIVE"
* Empresa "INNOVUS"
* InicioHome
* Herramienta "Gestión de contratos"
* Menu herramienta
* ConProps "Envíos de PushNots" "CUSTOMER_PREFERENCES" "TRUE"
* Menu herramienta
* ConProps "Envíos de PushNots" "PNS_APP" "*"
* Menu herramienta
* ConProps "Envíos de PushNots" "ORGANIZATION" "LATINIA"
* forzarrecarga

When inyecta PNS (xml) al adaptador WS normalAuth
Se hace el cambio en el xml Estos son los parámetros: fname,  empresa,  ref_contract, ref_app,  clave,  clavevalor,  content
* Set XML PNSunitario "push-pns-unitario.xml" "INNOVUS" "#wtest-pns" "lat1MobileApp0" "refUser" "userlatinia" "InsercionWS PNS "
Se inyecta el PNS en el adaptador. type, refProduct, adaptor
* Insercion WS "pns" "wtest-pns" "normalAuth"

Then Iniciamos sesión y verificamos en estadisticas que efectivamente se ha enviado el mensaje
 Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"
 Cumplimentar Formulario y Login

* InicioHome
* Menu "Análisis"
* Herramienta "Detalle de contenidos de transacciones"
* Menu herramienta
* BusquedaCriterio "#refUser" "userlatinia" "" "LATINIA"
* InicioHome

* LogOut

Escenario segundo: Insercion adaptador PriorityAuth WS
------------------------------------------------------
Given Existe xml para ser inyectado
When inyecta PNS (xml) al adaptador WS priorityAuth
Se hace el cambio en el xml Estos son los parámetros: fname,  empresa,  ref_contract, ref_app,  clave,  clavevalor,  content
* Set XML PNSunitario "push-pns-unitario.xml" "INNOVUS" "#wtest-pns" "lat1MobileApp0" "refUser" "userlatinia" "InsercionJMS PNS "
Se inyecta el PNS en el adaptador. type, refProduct, adaptor
* Insercion WS "pns" "wtest-pns" "priorityAuth"

Then verificamos en estadisticas que efectivamente se ha enviado el mensaje

* InicioHome
* Menu "Análisis"
* Herramienta "Detalle de contenidos de transacciones"
* Menu herramienta
* BusquedaCriterio "#refUser" "userlatinia" "" "LATINIA"
* InicioHome

* LogOut

Escenario tercero: Insercion adaptador MassiveAuth WS
-----------------------------------------------------
When inyecta PNS (xml) al adaptador WS massiveAuth
Se hace el cambio en el xml Estos son los parámetros: fname,  empresa,  ref_contract, ref_app,  clave,  clavevalor,  content
* Set XML PNSunitario "push-pns-unitario.xml" "INNOVUS" "#wtest-pns" "lat1MobileApp0" "refUser" "userlatinia" "InsercionJMS PNS "
Se inyecta el PNS en el adaptador. type, refProduct, adaptor
* Insercion WS "pns" "wtest-pns" "massiveAuth"

Then verificamos en estadisticas que efectivamente se ha enviado el mensaje

* InicioHome
* Menu "Análisis"
* Herramienta "Detalle de contenidos de transacciones"
* Menu herramienta
* BusquedaCriterio "#refUser" "userlatinia" "" "LATINIA"
* InicioHome

* LogOut
