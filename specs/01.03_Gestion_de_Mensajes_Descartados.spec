Herramienta gráfica de Mensajes descartados - Iniciando_spec: 01.03_Gestion_de_Mensajes_Descartados
===========================================
Tags:Analisis,sms,01.03_Gestion_de_Mensajes_Descartados

Dependencias.-
1.- Se requiere que exista algún descartado, de lo contrario no estamos probando nada.
Explicación.-
>> En la llamada a 'NavegarDescartados' se permite indicar el _refProduct_ y el _código de error_ que queremos analizar.
Por ejemplo _*refProduct=wapppush*_
Si no queremos analizar nada en concreto, podemos dejar que el escenario agarre el primer registro que encentre, para ello los parámetros serán "--"

Recomendación.-
>> Puede ejecutarse el escenario '01.07-_MT-SMS.scn', ello generará descartados para 'wapppush'.

Estos son los steps denominados 'de contexto' (ver: http://getgauge.io/documentation/user/current/gauge_terminologies/contexts.html)
Estos steps se ejecutan antes de cada escenario de este spec.
* Establecer host "" port "" dir ""
* Establecer login "" user "" pass ""
* Cumplimentar Formulario y Login
* Establecer cliente
* Hash Map
* Lista Propiedades desde licencia
Gestion descartados, Esperar, Almacen de Datos, Navegacion:
Escenario primero
-----------------
Given existe el menu control
* Hash Map
* Menu "Control"
* Herramienta "Mensajes descartados o erróneos"
* Menu herramienta
When se navega en descartados
* NavegarDescartados "wtest-pns" "--"
* Esperar "3"

LogOut:
* LogOut