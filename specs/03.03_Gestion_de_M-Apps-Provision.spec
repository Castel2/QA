Aprovisiona M-Apps en INNOVUS y en otra empresa, por ejemplo CAPITAL  - Iniciando_spec: 03.03_Gestion_de_M-Apps-Provision
=====================================================================

Tags:Provision,03.03_Gestion_de_M-App-Provision,Mapp
Preconditions:


Dependencias.-
Requiere que exista la Organización objeto de las pruebas. Por ejemplo 'CAPITAL'. Para ello puede ejecutarse el escenario 'Gestion de Empresas-Provision.scn'.
Con 'LATINIA' no habrá problema porque existe por defecto en INNOVUS.

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

* Proposito "provision"
* PNS_MApp_Manager "myMAppAutomated" "LATINIA"

LogOut:
* LogOut

h1.Aprovisionar M-Apps en empresa distinta a innovus pej: 'CAPITAL'

Creacion del contrato y del usuario:
* Establecer host "" port "" dir ""
* Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"
* Cumplimentar Formulario y Login
* Menu "Provisión"
* Herramienta "Gestión de contratos"
* Empresa "CAPITAL"
* Usuario "USER1"
* Nombre contrato "Gestión de M-Apps"
* tipoclause "sms"
* refcontract ""

* InicioHome
* Menu herramienta
* SinClause ""

* Herramienta "Gestión de usuarios"
* Menu herramienta
* Crear user

* Herramienta "Gestión de contratos"
* Menu herramienta

AsignarUsuario:
* Asignar user

LogOut:
* LogOut

Entro en la empresa distinta a INNOVUS para crear las M-Apps:
* Establecer host "" port "" dir ""
* Establecer login "CAPITAL" user "USER1" pass "USER1"
* Cumplimentar Formulario y Login
* InicioHome
* Menu "--"
* Herramienta "Gestión de M-Apps Automated"
* Menu herramienta

* Proposito "inf-103"
* PNS_MApp_Manager "mymappdesactivada" ""
* Proposito "provision"
* PNS_MApp_Manager "myMAppAutomated" ""

LogOut:
* LogOut