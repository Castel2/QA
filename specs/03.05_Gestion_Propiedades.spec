Preconditions:
h1.Verifica herramienta gráfica Gestión de 'Customer Property Management' - Iniciando_spec: 03.05_Gestion_Propiedades
===========================================================================
Tags:03.05_ Gestion_Propiedades,Provision
Dependencias.-
1.- Requiere que se encuentre licenciado algun valor en la propiedad de licencia 'license/entry/maxInfObjType'

LogIN, Navegacion, Almacen de Datos:
* Establecer host "" port "" dir ""
* Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"
* Cumplimentar Formulario y Login
* Establecer cliente
* Hash Map
* Lista Propiedades desde licencia
Lectura y verificación de licencia
* Licencia max objetos propobject "license/entry/maxInfObjTypes"
Escenario primero
-----------------
* Menu "Provisión"
* Herramienta "Gestión de propiedades de clientes"
* Menu herramienta

Gestion Propiedades, Almacen de Datos:
* Empresa "CAPITAL"
* UserProps
* Origenes
* OperProps "provision"
/* Indices
Gestion Propiedades, Almacen de Datos:
* Empresa "LATINIA"
* UserProps
* OperProps "provision"
* Origenes
/* Indices

Gestion Propiedades -> objetos, Almacen de Datos:
* Empresa "CAPITAL"
* Objetos
* Guardar objeto "automated"
* Guardar propiedad "propiedad 1"
Creación de objeto y propiedad
* Crear Objeto
* Agregar propiedades
Salida y reingreso a Gestión de propiedades -> objetos
* InicioHome
* Menu "Provisión"
* Herramienta "Gestión de propiedades de clientes"
* Menu herramienta
* Empresa "CAPITAL"
* Objetos
Verificación de creación de objeto y su propiedad
* Verificar ObjetoPropiedad
Eliminación de propiedad y del objeto
* Eliminar PropiedadObjeto
Salida y reingreso a Gestión de propiedades -> objetos
* InicioHome
* Menu "Provisión"
* Herramienta "Gestión de propiedades de clientes"
* Menu herramienta
* Empresa "CAPITAL"
* Objetos
Verificar eliminación de objeto
* Eliminacion correcta
LogOut:
* LogOut