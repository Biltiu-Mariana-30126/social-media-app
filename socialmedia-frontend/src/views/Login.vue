<template>
  <v-container>
    <v-form @submit.prevent="submitForm">
      <v-text-field
          v-model="email"
          label="Email"
          type="email"
          required
      ></v-text-field>
      <v-text-field
          v-model="password"
          label="Password"
          type="password"
          required
      ></v-text-field>
      <v-btn type="submit" color="primary">Login</v-btn>
    </v-form>
  </v-container>
</template>

<script>
export default {
  data() {
    return {
      email: '',
      password: '',
      error: ''
    };
  },
  methods: {
    async submitForm() {
      this.error = '';
      try {
        const resp = await fetch('/users/login', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            email: this.email,
            password: this.password
          })
        });

        if (resp.status === 202) {
          // 2FA is enabled…
          const { pre2faToken } = await resp.json();
          this.$router.push({
            path: '/2fa',
            query: { pre2faToken }
          });
          return;
        }

        if (resp.ok) {
          // no 2FA → final JWT + user
          const { token, user } = await resp.json();

          // ✨ Store both in localStorage
          localStorage.setItem('jwt', token);
          localStorage.setItem('currentUser', JSON.stringify(user));

          // and let App.vue pick it up
          this.$root.currentUser = user;

          this.$router.push('/');
          return;
        }

        if (resp.status === 401) {
          this.error = 'Invalid email or password.';
        } else {
          this.error = `Login failed (${resp.status})`;
        }
      } catch (err) {
        this.error = `Network error: ${err.message}`;
      }
    }
  }
};
</script>