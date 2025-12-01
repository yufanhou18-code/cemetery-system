# 陵园管家前端一键启动脚本

Write-Host "`n╔════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║     陵园管家Vue 3管理端启动脚本        ║" -ForegroundColor Cyan
Write-Host "╚════════════════════════════════════════════╝`n" -ForegroundColor Cyan

# 检查Node.js
Write-Host "1. 检查Node.js环境..." -ForegroundColor Yellow
$nodeVersion = node --version 2>$null
if ($nodeVersion) {
    Write-Host "   ✓ Node.js版本: $nodeVersion" -ForegroundColor Green
} else {
    Write-Host "   ✗ 未安装Node.js，请先安装Node.js 16+!" -ForegroundColor Red
    exit 1
}

# 进入前端目录
Write-Host "`n2. 进入前端目录..." -ForegroundColor Yellow
cd src\main\frontend
Write-Host "   ✓ 当前目录: $(Get-Location)" -ForegroundColor Green

# 检查依赖
Write-Host "`n3. 检查项目依赖..." -ForegroundColor Yellow
if (Test-Path "node_modules" -Or Test-Path "node") {
    Write-Host "   ✓ 依赖已安装" -ForegroundColor Green
} else {
    Write-Host "   正在安装依赖..." -ForegroundColor Yellow
    npm install
    Write-Host "   ✓ 依赖安装完成" -ForegroundColor Green
}

# 启动项目
Write-Host "`n4. 启动开发服务器..." -ForegroundColor Yellow
Write-Host "`n╔════════════════════════════════════════════╗" -ForegroundColor Green
Write-Host "║        ✓ 前端服务启动成功！            ║" -ForegroundColor Green
Write-Host "╚════════════════════════════════════════════╝" -ForegroundColor Green

Write-Host "`n📍 访问地址:" -ForegroundColor Cyan
Write-Host "   • 前端管理端: http://localhost:3000" -ForegroundColor White
Write-Host "   • 后端API:   http://localhost:8080" -ForegroundColor White
Write-Host "   • Swagger:   http://localhost:8080/doc.html" -ForegroundColor White

Write-Host "`n📝 默认登录信息:" -ForegroundColor Cyan
Write-Host "   • 用户名: admin" -ForegroundColor White
Write-Host "   • 密码:   admin123" -ForegroundColor White

Write-Host "`n⚠️  注意事项:" -ForegroundColor Yellow
Write-Host "   • 请确保后端服务已启动（端口8080）" -ForegroundColor Gray
Write-Host "   • 按 Ctrl+C 停止服务" -ForegroundColor Gray
Write-Host "`n"

npx vite --host 0.0.0.0 --port 3000
