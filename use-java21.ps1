$ErrorActionPreference = "Stop"

$base = Split-Path -Parent $MyInvocation.MyCommand.Path
$jdk = Join-Path $base ".jdk\jdk-21.0.11+10"
$java = Join-Path $jdk "bin\java.exe"

if (-not (Test-Path $java)) {
    throw "No se encontro Java 21 en $jdk."
}

$env:JAVA_HOME = $jdk
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"

Write-Host "JAVA_HOME=$env:JAVA_HOME" -ForegroundColor Green
java -version
