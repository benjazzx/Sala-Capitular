param(
    [Parameter(Position = 0)]
    [string]$Name
)

$ErrorActionPreference = "Stop"

$base = Split-Path -Parent $MyInvocation.MyCommand.Path
$jdk = Join-Path $base ".jdk\jdk-21.0.11+10"
$java = Join-Path $jdk "bin\java.exe"

if (-not (Test-Path $java)) {
    throw "No se encontro Java 21 en $jdk."
}

$resenaRoot = (Get-ChildItem -LiteralPath $base -Directory | Where-Object { $_.Name -like "Rese*Libro" } | Select-Object -First 1).Name

$services = [ordered]@{
    Rol = @{ Path = "Rol\Rol"; Port = 8081 }
    Catalogo = @{ Path = "Catalogo\Catalogo"; Port = 8083 }
    Estado = @{ Path = "Estado\Estado"; Port = 8084 }
    User = @{ Path = "User\User"; Port = 8082 }
    Libro = @{ Path = "Libro\Libro"; Port = 8085 }
    Estante = @{ Path = "Estante\Estante"; Port = 8086 }
    Historial = @{ Path = "Historial\Historial"; Port = 8087 }
    Multas = @{ Path = "Multas\Multas"; Port = 8088 }
    Detalle = @{ Path = "Detalle\Detalle"; Port = 8090 }
    ResenaLibro = @{ Path = "$resenaRoot\$resenaRoot"; Port = 8091 }
    ReservaLibro = @{ Path = "ReservaLibro\ReservaLibro"; Port = 8089 }
    ApiGateway = @{ Path = "api-gateway"; Port = 8080 }
}

if ([string]::IsNullOrWhiteSpace($Name)) {
    Write-Host "Uso: .\run-service.ps1 <Servicio>" -ForegroundColor Yellow
    Write-Host "Orden recomendado:" -ForegroundColor Cyan
    $i = 1
    foreach ($item in $services.GetEnumerator()) {
        Write-Host "$i. $($item.Key) ($($item.Value.Port))"
        $i++
    }
    exit 0
}

if (-not $services.Contains($Name)) {
    throw "Servicio no reconocido: $Name"
}

$env:JAVA_HOME = $jdk
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"

$service = $services[$Name]
$servicePath = Join-Path $base $service.Path

Write-Host "Ejecutando $Name en puerto $($service.Port) con Java 21" -ForegroundColor Green
Write-Host "Swagger: http://localhost:$($service.Port)/swagger-ui/index.html" -ForegroundColor Cyan

Push-Location $servicePath
try {
    & .\mvnw.cmd spring-boot:run
}
finally {
    Pop-Location
}
