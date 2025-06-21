<script setup>
import {onMounted, ref} from 'vue'

const msg = ref("");

onMounted(() => {
  fetch("/api/messages")
      .then((response) => response.text())
      .then((data) => {
        msg.value = data;
      });
})
</script>

<template>
  <h1 class="text-3xl font-bold">{{ msg }}</h1>
</template>

<style scoped>

</style>
