cd ..
./mvnw clean validate compile test package -Dapp.token="%1"

cd scripts