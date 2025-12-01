<template>
  <div class="not-found-container">
    <div class="content">
      <div class="code">404</div>
      <div class="message">页面未找到</div>
      <div class="desc">抱歉，您访问的页面不存在</div>
      <el-button type="primary" @click="goHome">返回首页</el-button>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const goHome = () => {
  router.push('/')
}
</script>

<style lang="scss" scoped>
.not-found-container {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);

  .content {
    text-align: center;
    color: #fff;

    .code {
      font-size: 120px;
      font-weight: bold;
      margin-bottom: 20px;
      text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
    }

    .message {
      font-size: 32px;
      margin-bottom: 10px;
    }

    .desc {
      font-size: 16px;
      margin-bottom: 30px;
      opacity: 0.9;
    }
  }
}
</style>
