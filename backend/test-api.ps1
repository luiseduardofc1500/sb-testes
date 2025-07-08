# Script para testar a API do Sistema Bancário
# Execute este script após iniciar o container

$API_BASE_URL = "http://localhost:8080"

Write-Host "🧪 Testando API do Sistema Bancário..." -ForegroundColor Yellow
Write-Host "URL Base: $API_BASE_URL" -ForegroundColor Cyan

# Função para fazer requests HTTP
function Test-Endpoint {
    param(
        [string]$Method,
        [string]$Url,
        [string]$Description,
        [string]$Body = $null
    )
    
    Write-Host "`n📋 Testando: $Description" -ForegroundColor Green
    Write-Host "   $Method $Url" -ForegroundColor Gray
    
    try {
        if ($Body) {
            $response = Invoke-RestMethod -Uri $Url -Method $Method -Body $Body -ContentType "application/json"
        } else {
            $response = Invoke-RestMethod -Uri $Url -Method $Method
        }
        
        Write-Host "   ✅ Sucesso!" -ForegroundColor Green
        $response | ConvertTo-Json -Depth 3
        return $true
    }
    catch {
        Write-Host "   ❌ Erro: $($_.Exception.Message)" -ForegroundColor Red
        return $false
    }
}

# Aguardar a aplicação iniciar
Write-Host "`n⏳ Aguardando a aplicação iniciar..." -ForegroundColor Yellow
do {
    Start-Sleep -Seconds 2
    try {
        $healthCheck = Invoke-RestMethod -Uri "$API_BASE_URL/actuator/health" -Method GET -TimeoutSec 5
        if ($healthCheck.status -eq "UP") {
            Write-Host "✅ Aplicação está funcionando!" -ForegroundColor Green
            break
        }
    }
    catch {
        Write-Host "." -NoNewline -ForegroundColor Yellow
    }
} while ($true)

# Testes dos endpoints
$tests = @(
    @{
        Method = "GET"
        Url = "$API_BASE_URL/actuator/health"
        Description = "Health Check"
    },
    @{
        Method = "GET"
        Url = "$API_BASE_URL/actuator/info"
        Description = "Informações da Aplicação"
    },
    @{
        Method = "GET"
        Url = "$API_BASE_URL/api/conta"
        Description = "Listar Contas"
    }
)

# Executar testes
$successCount = 0
$totalTests = $tests.Count

foreach ($test in $tests) {
    if (Test-Endpoint -Method $test.Method -Url $test.Url -Description $test.Description -Body $test.Body) {
        $successCount++
    }
    Start-Sleep -Seconds 1
}

# Teste de criação de conta (se o endpoint existir)
Write-Host "`n📝 Testando criação de conta..." -ForegroundColor Yellow
$newAccountBody = @{
    numero = "12345"
    titular = "João Silva"
    tipo = "CORRENTE"
    saldo = 1000.0
} | ConvertTo-Json

if (Test-Endpoint -Method "POST" -Url "$API_BASE_URL/api/conta" -Description "Criar Nova Conta" -Body $newAccountBody) {
    $successCount++
    $totalTests++
}

# Relatório final
Write-Host "`n" + "="*50 -ForegroundColor Cyan
Write-Host "📊 RELATÓRIO DOS TESTES" -ForegroundColor Cyan
Write-Host "="*50 -ForegroundColor Cyan
Write-Host "Testes executados: $totalTests" -ForegroundColor White
Write-Host "Sucessos: $successCount" -ForegroundColor Green
Write-Host "Falhas: $($totalTests - $successCount)" -ForegroundColor Red

if ($successCount -eq $totalTests) {
    Write-Host "`n🎉 Todos os testes passaram! A API está funcionando corretamente." -ForegroundColor Green
} else {
    Write-Host "`n⚠️  Alguns testes falharam. Verifique os logs da aplicação." -ForegroundColor Yellow
}

Write-Host "`n🌐 API disponível em: $API_BASE_URL" -ForegroundColor Cyan
Write-Host "📚 Documentação: Consulte o README para mais detalhes sobre os endpoints" -ForegroundColor Gray
