A partir de una BBDD limpia aprovisiona lo necesario para mover y verificar SMS, EMail, PNS  - Iniciando_spec: 00.02_PlataformaLimpia-ProvisionWS
===========================================================================================

Tags:Provision,00.02_PlataformaLimpia-ProvisionWS,CI
Preconditions:

Si la BBDD NO estuviese limpia, se comporta de manera incremental, esto es agrega datos de provisión a lo que ya exitiese previamente.

LogIN, Almacen de Datos:
* Establecer host "" port "" dir ""
* Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"

* Establecer cliente
* Hash Map
* Lista Propiedades desde licencia

Escenario Provisión Plataforma
------------------------------
Gestión de Aplicaciones
* Crear aplicación WS refProduct"AppAUTOMATED" refPlataforma"LIMSP"
* Crear aplicación WS refProduct"MiAplicacionPIR" refPlataforma"LIMSP"
* Asignar Rol WS "AppAUTOMATED" "ADAPTOR_WS"
* Asignar Rol WS "MiAplicacionPIR" "ADAPTOR_WS"
* Actualizar propiedad aplicación WS refProduct"wapppushemail" refPlataforma"LIMSP" propiedad"CONTENT_TEMPLATE_ENABLE" valor"APPLICATION" visible"true"
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
//Creo M-Apps en la empresa INNOVUS que es la que estoy autentificado ahora
* Organizacion "LATINIA"
* Dar de alta a MApp refApp"lat1MobileApp"
//Propiedades de usuario
* Nueva tabla
|NOMBRE|TIPO|IMPORTANTE|ENCRIPTADA|COMPARTIDA|
|chn_pref|simple|f|f|f|
|robinsongeneral|simple|f|f|f|
|robinsonchnlis|simple|f|f|f|
|cclist|simple|f|f|f|
|noads|simple|f|f|f|
|scoring|simple|f|f|t|
|vip|simple|t|f|f|
|gender|simple|f|f|f|
|gsm|clave|t|f|t|
|email|clave|t|f|t|
|dni|clave|f|f|t|
|twitter_name|clave|t|f|t|
|pais|grupo|f|f|f|
* Definir propiedades de Usuario
//Crear usuario y agegarle MApp
* refUser "userlatinia"
* Crear suscriptor WS
* Nueva tabla
|PROPIEDAD|VALOR|
|twitter_name||
|cclist|12345,54321,9876|
|noads|TRUE|
|chn_pref|pns|
|robinsonchnlist|pns|
|robinsongeneral|FALSE|
|scoring|111|
|vip|no|
|gender|M|
|dni|555888999|
|email||
|gsm|virtual|
|pais|España|
* Agregar propiedad desde WS
* Dar de alta a MApp refApp"lat1MobileApp0"
* Eliminar todas las MApps asociadas a usuario WS
* Registrar AppDevice a un usuario WS "true" "lat1MobileApp"
* Registrar AppDevice a un usuario WS "true" "lat1MobileApp0"
Plantillas
* idioma "es_ES"
* Agregar idioma Plantilla pordefecto"false"
* Grupo "grupo Creadas AUTOMATED"

* tipoMensaje "email"
* tipoContenido "text/html"
* refPlantilla "refPlantilla0"
* nomPlantilla "Mi plantilla"
* Crear Plantilla WS
* nomSeccion "0 - Encabezado"
* Crear seccion general
* nomSeccion "Mi sección especifica0"
* Crear seccion especifica
* nomSeccion "0 - Pie"
* Crear seccion general

* tipoMensaje "sms"
* tipoContenido "text/plain"
* refPlantilla "refPlantilla0"
* nomPlantilla "Mi plantilla"
* Crear Plantilla WS
* nomSeccion "0 - Encabezado"
* Crear seccion general
* nomSeccion "Mi sección especifica0"
* Crear seccion especifica
* nomSeccion "0 - Pie"
* Crear seccion general

* tipoMensaje "pns"
* refPlantilla "refPlantilla0"
* nomPlantilla "Mi plantilla"
* Crear Plantilla WS
* nomSeccion "0 - Encabezado"
* Crear seccion general
* nomSeccion "Mi sección especifica0"
* Crear seccion especifica
* nomSeccion "0 - Pie"
* Crear seccion general

* tipoMensaje "tweet"
* refPlantilla "refPlantilla0"
* nomPlantilla "Mi plantilla"
* Crear Plantilla WS
* nomSeccion "0 - Encabezado"
* Crear seccion general
* nomSeccion "Mi sección especifica0"
* Crear seccion especifica
* nomSeccion "0 - Pie"
* Crear seccion general
//Empresa
* Empresa "CAPITAL"
* Crear empresa WS
* Organizacion "CAPITAL"
* Crear ORG
//Usuarios
* Usuario "USER1"
* Crear nuevo usuario WS
//Contratos para CAPITAL
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

* refproduct "ui-msg-detail"
* refcontract ""
* crear contrato WS
* Asignar usuario a contrato

* refproduct "ui-msg-statistics"
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

* Nueva tabla
|NOMBRE|TIPO|IMPORTANTE|ENCRIPTADA|COMPARTIDA|
|chn_pref|simple|f|f|f|
|robinsongeneral|simple|f|f|f|
|robinsonchnlis|simple|f|f|f|
|cclist|simple|f|f|f|
|noads|simple|f|f|f|
|scoring|simple|f|f|t|
|vip|simple|t|f|f|
|gender|simple|f|f|f|
|gsm|clave|t|f|t|
|email|clave|t|f|t|
|dni|clave|f|f|t|
|twitter_name|clave|t|f|t|
|pais|grupo|f|f|f|
* Definir propiedades de Usuario
//Crear usuario y agegarle MApp
* refUser "usercapital"
* Crear suscriptor WS
* Nueva tabla
|PROPIEDAD|VALOR|
|twitter_name||
|cclist|12345,54321,9876|
|noads|TRUE|
|chn_pref|pns|
|robinsonchnlist|pns|
|robinsongeneral|FALSE|
|scoring|111|
|vip|no|
|gender|M|
|dni|555888999|
|email||
|gsm|virtual|
|pais|España|
* Agregar propiedad desde WS
* Dar de alta a MApp refApp"mymappautomated"
* Dar de alta a MApp refApp"mymappautomated0"
* Eliminar todas las MApps asociadas a usuario WS
* Registrar AppDevice a un usuario WS "true" "mymappautomated"
* Registrar AppDevice Apple a un usuario WS "false" "mymappautomated0"
* Asignar Propiedad/Contrato WS "MT_PNS" "LIMSP" "CUSTOMER_PREFERENCES" "TRUE"
* Asignar Propiedad/Contrato WS "MT_PNS" "LIMSP" "PNS_APP" "mymappautomated"
* Asignar Propiedad/Contrato WS "MT_PNS" "LIMSP" "ORGANIZATION" "CAPITAL"

* Forzar recarga WS
Escenario Prueba Funcionamiento GUI
----------------------------------
* Cumplimentar Formulario y Login
LATINIA
* Empresa "INNOVUS"
MO-MT
* Menu "Diagnosis"
* Herramienta "Simulador de móvil"
* Menu herramienta
* TransaccionesPullPush
|Aplicacion|Texto|Respuesta|
|ALIASCAPITAL1|ñ Ñ < > *  € ! ' á é í ó ú à è ì ò ù|Servicio WS. Servicio ofrecido por WS normal (ñ Ñ < > *  € ! ' á é í ó ú à è ì ò ù)|
* InicioHome
EMail
* refcontract "#wapppushemail"
* Asignar Propiedad/Contrato WS "#wapppushemail" "LIMSP" "CONTENT_TEMPLATE" "NONE"
* Asignar Clausula tipoClausula"MT" formato"email" operador"EMail-SMTP" canal"comercial@latinia.com" credito""
* forzarrecarga
* idioma "es_ES"
* Herramienta "Envíos email"
* Menu herramienta
* Titulo "Envio Email Automated"
* Texto "ñ Ñ < > $ € ! ' á é í ó ú à è ì ò ù"
* Parametro Variable "parametro"
* Valor Parametro "valor"
* Transaccionar EnvioEmail "testing@latinia.com" "EtiquetaEmail" "refPlantilla0" " "
* InicioHome
SMS
* refcontract "#wapppush"
* Asignar Clausula tipoClausula"MT" formato"sms" operador"Virtual" canal"+000001" credito""
* forzarrecarga
* Herramienta "Envíos SMS"
* Menu herramienta
* Transaccionar EnviosSMS "unknown" "Unknown ñ Ñ < > $ € ! ' á é í ó ú à è ì ò ù"
* Transaccionar EnviosSMS "virtual" "Virtual ñ Ñ < > $ € ! ' á é í ó ú à è ì ò ù"
* Transaccionar EnviosSMS Diferido "virtual" "Diferido Virtual"
* LogOut
* ldmcontable "histAndSummary"
CAPITAL
* Establecer host "" port "" dir ""
* Establecer login "CAPITAL" user "USER1" pass "USER1"
* Cumplimentar Formulario y Login

PNS
* Empresa "CAPITAL"
* Set XML PNSunitario "push-pns-unitario.xml" "CAPITAL" "MT_PNS" "mymappautomated" "refuser" "usercapital" "Unitario-Tiene un saldo de 88eur "
* Insercion WS "pns" "wtest-pns" "normalAuth"
* InicioHome
* Menu "--"
* Herramienta "Detalle de transacciones Automated"
* Menu herramienta
* BusquedaCriterio "#refUser" "usercapital" "" "CAPITAL"
* Ver estados "Entrega confirmada a usuario"

LogOut:
* LogOut

//Montar QA_TOOLS
* Establecer Instancia
* Establecer datos de acceso a AWS Tools
* Ejecutar proceso "LAT_SWF_QA_MountQATOOLS"
