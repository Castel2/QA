Comprueba que una M-App "desactivada", NO aparece en la lista de M-App para ser asociada a un suscriptor y token - Iniciando_spec: 03.09_MAppDesactivada
===============================================================================================================

Tags:Provision,bug,[INF-103],03.09_MAppDesactivada
Preconditions:

ATENCION al paso en que se asocia una MApp al suscriptor. Se requiere que exista dicho suscriptor de lo contrario petará. En caso necesario puede ejecutarse primeramente el escenario "Gestion suscriptores" el cual creará el suscriptor que usaremos después en el presente escenario

LogIN, Navegacion, Almacen de Datos:
* Establecer host "" port "" dir ""
* Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"
* Cumplimentar Formulario y Login
* Establecer cliente
* Hash Map
* Lista Propiedades desde licencia
Escenario primero
-----------------
* InicioHome
* Menu "Provisión"
* Herramienta "Gestión de M-Apps"
* Menu herramienta

* Proposito "inf-103"
* PNS_MApp_Manager "testinf103" "LATINIA"

* InicioHome
* Menu "Provisión"
* Herramienta "Customer Management Center"
* Menu herramienta

* Empresa "CAPITAL"
* MApps "usercapital" "testinf103"

LogOut:
* LogOut