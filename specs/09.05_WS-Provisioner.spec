Se prueba llamados WS-Provisioner- Iniciando_spec: 09.05_WS-Provisioner
==================================
Tags:09.05_WS-Provisioner,Regresion
Created by amartinez on 18/04/2018

This is an executable specification file which follows markdown syntax.
Every heading in this file denotes a scenario. Every bulleted point denotes a step.
* Establecer host "" port "" dir ""
* Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"
* Establecer cliente
* Hash Map
* Lista Propiedades desde licencia

Esenario SetConfidentialApp
---------------------------
* Organizacion "CAPITAL"
* refUser "capitaluser"
GIVEN Existe el usuario con 2 tokens
* Crear suscriptor WS
* Eliminar todas las MApps asociadas a usuario WS
* Registrar AppDevice a un usuario WS "" "mymappautomated0"
* Guardar Token y UUID llaveToken "token1" llaveUUID "uuid1"
* Registrar AppDevice a un usuario WS "" "mymappautomated0"
* Guardar Token y UUID llaveToken "token2" llaveUUID "uuid2"
WHEN se establece token1 con force=true
* SetConfidentialApp refCompany"CAPITAL" refUser"capitaluser" llaveUUID"uuid1" force"true"
THEN token1 isConfidential = true y token2 isConfidential = false
* Verificar si los dispositivos son confidenciales BUG-INF-346

WHEN se establece token2 con force=false
* SetConfidentialApp refCompany"CAPITAL" refUser"capitaluser" llaveUUID"uuid2" force"false"
THEN token1 isConfidential = true y token2 isConfidential = false
* Verificar si los dispositivos son confidenciales BUG-INF-346

WHEN se establece token2 con force=true
* SetConfidentialApp refCompany"CAPITAL" refUser"capitaluser" llaveUUID"uuid2" force"true"
THEN token1 isConfidential = false y token2 isConfidential = true
* Verificar si los dispositivos son confidenciales BUG-INF-346

WHEN se establece token2 con force= "" ---> vacío
* SetConfidentialApp refCompany"CAPITAL" refUser"capitaluser" llaveUUID"uuid2" force""
THEN token1 y token2 isConfidential = false
* Verificar si los dispositivos son confidenciales BUG-INF-346

WHEN se establece token2 con force="" ---> vacío
* SetConfidentialApp refCompany"CAPITAL" refUser"capitaluser" llaveUUID"uuid2" force""
THEN token1 isConfidential = false y token2 isConfidential = true
* Verificar si los dispositivos son confidenciales BUG-INF-346

WHEN se establece token1 con force="" ---> vacío
* SetConfidentialApp refCompany"CAPITAL" refUser"capitaluser" llaveUUID"uuid1" force""
THEN token1 isConfidential = false y token2 isConfidential = true
* Verificar si los dispositivos son confidenciales BUG-INF-346

* Eliminar MApp de usuario WS llaveToken"token1"
* Eliminar MApp de usuario WS llaveToken"token2"

Esenario PNS_APP
----------------
* Asignar Rol WS "lman-vcontent" "LD_PSTORAGE"
* Empresa "CAPITAL"
* Eliminar todas las MApps asociadas a usuario WS
* Asignar Propiedad/Contrato WS "MT_PNS" "LIMSP" "PNS_APP" "*"
* Asignar Propiedad/Contrato WS "MT_PNS" "LIMSP" "ORGANIZATION" "CAPITAL"
* Asignar Propiedad/Contrato WS "MT_PNS" "LIMSP" "CUSTOMER_PREFERENCES" "TRUE"
* Habilitar una MApp refApp "App_prueba1"
* Habilitar una MApp refApp "App_prueba2"
* Registrar AppDevice a un usuario WS "" "App_prueba1"
* Guardar Token y UUID llaveToken "token1" llaveUUID "uuid21"
* Registrar AppDevice a un usuario WS "" "App_prueba1"
* Guardar Token y UUID llaveToken "token2" llaveUUID "uuid1-2"
* Forzar recarga WS

When inyecta PNS (xml) al adaptador WS normalAuth
Se hace el cambio en el xml Estos son los parámetros: fname,  empresa,  ref_contract, ref_app,  clave,  clavevalor,  content
* Set XML PNSunitario "push-pns-unitario.xml" "CAPITAL" "MT_PNS" "App_prueba1" "refUser" "capitaluser" "InsercionWS PNS"
Se inyecta el PNS en el adaptador. type, refProduct, adaptor
* Insercion WS "pns" "wtest-pns" "normalAuth"
* ldmcontable "histAndSummary"
* Esperar "4"
* ldmcontable "histAndSummary"
* Esperar "4"
* Comprobar Msg refContract"MT_PNS"
* Verificar contenido en APP  refApp"App_prueba1" llaveUUID"uuid21" existeContenido"true"
* Verificar contenido en APP  refApp"App_prueba1" llaveUUID"uuid1-2" existeContenido"true"

* Registrar AppDevice a un usuario WS "" "App_prueba2"
* Guardar Token y UUID llaveToken "token3" llaveUUID "uuid2-1"
* Set XML PNSunitario "push-pns-unitario.xml" "CAPITAL" "MT_PNS" "" "refUser" "capitaluser" "InsercionWS PNS"
* Insercion WS "pns" "wtest-pns" "normalAuth"
* ldmcontable "histAndSummary"
* Esperar "4"
* ldmcontable "histAndSummary"
* Esperar "4"
* Comprobar Msg refContract"MT_PNS"
* Verificar contenido en APP  refApp"App_prueba1" llaveUUID"uuid1-2" existeContenido"true"
* Verificar contenido en APP  refApp"App_prueba2" llaveUUID"uuid2-1" existeContenido"true"

* Deshabilitar una MApp refApp "App_prueba2"

Esenario confidential
-----------------------
* SetConfidentialApp refCompany"CAPITAL" refUser"capitaluser" llaveUUID"uuid21" force"true"
* Verificar confidencial por APP TRUE "CAPITAL" "capitaluser" "App_prueba1" "uuid21"


Esenario PNS_DOWNLOADABLE
---------------------------
* Actualizar propiedad aplicación WS refProduct"AppAUTOMATED" refPlataforma"LIMSP" propiedad"PNS_DOWNLOADABLE" valor"TRUE" visible"true"
* Asignar Propiedad/Contrato WS "MT_PNS" "LIMSP" "PNS_DOWNLOADABLE" "TRUE"
* Forzar recarga WS
When inyecta PNS (xml) al adaptador WS normalAuth
Se hace el cambio en el xml Estos son los parámetros: fname,  empresa,  ref_contract, ref_app,  clave,  clavevalor,  content
* Set XML PNSunitario "push-pns-unitario.xml" "CAPITAL" "MT_PNS" "App_prueba1" "refUser" "capitaluser" "InsercionWS PNS"
Se inyecta el PNS en el adaptador. type, refProduct, adaptor
* Insercion WS "pns" "wtest-pns" "normalAuth"
* ldmcontable "histAndSummary"
* Esperar "4"
* ldmcontable "histAndSummary"
* Esperar "4"
* Comprobar Msg refContract"MT_PNS"
* Verificar contenido en APP  refApp"App_prueba1" llaveUUID"uuid21" existeContenido"true"
* Asignar Propiedad/Contrato WS "MT_PNS" "LIMSP" "PNS_DOWNLOADABLE" "FALSE"
* Forzar recarga WS
When inyecta PNS (xml) al adaptador WS normalAuth
Se hace el cambio en el xml Estos son los parámetros: fname,  empresa,  ref_contract, ref_app,  clave,  clavevalor,  content
* Set XML PNSunitario "push-pns-unitario.xml" "CAPITAL" "MT_PNS" "App_prueba1" "refUser" "capitaluser" "InsercionWS PNS"
Se inyecta el PNS en el adaptador. type, refProduct, adaptor
* Insercion WS "pns" "wtest-pns" "normalAuth"
* ldmcontable "histAndSummary"
* Esperar "4"
* ldmcontable "histAndSummary"
* Esperar "4"
* Comprobar Msg refContract"MT_PNS"
* Verificar contenido en APP  refApp"App_prueba1" llaveUUID"uuid21" existeContenido"false"
* Deshabilitar una MApp refApp "App_prueba1"
* Eliminar Propiedad/Contrato WS refContract"MT_PNS" refPlatform"LIMSP" property"PNS_DOWNLOADABLE"
* Forzar recarga WS

