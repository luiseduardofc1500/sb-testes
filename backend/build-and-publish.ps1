# Script PowerShell para construir e publicar a imagem Docker do Sistema Bancário
# Certifique-se de fazer login no Docker Hub antes de executar: docker login

# Variáveis de configuração
$DOCKER_USERNAME = "seu-usuario-dockerhub"  # Substitua pelo seu usuário do Docker Hub
$IMAGE_NAME = "sistema-bancario-api"
$VERSION = "1.0.0"
$FULL_IMAGE_NAME = "$DOCKER_USERNAME/$IMAGE_NAME:$VERSION"
$LATEST_IMAGE_NAME = "$DOCKER_USERNAME/$IMAGE_NAME:latest"

Write-Host "🔨 Construindo a imagem Docker..." -ForegroundColor Yellow
docker build -t $FULL_IMAGE_NAME .

if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ Imagem construída com sucesso!" -ForegroundColor Green
    
    # Tag como latest
    docker tag $FULL_IMAGE_NAME $LATEST_IMAGE_NAME
    
    Write-Host "📤 Enviando imagem para o Docker Hub..." -ForegroundColor Yellow
    docker push $FULL_IMAGE_NAME
    docker push $LATEST_IMAGE_NAME
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "🎉 Imagem publicada com sucesso no Docker Hub!" -ForegroundColor Green
        Write-Host "📋 Para executar a imagem:" -ForegroundColor Cyan
        Write-Host "   docker run -p 8080:8080 $FULL_IMAGE_NAME" -ForegroundColor White
        Write-Host ""
        Write-Host "🌐 Para testar a API:" -ForegroundColor Cyan
        Write-Host "   curl http://localhost:8080/actuator/health" -ForegroundColor White
    } else {
        Write-Host "❌ Erro ao enviar a imagem para o Docker Hub" -ForegroundColor Red
        exit 1
    }
} else {
    Write-Host "❌ Erro ao construir a imagem Docker" -ForegroundColor Red
    exit 1
}
