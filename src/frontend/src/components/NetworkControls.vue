<script setup>
  import { ref } from 'vue';

  const emit = defineEmits(['update:hasNetwork']);
  const props = defineProps({
    hasNetwork: Boolean,
  });

  const inProgress = ref(false);

  const train = () => {
    inProgress.value = true;
    const eventSource = new EventSource('/api/notification');
    eventSource.onerror = (error) => console.error('Connection error:', error);
    eventSource.addEventListener('trainingUpdate', (event) => {
      const message = JSON.parse(event.data);
      console.log(message);
    });
    fetch('/api/train', { method: 'POST' })
      .then((response) => response.text())
      .then(() => {
        inProgress.value = false;
        eventSource.close();
        emit('update:hasNetwork', true);
      });
  };

  const test = () => {
    inProgress.value = true;
    const eventSource = new EventSource('/api/notification');
    eventSource.onerror = (error) => console.error('Connection error:', error);
    eventSource.addEventListener('testingUpdate', (event) => {
      const message = JSON.parse(event.data);
      console.log(message);
    });
    fetch('/api/test', { method: 'POST' })
      .then((response) => response.text())
      .then(() => {
        inProgress.value = false;
        eventSource.close();
      });
  };
</script>

<template>
  <div class="flex flex-col gap-2">
    <div class="flex flex-row gap-2">
      <button class="flex flex-row items-center py-1 px-3" @click="train" :disabled="inProgress">TRAIN</button>
      <button class="flex flex-row items-center py-1 px-3" @click="test" :disabled="inProgress">TEST</button>
    </div>
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
