Comprueba el estado "(2,2) Finalizado en Limsp" de un SMS virtual de tipo MO  - Iniciando_spec: 01.09_Estados MO
============================================================================

Tags:Transaccionamiento,bug,01.09_Estados MO,MO
Preconditions:
Metadata:ScenarioType=Auto

Se genera un MO con el simulador de móvil
Va a Estadísticas y verifica que el MO generado tiene en estado (2,2) Finalizado en Limsp

* Establecer host "" port "" dir ""
* Establecer login "" user "" pass ""
* Cumplimentar Formulario y Login
* Establecer cliente
* Hash Map
* Lista Propiedades desde licencia
Envío de MO por WS
------------------
Given existe el simulador móvil
* Proposito "test"
* InicioHome
* Menu "Diagnosis"
* Herramienta "Simulador de móvil"
* Menu herramienta

When Se envía el mensaje pull-push desde el simulador móvil
* TransaccionesPullPush
|Aplicacion|Texto|Respuesta|
|ws1|probando estado MO|Servicio WS. Servicio ofrecido por WS normal (probando estado MO)|

Then retorna la respuesta esperada
And se verifica en estádisticas el envío del mensaje y el estado (2,2) Finalizando en Limsp
* InicioHome
* Menu "Análisis" 
* Herramienta "Detalle de contenidos de transacciones"
* Menu herramienta
* Nombre contrato "Servicio WS"
* Ver listado "prueba WebService"
* Ver estados "Finalizado en Limsp"

* LogOut