Comprueba el cambio de GUI de 'extendido' a 'clásico' y viceversa
=================================================================
Tags:Provision,bug,[SDP-638],06.14_GUI_Opciones
Preconditions:

Cambia el GUI a modo 'clásico' y lo regresa al modo 'extendido'
En caso de suceder un bug que impide regresar gráficamente a la visualización 'extendida', hay que solventarlo mediante BBDD, para ello se ha dispuesto el _script_ 'C:\Twist\Restablecer_classic.bat'

Estos son los steps denominados 'de contexto' (ver: http://getgauge.io/documentation/user/current/gauge_terminologies/contexts.html)
Estos steps se ejecutan antes de cada escenario de este spec.
* Establecer host "" port "" dir ""
* Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"
* Cumplimentar Formulario y Login


Escenario primero
-----------------
* Vista "classic"

LogOut:
* LogOut

* Establecer host "" port "" dir ""
* Establecer login "INNOVUS" user "VALIDACION" pass "BENCHMARK"
* Cumplimentar Formulario y Login


Opciones:
* Vista "extended"

LogOut:
* LogOut