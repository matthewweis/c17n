cd ..
mvnw.cmd clean
mvnw.cmd package -Dapp.token=%1 -Dapp.profile=release
mvnw.cmd exec:java -Dexec.mainClass=io.ignice.c17n.AppLauncher -Dapp.profile=release
cd scripts