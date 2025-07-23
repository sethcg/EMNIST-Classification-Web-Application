<script setup>
import {ref} from 'vue'

const msg = ref("");
const isTrainingDone = ref(true);

function train(event) {
  const eventSource = new EventSource("/api/notification");
  eventSource.defineModel

  eventSource.addEventListener("trainingUpdate", (event) => {
    const message = JSON.parse(event.data);
    console.log(message);
  });
  eventSource.onerror = (error) => {
    console.error("Connection error:", error);
    eventSource.close();
  };
  fetch("/api/train")
    .then((response) => response.text())
    .then((data) => {
      console.log("TRAINING COMPLETE");
      isTrainingDone.value = false;
      eventSource.close();
    });
}

function test(event) {
  
}
</script>

<template>
  <h1 class="text-3xl font-bold">{{ msg }}</h1>
  <button @click="train">TRAIN</button>
  <button @click="test" :disabled="isTrainingDone">TEST</button>
</template>

<style scoped>

</style>
