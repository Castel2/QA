Genera una PNS desde la herramienta gráfica 'wtest-pns' - Iniciando_spec: 03.08_MT_PNS
======================================================

Tags:Transaccionamiento,pns,03.08_MT_PNS,Smoke,Regresion,CI
Preconditions:


Dependencias.-
1.- Este escenario supone que toda la aprovisión ya está hecha. Es decir usuario, MApp, Contratos, etc...


LogIN, Almacen de Datos:
* Establecer host "" port "" dir ""
* Establecer login "" user "" pass ""
* Cumplimentar Formulario y Login
* Establecer cliente
* Hash Map
* Lista Propiedades desde licencia

Escenario primero
-----------------
Este workflow lo que hace es enviar una PNS con la herramienta 'wtest-pns'
Navegacion,Transaccionar,Almacen de Datos:
* Empresa "INNOVUS"
* Menu "Provisión"
* Herramienta "Gestión de contratos"
* Menu herramienta
* ConProps "Envíos de PushNots" "CUSTOMER_PREFERENCES" "TRUE"
* Menu herramienta
* ConProps "Envíos de PushNots" "PNS_APP" "*"
* Menu herramienta
* ConProps "Envíos de PushNots" "ORGANIZATION" "LATINIA"
* forzarrecarga

* Menu "Diagnosis"
* Herramienta "Envíos de PushNots"
* Menu herramienta
* Transaccionar EnvioPNS "wtest-pns - Envíos de PushNots" "#ref_user" "usercapital" "PNS mensaje público desde 'wtest-pns'" "Mensaje Privado desde 'wtest-pns'" "EtiquetaPNS"

LogOut:
* LogOut




LogIN, Almacen de Datos:
/* LoginEmpresa "INNOVUS"
/* LoginUser "VALIDACION"
/* LoginPassword "BENCHMARK"
/* Cumplimentar formulario y Login "" ""
/* Empresa"


h1.Se prepara para enviar un MT-BULK Masivo:
Lo primero es establecer las propiedades del contrato 'PNSMASSIVE=TRUE' y 'PNSAPP'
Navegacion, GestionContratos, Almacen de Datos, Inicio, InsercionWSsdp:
/* Menu "Provisión"
/* Herramienta "Gestión de contratos"
/* InicioHome
/* Menu herramienta
/* ConProps "Envíos de PushNots Automated" "PNS_MASSIVE" "TRUE"
/* Menu herramienta
/* ConProps "Envíos de PushNots Automated" "PNS_APP" "*"

LogOut:
/* LogOut



h1.Este workflow envia MT-BULK Masivos por el contrato generico de * wtest-pns. El PNS llega a TODOS los usuarios de una M-App
Se inyecta un XML directo al adaptador WS
InsercionWSsdp:
/* PushPNSmasivo "push-pns-masivo.xml" "CAPITAL" "wtest-pns" "MT-PNS" "mymappautomated0" "MT PNS Masivo inyectado por XML desde Automated '03.08_MT_PNS.scn' "

h1.
h1.//==Comprobamos Estadísticas del BULK==//
LogIN, Almacen de Datos:
/* LoginEmpresa "CAPITAL"
/* LoginUser "USER1"
/* LoginPassword "USER1"
/* Cumplimentar formulario y Login "" ""

Inicio,Navegacion,Almacen de Datos:
/* InicioHome
/* Menu "--"
/* Herramienta "Detalle de transacciones Automated"
/* Menu herramienta

Detalle de transacciones, Esperar, Almacen de Datos:
/* BusquedaCriterio "* refUser" "usercapital" ""
/* Nombre contrato "usercapital"
/* Ver estados "Entregado al proveedor"
/* Ver estados "Entrega confirmada a usuario"
/* Ver MApp "mymappautomated0"

LogOut:
/* LogOut