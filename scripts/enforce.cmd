cd ..
mvnw.cmd clean
mvnw.cmd package "-Dapp.token=%1"
mvnw.cmd exec:java -Dexec.mainClass="io.ignice.c17n.AppLauncher" -D internal
cd scripts