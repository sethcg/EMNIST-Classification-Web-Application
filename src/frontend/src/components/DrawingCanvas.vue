<script>
import VueDrawingCanvas from 'vue-drawing-canvas'
import CanvasControls from './CanvasControls.vue'
import NetworkControls from './NetworkControls.vue'

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
      line: 25,
      color: '#FFFFFF',
      strokeType: 'dash',
      lineCap: 'round',
      lineJoin: 'round',
      backgroundColor: '#000000',
    }
  },
}
</script>

<template>
  <div class="flex flex-row">
    <div>
      <div class="flex flex-row gap-2">
        <div>
          <vue-drawing-canvas
            ref="VueCanvasDrawing"
            v-model:image="image"
            :width="520"
            :height="520"
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

        <div class="output">
          <img :src="image" style="border: solid 1px #000000" width="28" height="28" />
        </div>
      </div>
      <CanvasControls v-model:eraser="eraser" />
      <NetworkControls />
    </div>
  </div>
</template>
