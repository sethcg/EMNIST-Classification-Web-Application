<script setup>
import { ref } from 'vue'

let isActive = ref(false);
const handleDrag = (event) => {
    switch (event.buttons) {
        // LEFT MOUSE CLICK
        case 1:
            isActive.value = true;
            return;
        // RIGHT MOUSE CLICK
        case 2:
            isActive.value = false;
            return;
        default:
            return;
    }
}

</script>

<template>
    <li :class="{ 'active': isActive }" class="select-none grid-button" @mouseover.prevent="handleDrag($event)">
    </li>
</template>

<style scoped>
    .active {
        background-color: rgb(255, 128, 0) !important;
    }

    .grid-button {
        background-color: rgb(120, 120, 120);
        width: 25px;
        height: 25px;

        padding: 0;
        border-radius: 0;
        border: 1px solid black;
    }
    :hover.grid-button {
        background-color: rgb(92, 92, 92);
    }
</style>
