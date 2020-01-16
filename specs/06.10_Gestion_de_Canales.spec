Verifica herramienta gráfica Gestión de Canales - Iniciando_spec: 06.10_Gestion_de_Canales
===============================================
Tags:Provision,06.10_Gestion_de_Canales,Smoke,Regresion,CI
Preconditions:

Nota.- En el paso 'Modelos', se requiere que existan las operadoras que allí se utilizan.

h1.Ejecución Manual
>> SDP-REQGEN-339 Proceso de FailOver manual de SMS, con contabilización adecuada


Estos son los steps denominados 'de contexto' (ver: http://getgauge.io/documentation/user/current/gauge_terminologies/contexts.html)
Estos steps se ejecutan antes de cada escenario de este spec.
* Establecer host "" port "" dir ""
* Establecer login "" user "" pass ""
* Cumplimentar Formulario y Login
* Establecer cliente
* Hash Map
* Lista Propiedades desde licencia
LogIN, Navegacion, Almacen de Datos:
Escenario primero
-----------------
* Menu "Provisión"
* Herramienta "Gestión de canales"
* Menu herramienta
* Proposito "--"

Gestion de Canales, Navegacion:
* CanalesEmail "comercial@latinia.com"
* Modelos "Virtual"
* Rutas "Virtual" "+000001" "SMS" "Colector Virtual"
* Menu herramienta
* Colectores "PruebaEntornoGrafico"
* FailOver

LogOut:
* LogOut