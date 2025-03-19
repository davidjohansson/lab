Projekt for att labba med next-auth, keycloak och oidc i allmanhet


Wiresharkfilter for trafiken mot keycloak:
`http and (tcp.srcport ==8080 or tcp.dstport==8080)`

## keycloak
-starta med ./bin/kc.sh start-dev
-admin finns pa http://localhost:8080
-admin: admin, admin
-user: labuser, labuser

log:
-skapat adminkonto, login admin, password admin
-skapat realm labrealm
-skapat user labuser, password labuser
-loggat in pa http://localhost:8080/realms/labrealm/account

## app
next.js med page-router, eftersom det verkar krangligt att satta
upp med app-router.

log:
-skapat tabeller i postgres lab:db (startar den med docker-compose up i ../rkb)
-fatt persistering av sessioner och accounts att fungera

todo:
-flytta databas med authinfo hit fran rkb, starta med samma docker-compose som startar nginx


## nginx
conf i nginx, starta med docker compose.

log
-satt upp /etc/hosts och nginx for att ha siten pa `oidc.lab`

 
