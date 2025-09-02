<template>
  <!-- <div style="display: contents">BasicLayout</div> -->
  <a-layout class="layout">
    <a-layout-header class="header"> <GlobalHeader /> </a-layout-header>
    <a-layout-content class="content"> <router-view /> </a-layout-content>
    <a-layout-footer class="footer"> footersite ©2024 Created by Ant UED </a-layout-footer>
  </a-layout>
</template>

<script lang="ts" setup>
import GlobalHeader from '@/components/GlobalHeader.vue'
import { ref } from 'vue'
</script>

<style scoped>
.layout {
  min-height: 100vh;
  background-color: #efefef;
  padding: 6px;
}
.layout .header {
  background: #fff;
  color: unset;
  text-align: center;
  font-size: 20px;
  line-height: 64px;
  margin-bottom: 16px;
  padding-inline: 20px;
}
.layout .content {
  text-align: left;
  margin: 16px auto 56px;

  /* background: #181717; */
}
.layout .footer {
  text-align: center;
  position: fixed;
  bottom: 0px;
  left: 0px;
  right: 0px;
}
</style>
