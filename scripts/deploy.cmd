if "%~1"=="" (
    echo Error: a token is required to deploy.
) else (
    cd ..
    mvnw.cmd clean compile exec:java package -Dexec.mainClass="io.ignice.c17n.Launcher" -Dapp.token="%1"
)