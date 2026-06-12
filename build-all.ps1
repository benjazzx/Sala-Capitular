$ErrorActionPreference = "Stop"

$base = Split-Path -Parent $MyInvocation.MyCommand.Path
$localJdk = Join-Path $base ".jdk\jdk-21.0.11+10"

if (-not (Test-Path (Join-Path $localJdk "bin\java.exe"))) {
    throw "No se encontro JDK 21 en $localJdk. Ejecuta primero .\use-java21.ps1 o instala JDK 21."
}

$env:JAVA_HOME = $localJdk
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"

$resenaRoot = (Get-ChildItem -LiteralPath $base -Directory | Where-Object { $_.Name -like "Rese*Libro" } | Select-Object -First 1).Name
if (-not $resenaRoot) {
    throw "No se encontro la carpeta de ResenaLibro."
}

$services = @(
    @{ Name = "Rol"; Path = "Rol\Rol"; Port = 8081 },
    @{ Name = "Catalogo"; Path = "Catalogo\Catalogo"; Port = 8083 },
    @{ Name = "Estado"; Path = "Estado\Estado"; Port = 8084 },
    @{ Name = "User"; Path = "User\User"; Port = 8082 },
    @{ Name = "Libro"; Path = "Libro\Libro"; Port = 8085 },
    @{ Name = "Estante"; Path = "Estante\Estante"; Port = 8086 },
    @{ Name = "Historial"; Path = "Historial\Historial"; Port = 8087 },
    @{ Name = "Multas"; Path = "Multas\Multas"; Port = 8088 },
    @{ Name = "Detalle"; Path = "Detalle\Detalle"; Port = 8090 },
    @{ Name = "ResenaLibro"; Path = "$resenaRoot\$resenaRoot"; Port = 8091 },
    @{ Name = "ReservaLibro"; Path = "ReservaLibro\ReservaLibro"; Port = 8089 },
    @{ Name = "ApiGateway"; Path = "api-gateway"; Port = 8080 }
)

Write-Host "Java activo:" -ForegroundColor Cyan
java -version
Write-Host ""

$failed = @()

foreach ($service in $services) {
    $fullPath = Join-Path $base $service.Path
    Write-Host "VERIFY $($service.Name) ($($service.Port))" -ForegroundColor Cyan

    Push-Location $fullPath
    try {
        & .\mvnw.cmd -q clean verify
        if ($LASTEXITCODE -ne 0) {
            $failed += $service.Name
        }
    }
    finally {
        Pop-Location
    }
}

if ($failed.Count -gt 0) {
    Write-Host "Fallaron: $($failed -join ', ')" -ForegroundColor Red
    exit 1
}

Write-Host "Todos los modulos pasaron clean verify con Java 21." -ForegroundColor Green
