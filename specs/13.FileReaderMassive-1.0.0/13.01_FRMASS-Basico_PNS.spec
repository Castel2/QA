Prueba FileReader con formato PNS - Iniciando_spec: 13.01_FRMASS-Basico_PNS
=================================

Tags: FRMASS, massive

Created by amartinez

Documentación: http://www.latiniaservices.com/supportsuite/buscador/es/Plataforma_LIMSP-SDP.htm#FileReader/Ejemplos_practicos_de_uso.htm%3FTocPath%3DSuite%2520de%2520Aplicaciones%7CFileReader%7C_____6

Puesto que FileReader interactúa de forma directa con los recursos disponibles en la Plataforma LIMSP© SDP 360º, y con la provisión realizada en Limsp, en los ejemplos a continuación se parte de los siguientes supuestos:

    Login/empresa: La empresa a través de la cual se realizarán los envíos es 'INNOVUS'.
    Contrato para todos los formatos: El contrato se denomina 'app_fr'.
    Prioridad: La licencia de Limsp tiene habilitada la función de priorización.
    Formato Cabecera: Se emplea la cabecera por defecto para el formato de los ejemplos.


Dependencias: 1. La aplicación 'lsms-filereader' debe existir para FR
              2. La aplicación 'lsms-FileReader' debe existir para FRMASS
              3. Debe existir en el CMC el usuario usercapital, si no está, se puede ejecutar la prueba “09.01_PlataformaLimpia-Provision” ó “03.06_Gestion_Suscriptores” pero verificando que se creará para LATINIA
              4. Se debe ejecutar la prueba “06.10_Gestion_de_Canales.spec”


* Establecer host "" port "" dir ""
* Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"
* Cumplimentar Formulario y Login
* Establecer cliente
* Hash Map
* Lista Propiedades desde licencia

Caso 1 PNS
-----------------------------------
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
Se le asigna valores a las propiedades necesarias para la correcta ejecución de la prueba
* Menu herramienta
* propiedades "lapp-filereader-massive" "CONTENT_TEMPLATE_ENABLE" "APPLICATION"
* InicioHome
* Menu herramienta
* propiedades "lapp-filereader-massive" "CONTENT_TEMPL_ENABLE_PRIV" "APPLICATION"
* InicioHome
* Menu herramienta
* propiedades "lapp-filereader-massive" "CUSTOMER_PREFERENCES" "TRUE"
* InicioHome
* Menu herramienta
* propiedades "lapp-filereader-massive" "INOT_CHN_PREF" "CONTRACT"
* InicioHome
* Menu herramienta
* propiedades "lapp-filereader-massive" "INOT_MAX_CHN" "4"
* InicioHome
* Menu herramienta
* propiedades "lapp-filereader-massive" "PNS_APP" "*"
* InicioHome
* Menu herramienta
* propiedades "lapp-filereader-massive" "ORGANIZATION" "visible"
* InicioHome
Se verifica si existe el contrato, de no ser así se crea el contrato y se le agregan las clausulas pns, sms y email
* Herramienta "Gestión de contratos"
* Menu herramienta
* refcontract "app_fr"
* tipoclause "pns"
* ConClause ""
* InicioHome
* Menu herramienta
* tipoclause "smsMT"
* ConClause ""
* tipoclause "sms"
* InicioHome
* Menu herramienta
* ComprobarClausulas "Movistar-ES"
* InicioHome
* Menu herramienta
* ComprobarClausulas "Vodafone-ES"
* InicioHome
* Menu herramienta
* ComprobarClausulas "Orange-ES"
* InicioHome
* Menu herramienta
* ComprobarClausulas "Virtual"
* InicioHome
* Menu herramienta
* tipoclause "email"
* ConClause "forzarrecarga"
* InicioHome
* Menu herramienta
* ConProps "Aplicación FileReader Massive Automated" "ORGANIZATION" "LATINIA"
* Menu herramienta

Se relaciona el contrato con un usuario
* Asignar user
* InicioHome
* forzarrecarga
Se establecen los datos necesarios para acceder al servidor y hacer la transferencia de archivos
* Ruta Origen "D" "pns"
ip y puerto de conexión sftp o ssh, usuario y pass
* Credenciales Linux "212.36.69.147" "2290" "bea" "latinia"
 Credenciales Linux "212.36.69.147" "2295" "root" "latinia"
* Ruta destino "/home/bea/filereader/massive"
 Ruta destino "/J2EE/IBM/WebSphere/AppServer/profiles/AppSrv01/filereader/massive"
 Ruta destino "/J2EE/IBM/WebSphere/AppServer/profiles/qaver08/filereader/massive"

Ejemplo1: Formato PNS1
When genera y procesa "pruebapns.ready"
* Cabecera
|EMPRESA|CONTRATO|PROVEDOR|FORMATO|ETIQUETA|NUM_MENSAJES|
|INNOVUS|app_fr||pns1||1|
* Cuerpo
|ID|CLAVE_CLIENTE|VARLOR_CLAVE|TEXTO|
|1||usercapital|texto prueba pns1|

* Escribir Archivo "pruebapns" "-1"
* ldmcontable "filereadermass"
* LogOut

* ldmcontable "histAndSummary"
* Cumplimentar Formulario y Login
Then comprueba que se ha renombrado con extensión.closed
* Traer Archivo ".closed"
* Es Igual
* Esperar "180"
* InicioHome
Then comprueba que muestra los mensajes en estadisticas
* Menu "Análisis"
* Herramienta "Detalle de contenidos de transacciones"
* Menu herramienta
* BusquedaCriterio "#refUser" "usercapital" "" "LATINIA"
* InicioHome

Ejemplo 2: Formato mi_formato_pns
* idioma "Español"
* Proposito ""
* tipoclause "PNS"
* Menu "Provisión"
* Herramienta "Gestión de plantillas"
* Menu herramienta
* Existe tipo de mensaje
* Crear Plantilla "grupo Creadas latinia" "Plantilla publica pns" "plant_publica_pns"
* InicioHome
* Menu herramienta
* Crear Seccion especifica "cuerpo" "Señor(a) ${CUSTOMER} se le informa contenido publico" "plant_publica_pns" "forzar"
* InicioHome
* Menu herramienta
* Crear Plantilla "grupo Creadas latinia" "Plantilla privada pns" "plant_priv_pns"
* InicioHome
* Menu herramienta
* Crear Seccion especifica "cuerpo" "contenido privado ${MONTO}" "plant_priv_pns" "forzar"

* Cabecera
|EMPRESA|CONTRATO|PROVEDOR|FORMATO|ETIQUETA|NUM_MENSAJES|
|INNOVUS|app_fr||miFormatopns||1|
* Cuerpo
|ID|CLAVE_CLIENTE|VARLOR_CLAVE|TEXTO_PUBLICO|TEXTO_PRIV|PLANTILLA_PUBLICA|PLANTILLA_PRIV|VARIABLE_PUBLICA|VARIABLE_PRIVADA|
|1||usercapital|texto publico1|texto privado1|plant_publica_pns|plant_priv_pns|monto=50000|customer=pepito|

* Escribir Archivo "pruebapns_miformato" "-1"
* ldmcontable "filereadermass"
LogOut:
* LogOut

* ldmcontable "histAndSummary"
* Cumplimentar Formulario y Login
* Traer Archivo ".closed"
* Es Igual
* Esperar "180"
* InicioHome
* Menu "Análisis"
* Herramienta "Detalle de contenidos de transacciones"
* Menu herramienta
* BusquedaCriterio "#refUser" "usercapital" "" "LATINIA"
* InicioHome

LogOut:
* LogOut

