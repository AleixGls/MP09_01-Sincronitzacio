mvn clean package

if ($LASTEXITCODE -ne 0) {
    Write-Host "Error: Maven no ha pogut compilar el projecte."
    exit $LASTEXITCODE
}

java -cp "target/classes" com.example.Exercici1