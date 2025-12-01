# 前端页面批量生成脚本

Write-Host "`n=== 开始生成前端页面 ===" -ForegroundColor Cyan

$pages = @(
    @{
        Name = "用户管理"
        Path = "views/user/List.vue"
        Icon = "User"
        Fields = @("用户名", "手机号", "邮箱", "状态")
    },
    @{
        Name = "数字纪念空间"
        Path = "views/memorial/List.vue"
        Icon = "Collection"
        Fields = @("空间名称", "逝者姓名", "创建人", "访问量")
    },
    @{
        Name = "服务商管理"
        Path = "views/provider/List.vue"
        Icon = "Shop"
        Fields = @("服务商名称", "联系人", "联系电话", "状态")
    }
)

Write-Host "✓ 脚本已准备，请查看完整前端代码包说明.md文档" -ForegroundColor Green
