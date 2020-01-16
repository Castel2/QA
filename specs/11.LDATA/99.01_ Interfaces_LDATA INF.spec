Comprueba el funcionamiento de diversas funcionalidades de Limsp_INF atacando directamente al LDATA.
===================================================================================================

Tags:ldata,99.01_ Interfaces_LDATA INF
Preconditions:


Dependencias.-
1.- Se requiere que este instalado, en la VM en que se realicen las pruebas, el componente denominado "limspinf-ltest.ear". El componente está publicado en "[host]/limspsdp-ltest" (no requiere autentificación).
2.- Se requiere que existan transacciones PNS en el histórico. En el caso por defecto, deben existir transacciones para la Empresa=CAPITAL, Usuario=usercapital, Mapp=MyMappTwisted0


SECUENCIA 1*.- Acciones realizadas
1- Listar contenidos de una MApp
2- Listar contenidos de una MApp limitando a 2 registros
3- Listar utilizando el idMsgExt del segundo mensaje de los que se listaron anteriormente
4- Listar utilizando los idMsgExt de los dos(2) mensajes que se listaron anteriormente
limspinfltest, Almacen de Datos, LogIN:
* Establecer host "" port "" dir ""
* Cumplimentar Formulario y Login

Escenario primero
-----------------
* Empresa "CAPITAL"
* Usuario "usercapital"
* Mapp "MyMappTwist0"
* resetear
* secuencia1



SECUENCIA 2*.- Acciones realizadas
1- Observar que los contenidos entre 2 fechas NO estan leídos (READ=FALSE)
Las fechas se determinan entre el momento 'presente' y los días atras pasados como parámetro. En el ejemplo 20 días
2- Marcar como leídos los contenidos entre 2 fechas
3- Observar que se han marcado como leídos (READ=TRUE)
4- Tomar el idMsgExt del segundo registro, y marcarlo como HIDDEN=TRUE
5- Observar, utilizando el idMsgExt que dicho registro se ha marcado como oculto (HIDDEN=TRUE)
6- Observar, utilizando idMsgExt que dicho registro se ha marcado como oculto (HIDDEN=TRUE)
limspinfltest, Almacen de Datos, LogIN:
* resetear
* secuencia2 "20"