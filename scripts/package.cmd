if "%~1"=="" (
    echo Error: a token is required to package.
) else (
    cd ..
    mvnw.cmd clean package -Dexec.mainClass="io.ignice.c17n.Launcher" -Dapp.token="%1"
)