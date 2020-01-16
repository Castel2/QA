Genera un Tweet desde la herramienta gráfica 'wtest-Tweet' - Iniciando_spec: 04.01_MT_Tweet
==========================================================
Tags:Transaccionamiento,tweet,04.01_MT_Tweet
Preconditions:

Estos son los steps denominados 'de contexto' (ver: http://getgauge.io/documentation/user/current/gauge_terminologies/contexts.html)
Estos steps se ejecutan antes de cada escenario de este spec.
* Establecer host "" port "" dir ""
* Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"
* Cumplimentar Formulario y Login
* Establecer cliente
* Hash Map
* Lista Propiedades desde licencia

Escenario primero
-----------------

//* Empresa "INNOVUS"
//* Menu "Provisión"
//* Herramienta "Gestión de contratos"
//* Menu herramienta
//* ConProps "Envíos a Twitter" "ORGANIZATION" "LATINIA"
//* forzarrecarga
* Transaccionar EnvioTweet virtual "usercapitaltwitter"

LogOut:
* LogOut