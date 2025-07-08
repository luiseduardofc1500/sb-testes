# Script Completo para Build, Test e Publicação Docker
# Sistema Bancário API

param(
    [string]$DockerUsername = "seu-usuario-dockerhub",
    [string]$Version = "1.0.0",
    [switch]$SkipTests,
    [switch]$SkipPush
)

Write-Host "🚀 Sistema Bancário - Build e Publicação Docker" -ForegroundColor Cyan
Write-Host "=" * 50 -ForegroundColor Cyan

# Configurações
$ImageName = "sistema-bancario-api"
$FullImageName = "$DockerUsername/$ImageName:$Version"
$LatestImageName = "$DockerUsername/$ImageName:latest"

# Verificar se Docker está executando
Write-Host "`n🔍 Verificando Docker..." -ForegroundColor Yellow
try {
    docker version | Out-Null
    Write-Host "✅ Docker está executando" -ForegroundColor Green
} catch {
    Write-Host "❌ Docker não está executando. Inicie o Docker Desktop primeiro." -ForegroundColor Red
    exit 1
}

# Verificar se está logado no Docker Hub
Write-Host "`n🔐 Verificando login no Docker Hub..." -ForegroundColor Yellow
try {
    $loginCheck = docker info 2>&1 | Select-String "Username"
    if ($loginCheck) {
        Write-Host "✅ Logado no Docker Hub" -ForegroundColor Green
    } else {
        Write-Host "⚠️  Não está logado no Docker Hub. Fazendo login..." -ForegroundColor Yellow
        docker login
        if ($LASTEXITCODE -ne 0) {
            Write-Host "❌ Falha no login do Docker Hub" -ForegroundColor Red
            exit 1
        }
    }
} catch {
    Write-Host "⚠️  Não foi possível verificar o status do login. Continuando..." -ForegroundColor Yellow
}

# Compilar aplicação Java
if (-not $SkipTests) {
    Write-Host "`n🏗️  Compilando aplicação Java..." -ForegroundColor Yellow
    .\mvnw.cmd clean package -DskipTests
    if ($LASTEXITCODE -ne 0) {
        Write-Host "❌ Falha na compilação Java" -ForegroundColor Red
        exit 1
    }
    Write-Host "✅ Aplicação compilada com sucesso" -ForegroundColor Green
}

# Build da imagem Docker
Write-Host "`n🐳 Construindo imagem Docker..." -ForegroundColor Yellow
Write-Host "   Imagem: $FullImageName" -ForegroundColor Gray

docker build -t $FullImageName -t $LatestImageName .
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Falha no build da imagem Docker" -ForegroundColor Red
    exit 1
}
Write-Host "✅ Imagem Docker construída com sucesso" -ForegroundColor Green

# Testar a imagem localmente
Write-Host "`n🧪 Testando imagem localmente..." -ForegroundColor Yellow
$containerId = docker run -d -p 8080:8080 $FullImageName
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Falha ao iniciar container de teste" -ForegroundColor Red
    exit 1
}

Write-Host "   Container iniciado: $containerId" -ForegroundColor Gray
Write-Host "   Aguardando aplicação iniciar..." -ForegroundColor Gray

# Aguardar aplicação iniciar
$maxRetries = 30
$retryCount = 0
$healthOk = $false

do {
    Start-Sleep -Seconds 2
    try {
        $response = Invoke-RestMethod -Uri "http://localhost:8080/actuator/health" -Method GET -TimeoutSec 5
        if ($response.status -eq "UP") {
            $healthOk = $true
            break
        }
    } catch {
        $retryCount++
        Write-Host "." -NoNewline -ForegroundColor Yellow
    }
} while ($retryCount -lt $maxRetries)

if ($healthOk) {
    Write-Host "`n✅ Aplicação está funcionando corretamente" -ForegroundColor Green
} else {
    Write-Host "`n❌ Aplicação não respondeu ao health check" -ForegroundColor Red
    Write-Host "   Logs do container:" -ForegroundColor Gray
    docker logs $containerId
}

# Parar container de teste
Write-Host "`n🛑 Parando container de teste..." -ForegroundColor Yellow
docker stop $containerId | Out-Null
docker rm $containerId | Out-Null

if (-not $healthOk) {
    Write-Host "❌ Teste falhou. Não será feito push da imagem." -ForegroundColor Red
    exit 1
}

# Publicar no Docker Hub
if (-not $SkipPush) {
    Write-Host "`n📤 Publicando no Docker Hub..." -ForegroundColor Yellow
    
    Write-Host "   Fazendo push da versão $Version..." -ForegroundColor Gray
    docker push $FullImageName
    if ($LASTEXITCODE -ne 0) {
        Write-Host "❌ Falha no push da versão $Version" -ForegroundColor Red
        exit 1
    }
    
    Write-Host "   Fazendo push da versão latest..." -ForegroundColor Gray
    docker push $LatestImageName
    if ($LASTEXITCODE -ne 0) {
        Write-Host "❌ Falha no push da versão latest" -ForegroundColor Red
        exit 1
    }
    
    Write-Host "✅ Imagem publicada com sucesso no Docker Hub" -ForegroundColor Green
} else {
    Write-Host "`n⏭️  Pulando publicação no Docker Hub (--SkipPush especificado)" -ForegroundColor Yellow
}

# Relatório final
Write-Host "`n" + "=" * 50 -ForegroundColor Cyan
Write-Host "🎉 PROCESSO CONCLUÍDO COM SUCESSO!" -ForegroundColor Cyan
Write-Host "=" * 50 -ForegroundColor Cyan

Write-Host "`n📋 Informações da Imagem:" -ForegroundColor White
Write-Host "   Nome: $FullImageName" -ForegroundColor Gray
Write-Host "   Tag Latest: $LatestImageName" -ForegroundColor Gray

if (-not $SkipPush) {
    Write-Host "`n🌐 URL no Docker Hub:" -ForegroundColor White
    Write-Host "   https://hub.docker.com/r/$DockerUsername/$ImageName" -ForegroundColor Blue
}

Write-Host "`n🚀 Para executar a imagem:" -ForegroundColor White
Write-Host "   docker run -p 8080:8080 $FullImageName" -ForegroundColor Green

Write-Host "`n🧪 Para testar:" -ForegroundColor White
Write-Host "   curl http://localhost:8080/actuator/health" -ForegroundColor Green
Write-Host "   ou execute: .\test-api.ps1" -ForegroundColor Green

Write-Host "`n✨ Build e publicação finalizados!" -ForegroundColor Cyan
