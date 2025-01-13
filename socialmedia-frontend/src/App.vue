<template>
  <v-app>
    <v-app-bar app>
      <v-toolbar-title>My Vuetify App</v-toolbar-title>
      <v-spacer></v-spacer>
      <!-- Use to attribute for navigation -->
      <!-- If logged in, show the Dashboard with current user -->
      <template v-if="currentUser">
        <v-btn text to="/dashboard">Dashboard</v-btn>
        <v-btn text @click="logout">Logout</v-btn>
      </template>

      <!-- If not logged in, show login button -->
      <template v-else>
        <v-btn text to="/login">Login</v-btn>
      </template>
    </v-app-bar>

    <!-- Your app content goes here -->
    <v-main>
      <!-- Content -->
      <router-view :currentUser="currentUser" />
    </v-main>
  </v-app>
</template>

<script>
export default {
  name: 'App',

  data() {
    return {
      currentUser: null // Default state for currentUser
    };
  },

  methods: {
    setCurrentUser(user) {
      this.currentUser = user; // Set the logged-in user
    },

    logout() {
      // Remove the user from localStorage
      localStorage.removeItem('currentUser');
      this.currentUser = null; // Reset the currentUser data
      this.$router.push('/login'); // Redirect to login
    }
  },

  created() {
    // Check for a logged-in user when the app loads
    const user = localStorage.getItem('currentUser');
    if (user) {
      this.currentUser = JSON.parse(user);
    }
  }
};
</script>
