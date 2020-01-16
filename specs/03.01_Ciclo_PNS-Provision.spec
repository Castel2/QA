Ciclo completo de provisión, generación y verificación de 1 envio PNS por la empresa CAPITAL  - Iniciando_spec: 03.01_Ciclo_PNS-Provision
============================================================================================
Tags:pns,Transaccionamiento,03.01_Ciclo_PNS-Provision
Preconditions:
Ejecutar al menos una vez, 09.01_PlataformaLimpia-Provision
Debe existir la propiedad ORGANIZATION en las propiedades de la aplicación
Se aprovisiona lo necesario para mover PNS por CAPITAL

Dependencias.- Se requiere que exista la propiedad 'MNGORGANIZATIONS=TRUE' en la aplicación de Gestión de Empresas.
- Se requiere que dentro de las propiedades de la aplicación, se encuentre ORGANIZATION

Nota.- Para obtener el estado 1/4 del colector virtual se requiere configurar
_config/bus/collectors/vcollector/mode =_ BUS

LogIN, Almacen de Datos:
* Establecer host "" port "" dir ""
* Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"
* Cumplimentar Formulario y Login
* Establecer cliente
* Hash Map
* Lista Propiedades desde licencia
Escenario primero
-----------------
* Menu "Provisión"
* Herramienta "Gestión de aplicaciones"
* Menu herramienta
* setLGUI "wsubscribers" "ADMIN_MODE " "TRUE"
* checkMNG_ORGANIZATIONS
* EmpresasOrganizaciones
* Empresa "CAPITAL"

* InicioHome
* Menu "Provisión"
* Herramienta "Gestión de aplicaciones"
* Menu herramienta
* propiedades "wtest-pns" "ORGANIZATION" "visible"

* InicioHome
* Menu "Provisión"
* Herramienta "Gestión de empresas"
* Menu herramienta
* Proposito "--"

Creo Canales, Rutas, etc..
* InicioHome
* Herramienta "Gestión de canales"
* Menu herramienta
* Rutas "Google" "*" "PNS" "Virtual"
* Menu herramienta
* Rutas "Apple" "*" "PNS" "Virtual"
* Proposito "traffictesting"
* Menu herramienta

* LogOut

h1.Aprovisiono M-Apps en empresa distinta a INNOVUS
* Establecer login "CAPITAL" user "USER1" pass "USER1"
* Cumplimentar Formulario y Login
* InicioHome
* Menu "--"
* Herramienta "Gestión de M-Apps Automated"
* Menu herramienta
* Proposito "provision"
* PNS_MApp_Manager "myMAppAutomated" ""

LogOut:
* LogOut

Regreso a INNOVUS para crear suscriptores con las M-Apps que recién acabo de crear en el paso anterior.
* Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"
* Cumplimentar Formulario y Login
* InicioHome
* Proposito "provision"
* Empresa "CAPITAL"
* Menu "Provisión"
* Herramienta "Gestión de propiedades de clientes"
* Menu herramienta
* UserProps
* InicioHome
* Herramienta "Customer Management Center"
* Menu herramienta
* CrearSuscriptor "usercapital" "conProp"
* MApps "usercapital" "myMAppAutomated0"
* InicioHome
* Herramienta "Gestión de contratos"
* Menu herramienta
* ConProps "Envíos de PushNots Automated" "CUSTOMER_PREFERENCES" "FALSE"
* Menu herramienta
* ConProps "Envíos de PushNots Automated" "PNS_APP" "*"
* Menu herramienta
* ConProps "Envíos de PushNots Automated" "ORGANIZATION" "CAPITAL"
* forzarrecarga
* LogOut

h1.Entro en CAPITAL y envio una PNS Unitaria al usuario que hemos aprovisionado antes. !!Esta PNS no ha de llegar porque 'CUSTOMER_PREFERENCES=FALSE'
* Establecer login "CAPITAL" user "USER1" pass "USER1"
* Cumplimentar Formulario y Login

Inserta un PNS unitario que se descartará, y lo busca en Estadisticas para comprobar que realmente no aparece
* Set XML PNSunitario "push-pns-unitario.xml" "CAPITAL" "MT_PNS" "mymappAutomated0" "refuser" "usercapital" "Unitario que no ha de llegar "
* Insercion WS "pns" "wtest-pns" "normalAuth"
* InicioHome
* Menu "--"
* Herramienta "Detalle de transacciones Automated"
* Menu herramienta

Detalle de transacciones, Almacen de Datos:
* BusquedaCriterio "#refUser" "usercapital" "noaparecer" "CAPITAL"

* LogOut

h1.Regreso a INNOVUS para establecer 'CUSTOMER_PREFERENCES=TRUE'
* Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"
* Cumplimentar Formulario y Login
* InicioHome
* Menu "Provisión"
* Herramienta "Gestión de contratos"
* Menu herramienta
* ConProps "Envíos de PushNots Automated" "CUSTOMER_PREFERENCES" "TRUE"
* forzarrecarga

* LogOut

Entro en CAPITAL y envio una PNS Unitaria al usuario que hemos aprovisionado antes. Esta PNS sí ha de llegar
* Establecer login "CAPITAL" user "USER1" pass "USER1"
* Cumplimentar Formulario y Login

//Inserta un PNS unitario y lo busca en Estadisticas
* Set XML PNSunitario "push-pns-unitario.xml" "CAPITAL" "MT_PNS" "mymappAutomated0" "refuser" "usercapital" "Unitario-Tiene un saldo de 88eur "
* Insercion WS "pns" "wtest-pns" "normalAuth"
* InicioHome
* Menu "--"
* Herramienta "Detalle de transacciones Automated"
* Menu herramienta
* BusquedaCriterio "#refUser" "usercapital" "" "CAPITAL"
* Ver estados "Entrega confirmada a usuario"
* LogOut
