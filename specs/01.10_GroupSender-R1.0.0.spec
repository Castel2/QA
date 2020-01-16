Herramientas gráficas de "Envíos masivos a grupos" y "Batch Monitor"  - Iniciando_spec: 01.10_GroupSender-R1.0.0
====================================================================
Tags:GS,massive

Preconditions:
Metadata:ScenarioType=Auto
Se comprueban las dos herramientas.
Los recursos para este escenario se ubican en "Q:\Twist\Util\MaterialGroupSender"

DEPENDENCIAS.- 
	1.- Los .CSV a utilizar deben cargarse 'manualmente' a la herramienta (pej: agenda1_sms.csv), ya que no se ha podido implementar de manera automática.

	2.- Plantillas volátiles - Ubicacion: "Q:\Twist\Util\MaterialGroupSender\Plantillas_Volatiles-Testing1.zip"

	3.- Las plantillas predefinidas se aprovisionan durante la ejecución de los escenarios "_06.xx-Gestion de 	Plantillas-xx_"
     Chequear que el idioma ESPAÑOL está aprovisionado, en G.Plantillas, como "Idioma por defecto".

	4.- En el paso del escenario 'ProgramarEnvio' se le pasa como parámetro el nombre de la plantilla que vamos a utilizar.



LogIN, Almacen de Datos:
* Establecer host "" port "" dir ""
* Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"
* Cumplimentar Formulario y Login
* Establecer cliente
* Hash Map
* Lista Propiedades desde licencia
Escenatio primero
-----------------
Navegacion, Almacen de Datos:
* Menu "Diagnosis"
* Herramienta "Envíos masivos a grupos"
* Menu herramienta

///Esta parte no funciona, est'a en desarrollo///*
GroupSender, Almacen de Datos:
/* Archivo "agenda1_sms.csv"
/* SubirArchivo

h1.Se realizan 10K envios, y SIN plantillas. Prioridad 'BAJA'
GroupSender, Almacen de Datos:
* Archivo "agenda10k_sms.csv"
* ProgramarEnvio "TextoAgenda 10k destinatarios" "--" "planificar" "2"
* EnviosProgramadosR100 "--"

h1.Se realiza un envio con plantilla predefinida
GroupSender, Almacen de Datos:
* Archivo "agenda1_sms.csv"
* ProgramarEnvio "refPlantilla0 - Mi plantilla" "" "" ""
* EnviosProgramadosR100 "esperar que acabe"

h1.Se realizan 3 envíos con plantilla volátil
GroupSender, Almacen de Datos:
* Archivo "agenda3_5campos_user.csv"
* ProgramarEnvio "Plantillas_Volatiles-Testing1.zip" "SMS, PNS, Tweet, EMail" "planificar" "5"
* EnviosProgramadosR100 "esperar que acabe"

h1.Se realizan 3 envíos de prioridad 'ALTA'
GroupSender, Almacen de Datos:
* Archivo "agenda3_5campos_user.csv"
* ProgramarEnvio "TextoAgenda de 3destinatarios" "SMS, PNS" "planificar" "8"
* EnviosProgramadosR100 "esperar que acabe"
h1.
LogOut:
* LogOut