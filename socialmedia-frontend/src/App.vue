<template>
  <v-app>
    <v-app-bar app>
      <v-toolbar-title>My Vuetify App</v-toolbar-title>
      <v-spacer />

      <!-- If logged in, show Dashboard & Logout -->
      <template v-if="currentUser">
        <v-btn text to="/">Dashboard</v-btn>
        <v-btn text @click="logout">Logout</v-btn>
      </template>

      <!-- If not, show Login/Signup -->
      <template v-else>
        <v-btn text to="/login">Login</v-btn>
        <v-btn text to="/signup">Signup</v-btn>
      </template>
    </v-app-bar>

    <v-main>
      <!-- pass currentUser into every view -->
      <router-view :currentUser="currentUser" />
    </v-main>
  </v-app>
</template>

<script>
export default {
  name: 'App',
  data() {
    return {
      currentUser: null
    };
  },
  created() {
    const raw = localStorage.getItem('currentUser');
    if (raw) {
      this.currentUser = JSON.parse(raw);
    }
  },
  methods: {
    logout() {
      localStorage.removeItem('jwt');
      localStorage.removeItem('currentUser');
      this.currentUser = null;
      this.$router.push('/login');
    }
  }
};
</script>
