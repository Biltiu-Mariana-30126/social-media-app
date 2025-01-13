<template>
  <v-container>
    <v-form @submit.prevent="submitForm">
      <v-text-field
          v-model="email"
          label="Enter your email"
          type="email"
          required
      ></v-text-field>
      <v-btn type="submit" color="primary">Login</v-btn>
    </v-form>
  </v-container>
</template>

<script>
export default {
  name: 'Login',
  data() {
    return {
      email: ''
    };
  },
  methods: {
    submitForm() {
      if (this.email) {
        // Here you can handle the login logic (e.g., storing user info, sending API request)
        this.$router.push('/'); // Redirect to Dashboard after successful login
      }
    }
  }
};
</script>
