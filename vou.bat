@echo off
cd /d "D:\code\testKTPM3\BrandVOU\eurekaserver"
start cmd /k "mvn spring-boot:run"

cd /d "D:\code\testKTPM3\BrandVOU\apigateway"
start cmd /k "mvn spring-boot:run"

cd /d "D:\code\testKTPM3\BrandVOU\accountservice"
start cmd /k "mvn spring-boot:run"

cd /d "D:\code\testKTPM3\BrandVOU\authservice"
start cmd /k "mvn spring-boot:run"

cd /d "D:\code\testKTPM3\BrandVOU\eventservice"
start cmd /k "mvn spring-boot:run"

exit