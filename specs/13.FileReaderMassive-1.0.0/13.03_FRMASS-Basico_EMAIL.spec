Prueba filereadermass con formato EMAIL - Iniciando_spec: 13.03_FRMASS-Basico_EMAIL
===================================

Tags: FRMASS, massive

Created by amartinez

Dependencias: 1. La aplicación 'lsms-filereadermass' debe existir para FR
              2. La aplicación 'lsms-filereadermass' debe existir para FRMASS
              3. Debe existir en el CMC el usuario usercapital
              4. Se debe ejecutar primero FRMASS-Basico_PNS para provisionar los contratos necesarios
              5. Los archivos adjuntos deben estar en la ruta establecida en el archivo de propiedades “lconf-general.properties”, por ejemplo config/importPath = /home/weblogic/limsp
                 La ruta se compone de ${pathBase}/${loginEnterprise}/template/${refTemplate}
                                         /home/weblogic/limsp/innovus/template/plant_email


* Establecer host "" port "" dir ""
* Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"
* Cumplimentar Formulario y Login
* Establecer cliente
* Hash Map
* Lista Propiedades desde licencia

Caso 1 EMAIL
------------
Given existen la provision correcta para la prueba
Se verifica que la aplicación 	Aplicacion FileReader exite
* Empresa "INNOVUS"
* Usuario "VALIDACION"
* Nombre contrato "Aplicación FileReader Massive"
* Menu "Provisión"
* Herramienta "Gestión de aplicaciones"
* Menu herramienta
* Verifica Nombres "lapp-filereader-massive"
* InicioHome
Se verifica si existe el contrato, de no ser así se crea el contrato y se le agrega la clausula pns
* Herramienta "Gestión de contratos"
* Menu herramienta
* refcontract "app_fr"
* tipoclause "pns"
* ConClause ""
* InicioHome
* Menu herramienta
* tipoclause "smsMT"
* ConClause ""
* InicioHome
* Menu herramienta
* tipoclause "email"
* ConClause ""
* Menu herramienta
* Asignar user
* InicioHome

Se establecen los datos necesarios para acceder al servidor y hacer la transferencia de archivos
* Ruta Origen "D" "email"
* Credenciales Linux "212.36.69.147" "2290" "bea" "latinia"
 Credenciales Linux "212.36.69.147" "2295" "root" "latinia"
* Ruta destino "/home/bea/filereader/massive"
 Ruta destino "/J2EE/IBM/WebSphere/AppServer/profiles/AppSrv01/filereader/massive"
 Ruta destino "/J2EE/IBM/WebSphere/AppServer/profiles/qaver08/filereader/massive"
Ejemplo1: Formato mail1
* Cabecera
|EMPRESA|CONTRATO|PROVEDOR|FORMATO|ETIQUETA|NUM_MENSAJES|
|INNOVUS|app_fr||mail1||1|
* Cuerpo
|ID|MAIL|ALIAS|ASUNTO|CONTENIDO|ETIQUETA|PLANTILLA|VARIABLES_PLANTILA|
|1|prueba@gmail.com|prueba|prueba email FR|prueba email1||||

* Escribir Archivo "pruebaemail" "3"
* ldmcontable "filereadermass"
* LogOut
* ldmcontable "histAndSummary"
* Cumplimentar Formulario y Login
Then comprueba que se ha renombrado con extensión .closed
* Traer Archivo ".closed"
* Es Igual
* Esperar "120"
* InicioHome
Then comprueba que muestra los mensajes en estadisticas
* Menu "Análisis"
Herramienta "Detalle de trans. email"
* Herramienta "Detalle de contenidos de transacciones de email"
* Menu herramienta
* BusquedaCriterio "email" "prueba@gmail.com" "" "LATINIA"
* InicioHome

Ejemplo2: Formato Mail1 con fecha de proceso del fichero, no olvidar cambiar el campo inicio en la cabecera
* Cabecera
|EMPRESA|CONTRATO|FORMATO|INICIO|PRIORIDAD|NUM_MENSAJES|
|[miCabecera3]INNOVUS|app_fr|mail1|inicio|3|1|
* Cuerpo
|ID|MAIL|ALIAS|ASUNTO|CONTENIDO|ETIQUETA|PLANTILLA|VARIABLES_PLANTILA|
|1|prueba@gmail.com|prueba|prueba email FR1|prueba email1||||

* Escribir Archivo "pruebaemail" "3"
* ldmcontable "filereadermass"
* Traer Archivo ".ready"
* Esperar "420"
* ldmcontable "filereadermass"
* LogOut
* ldmcontable "histAndSummary"
* Cumplimentar Formulario y Login
* Traer Archivo ".closed"
* Es Igual
* Esperar "120"
* InicioHome
* Menu "Análisis"
 Herramienta "Detalle de trans. email"
* Herramienta "Detalle de contenidos de transacciones de email"
* Menu herramienta
* BusquedaCriterio "email" "prueba@gmail.com" "" "LATINIA"
* InicioHome

Ejemplo3: Formato Mail1 con fecha de proceso del fichero, y Plantilla, No olvidar cambiar el campo INICIO en la cabecera
* Menu "Provisión"
* Herramienta "Gestión de plantillas"
* Menu herramienta
* Proposito ""
* tipoclause "Email"
* idioma "Español"
* Establece Variables "mi_variable_directa"
* Existe tipo de mensaje
* Crear Plantilla "grupo Creadas latinia" "Plantilla EMAIL" "plant_email"
* InicioHome
* Menu herramienta
* Crear Seccion especifica "cuerpo" "Señor(a) ${CUSTOMER} se le informa ${MONTO} contenido publico" "plant_email" "forzar"
* InicioHome

* Cabecera
|EMPRESA|CONTRATO|FORMATO|INICIO|PRIORIDAD|NUM_MENSAJES|
|[miCabecera3]INNOVUS|app_fr|mail1|inicio|3|1|
* Cuerpo
|ID|MAIL|ALIAS|ASUNTO|CONTENIDO|ETIQUETA|PLANTILLA|VARIABLES_PLANTILA|
|1|prueba@gmail.com|prueba|prueba email plantilla|prueba email1||plant_email|customer=pepito##monto=65000|

* Escribir Archivo "pruebaemail" "3"
* ldmcontable "filereadermass"
* Traer Archivo ".ready"
* Esperar "420"
* ldmcontable "filereadermass"
* LogOut
* ldmcontable "histAndSummary"
* Cumplimentar Formulario y Login

* Traer Archivo ".closed"
* Es Igual
* Esperar "120"
* InicioHome
* Menu "Análisis"
 Herramienta "Detalle de trans. email"
* Herramienta "Detalle de contenidos de transacciones de email"
* Menu herramienta
* BusquedaCriterio "email" "prueba@gmail.com" "" "LATINIA"
* InicioHome

Ejemplo4:Formato Mail1 con Plantilla y fichero adjuntos
* Menu "Provisión"
* Herramienta "Gestión de plantillas"
* Menu herramienta
* Proposito ""
* tipoclause "Email"
* idioma "Español"
* Establece Variables "mi_variable_directa"
* Existe tipo de mensaje
* Crear Plantilla "grupo Creadas latinia" "Plantilla EMAIL" "plant_email"
* InicioHome
* Menu herramienta
* Crear Seccion especifica "cuerpo" "Señor(a) ${CUSTOMER} se le informa ${MONTO} contenido publico" "plant_email" "forzarNO"
* InicioHome
* Cabecera
|EMPRESA|CONTRATO|PROVEDOR|FORMATO|ETIQUETA|NUM_MENSAJES|
|INNOVUS|app_fr||mail1||2|
* Cuerpo
|ID|MAIL|ALIAS|ASUNTO|CONTENIDO|ETIQUETA|PLANTILLA|VARIABLES_PLANTILA|
|1|prueba@gmail.com|prueba|prueba email plantilla|prueba email1||plant_email|customer=pepito##monto=65000|
|2|prueba2@gmail.com|prueba2|prueba2 email adjunto|prueba2 email1||plant_email|customer=pepito##monto=65000##file=adjunto_email.pdf|

* Escribir Archivo "pruebaemail" "3"
* ldmcontable "filereadermass"
* LogOut
* ldmcontable "histAndSummary"
* Cumplimentar Formulario y Login

* Traer Archivo ".closed"
* Es Igual
* Esperar "120"
* InicioHome
* Menu "Análisis"
 Herramienta "Detalle de trans. email"
* Herramienta "Detalle de contenidos de transacciones de email"
* Menu herramienta
* BusquedaCriterio "email" "prueba@gmail.com" "" "LATINIA"
* InicioHome

Ejemplo5: Formato Mail1 con Plantilla y varios ficheros adjuntos
* Menu "Provisión"
* Herramienta "Gestión de plantillas"
* Menu herramienta
* Proposito ""
* tipoclause "Email"
* idioma "Español"
* Establece Variables "mi_variable_directa"
* Existe tipo de mensaje
* Crear Plantilla "grupo Creadas latinia" "Plantilla EMAIL" "plant_email"
* InicioHome
* Menu herramienta
* Crear Seccion especifica "cuerpo" "Señor(a) ${CUSTOMER} se le informa ${MONTO} contenido publico" "plant_email" "forzarNO"
* InicioHome
* Cabecera
|EMPRESA|CONTRATO|PROVEDOR|FORMATO|ETIQUETA|NUM_MENSAJES|
|INNOVUS|app_fr||mail1||2|
* Cuerpo
|ID|MAIL|ALIAS|ASUNTO|CONTENIDO|ETIQUETA|PLANTILLA|VARIABLES_PLANTILA|
|1|prueba@gmail.com|prueba|prueba email plantilla|prueba email1||plant_email|customer=pepito##monto=65000|
|2|prueba2@gmail.com|prueba2|prueba2 email adjunto|prueba2 email1||plant_email|customer=pepito##monto=65000##file=adjunto_email.pdf,adjunto2_email.pdf|

* Escribir Archivo "pruebaemail" "3"
* ldmcontable "filereadermass"
* LogOut
* ldmcontable "histAndSummary"
* Cumplimentar Formulario y Login

* Traer Archivo ".closed"
* Es Igual
* Esperar "120"
* InicioHome
* Menu "Análisis"
 Herramienta "Detalle de trans. email"
* Herramienta "Detalle de contenidos de transacciones de email"
* Menu herramienta
* BusquedaCriterio "email" "prueba@gmail.com" "" "LATINIA"
* InicioHome

LogOut
*LogOut