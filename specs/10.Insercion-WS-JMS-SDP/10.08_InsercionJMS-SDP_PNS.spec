InsercionJMS PNS - Iniciando_spec: 10.08_InsercionJMS-SDP_PNS
=====================
Created by amartinez on 8/02/2017

Tags: InsercionJMS, InsercionAdaptor

Consiste en utilizar la herramienta rgtester para inyectar en los diferentes adaptadores JMS

El TC testea mensajes en formato PNS y los inyecta en los 3 diferentes adaptadores JMS, normalAuth, priorityAuth y massiveAuth

Dependencias:
1. Se requiere tener instalado en el servidor la aplicación "limsp-jms-rgtester.ear", este está en la ubicación "Z:\QC\Util\QA-Components\limsp-jms-rgtester"

* Establecer host "" port "" dir ""
* Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"
* Establecer cliente
* Hash Map
* Lista Propiedades desde licencia
     
Escenario primero: Insercion adaptador NormalAuth JMS
-----------------------------------------------------

* Cumplimentar Formulario y Login

Given existe la herramienta e invocamos la url = "/limsp-jms-rgtester/"
Given que existen los roles ADAPTOR_JMS_NORMAL, ADAPTOR_JMS_PRIORITY y ADAPTOR_JMS_MASSIVE en el contrato

* Menu "Provisión"
* Herramienta "Gestión de aplicaciones"
* Menu herramienta
* Roles "wtest-pns" "ADAPTOR_JMS_NORMAL"
* Roles "wtest-pns" "ADAPTOR_JMS_PRIORITY"
* Roles "wtest-pns" "ADAPTOR_JMS_MASSIVE"
* InicioHome
* LogOut

* Login RGTester
When inyecta PNS (xml) al adaptador JMS normalAuth
Se hace el cambio en el xml Estos son los parámetros: fname,  empresa,  ref_contract, ref_app,  clave,  clavevalor,  content
* Set XML PNSunitario "push-pns-unitario.xml" "INNOVUS" "#wtest-pns" "lat1MobileApp0" "refUser" "userlatinia" "InsercionJMS PNS "
Se inyecta el PNS en el adaptador. type, refProduct, adaptor
* Insercion JMS "pns" "wtest-pns" "normalAuth"

Then Iniciamos sesión y verificamos en estadisticas que efectivamente se ha enviado el mensaje
* Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"
* Cumplimentar Formulario y Login

* InicioHome
* Menu "Análisis"
* Herramienta "Detalle de contenidos de transacciones"
* Menu herramienta
* BusquedaCriterio "#refUser" "userlatinia" "" "LATINIA"
* InicioHome

* LogOut

Escenario segundo: Insercion adaptador PriorityAuth JMS
-------------------------------------------------------

Given existe la herramienta e invocamos la url = "/limsp-jms-rgtester/"
* Login RGTester

When inyecta PNS (xml) al adaptador JMS priorityAuth
Se hace el cambio en el xml Estos son los parámetros: fname,  empresa,  ref_contract, ref_app,  clave,  clavevalor,  content
* Set XML PNSunitario "push-pns-unitario.xml" "INNOVUS" "#wtest-pns" "lat1MobileApp0" "refUser" "userlatinia" "InsercionJMS PNS "
Se inyecta el PNS en el adaptador. type, refProduct, adaptor
* Insercion JMS "pns" "wtest-pns" "priorityAuth"

Then Iniciamos sesión y verificamos en estadisticas que efectivamente se ha enviado el mensaje
* Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"
* Cumplimentar Formulario y Login

* InicioHome
* Menu "Análisis"
* Herramienta "Detalle de contenidos de transacciones"
* Menu herramienta
* BusquedaCriterio "#refUser" "userlatinia" "" "LATINIA"
* InicioHome

* LogOut

Escenario tercero: Insercion adaptador MassiveAuth JMS
-------------------------------------------------------

Given existe la herramienta e invocamos la url = "/limsp-jms-rgtester/"
* Login RGTester

When inyecta PNS (xml) al adaptador JMS massiveAuth
Se hace el cambio en el xml Estos son los parámetros: fname,  empresa,  ref_contract, ref_app,  clave,  clavevalor,  content
* Set XML PNSunitario "push-pns-unitario.xml" "INNOVUS" "#wtest-pns" "lat1MobileApp0" "refUser" "userlatinia" "InsercionJMS PNS "
Se inyecta el PNS en el adaptador. type, refProduct, adaptor
* Insercion JMS "pns" "wtest-pns" "massiveAuth"

Then Iniciamos sesión y verificamos en estadisticas que efectivamente se ha enviado el mensaje
* Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"
* Cumplimentar Formulario y Login

* InicioHome
* Menu "Análisis"
* Herramienta "Detalle de contenidos de transacciones"
* Menu herramienta
* BusquedaCriterio "#refUser" "userlatinia" "" "LATINIA"
* InicioHome

* LogOut
