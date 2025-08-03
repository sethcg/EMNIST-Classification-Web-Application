<script setup>
  import { ref } from 'vue';
  import { Icon } from '@iconify/vue';

  import NetworkControls from './NetworkControls.vue';

  const props = defineProps({
    prediction: Number | null,
  });

  const hasNetwork = ref(false);
  const imageNum = ref(0);
  const accuracy = ref('0.0');
  const loss = ref('0.0');

  // GET NETWORK, AND STATISTICS IF APPLICABLE
  const getTrainingResults = async () => {
    await fetch('/api/stats', { method: 'POST' })
      .then((response) => response.text())
      .then((json) => {
        const data = JSON.parse(json);
        hasNetwork.value = String(data.hasNetwork).toLowerCase() === 'true';
        if (hasNetwork.value) {
          imageNum.value = Number.parseInt(data.imageNum);
          accuracy.value = Number.parseFloat(data.accuracy).toFixed(2);
          loss.value = Number.parseFloat(data.loss).toFixed(2);
        }
      })
      .catch((_error) => {
        hasNetwork.value = false;
      });
  };

  // INITIALIZE THE TRAINING RESULTS, IF ANY
  await getTrainingResults();
</script>

<template>
  <div class="flex flex-col size-full justify-between">
    <div class="flex flex-col grow gap-2">
      <div class="flex flex-row">
        <p class="font-bold font-rubik text-xl">STATUS</p>
      </div>

      <hr class="mb-[6px] py-[2px] text-neutral-300 bg-neutral-300" />

      <div v-if="hasNetwork">
        <div class="mb-6">
          <div class="flex flex-row grow items-center py-2 px-4 gap-4 bg-lime-900/80 rounded-lg" role="alert">
            <span class="flex justify-center items-center size-[32px] p-1 bg-lime-700/70 rounded-full">
              <Icon icon="fa-solid:check" class="text-lime-300 text-[20px]" />
            </span>
            <span class="text-lg text-lime-200 font-medium">Network ready for testing</span>
          </div>
        </div>

        <!-- NETWORK STATISTICS -->
        <div class="mb-6">
          <div class="flex flex-row">
            <span class="font-bold font-rubik text-xl">TRAINING RESULTS</span>
          </div>

          <hr class="my-[6px] py-[2px] text-neutral-300 bg-neutral-300" />

          <ul class="flex flex-col gap-[2px] ml-4 list-none">
            <li>
              <div class="flex flex-row gap-2">
                <span class="text-lg font-medium font-rubik">Accuracy:</span>
                <span class="self-center text-md font-normal">{{ accuracy }}%</span>
              </div>
            </li>
            <li>
              <div class="flex flex-row gap-2">
                <span class="text-lg font-medium font-rubik">Loss:</span>
                <span class="self-center text-md font-normal">{{ loss }}</span>
              </div>
            </li>
            <li>
              <div class="flex flex-row gap-2">
                <span class="text-lg font-medium font-rubik">Training Images:</span>
                <span class="self-center text-md font-normal">{{ imageNum }}</span>
              </div>
            </li>
          </ul>
        </div>

        <!-- NETWORK PREDICTION -->
        <div class="flex flex-row gap-2">
          <p class="font-bold font-rubik text-xl">PREDICTION:</p>
          <p class="font-bold font-rubik text-xl">{{ props.prediction }}</p>
        </div>
      </div>
      <div v-else>
        <div class="flex flex-row grow items-center py-2 px-4 gap-4 bg-orange-900/80 rounded-lg" role="alert">
          <span class="flex justify-center items-center size-[32px] p-1 bg-orange-500/70 rounded-full">
            <Icon icon="fa-solid:exclamation" class="text-orange-300 text-[20px]" />
          </span>
          <span class="text-lg text-orange-200 font-medium">Network not found</span>
        </div>
      </div>
    </div>
    <NetworkControls @trainingComplete="getTrainingResults" v-model:hasNetwork="hasNetwork" />
  </div>
</template>

<style scoped></style>
