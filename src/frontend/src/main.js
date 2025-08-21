import { createApp } from 'vue';
import App from './App.vue';
import './main.css';

// BEFORE PAGE LOAD FETCH THE NETWORK, IF ANY
fetch('/api/hasNetwork', { method: 'POST' })
    .then((response) => response.text())
    .then((data) => {
        createApp(App, { hasNetwork: data }).mount("#app");
    })
    .catch((error) => console.error(error));
