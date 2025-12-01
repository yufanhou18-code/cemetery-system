# 服务商管理API测试脚本
$baseUrl = "http://localhost:8080"

Write-Host "========== 开始测试服务商管理模块 API ==========" -ForegroundColor Green

# 1. 创建服务商
Write-Host "`n【1/14】测试创建服务商..." -ForegroundColor Cyan
$createProviderBody = @{
    providerName = "优质陵园服务公司"
    contactPerson = "张经理"
    contactPhone = "13800138001"
    province = "北京市"
    city = "北京市"
    district = "朝阳区"
    address = "朝阳区某某街道123号"
    businessLicense = "91110000MA01234567"
    businessScope = "殡葬服务、墓地销售、祭扫服务"
    introduction = "专业提供高品质殡葬服务，多年行业经验"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/service-provider" -Method Post -Body $createProviderBody -ContentType "application/json"
    $providerId = $response.data
    Write-Host "✓ 创建成功，服务商ID: $providerId" -ForegroundColor Green
} catch {
    Write-Host "✗ 创建失败: $($_.Exception.Message)" -ForegroundColor Red
}

# 2. 获取服务商详情
Write-Host "`n【2/14】测试获取服务商详情..." -ForegroundColor Cyan
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/service-provider/$providerId" -Method Get
    Write-Host "✓ 服务商名称: $($response.data.providerName)" -ForegroundColor Green
    Write-Host "  联系人: $($response.data.contactPerson)" -ForegroundColor Gray
    Write-Host "  联系电话: $($response.data.contactPhone)" -ForegroundColor Gray
    $providerNo = $response.data.providerNo
} catch {
    Write-Host "✗ 获取失败: $($_.Exception.Message)" -ForegroundColor Red
}

# 3. 根据编号查询服务商
Write-Host "`n【3/14】测试根据编号查询服务商..." -ForegroundColor Cyan
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/service-provider/no/$providerNo" -Method Get
    Write-Host "✓ 查询成功，服务商: $($response.data.providerName)" -ForegroundColor Green
} catch {
    Write-Host "✗ 查询失败: $($_.Exception.Message)" -ForegroundColor Red
}

# 4. 分页查询服务商
Write-Host "`n【4/14】测试分页查询服务商..." -ForegroundColor Cyan
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/service-provider/page?page=1&pageSize=10" -Method Get
    Write-Host "✓ 查询成功，总记录数: $($response.data.total)" -ForegroundColor Green
    Write-Host "  当前页记录数: $($response.data.records.Count)" -ForegroundColor Gray
} catch {
    Write-Host "✗ 查询失败: $($_.Exception.Message)" -ForegroundColor Red
}

# 5. 搜索服务商
Write-Host "`n【5/14】测试搜索服务商..." -ForegroundColor Cyan
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/service-provider/search?keyword=陵园&page=1&pageSize=10" -Method Get
    Write-Host "✓ 搜索成功，匹配记录数: $($response.data.total)" -ForegroundColor Green
} catch {
    Write-Host "✗ 搜索失败: $($_.Exception.Message)" -ForegroundColor Red
}

# 6. 获取推荐服务商
Write-Host "`n【6/14】测试获取推荐服务商..." -ForegroundColor Cyan
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/service-provider/recommended?limit=5" -Method Get
    Write-Host "✓ 获取成功，推荐服务商数量: $($response.data.Count)" -ForegroundColor Green
} catch {
    Write-Host "✗ 获取失败: $($_.Exception.Message)" -ForegroundColor Red
}

# 7. 更新服务商
Write-Host "`n【7/14】测试更新服务商..." -ForegroundColor Cyan
$updateProviderBody = @{
    id = $providerId
    providerName = "优质陵园服务公司（已更新）"
    contactPerson = "张经理"
    contactPhone = "13800138002"
    introduction = "专业提供高品质殡葬服务，20年行业经验"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/service-provider" -Method Put -Body $updateProviderBody -ContentType "application/json"
    Write-Host "✓ 更新成功" -ForegroundColor Green
} catch {
    Write-Host "✗ 更新失败: $($_.Exception.Message)" -ForegroundColor Red
}

# 8. 审核服务商（需要管理员权限，可能会失败）
Write-Host "`n【8/14】测试审核服务商..." -ForegroundColor Cyan
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/service-provider/$providerId/audit?approved=true&auditRemark=审核通过" -Method Post
    Write-Host "✓ 审核成功" -ForegroundColor Green
} catch {
    Write-Host "⚠ 审核失败（可能需要管理员权限）: $($_.Exception.Message)" -ForegroundColor Yellow
}

# 9. 启用服务商
Write-Host "`n【9/14】测试启用服务商..." -ForegroundColor Cyan
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/service-provider/$providerId/enable" -Method Post
    Write-Host "✓ 启用成功" -ForegroundColor Green
} catch {
    Write-Host "⚠ 启用失败: $($_.Exception.Message)" -ForegroundColor Yellow
}

# 10. 获取服务商统计信息
Write-Host "`n【10/14】测试获取服务商统计信息..." -ForegroundColor Cyan
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/service-provider/statistics" -Method Get
    Write-Host "✓ 获取成功" -ForegroundColor Green
    Write-Host "  统计数据: $($response.data | ConvertTo-Json -Compress)" -ForegroundColor Gray
} catch {
    Write-Host "✗ 获取失败: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n========== 服务商管理模块测试完成 ==========" -ForegroundColor Green

# 测试服务项目管理模块
Write-Host "`n`n========== 开始测试服务项目管理模块 API ==========" -ForegroundColor Green

# 11. 创建服务项目
Write-Host "`n【1/14】测试创建服务项目..." -ForegroundColor Cyan
$createServiceBody = @{
    providerId = $providerId
    serviceName = "豪华墓碑雕刻服务"
    serviceCategory = 1
    serviceType = 1
    price = 5800.00
    originalPrice = 6800.00
    priceUnit = "次"
    description = "提供高品质墓碑雕刻服务，含设计、雕刻、安装"
    serviceContent = "1.专业设计师设计 2.精细雕刻工艺 3.现场安装调试"
    duration = 3
    stock = 100
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/provider-service" -Method Post -Body $createServiceBody -ContentType "application/json"
    $serviceId = $response.data
    Write-Host "✓ 创建成功，服务项目ID: $serviceId" -ForegroundColor Green
} catch {
    Write-Host "✗ 创建失败: $($_.Exception.Message)" -ForegroundColor Red
}

# 12