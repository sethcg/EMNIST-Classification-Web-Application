<script setup>
  import { ref, watch } from 'vue';
  import { Icon } from '@iconify/vue';

  import VueDrawingCanvas from 'vue-drawing-canvas';
  import CanvasControls from './CanvasControls.vue';

  // PING THE SERVER, TO ENSURE THERE IS A CONNECTION
  await fetch('/api/ping', { method: 'POST' })
    .then((response) => response.text())
    .then((message) => {
      console.log(message);
    })
    .catch((error) => {
      console.error(error);
    });

  const emit = defineEmits(['update:prediction']);
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

  const DrawingCanvas = ref(null);
  const image = ref('');
  const eraser = ref(false);
  const lineWidth = ref(44);

  // ON IMAGE CHANGE, UPDATE PREDICTION
  watch(image, () => {
    if (DrawingCanvas.value.isEmpty()) return;

    // RE-DRAW IMAGE, DOWNSCALED TO [28] x [28] PIXELS
    const canvas = document.getElementById(DrawingCanvas.value.canvasId);
    const smallContext = document.getElementById('output').getContext('2d', { willReadFrequently: true });
    smallContext.drawImage(canvas, 0, 0, canvas.width, canvas.height, 0, 0, 28, 28);

    let image = [];
    let colorArray = [];
    const imageData = smallContext.getImageData(0, 0, 28, 28, { colorSpace: 'srgb' }).data;
    // LOOP THROUGH "Uint8ClampedArray" OF RGBA COLOR VALUES, IN TOTAL [784] PIXEL ARRAY
    for (let i = 0; i < imageData.length; i += 4) {
      const red = imageData[i];
      const green = imageData[i + 1];
      const blue = imageData[i + 2];
      const alpha = imageData[i + 3];
      const colorValue = (alpha << 24) | (red << 16) | (green << 8) | blue;
      const normalizedValue = ((colorValue >> 16) & 0xff) / 255;
      colorArray.push(normalizedValue);
      if (colorArray.length == 28) {
        // FORMAT ARRAY INTO IMAGE MATRIX
        image.push(colorArray);
        colorArray = [];
      }
    }

    // GET DIGIT PREDICTION
    fetch('/api/predict', {
      method: 'POST',
      body: JSON.stringify(image),
      headers: { 'Content-Type': 'application/json; charset=utf-8' },
    })
      .then((response) => response.text())
      .then((data) => {
        const digit = parseInt(data);
        if (!isNaN(digit)) {
          console.log(digit);
          emit('update:prediction', digit);
        }
      });
  });
</script>

<template>
  <div class="size-full">
    <div class="grid place-items-center">
      <div class="relative z-0">
        <VueDrawingCanvas
          ref="DrawingCanvas"
          v-model:image="image"
          :eraser="eraser"
          :lineWidth="lineWidth"
          :lock="false"
          :fill-shape="false"
          :styles="{ border: 'solid 1px #000' }"
          width="560"
          height="560"
          stroke-type="dash"
          line-cap="round"
          line-join="round"
          color="white"
          background-color="black"
          saveAs="png">
        </VueDrawingCanvas>
        <div v-if="!props.hasNetwork" class="absolute inset-0 flex justify-center items-center z-10 select-none">
          <Icon icon="lucide:ban" class="blur-none text-red-800 text-[240px] pointer-events-none outline-none" />
        </div>
      </div>

      <canvas id="output" class="hidden"></canvas>
      <CanvasControls :eraser="eraser" :hasNetwork="!props.hasNetwork" />
    </div>
  </div>
</template>
