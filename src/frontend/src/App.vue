<script setup>
  import { ref } from 'vue';
  import DrawingCanvas from './components/Canvas.vue';
  import Network from './components/Network.vue';

  const prediction = ref(null);
</script>

<template>
  <div class="flex flex-col gap-2 size-full place-items-center justify-center">
    <div class="pt-8 p-4 font-extrabold text-4xl">Convolutional Neural Network</div>
    <div class="flex flex-row gap-4 size-full justify-center">
      <div class="max-w-[1280px]">
        <DrawingCanvas v-model:prediction="prediction" />
      </div>
      <div class="max-w-[320px] max-h-[560px] grow">
        <Suspense>
          <Network v-model:prediction="prediction" />
          <template #fallback></template>
        </Suspense>
      </div>
    </div>
  </div>
</template>
