Prueba FileReader con formato INOT - Iniciando_spec: 13.02_FRMASS-Basico_INOT
===================================

Tags: FRMASS, massive

Created by amartinez

Dependencias: 1. La aplicación 'lsms-filereader' debe existir para FR
              2. La aplicación 'FileReaderMassive' debe existir para FRMASS
              3. Debe existir en el CMC el usuario usercapitall
              4. Se debe ejecutar primero FRMASS-Basico_PNS para provisionar los contratos necesarios

* Establecer host "" port "" dir ""
* Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"
* Cumplimentar Formulario y Login
* Establecer cliente
* Hash Map
* Lista Propiedades desde licencia

Caso 1 INOT
-----------
* Empresa "INNOVUS"
* Usuario "VALIDACION"
* Nombre contrato "Aplicación FileReader Massive"
* Menu "Provisión"
* Herramienta "Gestión de aplicaciones"
* Menu herramienta
* Verifica Nombres "lapp-filereader-massive"
* InicioHome
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
* Ruta Origen "D" "inot"
 Credenciales Linux "212.36.69.147" "2295" "root" "latinia"
* Credenciales Linux "212.36.69.147" "2290" "bea" "latinia"
 Ruta destino "/J2EE/IBM/WebSphere/AppServer/profiles/qaver08/filereader/massive"
* Ruta destino "/home/bea/filereader/massive"
 Ruta destino "/J2EE/IBM/WebSphere/AppServer/profiles/AppSrv01/filereader/massive"

Ejemplo 1. Formato iNOT1
* Cabecera
|EMPRESA|CONTRATO|PROVEDOR|FORMATO|ETIQUETA|NUM_MENSAJES|
|INNOVUS|app_fr||inot1||1|
* Cuerpo
|ID|CLAVE|VALOR_CLAVE|TEXTO|
|1||usercapital|pruebainot|

* Escribir Archivo "pruebainot" "-1"
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
* Herramienta "Detalle de contenidos de transacciones"
* Menu herramienta
* BusquedaCriterio "#refUser" "usercapital" "" "LATINIA"
* InicioHome

Ejemplo2: Formato iNOT2
* idioma "Español"
* Proposito ""
* tipoclause "PNS"
* Menu "Provisión"
* Herramienta "Gestión de plantillas"
* Menu herramienta
* Existe tipo de mensaje
* Crear Plantilla "grupo Creadas latinia" "Plantilla publica" "miPlantillaPublica"
* InicioHome
* Menu herramienta
* Crear Seccion especifica "cuerpo" "Señor(a) ${VAR_PUBLICA1} ${VAR_PUBLICA2} se le informa contenido publico" "miPlantillaPublica" "forzar"
* InicioHome
* Menu herramienta
* Crear Plantilla "grupo Creadas latinia" "Plantilla privada" "miPlantillaPriv"
* InicioHome
* Menu herramienta
* Crear Seccion especifica "cuerpo" "contenido privado ${VAR_PRIV1} ${VAR_PRIV2}" "miPlantillaPriv" "forzar"
* tipoclause "SMS"
* Existe tipo de mensaje
* InicioHome
* Menu herramienta
* Crear Plantilla "grupo Creadas latinia" "Plantilla publica" "miPlantillaPublica"
* InicioHome
* Menu herramienta
* Crear Seccion especifica "cuerpo" "Señor(a) ${VAR_PUBLICA1} ${VAR_PUBLICA2} se le informa contenido publico" "miPlantillaPublica" "forzar"
* InicioHome
* Menu herramienta
* Crear Plantilla "grupo Creadas latinia" "Plantilla privada" "miPlantillaPriv"
* InicioHome
* Menu herramienta
* Crear Seccion especifica "cuerpo" "contenido privado ${VAR_PRIV1} ${VAR_PRIV2}" "miPlantillaPriv" "forzar"
* tipoclause "Email"
* Existe tipo de mensaje
* InicioHome
* Menu herramienta
* Crear Plantilla "grupo Creadas latinia" "Plantilla publica" "miPlantillaPublica"
* InicioHome
* Menu herramienta
* Crear Seccion especifica "cuerpo" "Señor(a) ${VAR_PUBLICA1} ${VAR_PUBLICA2} se le informa contenido publico" "miPlantillaPublica" "forzar"
* InicioHome
* Menu herramienta
* Crear Plantilla "grupo Creadas latinia" "Plantilla privada" "miPlantillaPriv"
* InicioHome
* Menu herramienta
* Crear Seccion especifica "cuerpo" "contenido privado ${VAR_PRIV1} ${VAR_PRIV2}" "miPlantillaPriv" "forzar"

* Cabecera
|EMPRESA|CONTRATO|PROVEDOR|FORMATO|ETIQUETA|NUM_MENSAJES|
|INNOVUS|app_fr||inot2||1|
* Cuerpo
|ID|CLAVE|VALOR_CLAVE|ADRESSES|TEXTO|PLANTILLA_PUBLICA|VARIABLES_PLANTILLA_PUBLICA|PLANTILLA_PRIV|VARIABLES_PLANTILLAPRIV|
|1||usercapital|email=comercial@latinia.com##phone=659666666|Prueba Inot2 FR|miPlantillaPublica|var_publica1=publica##var_publica2=publica2|miPlantillaPriv|var_priv1=privada1##var_priv2=privada2|

* Escribir Archivo "pruebainot" "6"
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
* BusquedaCriterio "#refUser" "usercapital" "" "LATINIA"
* InicioHome

Ejemplo3: Formato INOT3
* Cabecera
|EMPRESA|CONTRATO|PROVEDOR|FORMATO|ETIQUETA|NUM_MENSAJES|
|INNOVUS|app_fr||inot3||1|
* Cuerpo
|ID|CLAVE|VALOR_CLAVE|ADRESSES|TEXTO|CHANNELS|DELIV_CHN_MAX|PLANTILLA_PUBLICA|VARIABLES_PLANTILLA_PUBLICA|PLANTILLA_PRIV|VARIABLES_PLANTILLA_PRIV|
|1||usercapital|email=comercial@latinia.com##phone=659666666|Prueba Inot3 FR|sms,email|2|miPlantillaPublica|var_publica1=publicainot3##var_publica2=publica2|miPlantillaPriv|var_priv1=privada1##var_priv2=privada2|

* Escribir Archivo "pruebainot" "8"
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
* BusquedaCriterio "#refUser" "usercapital" "" "LATINIA"
* InicioHome

* LogOut