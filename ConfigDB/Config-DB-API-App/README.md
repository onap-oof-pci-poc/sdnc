# Config-DB-API-APP
This App exposes the below APIs to access the config DB.

GET /SDNCConfigDBAPI/getCellList/{networkId}/{ts}
GET /SDNCConfigDBAPI/getPCI/{cellId}/{ts}
GET /SDNCConfigDBAPI/getNbrList/{cellId}/{ts}
GET /SDNCConfigDBAPI/getPnfName/{cellId}/{ts}
Example : GET /SDNCConfigDBAPI/getPnfName/Chn1009/2018-10-23 11:27:31

PUT /SDNCConfigDBAPI/insertData
(For bulk upload use attached sample payload RAN_DumpFile_09Oct18.json)



Prerequisite:
1.MariaDB 10.3
2.apache-maven-3.5.4
3.jdk1.8.0

Below are the steps to execute this App.

1.Build the App

mvn clean install or mvn install

Example: C:\ConfigDB\Config-DB-API-App\App>mvn clean install

2. Run the App

mvn spring-boot:run

Example : C:\ConfigDB\Config-DB-API-App\App>mvn spring-boot:run





