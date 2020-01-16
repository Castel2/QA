Desdoblamiento de INOT a EMAIL
==============================
Created by amartinez on 5/06/2019
Tags:Transaccionamiento,14.01_DesdoblamientoINOT_EMAIL,Regresion,CI
This is an executable specification file which follows markdown syntax.
Every heading in this file denotes a scenario. Every bulleted point denotes a step.

* Establecer host "" port "" dir ""
* Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"
* Establecer cliente
* Hash Map
* Lista Propiedades desde licencia

Escenario primero
------------------
GIVEN
* Empresa "CAPITAL"
* refcontract "MT_SEND"
* Asignar Propiedad/Contrato WS "MT_SEND" "LIMSP" "INOT_MAX_CHN" "1"
* Asignar Propiedad/Contrato WS "MT_SEND" "LIMSP" "INOT_CUSTOMER_ADDRESS" "APPLICATION_PREFERRED"
* Asignar Propiedad/Contrato WS "MT_SEND" "LIMSP" "INOT_CHN_PREF" "CONTRACT"
* Asignar Clausula tipoClausula"MT" formato"email" operador"EMail-SMTP" canal"comercial@latinia.com" credito""

* Forzar recarga WS
WHEN Se envía el inot
* Set XML INOT to EMAIL "push-inot-email.xml" "CAPITAL" "MT_SEND" "prueba@gmail.com" "comercial@latinia.com" "Test from " "Test inot email"
* Insercion WS "inot" "AppAUTOMATED" "normalAuth"
THEN
* Establecer login "CAPITAL" user "USER1" pass "USER1"
* Cumplimentar Formulario y Login
* Menu "Herramientas"
* Herramienta "Detalle de transacciones email Automated"
* Menu herramienta
* Verificar from duplicado email
* LogOut

* Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"
* Eliminar Propiedad/Contrato WS refContract"MT_SEND" refPlatform"LIMSP" property"INOT_MAX_CHN"
* Eliminar Propiedad/Contrato WS refContract"MT_SEND" refPlatform"LIMSP" property"INOT_CUSTOMER_ADDRESS"
* Eliminar Propiedad/Contrato WS refContract"MT_SEND" refPlatform"LIMSP" property"INOT_CHN_PREF"