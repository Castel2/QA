Verifica herramienta gráfica Gestión de MApp  - Iniciando_spec: 03.04_Gestion_de_M-Apps
============================================
Tags:Provision,03.04_Gestion_de_M-Apps,Mapp
Preconditions:

Dependencia
1.- Requiere existencia de las 'organizaciones' con las que va a operar
Pueden crearse empresas con el escenario 'Provision-G Empresas.scn'

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
* Proposito "provision"
* InicioHome
* Menu "Provisión"
* Herramienta "Gestión de M-Apps"
* Menu herramienta
* PNS_MApp_Manager "lat1MobileApp" "LATINIA"
* PNS_MApp_Manager "lat2MobileApp" "LATINIA"

GIVEN Existe la organización y Usuario
* Organizacion "CAPITAL"
* refUser "usuario2"
* Crear suscriptor WS
* Eliminar todas las MApps asociadas a usuario WS
WHEN Se registra una nueva MApp confidencial
* Registrar AppDevice a un usuario WS "true" "mymappautomated0"
THEN se verifica que en el campo "isConfidential" se guarde el valor correcto (true o false)
* isConfidential
* Eliminar MApp de usuario WS llaveToken"token"

LogOut:
* LogOut