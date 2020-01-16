InsercionJMS EMAIL - Iniciando_spec: 10.07_InsercionJMS-SDP_EMAIL
=====================
Created by amartinez on 8/02/2017

Tags: InsercionJMS, InsercionAdaptor

Consiste en utilizar la herramienta rgtester para inyectar en los diferentes adaptadores JMS

El TC testea mensajes en formato EMAIL y los inyecta en los 3 diferentes adaptadores JMS, normalAuth, priorityAuth y massiveAuth

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
* Roles "wapppushemail" "ADAPTOR_JMS_NORMAL"
* Roles "wapppushemail" "ADAPTOR_JMS_PRIORITY"
* Roles "wapppushemail" "ADAPTOR_JMS_MASSIVE"
* InicioHome
* LogOut

* Login RGTester

When inyecta EMAIL (xml) al adaptador JMS normalAuth
Se hace el cambio en el xml Estos son los parámetros: fname,  empresa,  ref_contract, destinatario,  subject,  content
* Set XML EMAIL "push-email.xml" "INNOVUS" "#wapppushemail" "prueba@gmail.com" "Insercion JMS " "Texto Insercion"
Se inyecta el EMAIL en el adaptador. type, refProduct, adaptor
* Insercion JMS "email" "wapppushemail" "normalAuth"

Then Iniciamos sesión y verificamos en estadisticas que efectivamente se ha enviado el mensaje
* Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"
* Cumplimentar Formulario y Login

* InicioHome
* Menu "Análisis"
* Herramienta "Detalle de contenidos de transacciones de email"
* Menu herramienta
* BusquedaCriterio "email" "prueba@gmail.com" "" "LATINIA"
* InicioHome

* LogOut

Escenario Segundo: Insercion adaptador PriorityAuth JMS
-------------------------------------------------------

Given existe la herramienta e invocamos la url = "/limsp-jms-rgtester/"
* Login RGTester

When inyecta EMAIL (xml) al adaptador JMS PriorityAuth
Se hace el cambio en el xml Estos son los parámetros: fname,  empresa,  ref_contract, destinatario,  subject,  content
* Set XML EMAIL "push-email.xml" "INNOVUS" "#wapppushemail" "prueba@gmail.com" "Insercion JMS" "Texto Insercion"
Se inyecta el EMAIL en el adaptador. type, refProduct, adaptor
* Insercion JMS "email" "wapppushemail" "priorityAuth"

Then Iniciamos sesión y verificamos en estadisticas que efectivamente se ha enviado el mensaje
* Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"
* Cumplimentar Formulario y Login

* InicioHome
* Menu "Análisis"
* Herramienta "Detalle de contenidos de transacciones de email"
* Menu herramienta
* BusquedaCriterio "email" "prueba@gmail.com" "" "LATINIA"
* InicioHome

* LogOut

Escenario Tercero: Insercion adaptador MassiveAuth JMS
-------------------------------------------------------

Given existe la herramienta e invocamos la url = "/limsp-jms-rgtester/"
* Login RGTester

When inyecta EMAIL (xml) al adaptador JMS MassiveAuth
Se hace el cambio en el xml Estos son los parámetros: fname,  empresa,  ref_contract, destinatario,  subject,  content
* Set XML EMAIL "push-email.xml" "INNOVUS" "#wapppushemail" "prueba@gmail.com" "Insercion JMS" "Texto Insercion"
Se inyecta el EMAIL en el adaptador. type, refProduct, adaptor
* Insercion JMS "email" "wapppushemail" "massiveAuth"

Then Iniciamos sesión y verificamos en estadisticas que efectivamente se ha enviado el mensaje
* Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"
* Cumplimentar Formulario y Login

* InicioHome
* Menu "Análisis"
 * Herramienta "Detalle de contenidos de transacciones de email"
* Menu herramienta
* BusquedaCriterio "email" "prueba@gmail.com" "" "LATINIA"
* InicioHome

* LogOut
