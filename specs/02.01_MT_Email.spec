Genera desde la herramienta WappPush-Email un Email con fichero adjunto  - Iniciando_spec: 02.01_MT_Email
=======================================================================

Tags:Transaccionamiento,email,02.01_MT_Email,Smoke,Regresion,CI
Preconditions: Debe existir el archivo a adjuntar en la ubicación “C:/Testing/Gauge_projects/Util/Archivos/HomeLimsp.png”
Metadata:ScenarioType=Auto

Nota: Este escenario supone que toda la aprovisión ya está hecha.

Estos son los steps denominados 'de contexto' (ver: http://getgauge.io/documentation/user/current/gauge_terminologies/contexts.html)
Estos steps se ejecutan antes de cada escenario de este spec.
* Establecer host "" port "" dir ""
* Establecer login "" user "" pass ""
* Cumplimentar Formulario y Login
* Establecer cliente
* Hash Map
* Lista Propiedades desde licencia
Escenario primero
-----------------
* Menu "Provisión"
* Herramienta "Gestión de contratos"
* Proposito "cambiar"
* Nombre contrato "Envíos email"
* Empresa "INNOVUS"
* Menu herramienta
* Cambiar Propiedad Contrato "CONTENT_TEMPLATE"
* InicioHome
* forzarrecarga
* Menu "Diagnosis"
* Herramienta "Envíos email"
* Menu herramienta
* idioma "--"
* Titulo "Envio Email Automated"
* Texto "ñ Ñ < > $ € ! ' á é í ó ú à è ì ò ù"
* Parametro Variable "parametro"
* Valor Parametro "valor"
* Transaccionar EnvioEmail "userlatinia@latinia.com" "EtiquetaEmail" "" "HomeLimsp.png"
* InicioHome
* Menu "Análisis"
* Herramienta "Detalle de contenidos de transacciones de email"
* Menu herramienta
* Ver Adjunto
* InicioHome
* Menu "Provisión"
* Herramienta "Gestión de contratos"
* Proposito "devolver"
* Menu herramienta
* Cambiar Propiedad Contrato "CONTENT_TEMPLATE"
* forzarrecarga

R395-4.1.0
* Busqueda Emails por clave de usuario "email" "userlatinia@latinia.com" "LATINIA"

LogOut:
* LogOut

