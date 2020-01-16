Consulta en 'Detalle de transacciones', una transacción dada en este mismo escenario - Iniciando_spec: 06.13_Estadisticas
====================================================================================
Tags:Analisis,06.13_Estadisticas
Preconditions:
Metadata:ScenarioType=Auto

Explicación.-
>> En la línea '_*BusquedaCriterio*_' se ha de configurar qué es lo que estamos buscando en el filtro de estadísticas.

Dependencias.-
>> Se requiere que exista tráfico para el criterio que estamos buscando

h1.Ejecución Manual
>> Comprobar que se muestra la referencia de la plantilla en "Detalle de transacciones"
>> Los labels deben aparecer en estadísticas solo si tienen tráfico 
* Establecer host "" port "" dir ""

LTimers:
* ldmcontable "histAndSummary"

Accedo a la Limsp a ver Estadísticas
Estos son los steps denominados 'de contexto' (ver: http://getgauge.io/documentation/user/current/gauge_terminologies/contexts.html)
Estos steps se ejecutan antes de cada escenario de este spec.
* Establecer login "" user "" pass ""
* Cumplimentar Formulario y Login
* Establecer cliente
* Hash Map
* Lista Propiedades desde licencia

Escenario primero
-----------------
* InicioHome
* Menu "Análisis"
* Herramienta "Detalle de contenidos de transacciones"
 Menu herramienta "hpqc:nombre_del_paso"
* Menu herramienta

Detalle de transacciones
* Ver listado "wapppush"
* Esperar "3"
* BusquedaCriterio "telefono" "virtual" "" ""
* Buscar por "virtual"
* Ver estados "Finalizado en el proveedor"
/* Ver estados "Expirado por Plataforma"
/* BusquedaCriterio "* refUser" "liliuser"
* Esperar "3"
* Exportacion "Exportacion Automática con Automated"
* Esperar "3"

Estadisticas
* InicioHome
* Menu "Análisis"
* Herramienta "Estadísticas de tráfico"
* Menu herramienta
* Estadisticas "miCSVEstadisticas creado con Automated"

LogOut:
* LogOut


