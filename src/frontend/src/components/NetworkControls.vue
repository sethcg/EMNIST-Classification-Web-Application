<script setup>
  import { ref } from 'vue';

  const emit = defineEmits([
    'trainingComplete',
    'testingComplete',
    'setTrainingInProgress',
    'setTrainingProgress',
    'setTestingInProgress',
    'setTestingProgress',
  ]);
  const props = defineProps({
    hasNetwork: {
      type: Boolean,
      required: true,
    },
  });

  const disabledTrain = ref(false);
  const disabledTest = ref(!props.hasNetwork);

  const train = () => {
    emit('setTrainingInProgress', true);
    disabledTrain.value = true;
    disabledTest.value = true;
    const eventSource = new EventSource('/api/notification');
    eventSource.onerror = (error) => console.error('Connection error:', error);
    eventSource.addEventListener('trainingUpdate', (event) => {
      const data = JSON.parse(event.data);
      emit('setTrainingProgress', data);
    });
    fetch('/api/train', { method: 'POST' })
      .then((response) => response.text())
      .then(() => {
        emit('setTrainingInProgress', false);
        emit('trainingComplete');
        eventSource.close();
        disabledTrain.value = false;
        disabledTest.value = false;
      });
  };

  const test = () => {
    emit('setTestingInProgress', true);
    disabledTrain.value = true;
    disabledTest.value = true;
    const eventSource = new EventSource('/api/notification');
    eventSource.onerror = (error) => console.error('Connection error:', error);
    eventSource.addEventListener('testingUpdate', (event) => {
      const data = JSON.parse(event.data);
      emit('setTestingProgress', data);
    });
    fetch('/api/test', { method: 'POST' })
      .then((response) => response.text())
      .then(() => {
        emit('setTestingInProgress', false);
        emit('testingComplete');
        eventSource.close();
        disabledTrain.value = false;
        disabledTest.value = false;
      });
  };
</script>

<template>
  <div class="flex flex-col gap-2">
    <div class="flex flex-row gap-2">
      <button class="flex flex-row items-center py-1 px-3 disabled:opacity-60" @click="train" :disabled="disabledTrain">
        TRAIN
      </button>
      <button
        class="flex flex-row items-center py-1 px-3 disabled:opacity-60"
        :class="{ hidden: !hasNetwork }"
        :disabled="disabledTest"
        @click="test">
        TEST
      </button>
    </div>
  </div>
</template>

<style scoped>
  button {
    border-radius: 8px;
    border: 2px solid transparent;
    background-color: var(--color-neutral-900);
    cursor: pointer;
    transition: border-color 0.25s;
  }
  button:hover:not(.active) {
    border-color: var(--color-teal-700);
  }
  button:hover:disabled {
    border-color: transparent;
    cursor: default;
  }
</style>
