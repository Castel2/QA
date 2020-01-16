A partir de una BBDD limpia aprovisiona lo necesario para mover y verificar SMS, EMail, PNS  - Iniciando_spec: 09.01_PlataformaLimpia-Provision
===========================================================================================

Tags:Provision,09.01_PlataformaLimpia-Provision
Preconditions:

Si la BBDD no estuviese limpia, se comporta de manera incremental, esto es agrega datos de provisión a lo que ya exitiese previamente.
Este escenario es una herramienta ideal para generar volumen de cosas contratadas en una Plataforma
NOTA.- En caso de probar SDP+INF pero sin AE!!! Fallará el paso en que se prueban los INDICES de la herramienta "Gestión de propiedades de los clientes". Para que el escenario pase hay que 'desactivar' este paso.

LogIN, Almacen de Datos:
* Establecer host "" port "" dir ""
* Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"
* Cumplimentar Formulario y Login
* Establecer cliente
* Hash Map
* Lista Propiedades desde licencia
Escenario primero
-----------------
* Proposito "provision"
* Actualizar propiedad aplicación WS refProduct"wapppushemail" refPlataforma"LIMSP" propiedad"CONTENT_TEMPLATE_ENABLE" valor"APPLICATION" visible"true"
* Crear aplicación WS refProduct"AppAUTOMATED" refPlataforma"LIMSP"
* Actualizar propiedad aplicación WS refProduct"wsubscribers" refPlataforma"LGUI" propiedad"ADMIN_MODE" valor"TRUE" visible""
* Actualizar propiedad aplicación WS refProduct"wgestenterprise" refPlataforma"LGUI" propiedad"MNG_ORGANIZATIONS" valor"TRUE" visible"true"
* Actualizar propiedad aplicación WS refProduct"wtest-pns" refPlataforma"LIMSP" propiedad"ORGANIZATION" valor"" visible"true"
* Actualizar propiedad aplicación WS refProduct"wtest-pns" refPlataforma"LIMSP" propiedad"PNS_APP" valor"" visible"true"
* Actualizar propiedad aplicación WS refProduct"wtest-pns" refPlataforma"LIMSP" propiedad"CUSTOMER_PREFERENCES" valor"FALSE" visible"true"
* Actualizar propiedad aplicación WS refProduct"AppAUTOMATED" refPlataforma"LIMSP" propiedad"CUSTOMER_PREFERENCES" valor"FALSE" visible"true"
* Actualizar propiedad aplicación WS refProduct"AppAUTOMATED" refPlataforma"LIMSP" propiedad"ORGANIZATION" valor"" visible"true"
* Actualizar propiedad aplicación WS refProduct"AppAUTOMATED" refPlataforma"LIMSP" propiedad"INOT_MAX_CHN" valor"0" visible"true"
* Actualizar propiedad aplicación WS refProduct"AppAUTOMATED" refPlataforma"LIMSP" propiedad"INOT_CHN_PREF" valor"CONTRACT" visible"true"
* Actualizar propiedad aplicación WS refProduct"AppAUTOMATED" refPlataforma"LIMSP" propiedad"PNS_APP" valor"" visible"true"
* Actualizar propiedad aplicación WS refProduct"AppAUTOMATED" refPlataforma"LIMSP" propiedad"PNS_DOWNLOADABLE" valor"" visible"true"
* Actualizar propiedad aplicación WS refProduct"AppAUTOMATED" refPlataforma"LIMSP" propiedad"PNS_CONFIDENTIAL" valor"TRUE" visible"true"
* Actualizar propiedad aplicación WS refProduct"AppAUTOMATED" refPlataforma"LIMSP" propiedad"PNS_CONFIDENTIAL_NONE" valor"DEFAULT" visible"true"
//canales
* Crear Canal Email WS canal"comercial@latinia.com"
* Crear Modelo SMS WS modelo"MT" operador"Virtual" canal"+000001"
* Establecer Ruta de Salida refOperador"Google" canal"*" formato"PNS"
* Establecer Ruta de Salida refOperador"Apple" canal"*" formato"PNS"
* Establecer Ruta de Salida refOperador"EMail-SMTP" canal"comercial@latinia.com" formato"EMail"
* Establecer Ruta de Salida refOperador"Virtual" canal"+000001" formato"SMS"
//Empresa
* Empresa "CAPITAL"
* Crear empresa WS
* Organizacion "CAPITAL"
* Crear ORG
//Usuarios
* Usuario "USER1"
* Crear nuevo usuario WS
//Contratos
* refproduct "lman-vcontent"
* refcontract ""
* crear contrato WS
* Asignar usuario a contrato

* refproduct "lman-vcontentmail"
* refcontract ""
* crear contrato WS
* Asignar usuario a contrato

* refproduct "lman-vstats"
* refcontract ""
* crear contrato WS
* Asignar usuario a contrato

* refproduct "limsp-ui-mdetail"
* refcontract ""
* crear contrato WS
* Asignar usuario a contrato

* refproduct "limsp-ui-stats"
* refcontract ""
* crear contrato WS
* Asignar usuario a contrato

* refproduct "lman-pnsmapps"
* refcontract ""
* crear contrato WS
* Asignar usuario a contrato

* refproduct "wtest-pns"
* refcontract "MT_PNS"
* crear contrato WS
* Asignar usuario a contrato
* Asignar Clausula tipoClausula"MT" formato"pns" operador"Google" canal"*" credito""
* Asignar Clausula tipoClausula"MT" formato"pns" operador"Apple" canal"*" credito""

* refproduct "AppAUTOMATED"
* refcontract "MT_SEND"
* crear contrato WS
* Asignar usuario a contrato
* Asignar Clausula tipoClausula"MT" formato"pns" operador"Google" canal"*" credito""
* Asignar Clausula tipoClausula"MT" formato"pns" operador"Apple" canal"*" credito""
* Asignar Clausula tipoClausula"MT" formato"sms" operador"Virtual" canal"+000001" credito""
* Asignar Clausula tipoClausula"MT" formato"email" operador"EMail-SMTP" canal"comercial@latinia.com" credito""

* refproduct "ServiceAdaptorWS"
* refcontract "MO_MT"
* crear contrato WS
* Asignar usuario a contrato
* Asignar Clausula tipoClausula"MOMT" formato"sms" operador"Virtual" canal"+000001" credito""

//----------------------------LGUI-----------------------------------------------------------
Creo M-Apps en la empresa INNOVUS que es la que estoy autentificado ahora
* InicioHome
* Menu "Provisión"
* Herramienta "Gestión de M-Apps"
* Menu herramienta

* Proposito "--"
* PNS_MApp_Manager "lat1MobileApp" "LATINIA"
* PNS_MApp_Manager "lat2MobileApp" "LATINIA"
* PNS_MApp_Manager "lat3MobileApp" "LATINIA"

* InicioHome
* Proposito "provision"
* Empresa "LATINIA"
* Menu "Provisión"
* Herramienta "Gestión de propiedades de clientes"
* Menu herramienta

* UserProps
Indices

* InicioHome
* Herramienta "Customer Management Center"
* Menu herramienta
* CrearSuscriptor "userlatinia" "conProp"
* MApps "userlatinia" "lat1MobileApp0"


Creo Plantillas
* InicioHome
* Menu "Provisión"
* Herramienta "Gestión de plantillas"
* Menu herramienta

* tipoclause "Email"
* idioma "es_ES"
* Establece Idioma
* Establece Variables "mi_variable_directa"
* Existe tipo de mensaje
* Borrar Antigua "plantilla" "refPlantilla0"
* Borrar Antigua "seccion" "0 - Encabezado"
* Borrar Antigua "seccion" "0 - Pie"
* Crear Seccion generica "0 - Encabezado" ""
* Crear Plantilla "grupo Creadas AUTOMATED" "Mi plantilla" "refPlantilla0"
* InicioHome
* Menu herramienta
* Asignar Seccion generica "0 - Encabezado" "refPlantilla0"
* InicioHome
* Menu herramienta
* Crear Seccion generica "0 - Pie" ""
* Asignar Seccion generica "0 - Pie" "refPlantilla0"
* InicioHome
* Menu herramienta
* Crear Seccion especifica "Mi sección especifica0" "Contenido sección específica" "refPlantilla0" "forzarNO"
* InicioHome
* Menu herramienta
* VisualizarPlantilla "refPlantilla0"

* InicioHome
* Menu herramienta
* tipoclause "SMS"
* idioma "es_ES"
* Establece Idioma
* Establece Variables "mi_variable_directa"
* Existe tipo de mensaje
* Borrar Antigua "plantilla" "refPlantilla0"
* Borrar Antigua "seccion" "0 - Encabezado"
* Borrar Antigua "seccion" "0 - Pie"
* Crear Seccion generica "0 - Encabezado" ""
* Crear Plantilla "grupo Creadas AUTOMATED" "Mi plantilla" "refPlantilla0"
* InicioHome
* Menu herramienta
* Asignar Seccion generica "0 - Encabezado" "refPlantilla0"
* InicioHome
* Menu herramienta
* Crear Seccion generica "0 - Pie" ""
* Asignar Seccion generica "0 - Pie" "refPlantilla0"
* InicioHome
* Menu herramienta
* Crear Seccion especifica "Mi sección especifica0" "Contenido sección específica" "refPlantilla0" "forzarNO"
* InicioHome
* Menu herramienta
* VisualizarPlantilla "refPlantilla0"

* InicioHome
* Menu herramienta
* tipoclause "PNS"
* idioma "es_ES"
* Establece Idioma
* Establece Variables "mi_variable_directa"
* Existe tipo de mensaje
* Borrar Antigua "plantilla" "refPlantilla0"
* Borrar Antigua "seccion" "0 - Encabezado"
* Borrar Antigua "seccion" "0 - Pie"
* Crear Seccion generica "0 - Encabezado" ""
* Crear Plantilla "grupo Creadas AUTOMATED" "Mi plantilla" "refPlantilla0"
* InicioHome
* Menu herramienta
* Asignar Seccion generica "0 - Encabezado" "refPlantilla0"
* InicioHome
* Menu herramienta
* Crear Seccion generica "0 - Pie" ""
* Asignar Seccion generica "0 - Pie" "refPlantilla0"
* InicioHome
* Menu herramienta
* Crear Seccion especifica "Mi sección especifica0" "Contenido sección específica" "refPlantilla0" "forzarNO"
* InicioHome
* Menu herramienta
* VisualizarPlantilla "refPlantilla0"

* InicioHome
* Menu "Análisis"
* Herramienta "Generador de informes PDF"
* Menu herramienta
* Crear
* Programar
* Email
* Activar

* InicioHome
* Menu "Provisión"
* Herramienta "Gestión de aplicaciones"
* Menu herramienta
* Crear ""
* Roles "MiAplicacionPIR" "ADAPTOR_WS"
* InicioHome
* Menu herramienta
* Roles "AppAUTOMATED" "ADAPTOR_WS"
* forzarrecarga
Muevo transacciones en la empresa definida al comienzo.

* InicioHome
* Menu "Diagnosis"
* Empresa "CAPITAL"
* Herramienta "Simulador de móvil"
* Menu herramienta
* TransaccionesPullPush
|Aplicacion|Texto|Respuesta|
|ALIASCAPITAL1|ñ Ñ < > *  € ! ' á é í ó ú à è ì ò ù|Servicio WS. Servicio ofrecido por WS normal (ñ Ñ < > *  € ! ' á é í ó ú à è ì ò ù)|
|ALIASCAPITAL1|ñ Ñ < > *  € ! ' á é í ó ú à è ì ò ù|Servicio WS. Servicio ofrecido por WS normal (ñ Ñ < > *  € ! ' á é í ó ú à è ì ò ù)|
|ALIASCAPITAL1|ñ Ñ < > *  € ! ' á é í ó ú à è ì ò ù|Servicio WS. Servicio ofrecido por WS normal (ñ Ñ < > *  € ! ' á é í ó ú à è ì ò ù)|

* tipoclause "email"
* InicioHome
* Empresa "INNOVUS"
* Menu "Provisión"
* Herramienta "Gestión de contratos"
* Nombre contrato "Envíos email"
* Menu herramienta
* ComprobarClausulas "EMail-SMTP"
* InicioHome
* Menu herramienta
* Proposito "cambiar"
* Cambiar Propiedad Contrato "CONTENT_TEMPLATE"
* InicioHome
* forzarrecarga
* Menu "Diagnosis"
* Herramienta "Envíos email"
* Menu herramienta
* Titulo "Envio Email Automated"
* Texto "ñ Ñ < > $ € ! ' á é í ó ú à è ì ò ù"
* Parametro Variable "parametro"
* Valor Parametro "valor"
* Transaccionar EnvioEmail "testing@latinia.com" "EtiquetaEmail" "refPlantilla0" " "
* InicioHome
* Menu herramienta
* Titulo "Envio Email Automated"
* Texto "ñ Ñ < > $ € ! ' á é í ó ú à è ì ò ù"
* Parametro Variable "parametro"
* Valor Parametro "valor"
* Transaccionar EnvioEmail "testing@latinia.com" "EtiquetaEmail" "refPlantilla0" " "
* InicioHome
* Menu herramienta
* Titulo "Envio Email Automated"
* Texto "ñ Ñ < > $ € ! ' á é í ó ú à è ì ò ù"
* Parametro Variable "parametro"
* Valor Parametro "valor"
* Transaccionar EnvioEmail "testing@latinia.com" "EtiquetaEmail" "refPlantilla0" " "
* InicioHome
* Menu herramienta
* Titulo "Envio Email Automated"
* Texto "ñ Ñ < > $ € ! ' á é í ó ú à è ì ò ù"
* Parametro Variable "parametro"
* Valor Parametro "valor"
* Transaccionar EnvioEmail "testing@latinia.com" "EtiquetaEmail" "refPlantilla0" " "
* InicioHome
* Menu "Provisión"
* Herramienta "Gestión de contratos"
* Proposito "devolver"
* Menu herramienta
* Cambiar Propiedad Contrato "CONTENT_TEMPLATE"
* forzarrecarga
* InicioHome

* tipoclause "sms"
* Menu "Provisión"
* Herramienta "Gestión de contratos"
* Nombre contrato "Envíos SMS"
* Menu herramienta
* Empresa "INNOVUS"
* ComprobarClausulas "Virtual"
* Menu "Diagnosis"
* Herramienta "Envíos SMS"
* InicioHome
* Menu herramienta
* Transaccionar EnviosSMS "unknown" "Unknown ñ Ñ < > $ € ! ' á é í ó ú à è ì ò ù"
* Transaccionar EnviosSMS "virtual" "Virtual ñ Ñ < > $ € ! ' á é í ó ú à è ì ò ù"
* Transaccionar EnviosSMS Diferido "virtual" "Diferido Virtual"

LogOut:
* LogOut

Ejecuto Hist&Summary para tener estadísticas
* ldmcontable "histAndSummary"

Aprovisiono y veo estadísticas en empresa distinta a innovus. Normalmente utilizamos CAPITAL. En caso de cambiarla, se deben adaptar las lineas aqui debajo en el paso de la creación de suscriptores.
* Establecer host "" port "" dir ""
* Establecer login "CAPITAL" user "USER1" pass "USER1"
* Cumplimentar Formulario y Login

* InicioHome
* Menu "Herramientas"
* Herramienta "Gestión de M-Apps Automated"
* Menu herramienta

* Proposito "provision"
* PNS_MApp_Manager "mymappautomated" ""
* PNS_MApp_Manager "myMApp" ""
* PNS_MApp_Manager "yourMApp" ""
* PNS_MApp_Manager "ourMApp" ""
* PNS_MApp_Manager "TrickyMApp" ""
* PNS_MApp_Manager "yesMApp" ""

* InicioHome
* Menu "--"
* Herramienta "Detalle de transacciones Automated"
* Menu herramienta

Detalle de transacciones:
* Ver listado "ServiceAdaptor WS Automated"
* Exportacion "Exportacion Automática con Automated"

* InicioHome
* Herramienta "Estadísticas Automated"
* Menu herramienta
* Estadisticas "miCSVEstadisticas creado con Automated"

LogOut:
* LogOut

Escenario segundo
-----------------
Regreso a INNOVUS para crear suscriptores con las M-Apps que recién acabo de crear en el paso anterior.
USER1
* Menu "Provisión"
* Herramienta "Gestión de propiedades de clientes"
* Menu herramienta

Aqui habrá que desactivar el paso de 'Indices' en el caso que no esté instalado AlertEngine (AE)
* Empresa "CAPITAL"
* Origenes
* UserProps
* OperProps "provision"
Indices

* InicioHome
* Herramienta "Customer Management Center"
* Menu herramienta
* Proposito "provision"
* Empresa "CAPITAL"
* CrearSuscriptor "liliuser" "conProp"
* CrearSuscriptor "usercapital" "conProp"
* MApps "usercapital" "mymappautomated0"
* MApps "liliuser" "mymapp0"
* MApps "liliuser" "yourmapp0"

Se configura para que los masivos no salgan
* InicioHome
* Herramienta "Gestión de contratos"
* Menu herramienta
* ConProps "Envíos de PushNots Automated" "CUSTOMER_PREFERENCES" "TRUE"
* Menu herramienta
* ConProps "Envíos de PushNots Automated" "PNS_APP" "mymappautomated0"
* Menu herramienta
* ConProps "Envíos de PushNots Automated" "ORGANIZATION" "CAPITAL"
* Menu herramienta
* ConProps "Envíos de PushNots Automated" "PNS_MASSIVE" "TRUE"
* forzarrecarga

LogOut:
* LogOut

Entro en CAPITAL y envio una PNS UNITARIA al usuario que hemos aprovisionado antes
//* Establecer host "" port "" dir ""
* Establecer login "CAPITAL" user "USER1" pass "USER1"
* Cumplimentar Formulario y Login

PNS Unitaria mediante inserción WS y la busca en estadísticas
InsercionWSsdp:
*Set XML PNSunitario "push-pns-unitario.xml" "CAPITAL" "MT_PNS" "mymappautomated0" "refuser" "usercapital" "Unitario-Tiene un saldo de 88eur "
* Insercion WS "pns" "wtest-pns" "normalAuth"
* InicioHome
* Menu "--" 
* Herramienta "Detalle de transacciones Automated"
* Menu herramienta
* BusquedaCriterio "#refUser" "usercapital" "" "CAPITAL"
* Ver estados "Entrega confirmada a usuario"

LogOut:
* LogOut
//--------------------------------------------------------------------------------


