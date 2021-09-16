cd ..
mvnw clean

REM todo make auto-yes on these docker commands
docker system prune
docker image prune
docker volume prune