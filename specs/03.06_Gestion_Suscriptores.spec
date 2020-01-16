Verifica herramienta gráfica Gestión de 'Customer Management Center' - Iniciando_spec: 03.06_Gestion_Suscriptores
===================================================================

Tags:03.06_Gestion_Suscriptores,bug, Provision
Preconditions:

En el metodo MApps se debe ejecutar dos veces.

Dependencias.-
1.- Requiere que exista la Organización objeto de las pruebas. PEj CAPITAL. Puede ejecutarse para ello 'Provision-G_Empresas.scn'.
2.- Requiere  'ADMIN_MODE=TRUE' para la aplicación CMC
3.- Requiere ejecución de CPM 'Gestion_Propiedades.scn'
4.- Requiere ejecución de 'Gestion de M-App-Provision.scn' que aprovisiona M-App fuera de INNOVUS
    En el paso en que se asocian MApps: En el caso de no existir las MApps, este paso fallará.
    Puede ejecutarse primeramente el escenario 'Gestion de M-App-Provision.scn' el cual aprovicionará las MApps que utilizaremos después en el presente escenario.
5.- Requiere la existencia de los Servicios de Suscripción que se utilicen. Para ello puede ejecutarse el escenario 'Catalogo Operaciones.scn'. También sucede que si estamos probando una instalación que NO tiene AE, en tal caso no habrá que probar los servicios de suscripción.

La prueba del 'propósito=inf-103' comprueba que no se vinculen nuevos usuarios a las M-Apps desactivadas. Para ello se debe pasar como parametro una MApp que esté desactivada

LogIN, Navegacion, Almacen de Datos:
* Establecer host "" port "" dir ""
* Establecer login "" user "" pass ""
* Cumplimentar Formulario y Login
* Establecer cliente
* Hash Map
* Lista Propiedades desde licencia
Escenario primero
-----------------
* Menu "Provisión"
* Herramienta "Customer Management Center"
* Menu herramienta
* Proposito "provision"
* Empresa "LATINIA"
* Empresa "CAPITAL"
* CrearSuscriptor "usercapital" "conProp"
* Buscar "usercapital"
* check360
* checkDatos


LogOut:
* LogOut