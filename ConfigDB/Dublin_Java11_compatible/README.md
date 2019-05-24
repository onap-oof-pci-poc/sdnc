# Config-DB-API-APP V4.0.0
This App exposes the below APIs to access the config DB.


1.GET /api/sdnc-config-db/v4/getCell/{cellId} 

2.GET /api/sdnc-config-db/v4/getCellList/{networkId}/{ts} 

3.GET /api/sdnc-config-db/v4/getNbrCellsNetwork/{networkId} 

4.GET /api/sdnc-config-db/v4/getNbrList/{cellId}/{ts} 

5.GET /api/sdnc-config-db/v4/getPCI/{cellId}/{ts} 

6.GET /api/sdnc-config-db/v4/getPnfId/{cellId}/{ts} 

Example : GET /api/sdnc-config-db/v4/getPnfId/Chn1009/2019-03-23 11:27:31

7.PUT /api/sdnc-config-db/v4/insertData 

(For bulk upload use attached sample payload RAN_DumpFile_15Mar19.json)

8.PUT /api/sdnc-config-db/v4/createNbr/{cellId} 


9.PATCH /api/sdnc-config-db/v4/modifyNbrHO/{cellId}/{targetCellId} 


Example:

PATCH /api/sdnc-config-db/v4/modifyNbrHO/Chn0000/Chn0016 HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Authorization: Basic cm9vdDpzZWNyZXQ=
Cache-Control: no-cache
Postman-Token: 12f6ba10-e855-4736-a1d1-192fd2aa4399

{

"ho":false

}




10.PATCH /api/sdnc-config-db/v4/modifyPci/{cellId} 

Example :
PATCH /api/sdnc-config-db/v4/modifyPci/Chn0000 HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Authorization: Basic cm9vdDpzZWNyZXQ=
Cache-Control: no-cache
Postman-Token: 12f6ba10-e855-4736-a1d1-192fd2aa4399

{

"pci-value":1

}

11.PATCH /api/sdnc-config-db/v4/modifyPnfId/{cellId} 

Example:

PATCH /api/sdnc-config-db/v4/modifyPnfId/Chn0000 HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Authorization: Basic cm9vdDpzZWNyZXQ=
Cache-Control: no-cache
Postman-Token: 4289dd50-0e93-c0af-53cc-c46704d89bd1

{

"pnf-id":"ncserver11"

}



12.DELETE /api/sdnc-config-db/v4/deleteNbr/{cellId}/{targetCellId} 



Prerequisite:
1.MariaDB 10.3
2.apache-maven-3.5.4
3.jdk11.0.3


Below are the steps to execute this App(Create DB with attached Schema before this).
1.Update the src/main/resources/application.properties for DB and Loggers setttings.

2.Build the App

mvn clean install or mvn install

Example: C:\ConfigDB\Config-DB-API-App\App>mvn clean install

3. Run the App

mvn spring-boot:run

Example : C:\ConfigDB\Config-DB-API-App\App>mvn spring-boot:run
