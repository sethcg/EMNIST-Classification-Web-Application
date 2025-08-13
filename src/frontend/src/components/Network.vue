<script setup>
  import { ref } from 'vue';
  import { Icon } from '@iconify/vue';

  import NetworkControls from './NetworkControls.vue';

  const emit = defineEmits(['update:hasNetwork', 'update:prediction']);
  const props = defineProps({
    prediction: {
      type: Number,
      required: true,
    },
    hasNetwork: {
      type: Boolean,
      required: true,
    },
  });

  const trainingInProgress = ref(false);
  const testingInProgress = ref(false);

  const trainingImageNum = ref(0);
  const trainingAccuracy = ref('0.0');
  const trainingLoss = ref('0.0');

  const testingImageNum = ref(0);
  const testingAccuracy = ref('0.0');
  const testingLoss = ref('0.0');

  let imageNum = 0;

  // HANDLE TRAINING PROGRESS VALUES, UPDATING EACH BATCH
  const setTrainingInProgress = (inProgress) => {
    imageNum = 0;
    emit('update:prediction', null);
    emit('update:hasNetwork', false);
    trainingInProgress.value = inProgress;

    trainingImageNum.value = 0;
    trainingAccuracy.value = '0.0';
    trainingLoss.value = '0.0';

    testingImageNum.value = 0;
    testingAccuracy.value = '0.0';
    testingLoss.value = '0.0';
  };

  const setTrainingProgress = (data) => {
    imageNum += Number.parseInt(data.steps);
    trainingImageNum.value = Number.parseInt(imageNum);
    trainingAccuracy.value = Number.parseFloat(data.accuracy).toFixed(2);
    trainingLoss.value = Number.parseFloat(data.loss).toFixed(2);
  };

  // HANDLE TESTING PROGRESS VALUES, UPDATING EACH BATCH
  const setTestingInProgress = (inProgress) => {
    imageNum = 0;
    testingInProgress.value = inProgress;
    testingImageNum.value = 0;
    testingAccuracy.value = '0.0';
    testingLoss.value = '0.0';
  };

  const setTestingProgress = (data) => {
    imageNum += Number.parseInt(data.steps);
    testingImageNum.value = Number.parseInt(imageNum);
    testingAccuracy.value = Number.parseFloat(data.accuracy).toFixed(2);
    testingLoss.value = Number.parseFloat(data.loss).toFixed(2);
  };

  // GET NETWORK TRAINING STATISTICS IF APPLICABLE
  const getTrainingResults = async () => {
    try {
      await fetch('/api/trainingStats', { method: 'POST' })
        .then((response) => response.text())
        .then((json) => {
          const data = JSON.parse(json);
          if (String(data.hasNetwork).toLowerCase() === 'true') {
            trainingImageNum.value = Number.parseInt(data.imageNum);
            trainingAccuracy.value = Number.parseFloat(data.accuracy).toFixed(2);
            trainingLoss.value = Number.parseFloat(data.loss).toFixed(2);
            emit('update:hasNetwork', true);

            testingImageNum.value = 0;
            testingAccuracy.value = '0.0';
            testingLoss.value = '0.0';
          }
        });
    } catch (error) {
      emit('update:hasNetwork', false);
    }
  };

  // GET NETWORK TESTING STATISTICS IF APPLICABLE
  const getTestingResults = async () => {
    await fetch('/api/testingStats', { method: 'POST' })
      .then((response) => response.text())
      .then((json) => {
        const data = JSON.parse(json);
        if (String(data.hasNetwork).toLowerCase() === 'true') {
          testingImageNum.value = Number.parseInt(data.imageNum);
          testingAccuracy.value = Number.parseFloat(data.accuracy).toFixed(2);
          testingLoss.value = Number.parseFloat(data.loss).toFixed(2);
        }
      });
  };

  // INITIALIZE THE TRAINING/TESTING RESULTS, IF ANY
  await getTrainingResults();
  await getTestingResults();
</script>

<template>
  <div class="flex flex-col size-full justify-between">
    <div class="flex flex-col grow gap-2">
      <div class="flex flex-row">
        <p class="font-bold font-rubik text-xl">STATUS</p>
      </div>

      <hr class="mb-[6px] py-[2px] text-neutral-300 bg-neutral-300" />

      <div v-if="hasNetwork && !trainingInProgress">
        <div class="mb-4">
          <div class="flex flex-row grow items-center py-2 px-4 gap-4 bg-lime-900/80 rounded-lg" role="alert">
            <span class="flex justify-center items-center size-[32px] p-1 bg-lime-700/70 rounded-full">
              <Icon icon="fa-solid:check" class="text-lime-300 text-[20px]" />
            </span>
            <span class="text-lg text-lime-200 font-medium">Network ready for testing</span>
          </div>
        </div>
      </div>
      <div v-else-if="trainingInProgress">
        <div class="flex flex-row grow items-center py-2 px-4 gap-4 bg-orange-900/80 rounded-lg" role="alert">
          <span class="flex justify-center items-center size-[32px] p-1 bg-orange-500/70 rounded-full">
            <Icon icon="fa7-solid:clock-rotate-left" class="text-orange-300 text-[20px]" />
          </span>
          <span class="text-lg text-orange-200 font-medium">Network training in progress</span>
        </div>
      </div>
      <div v-else>
        <div class="flex flex-row grow items-center py-2 px-4 gap-4 bg-red-900/80 rounded-lg" role="alert">
          <span class="flex justify-center items-center size-[32px] p-1 bg-red-500/70 rounded-full">
            <Icon icon="fa-solid:exclamation" class="text-red-300 text-[20px]" />
          </span>
          <span class="text-lg text-red-200 font-medium">Network not found</span>
        </div>
      </div>

      <!-- TRAINING -->
      <div v-if="hasNetwork || trainingInProgress">
        <div class="flex flex-row">
          <span class="font-bold font-rubik text-xl">TRAINING RESULTS</span>
        </div>

        <hr class="my-[6px] py-[2px] text-neutral-300 bg-neutral-300" />

        <div class="mb-4">
          <ul class="flex flex-col gap-[2px] ml-4 list-none">
            <li>
              <div class="flex flex-row gap-2">
                <span class="text-lg font-medium font-rubik">Accuracy:</span>
                <span class="self-center text-md font-normal">{{ trainingAccuracy }}%</span>
              </div>
            </li>
            <li>
              <div class="flex flex-row gap-2">
                <span class="text-lg font-medium font-rubik">Loss:</span>
                <span class="self-center text-md font-normal">{{ trainingLoss }}</span>
              </div>
            </li>
            <li>
              <div class="flex flex-row gap-2">
                <span class="text-lg font-medium font-rubik">Training Images:</span>
                <span class="self-center text-md font-normal">{{ trainingImageNum }}</span>
              </div>
            </li>
          </ul>
        </div>
      </div>

      <!-- TESTING -->
      <div v-if="hasNetwork">
        <div class="flex flex-row">
          <span class="font-bold font-rubik text-xl">TESTING RESULTS</span>
        </div>

        <hr class="my-[6px] py-[2px] text-neutral-300 bg-neutral-300" />

        <div class="mb-4">
          <ul class="flex flex-col gap-[2px] ml-4 list-none">
            <li>
              <div class="flex flex-row gap-2">
                <span class="text-lg font-medium font-rubik">Accuracy:</span>
                <span class="self-center text-md font-normal">{{ testingAccuracy }}%</span>
              </div>
            </li>
            <li>
              <div class="flex flex-row gap-2">
                <span class="text-lg font-medium font-rubik">Loss:</span>
                <span class="self-center text-md font-normal">{{ testingLoss }}</span>
              </div>
            </li>
            <li>
              <div class="flex flex-row gap-2">
                <span class="text-lg font-medium font-rubik">Testing Images:</span>
                <span class="self-center text-md font-normal">{{ testingImageNum }}</span>
              </div>
            </li>
          </ul>
        </div>
      </div>

      <!-- NETWORK PREDICTION -->
      <div v-if="hasNetwork" class="flex flex-row gap-2">
        <p class="font-bold font-rubik text-xl">PREDICTION:</p>
        <p class="font-bold font-rubik text-xl">{{ prediction }}</p>
      </div>
    </div>
    <NetworkControls
      @trainingComplete="getTrainingResults"
      @testingComplete="getTestingResults"
      @setTrainingInProgress="setTrainingInProgress"
      @setTrainingProgress="setTrainingProgress"
      @setTestingInProgress="setTestingInProgress"
      @setTestingProgress="setTestingProgress"
      :hasNetwork="hasNetwork" />
  </div>
</template>

<style scoped></style>
