Prueba FileReader con formato SMS - Iniciando_spec: 13.04_FRMASS-Basico_SMS
=================================

Tags: FRMASS, massive

Created by amartinez

Documentación: http://www.latiniaservices.com/supportsuite/buscador/es/Plataforma_LIMSP-SDP.htm#FileReader/Ejemplos_practicos_de_uso.htm%3FTocPath%3DSuite%2520de%2520Aplicaciones%7CFileReader%7C_____6

Dependencias: 1. La aplicación 'lsms-filereader' debe existir para FR
              2. La aplicación 'FileReaderMassive' debe existir para FRMASS
              3. Debe existir en el CMC el usuario usercapital
              4. Se debe ejecutar primero FRMASS-Basico_PNS para provisionar los contratos necesarios

* Establecer host "" port "" dir ""
* Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"
* Cumplimentar Formulario y Login
* Establecer cliente
* Hash Map
* Lista Propiedades desde licencia

Caso 1 SMS
----------
Given existen la provision correcta para la prueba
Se verifica que la aplicación lsms-filereader exite
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
 Nombre contrato "Contrato PNS FileReader"
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
* Herramienta "Gestión de canales"
* Menu herramienta
* Rutas "Virtual" "+000001" "SMS" "Virtual"
* Menu herramienta
* Rutas "Unknown" "+000001" "SMS" "Virtual"
Se establecen los datos necesarios para acceder al servidor y hacer la transferencia de archivos
* Ruta Origen "D" "sms"
* Credenciales Linux "212.36.69.147" "2290" "bea" "latinia"
 Credenciales Linux "212.36.69.147" "2295" "root" "latinia"
* Ruta destino "/home/bea/filereader/massive"
 Ruta destino "/J2EE/IBM/WebSphere/AppServer/profiles/AppSrv01/filereader/massive"
 Ruta destino "/J2EE/IBM/WebSphere/AppServer/profiles/qaver08/filereader/massive"

Ejemplo 1: Fichero con formato SMS1, con dos mensajes, uno con prefijo +34 y el otro sin
* Cabecera
|EMPRESA|CONTRATO|PROVEDOR|FORMATO|ETIQUETA|NUM_MENSAJES|
|INNOVUS|app_fr||sms1||2|
* Cuerpo
|ID|GSM|TEXTO|
|1|653903752|texto prueba sms1|
|2|+34670019194|texto prueba sms1 con +34|
* Escribir Archivo "pruebasms" "-1"
* ldmcontable "filereadermass"
* LogOut
* ldmcontable "histAndSummary"
* Cumplimentar Formulario y Login

Then comprueba que se ha renombrado "pruebasms.closed"
* Traer Archivo ".closed"
* Es Igual
* Esperar "120"
* InicioHome

Then comprueba que muestra los mensajes en estadisticas
* Menu "Análisis"
* Herramienta "Detalle de contenidos de transacciones"
* Menu herramienta
* BusquedaCriterio "telefono" "+34670019194" "" ""
* InicioHome

Ejemplo 2: Fichero con formato SMS1, con dos mensajes. Se fuerza la operadora para que sea MOVISTAR
* Cabecera
|EMPRESA|CONTRATO|PROVEDOR|FORMATO|ETIQUETA|NUM_MENSAJES|
|INNOVUS|app_fr|MOVISTAR|sms1||2|
* Cuerpo
|ID|GSM|TEXTO|
|1|653903752|texto prueba sms1|
|2|+34670019194|texto prueba sms1 con +34|

* Escribir Archivo "pruebasms" "-1"
* ldmcontable "filereadermass"
* LogOut
* ldmcontable "histAndSummary"
* Cumplimentar Formulario y Login

* Traer Archivo ".closed"
* Es Igual
* Esperar "120"
* InicioHome
* Menu "Análisis"
* Herramienta "Detalle de contenidos de transacciones"
* Menu herramienta
* BusquedaCriterio "telefono" "+34670019194" "" ""
* InicioHome

Ejemplo 3: Formato sms2 El mensaje enviado, se programará como diferido en el operador, para ser entregado al destinatario
* Cabecera
|EMPRESA|CONTRATO|PROVEDOR|FORMATO|ETIQUETA|NUM_MENSAJES|
|INNOVUS|app_fr||sms2||1|
* Cuerpo
|ID|GSM|DIFERIDO|CADUCIDAD|TEXTO|
|1|653903752|diferido||texto prueba diferido sms|
* Escribir Archivo "pruebasms" "-1"
* ldmcontable "filereadermass"
* LogOut
* ldmcontable "histAndSummary"
* Cumplimentar Formulario y Login

* Traer Archivo ".closed"
* Es Igual
* Esperar "120"
* InicioHome
* Menu "Análisis"
* Herramienta "Detalle de contenidos de transacciones"
* Menu herramienta
* Nombre contrato "Aplicación FileReader Massive Automated"
* Ver listado "Aplicación FileReader Massive Automated"
* Ver estados "Entrega confirmada a usuario"
* InicioHome

Ejemplo 4: Formato SMS2 con caducidad
* Cabecera
|EMPRESA|CONTRATO|PROVEDOR|FORMATO|ETIQUETA|NUM_MENSAJES|
|INNOVUS|app_fr||sms2||1|
* Cuerpo
|ID|GSM|DIFERIDO|CADUCIDAD|TEXTO|
|1|653903752||caducidad|texto prueba diferido sms|

* Escribir Archivo "pruebasms" "-1"
* ldmcontable "filereadermass"
* LogOut
* ldmcontable "histAndSummary"
* Cumplimentar Formulario y Login
* Traer Archivo ".closed"
* Es Igual
* Esperar "120"
* InicioHome
* Menu "Análisis"
* Herramienta "Detalle de contenidos de transacciones"
* Menu herramienta
* Nombre contrato "Aplicación FileReader Massive Automated"
* Ver listado "Aplicación FileReader Massive Automated"
* Ver estados "Expirado por Plataforma"
* InicioHome

Ejemplo 5: Formato SMS3 con entrega diferida y fecha de proceso del fichero
* Cabecera
|EMPRESA|CONTRATO|FORMATO|INICIO|PRIORIDAD|NUM_MENSAJES|
|[miCabecera3]INNOVUS|app_fr|sms3|inicio|3|1|
* Cuerpo
|ID|GSM|DIFERIDO|CADUCIDAD|TEXTO|ETIQUETA|PLANTILLA|VARIABLES|
|1|653903752|diferido||texto prueba diferido sms3||||
Dado que se debe indicar un random para la identificación del mensaje en las estadisticas
se debe poner la posición dentro de la tabla donde se debe poner el random, cuando no hau plantillas se debe poner en el campo texto
para este caso el campo texto está en la 5 posición, las posiciones van desde 0, por lo cual la pocisión es 4, si se desea poner al final, en la última posició
simplemente indicamos -1
* Escribir Archivo "pruebasms" "4"
* ldmcontable "filereadermass"
* Traer Archivo ".ready"
* Esperar "360"
* ldmcontable "filereadermass"

* LogOut
* ldmcontable "histAndSummary"
* Cumplimentar Formulario y Login
* Traer Archivo ".closed"
* Es Igual
* Esperar "120"
* InicioHome
* Menu "Análisis"
* Herramienta "Detalle de contenidos de transacciones"
* Menu herramienta
* Nombre contrato "Aplicación FileReader Massive Automated"
* Ver listado "Aplicación FileReader Massive Automated"
* Ver estados "Entrega confirmada a usuario"
* InicioHome

Ejemplo 6: Formato SMS3 con plantilla Se utiliza la plantilla 'miPlantillaSMS', la cual tiene 2 parámetros
* idioma "Español"
* Proposito ""
* tipoclause "SMS"
* Menu "Provisión"
* Herramienta "Gestión de plantillas"
* Menu herramienta
* Existe tipo de mensaje
* Crear Plantilla "grupo Creadas latinia" "Plantilla SMS" "plant_sms"
* InicioHome
* Menu herramienta
* Crear Seccion especifica "cuerpo" "Señor(a) ${CUSTOMER} se le informa ${MONTO} contenido publico" "plant_sms" "forzar"
* InicioHome
* Cabecera
|EMPRESA|CONTRATO|PROVEDOR|FORMATO|ETIQUETA|NUM_MENSAJES|
|INNOVUS|app_fr|MOVISTAR|sms3||1|
* Cuerpo
|ID|GSM|DIFERIDO|CADUCIDAD|TEXTO|ETIQUETA|PLANTILLA|VARIABLES|
|1|653903752|||texto prueba sms1||plant_sms|customer=pepito##monto=50000|

* Escribir Archivo "pruebasms" "-1"
* ldmcontable "filereadermass"
* LogOut
* ldmcontable "histAndSummary"
* Cumplimentar Formulario y Login

* Traer Archivo ".closed"
* Es Igual
* Esperar "120"
* InicioHome
* Menu "Análisis"
* Herramienta "Detalle de contenidos de transacciones"
* Menu herramienta
* BusquedaCriterio "telefono" "+34653903752" "" ""
* InicioHome

Ejemplo 7: Formato SMS3 con plantilla, entrega diferida y fecha de proceso del fichero
* Cabecera
|EMPRESA|CONTRATO|FORMATO|INICIO|PRIORIDAD|NUM_MENSAJES|
|[miCabecera3]INNOVUS|app_fr|sms3|inicio|3|1|
* Cuerpo
|ID|GSM|DIFERIDO|CADUCIDAD|TEXTO|ETIQUETA|PLANTILLA|VARIABLES|
|1|653903752|diferido||texto prueba diferido sms3||plant_sms|customer=pepito##monto=60000|
* Escribir Archivo "pruebasms" "-1"
* ldmcontable "filereadermass"
* Traer Archivo ".ready"
* Esperar "360"
* ldmcontable "filereadermass"

* LogOut
* ldmcontable "histAndSummary"
* Cumplimentar Formulario y Login
* Traer Archivo ".closed"
* Es Igual
* Esperar "120"
* InicioHome
* Menu "Análisis"
* Herramienta "Detalle de contenidos de transacciones"
* Menu herramienta
* Nombre contrato "Aplicación FileReader Massive Automated"
* Ver listado "Aplicación FileReader Massive Automated"
* Ver estados "Entrega confirmada a usuario"
* InicioHome

Ejemplo8: Formato SMS3 con Plantilla, entrega diferida, fecha de proceso del fichero, y fecha de expiración del fichero
* Cabecera
|EMPRESA|CONTRATO|FORMATO|INICIO|TS_EXPIRE|PRIORIDAD|NUM_MENSAJES|
|[miCabecera4]INNOVUS|app_fr|sms3|inicio|expire|3|1|
* Cuerpo
|ID|GSM|DIFERIDO|CADUCIDAD|TEXTO|ETIQUETA|PLANTILLA|VARIABLES|
|1|653903752|201612230310||texto prueba diferido sms3||plant_sms|customer=pepito##monto=60000|

* Escribir Archivo "pruebasms" "-1"
* ldmcontable "filereadermass"
* Traer Archivo ".ready"
* Esperar "360"
* ldmcontable "filereadermass"
* Traer Archivo ".expired"
* InicioHome

LogOut:
* LogOut