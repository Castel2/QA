Verifica herramienta gráfica Gestión de Usuarios - Iniciando_spec: 06.15_Gestion_de_Usuarios
================================================
Tags:06.15_Gestion_de_Usuarios, Provision
Preconditions:

Comprueba la funcionalidad 'Borrado de usuarios GUI sin actividad'; descrita en el Requisito 'SDP-REQGEN-162' y realizada en la tarea 'SDP-742'

01-AGO-2015 ATENCION, se ha comentado la prueba de 'SinActividadUser' porque existe un problema técnico que impide la realización de la prueba.*

Para esta comprobación se consulta el valor de la propiedad de LConfig 'config/lprocess/lp-maintenance/configs/clearGuiUsers/olderDays ='
y después se invoca 'http://host:port/lp-maintenance/clearGuiUsers' para la eliminación de los usuarios sin actividad.

Dependencias.-
1.- La prueba de 'clearGuiUsers' se activa estableciendo el usuario como 'SinActividadUser' y la empresa como algo distinto a INNOVUS.*
Para verificar el funcionamiento de 'clearGuiUsers' se requiere la existencia, previa a la ejecución de este escenario, de un usuario con al menos 60 dias de inactividad. Esto se ha solucionado en VMRR5 con un Trigger de BBDD que establece el periodo de inactividad del usuario 'SinActividadUser' automáticamente a 60 días, nada más crearlo. Este mecanismo debe ser replicado en toda aquella VM en que quiera probarse '_SinActividadUser_'. De lo contrario, basta con no ejecutar esta parte de la prueba, y hacerlo con otro usuario cualquiera; aunque en este caso no se está probando el requisito 'SDP-REQGEN-162'.
El mismo escenario se encarga de crear un usuario (_SinActividadUser_) en caso de no existir dicho usuario.
La funcionalidad que borra los usuarios sin actividad se configura en '_lp-maintenance.properties_' 
en la entrada "_config/lprocess/lp-maintenance/configs/clearGuiUsers/olderDays_ = 60"

2.- Se requiere la existencia de las empresas en las que van a crearse los usuarios


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
* Menu "Provisión"
* Herramienta "Gestión de usuarios"
* Menu herramienta

* Empresa ""
* Usuario "USER1"
* Crear user
* Menu herramienta
* Empresa ""
Integración de usuarios con dominios de LDAP, debe ir dos backslash porque el backslash en java es un comodin o escape squence
* Usuario "INNOVUS\\USER1"
* Crear user
LogOut:
* LogOut