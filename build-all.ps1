# Script para compilar, testear y ejecutar todos los microservicios

# Configurar Java 21
$env:JAVA_HOME = 'C:\Users\Felipe\AppData\Local\jdks\jdk-21.0.10'
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"

$base = 'd:\workspace\Duoc\Sala-Capitular'
$services = @(
    ('Rol', 'Rol\Rol', 8081),
    ('User', 'User\User', 8082),
    ('Catalogo', 'Catalogo\Catalogo', 8083),
    ('Estado', 'Estado\Estado', 8084),
    ('Libro', 'Libro\Libro', 8085),
    ('Estante', 'Estante\Estante', 8086),
    ('Historial', 'Historial\Historial', 8087),
    ('Multas', 'Multas\Multas', 8088),
    ('ReservaLibro', 'ReservaLibro\ReservaLibro', 8089),
    ('Detalle', 'Detalle\Detalle', 8090),
    ('ReseñaLibro', 'ReseñaLibro\ReseñaLibro', 8091)
)

$report = @()

Write-Host "╔════════════════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║          DIAGNÓSTICO Y COMPILACIÓN DE MICROSERVICIOS              ║" -ForegroundColor Cyan
Write-Host "║                  Sala Capitular - Java 21                          ║" -ForegroundColor Cyan
Write-Host "╚════════════════════════════════════════════════════════════════════╝" -ForegroundColor Cyan
Write-Host ""

# Verific Java version
Write-Host "✓ Java Version:" -ForegroundColor Yellow
java -version 2>&1 | Select-Object -First 1
Write-Host ""

# Verificar Maven en cada servicio
Write-Host "╔════════════════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║                    COMPILACIÓN POR SERVICIO                        ║" -ForegroundColor Cyan
Write-Host "╚════════════════════════════════════════════════════════════════════╝" -ForegroundColor Cyan
Write-Host ""

foreach ($tuple in $services) {
    $name = $tuple[0]
    $path = $tuple[1]
    $port = $tuple[2]
    $fullPath = Join-Path $base $path
    
    if (Test-Path $fullPath) {
        Write-Host "[$name] Compilando..." -NoNewline
        Push-Location $fullPath
        
        $startTime = Get-Date
        $output = & .\mvnw.cmd clean compile -DskipTests 2>&1
        $endTime = Get-Date
        $duration = ($endTime - $startTime).TotalSeconds
        
        $buildResult = $output | Select-String "BUILD SUCCESS|BUILD FAILURE" | Select-Object -Last 1
        
        if ($buildResult -match "SUCCESS") {
            Write-Host " ✅ OK (${duration}s)" -ForegroundColor Green
            $report += @{
                Service = $name
                Status = "SUCCESS"
                Port = $port
                Duration = $duration
                Error = $null
            }
        } else {
            Write-Host " ❌ ERROR (${duration}s)" -ForegroundColor Red
            $errorMsg = ($output | Select-String "\[ERROR\]" | Select-Object -Last 1).ToString()
            Write-Host "   Error: $errorMsg" -ForegroundColor Red
            $report += @{
                Service = $name
                Status = "FAILURE"
                Port = $port
                Duration = $duration
                Error = $errorMsg
            }
        }
        
        Pop-Location
    } else {
        Write-Host "[$name] ⚠️  No existe en $path" -ForegroundColor Yellow
        $report += @{
            Service = $name
            Status = "NOT_FOUND"
            Port = $port
            Duration = 0
            Error = "Ruta no encontrada"
        }
    }
}

# Resumen
Write-Host ""
Write-Host "╔════════════════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║                           RESUMEN                                  ║" -ForegroundColor Cyan
Write-Host "╚════════════════════════════════════════════════════════════════════╝" -ForegroundColor Cyan
Write-Host ""

$successCount = ($report | Where-Object { $_.Status -eq "SUCCESS" }).Count
$failureCount = ($report | Where-Object { $_.Status -eq "FAILURE" }).Count
$totalTime = ($report | Measure-Object -Property Duration -Sum).Sum

Write-Host "✅ Compilados exitosamente: $successCount" -ForegroundColor Green
Write-Host "❌ Errores: $failureCount" -ForegroundColor Red
Write-Host "⏱️  Tiempo total: ${totalTime}s" -ForegroundColor Yellow
Write-Host ""

if ($failureCount -gt 0) {
    Write-Host "Servicios con errores:" -ForegroundColor Red
    $report | Where-Object { $_.Status -eq "FAILURE" } | ForEach-Object {
        Write-Host "  ❌ $($_.Service) - $($_.Error)" -ForegroundColor Red
    }
    Write-Host ""
}

Write-Host "╔════════════════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║            PUERTOS DISPONIBLES PARA EJECUCIÓN                      ║" -ForegroundColor Cyan
Write-Host "╚════════════════════════════════════════════════════════════════════╝" -ForegroundColor Cyan
Write-Host ""

$report | Where-Object { $_.Status -eq "SUCCESS" } | ForEach-Object {
    Write-Host "  $($_.Service.PadRight(20)) → http://localhost:$($_.Port)" -ForegroundColor Green
    Write-Host "  $(' ' * 20)   Swagger: http://localhost:$($_.Port)/swagger-ui.html" -ForegroundColor Gray
}

Write-Host ""
Write-Host "✓ Compilación completada" -ForegroundColor Green
