<script setup>
  import { ref } from 'vue';
  import * as api from '../api/emnistApi.js';

  const emit = defineEmits([
    'trainingComplete',
    'testingComplete',
    'setTrainingInProgress',
    'setTrainingProgress',
    'setTestingInProgress',
    'setTestingProgress',
  ]);
  const props = defineProps({
    hasNetwork: Boolean,
  });

  const disabledTrain = ref(false);
  const disabledTest = ref(!props.hasNetwork);

  // HANDLE TRAINING THE CONVOLUTIONAL NEURAL NETWORK
  const train = async () => {
    emit('setTrainingInProgress', true);
    (disabledTrain.value, (disabledTest.value = true));

    const eventSource = new EventSource('/api/notification');
    eventSource.onerror = (error) => console.error('Connection error:', error);
    eventSource.addEventListener('trainingUpdate', (event) => {
      const data = JSON.parse(event.data);
      emit('setTrainingProgress', data);
    });

    await api.train(() => {
      emit('setTrainingInProgress', false);
      emit('trainingComplete');
      eventSource.close();
      (disabledTrain.value, (disabledTest.value = false));
    });
  };

  // HANDLE TESTING THE CONVOLUTIONAL NEURAL NETWORK
  const test = async () => {
    emit('setTestingInProgress', true);
    (disabledTrain.value, (disabledTest.value = true));

    const eventSource = new EventSource('/api/notification');
    eventSource.onerror = (error) => console.error('Connection error:', error);
    eventSource.addEventListener('testingUpdate', (event) => {
      const data = JSON.parse(event.data);
      emit('setTestingProgress', data);
    });

    await api.test(() => {
      emit('setTestingInProgress', false);
      emit('testingComplete');
      eventSource.close();
      (disabledTrain.value, (disabledTest.value = false));
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
