<script>
  import VueDrawingCanvas from 'vue-drawing-canvas';
  import CanvasControls from './CanvasControls.vue';
  import NetworkControls from './NetworkControls.vue';

  export default {
    components: {
      VueDrawingCanvas,
      CanvasControls,
      NetworkControls,
    },
    data() {
      return {
        image: '',
        saveAs: 'png',
        eraser: false,
        line: 44,
        color: '#FFFFFF',
        strokeType: 'dash',
        lineCap: 'round',
        lineJoin: 'round',
        backgroundColor: '#000000',
      };
    },
    watch: {
      image: () => {
        // RE-DRAW IMAGE, DOWNSCALED TO [28] x [28] PIXELS
        const canvas = document.getElementById('VueDrawingCanvas');
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
            console.log(data);
          });
      },
    },
  };
</script>

<template>
  <div class="flex flex-row">
    <div>
      <div class="flex flex-row gap-2">
        <div>
          <vue-drawing-canvas
            ref="VueCanvasDrawing"
            v-model:image="image"
            :width="560"
            :height="560"
            :stroke-type="strokeType"
            :line-cap="lineCap"
            :line-join="lineJoin"
            :fill-shape="false"
            :eraser="eraser"
            :lineWidth="line"
            :color="color"
            :background-color="backgroundColor"
            :saveAs="saveAs"
            :styles="{ border: 'solid 1px #000' }"
            :lock="false" />
        </div>

        <canvas id="output" class="hidden"></canvas>
      </div>
      <CanvasControls v-model:eraser="eraser" />
      <NetworkControls />
    </div>
  </div>
</template>
