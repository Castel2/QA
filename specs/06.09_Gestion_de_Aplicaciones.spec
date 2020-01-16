Specification Heading - Iniciando_spec: 06.09_Gestion_de_Aplicaciones
=====================
Created by xruiz on 05/02/2016
tags:Provision, 06.09_Gestion_de_Aplicaciones,Smoke,Regresion,CI

Verifica herramienta gráfica Gestión de Aplicaciones!!!!

Estos son los steps denominados 'de contexto' (ver: http://getgauge.io/documentation/user/current/gauge_terminologies/contexts.html)
Estos steps se ejecutan antes de cada escenario de este spec.
* Establecer host "" port "" dir ""
* Establecer login "" user "" pass ""
* Cumplimentar Formulario y Login
* Establecer cliente
* Hash Map
* Lista Propiedades desde licencia

Primer Primero
---------------
* Menu "Provisión"
* Herramienta "Gestión de aplicaciones"
* Menu herramienta
* Verifica Nombres "contracts,defaultsms,lman-ctrlpanel,lman-reports,lman-vcontent,lman-vcontentmail,lman-vstats,lman-vstatsmail"
* Verifica Nombres "ServiceAdaptorJMS,ServiceAdaptorWS,ServiceAdaptorWSAuth,vcollector,wappmsgdiscarded,wapppush,wapppushemail,wappqcredit"
* Verifica Nombres "wgestagent,wgestchannel,wgestcontract,wgestcredit,wgestenterprise,wgestproduct,wgesttemplate,wgestuser"
* Verifica Nombres "Lista de contratos,DefaultSMS,Panel de control de la plataforma,Generador de informes de tráfico,Detalle de transacciones,Detalle de transacciones email,Estadísticas,Estadísticas email"
* Verifica Nombres "ServiceAdaptor JMS,ServiceAdaptor WS,ServiceAdaptor WS Auth,Colector Virtual,Gestión de mensajes descartados,Gestor PUSH,Gestor PUSH EMAIL,Consulta crédito"
* Verifica Nombres "Gestión de agentes,Gestión de canales,Gestión de contratos,Gestión de créditos,Gestión de empresas,Gestión de aplicaciones,Gestión de plantillas,Gestión de usuarios"

* Crear "MiAplicacionPIR"
* Roles "MiAplicacionPIR" "ADAPTOR_WS"
* InicioHome
* Menu herramienta
* setLGUI "wsubscribers" "ADMIN_MODE " "TRUE"

* InicioHome
* Menu herramienta
* Crear "InserWS-Automated"
* Roles "InserWS-Automated" "ADAPTOR_WS"

* LogOut