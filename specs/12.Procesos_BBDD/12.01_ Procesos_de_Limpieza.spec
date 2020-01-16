Ejecuta mediante invocacion de las URL del LTimers, los procesos contables y de limpieza - Iniciando_spec: 12.01_ Procesos_de_Limpieza
=========================================================================================

Tags:Analisis,99.03_ Procesos_de_Limpieza
Preconditions:
Metadata:ScenarioType=Auto

Ejecuta procesos de limpieza mediante invocacion de las URL del LTimers
Comprueba que en lprocess.log aparecen las entradas correctas. Para ello va al 'Panel de Control' a ver el log 'Process'.
Se comprueba en 'lprocess act' que para el dia de hoy, no existan entradas del tipo ERR, ENT o TMO

Escenario primero LD-MCONTABLE
------------------------------
* Establecer host "" port "" dir ""
* Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"
* Cumplimentar Formulario y Login

* ldmcontable "histAndSummary"
* Esperar "4"
* ldmcontable "consolidatestates"
* Esperar "4"
* ldmcontable "consolidatedb"
* Esperar "4"
* ldmcontable "consolidateall"
* Esperar "4"


Escenario segundo LP-MAINTENANCE
--------------------------------
* lpmaintenance "clearBinary"
* Esperar "4"
* lpmaintenance "clearHistory"
* Esperar "4"
* lpmaintenance "clearCahe"
* Esperar "4"
* lpmaintenance "clearInf"
* Esperar "4"
* lpmaintenance "clearPrivateContent"
* Esperar "4"
* lpmaintenance "clearGuiUsers"


Inicio,Navegacion,Almacen de Datos, Panel de Control:
/* InicioHome
/* Menu "Control"
/* Herramienta "Panel de control"
/* Menu herramienta
/* Log check lprocess_act "ld-mcontablehistAndSummary"


Inicio,Navegacion,Almacen de Datos, Panel de Control:
/* InicioHome
/* Menu herramienta
/* Log check lprocess_act "lserv-delivery"


h1.Ejecución Manual
>>     Debe indentifcarse gráficamente cuando un masivo ha expirado por tiempo de finalización, aunque tenga mensajes pendientes. El estado del masivo debe ser el habitual de finalizado correctamente, aunque falta mostrar intervalo de tiempo del envío.

>> Los mensajes individuales del masivo almacenados en las tablas de detalle de mensajes (estadísticas) deben
	(A) Finalizarse con un estado final cuando el masivo ha expirado. Propuesta de estado: "Mensaje expirado (por plataforma)".
	(B) No finalizarse nunca (o solo pasado muuucho tiempo) cuando el envío masivo no tiene fecha de finalización/caducidad. Se considera que si el masivo no tiene fecha final es que sus mensajes acabaran saliendo en algún momento. HECHO!, se finalizarán pasado un mes, o según la configuración general establecida para los masivos. Ver en comentarios las configuraciones.

>>	Deben eliminarse todos los mensajes desdoblados (en tablas de masivos) o retenidos (en tablas de retención) pertenecientes a cualquier envío masivo que ya haya finalizado de forma correcta o incorrecta. Se admite un offset de tiempo para este purgado.

>>	Identificar implicaciones contables de consolidar mensajes de envíos masivos que fueron procesados hace una semana o dos, pero que hasta después de una semana o dos no son realmente enviados o cancelados, y por tanto están dos semanas sin estado final.
