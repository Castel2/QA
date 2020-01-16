[SDP-687] Comprueba las funciones de plantillas en SDP 'LEFT' 'RIGHT' 'IMG'  - Iniciando_spec: 02.02_G_Plantillas_FUNCIONES_[SDP-687]v2
==========================================================================

Tags:transaccionamiento, email
Preconditions:
Requisitos.-
1.- El escenario supone que está licenciada el transaccionamiento EMAIL

2.- Se requiere ubicar en el BinaryStore el fichero 'HomeLimsp.png'
	El path se especifica en lconfig 'ld-binarystore'
	Por ejemplo si tenemos "_config/ldata/ld-binarystore/tbinarystore/fileExtern = /LIMSP-SDP/extern"
...habrá que disponer la ruta Weblogic "/LIMSP-SDP/extern/innovus/img/HomeLimsp.png"
o para WAS "/J2EE/IBM/WebSphere/AppServer/profiles/AppSrv01/extern/innovus/img/HomeLimsp.png" o "/J2EE/IBM/WebSphere/AppServer/profiles/qaver08/extern/innovus/img/HomeLimsp.png"

Véase (http://devel-51:8080/browse/SDP-687)

3. - Se requiere habilitar la configuración para guardar parámetros de plantilla, en el properties lmod-transactionStore ubicar config/bus/transactionStore/storeTemplateParams= emoji_automated1
o 'dejar el config/bus/transactionStore/storeTemplateParams' sin ningun objeto indicado. Véase (https://jira.corp.latiniaservices.com/browse/SDP-760)

* Establecer host "" port "" dir ""
* Establecer login "" user "" pass ""
* Cumplimentar Formulario y Login
* Establecer cliente
* Hash Map
* Lista Propiedades desde licencia
* Verificar properties emojis "lmod-transactionStore.properties" "config/bus/transactionStore/storeTemplateParams"
Escenario primero
-----------------
//* Hash Map
* InicioHome
* Menu "Provisión"
* Herramienta "Gestión de plantillas"
* Menu herramienta
* tipoclause "Email"
* idioma "es_ES"
* Establece Idioma
* Establece Variables "mi_variable_directa"
* Existe tipo de mensaje
* Borrar Antigua "plantilla" "refPlantilla1"
* Borrar Antigua "plantilla" "refPlantilla2"
* Borrar Antigua "plantilla" "refPlantilla3"
* Borrar Antigua "plantilla" "refPlantilla4"
* Borrar Antigua "plantilla" "refPlantillaEmojis"
* Borrar Antigua "seccion" "1 - Encabezado"
* Borrar Antigua "seccion" "1 - Pie"
* Crear Seccion generica "1 - Encabezado" ""
* Crear Seccion generica "1 - Pie" ""

* Crear Plantilla "grupo Creadas AUTOMATED" "Mi plantilla1" "refPlantilla1"
* Asignar Seccion generica "1 - Encabezado" "refPlantilla1"
* Asignar Seccion generica "1 - Pie" "refPlantilla1"
* Proposito "FuncionesLeftRight"
* Crear Seccion especifica "Prueba funciones right left" "Probando LEFT RIGHT" "refPlantilla1" "NOforzar"

* Crear Plantilla "grupo Creadas AUTOMATED" "Mi plantilla2" "refPlantilla2"
* Asignar Seccion generica "1 - Encabezado" "refPlantilla2"
* Asignar Seccion generica "1 - Pie" "refPlantilla2"
* Proposito "FuncionesIMG"
* Crear Seccion especifica "Prueba funciones IMG" "Probando Imágenes incrustadas" "refPlantilla2" "NOforzar"

* Crear Plantilla "grupo Creadas AUTOMATED" "Mi plantilla3" "refPlantilla3"
* Asignar Seccion generica "1 - Encabezado" "refPlantilla3"
* Asignar Seccion generica "1 - Pie" "refPlantilla3"
* Proposito "FuncionesEQUALS"
* Crear Seccion especifica "Prueba funcion EQUALS" "Probando EQUALS" "refPlantilla3" "NOforzar"

* Crear Plantilla "grupo Creadas AUTOMATED" "Mi plantilla4" "refPlantilla4"
* Asignar Seccion generica "1 - Encabezado" "refPlantilla4"
* Asignar Seccion generica "1 - Pie" "refPlantilla4"
* Proposito "FuncionesURLEnconde"
* Crear Seccion especifica "Prueba funcion urlEnconde" "Probando urlEnconde" "refPlantilla4" "NOforzar"

* Valor Parametro "😀 😃 🤪 😇"
* Crear Variable "Emoji_Automated1"
* Crear Mapa Valores "Mapa_Emoji_Automated"

* Crear Plantilla "grupo Creadas AUTOMATED" "Mi plantillaEmojis" "refPlantillaEmojis"
* Asignar Seccion generica "1 - Encabezado" "refPlantillaEmojis"
* Asignar Seccion generica "1 - Pie" "refPlantillaEmojis"
* Proposito "FuncionesEMOJIS"
* Crear Seccion especifica "Prueba funciones variablesEmojis" "Probando variables de emojis: ${Emoji_Automated1}" "refPlantillaEmojis" "NOforzar"
* Crear Seccion especifica "Prueba funciones mapaEmojis" "Probando variables de emojis: <limsp:map name=\"mapa_emoji_automated\">clave_automated</limsp:map>" "refPlantillaEmojis" "forzar"

* Menu "--"
* Herramienta "Gestión de aplicaciones"
* InicioHome
* Menu herramienta
* propiedades "wapppushemail" "CONTENT_TEMPLATE_ENABLE" "APPLICATION"

* Menu "Diagnosis"
* Herramienta "Envíos email"
* InicioHome
* Menu herramienta
* Titulo "Funciones de plantillas LEFT RIGHT"
* Texto "--"
* Parametro Variable "parametro"
* Valor Parametro "valor"
* Transaccionar EnvioEmail "testing@latinia.com" "EtiquetaEmail" "refPlantilla1" " "
* InicioHome
* Menu "Análisis"
* Herramienta "Detalle de contenidos de transacciones de email"
* Menu herramienta
* Ver Plantilla

* Menu "Diagnosis"
* Herramienta "Envíos email"
* InicioHome
* Menu herramienta
* Titulo "Funciones de plantillas IMG"
* Texto "--"
* Parametro Variable "parametro"
* Valor Parametro "valor"
* Transaccionar EnvioEmail "testing@latinia.com" "EtiquetaEmail" "refPlantilla2" " "
* InicioHome
* Menu "Análisis"
* Herramienta "Detalle de contenidos de transacciones de email"
* Menu herramienta
* Ver Plantilla

* Menu "Diagnosis"
* Herramienta "Envíos email"
* InicioHome
* Menu herramienta
* Titulo "Funciones de plantillas EQUALS"
* Texto "--"
* Parametro Variable "parametro"
* Valor Parametro "valor"
* Transaccionar EnvioEmail "testing@latinia.com" "EtiquetaEmail" "refPlantilla3" " "
* InicioHome
* Menu "Análisis"
* Herramienta "Detalle de contenidos de transacciones de email"
* Menu herramienta
* Ver Plantilla

* Menu "Diagnosis"
* Herramienta "Envíos email"
* InicioHome
* Menu herramienta
* Titulo "Funciones de plantillas URLEnconde"
* Texto "--"
* Parametro Variable "parametro"
* Valor Parametro "valor"
* Transaccionar EnvioEmail "testing@latinia.com" "EtiquetaEmail" "refPlantilla4" " "
* InicioHome
* Menu "Análisis"
* Herramienta "Detalle de contenidos de transacciones de email"
* Menu herramienta
* Ver Plantilla

//===== test Emojis  =========
* InicioHome
* Menu "Diagnosis"
* Herramienta "Envíos email"
* InicioHome
* Menu herramienta
* Titulo "Funciones de plantillas EMOJIS"
* Texto "--"
* Parametro Variable "parametro"
* Valor Parametro "😀 😃 🤪 😇"
* Transaccionar EnvioEmail "testing@latinia.com" "EtiquetaEmail" "refPlantillaEmojis" " "
* InicioHome
* Menu "Análisis"
* Herramienta "Detalle de contenidos de transacciones de email"
* Menu herramienta
* Ver Plantilla

* Menu "Diagnosis"
* Herramienta "Envíos email"
* InicioHome
* Menu herramienta
* Titulo "Funciones de plantillas EMOJIS"
* Texto "--"
* Parametro Variable "emoji_automated1"
* Valor Parametro "😭 😡 🙈"
* Transaccionar EnvioEmail "testing@latinia.com" "EtiquetaEmail" "refPlantillaEmojis" " "
* InicioHome
* Menu "Análisis"
* Herramienta "Detalle de contenidos de transacciones de email"
* Menu herramienta
* Ver Plantilla

LogOut:
* LogOut