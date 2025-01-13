import { createApp } from 'vue';
import App from './App.vue';
import { createVuetify } from 'vuetify';
import 'vuetify/styles';
import router from "@/router"; // import the styles

// createVuetify is used in Vuetify 3
const vuetify = createVuetify();

const app = createApp(App);

// Use Vuetify
app.use(vuetify);
app.use(router);

// Mount the app
app.mount('#app');