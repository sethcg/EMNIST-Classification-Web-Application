<script setup>
import {onMounted, ref} from 'vue'

const msg = ref("");

onMounted(() => {
  fetch("/api/network")
      .then((response) => response.text())
      .then((data) => {
        msg.value = data;
      });
})

function test(event) {
    fetch("/api/network/test")
      .then((response) => response.text())
      .then((data) => {
        msg.value = data;
      });
}
</script>

<template>
  <h1 class="text-3xl font-bold">{{ msg }}</h1>
  <button @click="test">TEST NETWORK</button>
</template>

<style scoped>

</style>
