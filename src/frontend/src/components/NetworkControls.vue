<script setup>
  import { ref } from 'vue';

  const msg = ref('');
  const disableTesting = ref(true);

  function train(event) {
    const eventSource = new EventSource('/api/notification');
    eventSource.onerror = (error) => console.error('Connection error:', error);
    eventSource.addEventListener('trainingUpdate', (event) => {
      const message = JSON.parse(event.data);
      console.log(message);
    });
    fetch('/api/train', { method: 'POST' })
      .then((response) => response.text())
      .then(() => {
        disableTesting.value = false;
        eventSource.close();
      });
  }

  function test(event) {
    const eventSource = new EventSource('/api/notification');
    eventSource.onerror = (error) => console.error('Connection error:', error);
    eventSource.addEventListener('testingUpdate', (event) => {
      const message = JSON.parse(event.data);
      console.log(message);
    });
    fetch('/api/test', { method: 'POST' })
      .then((response) => response.text())
      .then(() => eventSource.close());
  }
</script>

<template>
  <div class="flex flex-col gap-2 my-2">
    <div class="flex flex-row gap-2">
      <button class="flex flex-row items-center py-2 px-4" @click="train">TRAIN</button>
      <button class="flex flex-row items-center py-2 px-4" @click="test" :disabled="disableTesting">TEST</button>
    </div>
    <h1 class="text-xl font-bold">{{ msg }}</h1>
  </div>
</template>

<style scoped>
  button {
    border-radius: 8px;
    border: 2px solid transparent;
    background-color: var(--color-slate-900);
    cursor: pointer;
    transition: border-color 0.25s;
  }
  button:hover:not(.active) {
    border-color: var(--color-teal-700);
  }
</style>
