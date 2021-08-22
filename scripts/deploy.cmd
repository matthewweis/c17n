cd ..
@REM mvnw.cmd clean
@REM mvnw.cmd package -Dapp.token="%1"
@REM mvnw.cmd exec:java -Dexec.mainClass="io.ignice.c17n.AppLauncher"
./mvnw clean validate compile exec:java package -Dexec.mainClass="io.ignice.c17n.AppLauncher" -Dapp.token="%1"
cd scripts