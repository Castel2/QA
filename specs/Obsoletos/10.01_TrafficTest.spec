Inyecta transacciones por adaptador WS, utilizando el componente externo "TrafficTest". Después comprueba en Estadísticas. También comprueba la correcta provisión.
=============================================================================================

Tags:sms,pns,email,99.05_TrafficTest,Transaccionamiento
Preconditions:


Dependencias.-
1.- Se requiere la ejecución correcta de "Provision-PlataformaLimpia.scn", para que la ejecución de TrafficTesting funcione (%GAUGE_UNIDAD%\Util\TrafficTest\execute.cmd)

Descripción.- A partir de un escenario de provisión predefinido, se inyectan transacciones por WS y se comprueba en estadisticas que la salida corresponde con lo esperado.
Este escenario establece las "RUTAS" de colectores hacia el colector de TRAFFICTESTING. Tener esto en cuenta para las pruebas que se hagan a postariori ya que estarán cambiadas.

La linea de comando será como por ejemplo.-
C:\Util\TrafficTest>java -cp TrafficTesting.jar;trafficTest_lib/* com.latinia.limsp.test.TrafficTest -p host=vmrr5 port=7001 numMails=2 company=INNOVUS contract=* wapppushemail refProduct=wapppushemail randomNumstr=1234 -r email settings_escenarios.xml -l >> logejecucion.log

Dependencias.-
Requiere que existan los contratos y la empresa utilizada en la prueba. Esto puede hacerse ejecutando los escenarios de '_09.01PlataformaLimpia-Provision.scn_' y '10.01_InsercionWS-SDP.scn'


h1.Ejecución Manual
>> SDP-1059 Envíos de mensajes por un mismo contrato balanceado (aleatorio) entre diferentes colectores


LogIN, Navegacion, Almacen de Datos:
* Establecer host "" port "" dir ""
* Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"
* Cumplimentar Formulario y Login

Escenario primero
-----------------
* Menu "Provisión"
* Herramienta "Gestión de canales"
* Menu herramienta
* Proposito "traffictesting"

Localiza y aprovisiona las rutas indicadas, cambiandoles el colector asignado. En este caso asigna 'TrafficTest' que es el colector que deseamos para inyectar después transacciones desde la aplicación de inyección masiva
* Colectores "TRAFFICTEST"
* Rutas "Virtual" "+000001" "SMS" "TRAFFICTEST"
* Menu herramienta
* Rutas "Unknown" "+000001" "SMS" "TRAFFICTEST"
* Menu herramienta
* Rutas "Google" "*" "PNS" "TRAFFICTEST"
* Menu herramienta
* Rutas "EMail-SMTP" "comercial@latinia.com" "EMail" "TRAFFICTEST"
* Menu herramienta
 FailOver

* tipoclause "sms"
* Nombre contrato "Envíos SMS"
* Menu "Provisión"
* Herramienta "Gestión de contratos"
* InicioHome
* Menu herramienta
* Empresa "INNOVUS"
* ComprobarClausulas "Virtual"
* InicioHome
* Menu herramienta
* ComprobarClausulas "Unknown"
* InicioHome
* Menu herramienta
* ComprobarClausulas "Movistar-ES"
* Menu "Provisión"
* Herramienta "Gestión de contratos"
* InicioHome
* Menu herramienta
* Empresa "CAPITAL"
* Nombre contrato "InserWS-Automated"
* refcontract "InsercionWS"
* tipoclause "email"
* InicioHome
* Menu herramienta
* ConClause ""
* Menu herramienta
* ComprobarClausulas "EMail-SMTP"

LogOut:
* LogOut

//Parámetros= "Cantidad de mensajes", "Empresa", "refContract", "refApp", "refTest"
* TrafficTest1 "100" "CAPITAL" "InsercionWS" "InserWS-Automated" "email"

LTimers:
* ldmcontable "histAndSummary"
* Establecer host "" port "" dir ""
* Establecer login "CAPITAL" user "USER1" pass "USER1"
* Cumplimentar Formulario y Login
* InicioHome
* Menu "Herramientas" 
* Herramienta "Detalle de transacciones email Automated"
* Menu herramienta

/* BusquedaCriterio "* refUser" "lili"
* BusquedaCriterio "email" "comercial@latinia.com" "" "CAPITAL"
/* Nombre contrato "+34659666666"
/* Ver estados "Entregado al proveedor"

LogOut:
* LogOut