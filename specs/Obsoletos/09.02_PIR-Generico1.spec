Transaccionamiento básico SMS, 'hist&summary', y herramientas gráficas de consulta
====================================================================================
Tags:Transaccionamiento,sms,09.02_PIR-Generico1
Preconditions:
Metadata:ScenarioType=Auto


Nota:* Este PIR prueba caracteristicas de licencia estándar, *NO* probando por lo tanto fucionalidades opcionales como pej: "_G.Plantillas_".

LogIN, Almacen de Datos:
* Establecer host "" port "" dir ""
* Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"
* Cumplimentar Formulario y Login
* Establecer cliente
* Hash Map
* Lista Propiedades desde licencia
Escenario primero
-----------------

* Empresa "---"
* Proposito "test"

Navegacion, Transaccionar, Inicio,Almacen de Datos, Gestion de Canales:
* InicioHome
* Menu "Provisión"
* Herramienta "Gestión de canales"
* Menu herramienta
* Rutas "Virtual" "+000001" "SMS" "Virtual"
* Menu herramienta
* Rutas "Unknown" "+000001" "SMS" "Virtual"
* Menu "Diagnosis"
* Herramienta "Envíos SMS"
* Menu herramienta
//* Transaccionar EnviosSMS "+34639937553" "Movistar ñ Ñ < > $ € ! ' á é í ó ú à è ì ò ù"
//* Transaccionar EnviosSMS "+34670019194" "Vodafone ñ Ñ < > $ € ! ' á é í ó ú à è ì ò ù"
//* Transaccionar EnviosSMS "+34653903752" "Orange ñ Ñ < > $ € ! ' á é í ó ú à è ì ò ù"
* Transaccionar EnviosSMS "unknown" "Unknown ñ Ñ < > $ € ! ' á é í ó ú à è ì ò ù"
* Transaccionar EnviosSMS "virtual" "Virtual ñ Ñ < > $ € ! ' á é í ó ú à è ì ò ù"
* Transaccionar EnviosSMS Diferido "virtual" "Diferido Virtual"

Inicio, Navegacion, Almacen de Datos:
* InicioHome
* Menu "Diagnosis"
* Herramienta "Simulador de móvil"
* Menu herramienta
* TransaccionesPullPush
|aplicacion|texto|respuesta|
|ws1|ñ Ñ < > *  € ! ' á é í ó ú à è ì ò ù|Servicio WS. Servicio ofrecido por WS normal (ñ Ñ < > *  € ! ' á é í ó ú à è ì ò ù)|
|jms1|ñ Ñ < > *  € ! ' á é í ó ú à è ì ò ù|Servicio JMS. Servicio ofrecido por JMS normal (ñ Ñ < > *  € ! ' á é í ó ú à è ì ò ù)|

Inicio,Navegacion,Almacen de Datos:
* InicioHome
* Menu "Provisión"
* Herramienta "Recepciones no identificadas"
* Menu herramienta

Transaccionar:
* Transaccionar MO DefaultSMS "Default_CualquierCosa"

LogOut:
* LogOut


===* 07 Estadisticas  *===
LTimers:
* ldmcontable "histAndSummary"


LogIN, Almacen de Datos:
* Establecer host "" port "" dir ""
* Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"
* Cumplimentar Formulario y Login

* InicioHome
* Menu "Control"
* Herramienta "Mensajes descartados o erróneos"
* Menu herramienta
* NavegarDescartados "--" "--"

* InicioHome
* Menu "Análisis"
* Herramienta "Detalle de contenidos de transacciones"
* Menu herramienta

* Ver listado "prueba WebService"
* Exportacion "Exportacion Automática con Twist"

* InicioHome
* Menu "Análisis"
* Herramienta "Estadísticas de tráfico"
* Menu herramienta

* Estadisticas "miCSVEstadisticas creado con Twist"

===* 08_Informes_de_Trafico_PDF *===
* InicioHome
* Menu "Análisis"
* Herramienta "Generador de informes PDF"
* Menu herramienta
* Crear
* Programar
* Email
* Activar

* InicioHome
* Menu "Control"
* Herramienta "Panel de control"
* Menu herramienta

* General
* Logs
* Generar_Exportacion "Mis logs Twist" "Export logs automaticos con Twist"
* Conectividad
* BBDD "ver"

* InicioHome
* Menu "Provisión"
* Herramienta "Gestión de créditos"
* Menu herramienta

* Credito "GENERAL"

* InicioHome
* Menu "Control"
* Herramienta "Información de módulos"
* Menu herramienta
* Item1_AdaptorInJMSCollector
* Atras
* Item2_Core
* Atras
* ExportTodo
* Export_busutil
* Esperar "3"

LogOut:
* LogOut
