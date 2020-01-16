Verifica herramienta gráfica Gestión de Contratos - Iniciando_spec: 06.11_Gestion_de_Contratos
=================================================
Tags:Provision,06.11_Gestion_de_Contratos,Smoke,Regresion,CI
Preconditions:


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
* Herramienta "Gestión de contratos"
* Empresa ""
* Usuario ""
* Nombre contrato "ServiceAdaptor WS"
* tipoclause "sms"
* refcontract ""

* InicioHome
* Menu herramienta

* ConClause ""
* Menu herramienta
* Asignar user
* Menu herramienta
* EliminaContrato

Escenario segundo: Crea un contrato con cláusula y elimina inmediatamente la cláusula. Comprueba que se haya eliminado
* Menu herramienta
* tipoclause "email"
* ConClause ""
* Menu herramienta
* eliminaClausula

LogOut:
* LogOut