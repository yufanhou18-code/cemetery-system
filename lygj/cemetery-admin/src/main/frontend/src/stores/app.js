import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useAppStore = defineStore('app', () => {
  const sidebarCollapsed = ref(false)
  const theme = ref(localStorage.getItem('theme') || 'light')
  const breadcrumbs = ref([])

  const toggleSidebar = () => {
    sidebarCollapsed.value = !sidebarCollapsed.value
  }

  const setTheme = (newTheme) => {
    theme.value = newTheme
    localStorage.setItem('theme', newTheme)
    document.documentElement.setAttribute('data-theme', newTheme)
  }

  const setBreadcrumbs = (items) => {
    breadcrumbs.value = items
  }

  return {
    sidebarCollapsed,
    theme,
    breadcrumbs,
    toggleSidebar,
    setTheme,
    setBreadcrumbs
  }
})
