Comprueba que funciona el sistema de actualización de Tokens de las M-Apps. - Iniciando_spec: 03.07_desRegistroToken
==========================================================================

Tags:Analisis,03.07_desRegistroToken,[INF-123],Mapp
Preconditions:
Metadata:ScenarioType=Auto


Dependencias.- 
1.- Requiere que se haya instalado en la VM de pruebas el componente '_limspinf-ltest_', que se encarga de desregistrar un _token_ obsoleto, y registrar en su lugar un _token_ actualizado.
2.- Requiere que exista la Organización, la M-App y el cliente sobre los que se realizará la prueba.

LogIN, Navegacion, Almacen de Datos:
* Establecer host "" port "" dir ""
* Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"
* Cumplimentar Formulario y Login
* Establecer cliente
* Hash Map
* Lista Propiedades desde licencia
Escenario primero
-----------------
* Menu "Provisión"
* Herramienta "Customer Management Center"
* Menu herramienta

Gestion Suscriptores, Almacen de Datos:
* Proposito "--"
* Empresa "CAPITAL"
* checkToken "usercapital" "mymappautomated0"

LogOut:
* LogOut



Invocación del 'servlet' que cambia el token actual por el nuevo
Gestion Suscriptores:
* cambiaToken "mymappautomated0"


Se comprueba que el token se haya cambiado correctamente
LogIN, Navegacion, Almacen de Datos:
* Cumplimentar Formulario y Login
* Menu herramienta

Gestion Suscriptores, Almacen de Datos:
* checkToken "usercapital" "mymappautomated0"

LogOut:
* LogOut