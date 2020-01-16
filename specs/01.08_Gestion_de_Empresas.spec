Aprovisiona empresas en la herramienta gráfica de Gestión de Empresas  - Iniciando_spec: 01.08_Gestion_de_Empresas
=====================================================================
Tags:Provision,01.08_Gestion_de_Empresas-Provision
Preconditions:

Explicación.-
1.- Organizaciones: Se tiene en cuenta la existencia de la propiedad aplicacion/contrato '_MNGORGANIZATIONS_' para la realización de validaciones a nivel de 'organización'. En caso de no existir dicha propiedad, el escenario no valida el apartado relativo a 'organizaciones'.
2.- Creación incremental: El parámetro "_Proposito=Provision_" indica que si la empresa ya existiera, se creará otra igual añadiendo sufijo numérico incremental. Ej: Capital >> Capital0 >> Capital1 >> ...

Dependencias.-
Comprobar el número máximo de empresas permitidas por licencia.

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
Give existe la propiedad MNG_ORGANIZATIONS
* Menu "Provisión"
* Herramienta "Gestión de aplicaciones"
* Menu herramienta
* checkMNG_ORGANIZATIONS
* EmpresasOrganizaciones
* InicioHome
* Herramienta "Gestión de empresas"
* Menu herramienta

When creo la empresa
* Proposito "provision"
* GEmpresas "CAPITAL" "sin cambio"
* GEmpresas "ALFA" "sin cambio"
* GEmpresas "ALFA" "INNOVUS"

LogOut:
* LogOut