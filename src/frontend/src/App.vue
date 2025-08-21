<script setup>
  import { ref } from 'vue';
  import DrawingCanvas from './components/Canvas.vue';
  import Network from './components/Network.vue';

  const props = defineProps({
    hasNetwork: Boolean,
  });
  
  const hasNetwork = ref(props.hasNetwork);
  const prediction = ref(null);

</script>

<template>
  <div class="flex flex-col size-full gap-2 p-4 place-items-center justify-center">
    <div class="py-4 font-bold font-rubik text-4xl">CONVOLUTIONAL NEURAL NETWORK</div>
    <div class="flex flex-row gap-[20px] size-full justify-center">
      <div class="max-w-[1280px]">
        <Suspense>
          <DrawingCanvas v-model:prediction="prediction" v-model:hasNetwork="hasNetwork" />
          <template #fallback></template>
        </Suspense>
      </div>
      <div class="max-w-[320px] max-h-[560px] grow">
        <Suspense>
          <Network v-model:prediction="prediction" v-model:hasNetwork="hasNetwork" />
          <template #fallback></template>
        </Suspense>
      </div>
    </div>
  </div>
</template>
