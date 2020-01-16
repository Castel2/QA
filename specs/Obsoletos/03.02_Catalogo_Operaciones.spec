Comprueba la herramienta GUI de "Catálogo de Operaciones"  - Iniciando_spec: 03.02_Catalogo_Operaciones
========================================================
Tags:03.02_Catalogo_Operaciones,Provision
Preconditions:

@param grupo en el que se creará la operacion indicada
@param nombre de la operación que se va a crear
@param descripción de la operación que se va a crear
@param trama que se va a utilizar. Válido solo para instalaciones con AlertEngine. En caso contrario dejar vacío.
@param "provision" indica si lo que aprovisiona lo elimina al finalizar, dejando la provisión tal como estaba al comienzo; o si por el contratio lo deja ahí para futuros usos.

Dependencias - 
1.- Requiere, en el caso de estar probandolo con AlertEngine, que existan las tramas. Para ello se ejecuta 'Frame_Editor.scn' en modo 'PROVISION'.
2.- Requiere que exista la empresa objeto de las pruebas. Por ejemplo 'CAPITAL'. Para ello se ejecuta 'Provision-G_Empresas.scn'.

* Establecer host "" port "" dir ""
* Establecer login "" user "" pass ""
* Cumplimentar Formulario y Login
* Establecer cliente
* Hash Map
* Lista Propiedades desde licencia
Escenario primero
-----------------

* Menu "Provisión"
* Herramienta "Catálogo de operaciones bancarias"
* Menu herramienta
* Empresa "CAPITAL"

* Crear Operacion "Movimientos en cuenta" "PAYROLL" "Ingreso de Nomina" "Trama de uso de cuentas" "provision"
* Crear Operacion "Movimientos en tarjeta" "MOV_MOV" "Cargos en tarjetas de credito" "Trama de uso de tarjetas" "provision"

LogOut:
* LogOut