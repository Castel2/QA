Inyecta transacciones por adaptador WS, utilizando el componente externo "TrafficTest".
======================================================================================

Tags:sms,pns,email,99.04_TrafficTest_corto,Transaccionamiento
Preconditions:
No se realizan comprobaciones de la provisión previa al envio. Ya se supone que todo está correctamente aprovisionado.

Descripción.- A partir de un escenario de provisión predefinido, se inyectan transacciones por WS

Dependencias.-
1.- Se requiere la ejecución correcta de "_09.01PlataformaLimpia-Provision.scn_", para que la ejecución de TrafficTesting funcione (%TWIST _UNIDAD%\Util\TrafficTest\execute.cmd)
2.- Se requiere la correcta ejecución al menos 1 vez del escenario '10.02 _TrafficTest.scn' para que se aprovisionen clausulas y rutas apropiadas.

* Establecer host "" port "" dir ""
* Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"
* Cumplimentar Formulario y Login

Escenario primero
-----------------

//Realización de las inyecciones
Parámetros= "Cantidad de mensajes", "Empresa", "refContract", "refApp", "refTest"
TrafficTest:
/#TrafficTest1 "2" "CAPITAL" "InsercionWS" "InsercionWS-Twist" "email"
* TrafficTest1 "10" "INNOVUS" "#wapppush" "wapppush" "sms"

LTimers:
* ldmcontable "histAndSummary"

LogOut:
* LogOut