<script setup>
import { ref } from 'vue'
import Cell from './Cell.vue';

const items = ref([...Array(784).fill(0)]);

</script>

<template>
    <ul class="select-none grid grid-cols-28 grid-rows-28 border-[1px] border-black">
        <Cell v-for="index in items" :key="index" />
    </ul>
</template>

<style scoped>

</style>
