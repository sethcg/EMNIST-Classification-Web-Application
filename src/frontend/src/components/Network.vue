<script setup>
  import { onMounted, ref } from 'vue';
  import { Icon } from '@iconify/vue';

  import NetworkControls from './NetworkControls.vue';

  const props = defineProps({
    prediction: Number | null,
  });

  const hasNetwork = ref(true);
  const accuracy = ref(0);
  const loss = ref(0);
  const imageNum = ref(0);

  // ON MOUNT CHECK FOR SAVED NETWORK
  onMounted(() => {
    fetch('/api/mount', { method: 'POST' })
      .then((response) => response.text())
      .then((data) => {
        hasNetwork.value = true;
      })
      .catch((error) => {
        // hasNetwork.value = false;
      });
  });
</script>

<template>
  <div class="flex flex-col size-full justify-between">
    <div class="flex flex-col grow gap-2">
      <div class="flex flex-row">
        <p class="font-bold text-xl">Status</p>
      </div>

      <div v-if="hasNetwork">
        <div class="mb-6">
          <div class="flex flex-row grow items-center py-2 px-4 gap-4 bg-lime-900/80 rounded-lg" role="alert">
            <span class="flex justify-center items-center size-[32px] p-1 bg-lime-700/70 rounded-full">
              <Icon icon="fa-solid:check" class="text-lime-300 text-[20px]" />
            </span>
            <span class="text-md text-lime-200 font-bold">Network ready for testing</span>
          </div>
        </div>

        <!-- NETWORK STATISTICS -->
        <div class="mb-6">
          <div class="flex flex-row">
            <span class="font-bold text-xl">Statistics</span>
          </div>
          <ul class="flex flex-col gap-[2px] ml-4 list-none">
            <li>
              <div class="flex flex-row gap-2">
                <span class="font-semibold">Accuracy:</span>
                <span>{{ accuracy }}%</span>
              </div>
            </li>
            <li>
              <div class="flex flex-row gap-2">
                <span class="font-semibold">Loss:</span>
                <span>{{ loss }}</span>
              </div>
            </li>
            <li>
              <div class="flex flex-row gap-2">
                <span class="font-semibold">Training Images Used:</span>
                <span>{{ imageNum }}</span>
              </div>
            </li>
          </ul>
        </div>

        <!-- NETWORK PREDICTION -->
        <div class="flex flex-row gap-2">
          <p class="font-bold text-xl">Prediction:</p>
          <p class="font-bold text-xl">{{ props.prediction }}</p>
        </div>
      </div>
      <div v-else>
        <div class="flex flex-row grow items-center py-2 px-4 gap-4 bg-orange-900/80 rounded-lg" role="alert">
          <span class="flex justify-center items-center size-[32px] p-1 bg-orange-500/70 rounded-full">
            <Icon icon="fa-solid:exclamation" class="text-orange-300 text-[20px]" />
          </span>
          <span class="text-md text-orange-200 font-extrabold">Network not found</span>
        </div>
      </div>
    </div>
    <NetworkControls v-model:hasNetwork="hasNetwork" />
  </div>
</template>

<style scoped></style>
