// mine.js
Page({
  data: {
    userInfo: {}
  },

  onLoad() {
    this.loadUserInfo()
  },

  loadUserInfo() {
    const userInfo = wx.getStorageSync('userInfo')
    if (userInfo) {
      this.setData({
        userInfo: userInfo
      })
    }
  },

  navigateToOrders() {
    wx.showToast({
      title: '我的预约功能开发中',
      icon: 'none'
    })
  },

  navigateToGraves() {
    wx.showToast({
      title: '我的墓位功能开发中',
      icon: 'none'
    })
  },

  navigateToMemorials() {
    wx.showToast({
      title: '祭扫记录功能开发中',
      icon: 'none'
    })
  },

  navigateToSettings() {
    wx.showToast({
      title: '设置功能开发中',
      icon: 'none'
    })
  },

  navigateToAbout() {
    wx.showToast({
      title: '关于我们功能开发中',
      icon: 'none'
    })
  }
})
