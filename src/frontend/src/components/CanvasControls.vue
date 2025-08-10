<script setup>
  import { ref } from 'vue';
  import { Icon } from '@iconify/vue';

  const emit = defineEmits(['update:eraser']);
  const props = defineProps({
    eraser: {
      type: Boolean,
      required: true,
    },
    hasNetwork: {
      type: Boolean,
      required: true,
    },
  });

  const isDrawActive = ref(true);
  const isEraseActive = ref(false);

  const updateEraser = (event) => {
    const enableErase = event.target.id === 'EraseButton';
    isDrawActive.value = !enableErase;
    isEraseActive.value = enableErase;
    emit('update:eraser', enableErase);
  };
</script>

<template>
  <div class="flex flex-row justify-self-start gap-2 my-2">
    <button class="flex flex-row items-center py-2 px-4" @click.prevent="$parent.$refs.DrawingCanvas.undo()">
      <Icon icon="lucide:undo-2" class="text-[24px] pointer-events-none outline-none" />
      <span class="hidden">UNDO</span>
    </button>

    <button class="flex flex-row items-center py-2 px-4" @click.prevent="$parent.$refs.DrawingCanvas.redo()">
      <Icon icon="lucide:redo-2" class="text-[24px] pointer-events-none outline-none" />
      <span class="hidden">REDO</span>
    </button>

    <button id="DrawButton" class="flex flex-row items-center py-2 px-4" :class="{ active: isDrawActive }" @click.prevent="updateEraser($event)">
      <Icon icon="lucide:pencil-line" class="text-[24px] pointer-events-none outline-none" />
      <span class="hidden">DRAW</span>
    </button>

    <button id="EraseButton" class="flex flex-row items-center py-2 px-4" :class="{ active: isEraseActive }" @click.prevent="updateEraser($event)">
      <Icon icon="lucide:eraser" class="text-[24px] pointer-events-none outline-none" />
      <span class="hidden">ERASE</span>
    </button>

    <button class="flex flex-row items-center py-2 px-4" @click.prevent="$parent.$refs.DrawingCanvas.reset()">
      <Icon icon="lucide:trash-2" class="text-[24px] pointer-events-none outline-none" />
      <span class="hidden">RESET</span>
    </button>
  </div>
</template>

<style scoped>
  button {
    border-radius: 8px;
    border: 2px solid transparent;
    background-color: var(--color-neutral-900);
    cursor: pointer;
    transition: border-color 0.25s;
    opacity: 1;
  }
  button.active {
    border-color: var(--color-lime-600);
  }
  /* button.disabled {
    opacity: 0.67;
    cursor: default;
  } */
  button:hover:not(.active):and(.editable) {
    border-color: var(--color-teal-700);
  }
</style>
