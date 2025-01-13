<template>
  <v-app>
    <!-- Navbar -->
    <v-app-bar app>
      <v-toolbar-title>Social Media App</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn text @click="goToDashboard">Dashboard</v-btn>
      <v-btn text @click="goToLogin">Login</v-btn>
    </v-app-bar>

    <!-- Main content -->
    <router-view />
  </v-app>
</template>

<script>
export default {
  name: 'App',
  methods: {
    goToDashboard() {
      this.$router.push('/');
    },
    goToLogin() {
      this.$router.push('/login');
    }
  }
};
</script>
