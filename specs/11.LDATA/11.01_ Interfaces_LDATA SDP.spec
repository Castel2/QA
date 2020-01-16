Se requiere que existan transacciones en el histórico - Iniciando_spec: 11.01_ Interfaces_LDATA SDP
=====================================================

Tags:99.02_ Interfaces_LDATA SDP
Preconditions:
h1.Comprueba el funcionamiento de diversas funcionalidades LimspSDP invocando directamente al LDATA.

Dependencias.-*
1.- Se requiere que existan transacciones en el histórico.
2.- Se requiere que las aplicaciones gráficas 'lman-vcontent' y 'lman-vcontentmail' tengan el role 'LD PSTORAGE'. Atención porque esto ya lo aprovisiona automáticamente al comienzo del presente escenario.*

Explicacion.-*		
Prueba los siguientes metodos del PStorage.- "listContracts", "listContent", "listContentEMail", "MessageStatusDetail"

Los parametros que se le pasan a la secuencia son, por este orden.-
>> RefProduct para el acceso al ldata
>> Password del product para el acceso al ldata
>> Enterprise para el acceso al ldata
>> Login user para el acceso al ldata
>> RefContract para el acceso al ldata
>> Dias atrás que irá a buscar contratos y contenidos de transacciones

En la consola puede verse la ejecución de las tareas

Comienza la provision del ROLE
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
* InicioHome
* Menu herramienta
* Roles "lman-vcontentmail" "LD_PSTORAGE" "agregar"
* Roles "lman-vcontent" "LD_PSTORAGE" "agregar"

LogOut:
* LogOut

Aqui comienzan las pruebas
Se debe especificar el contenedor (WAS o WL)
* Interfaz LD_SDP "lman-vcontent" "latinia" "lman-vcontent" "CAPITAL" "USER1" "30"

