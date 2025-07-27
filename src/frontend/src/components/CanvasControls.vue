<script>
import { defineComponent, ref } from 'vue'
import { Icon } from '@iconify/vue'

const isDrawActive = ref(true)
const isEraseActive = ref(false)

export default defineComponent({
  components: {
    Icon,
  },
  props: {
    eraser: {
      type: Boolean,
      required: true,
    },
  },
  emits: ['update:eraser'],
  setup(_props, { emit }) {
    const updateEraser = event => {
      const enableErase = event.target.id === 'EraseButton'
      isDrawActive.value = !enableErase
      isEraseActive.value = enableErase
      emit('update:eraser', enableErase)
    }
    return { updateEraser }
  },
  data() {
    return {
      isDrawActive: isDrawActive,
      isEraseActive: isEraseActive,
    }
  },
})
</script>

<template>
  <div class="flex flex-row gap-2 my-2">
    <button class="flex flex-row items-center py-2 px-4" @click.prevent="$parent.$refs.VueCanvasDrawing.undo()">
      <Icon icon="mdi:undo-variant" class="text-[24px] pointer-events-none outline-none" />
      <span class="hidden">UNDO</span>
    </button>

    <button class="flex flex-row items-center py-2 px-4" @click.prevent="$parent.$refs.VueCanvasDrawing.redo()">
      <Icon icon="mdi:redo-variant" class="text-[24px] pointer-events-none outline-none" />
      <span class="hidden">REDO</span>
    </button>

    <button id="DrawButton" class="flex flex-row items-center py-2 px-4" v-bind:class="{ active: isDrawActive }" @click.prevent="updateEraser($event)">
      <Icon icon="mdi:pencil" class="text-[24px] pointer-events-none outline-none" />
      <span class="hidden">DRAW</span>
    </button>

    <button id="EraseButton" class="flex flex-row items-center py-2 px-4" v-bind:class="{ active: isEraseActive }" @click.prevent="updateEraser($event)">
      <Icon icon="mdi:eraser" class="text-[24px] pointer-events-none outline-none" />
      <span class="hidden">ERASE</span>
    </button>

    <button class="flex flex-row items-center py-2 px-4" @click.prevent="$parent.$refs.VueCanvasDrawing.reset()">
      <Icon icon="mdi:trash-can-empty" class="text-[24px] pointer-events-none outline-none" />
      <span class="hidden">RESET</span>
    </button>
  </div>
</template>

<style scoped>
button.active {
  border-color: var(--color-lime-600);
}
button {
  border-radius: 8px;
  border: 2px solid transparent;
  background-color: var(--color-slate-900);
  cursor: pointer;
  transition: border-color 0.25s;
}
button:hover:not(.active) {
  border-color: var(--color-teal-700);
}
</style>
