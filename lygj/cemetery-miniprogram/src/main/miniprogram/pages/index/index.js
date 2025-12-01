// index.js
Page({
  data: {
    notices: [
      { id: 1, title: '清明节祭扫预约通知', date: '2025-03-20' },
      { id: 2, title: '墓园环境提升公告', date: '2025-03-15' },
      { id: 3, title: '春节开放时间调整', date: '2025-01-25' }
    ]
  },

  onLoad() {
    // 页面加载时执行
    this.loadNotices()
  },

  loadNotices() {
    // 加载公告列表
    // 实际项目中这里应该调用API
  },

  navigateToReservation() {
    wx.showToast({
      title: '预约功能开发中',
      icon: 'none'
    })
  },

  navigateToGrave() {
    wx.showToast({
      title: '墓位查询功能开发中',
      icon: 'none'
    })
  },

  navigateToOnlineWorship() {
    wx.showToast({
      title: '在线祭扫功能开发中',
      icon: 'none'
    })
  },

  navigateToNavigation() {
    wx.showToast({
      title: '墓园导航功能开发中',
      icon: 'none'
    })
  }
})
