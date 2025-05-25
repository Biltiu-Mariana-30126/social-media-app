<template>
  <v-container class="fill-height" fluid>
    <v-row align="center" justify="center">
      <v-col cols="12" sm="6" md="4">
        <v-card>
          <v-card-title class="text-h5">Two-Factor Authentication</v-card-title>
          <v-card-text>
            <v-alert
                v-if="error"
                type="error"
                dense
                text
                class="mb-4"
            >
              {{ error }}
            </v-alert>
            <v-form @submit.prevent="verify">
              <v-text-field
                  v-model="code"
                  label="Enter 6-digit code"
                  maxlength="6"
                  required
              />
              <v-btn
                  type="submit"
                  color="primary"
                  :loading="loading"
                  block
              >
                Verify
              </v-btn>
            </v-form>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  name: 'TwoFactor',
  data() {
    return {
      code: '',
      error: '',
      loading: false
    };
  },
  methods: {
    async verify() {
      this.error = '';
      this.loading = true;

      const pre2faToken = this.$route.query.pre2faToken;
      if (!pre2faToken) {
        this.error = 'Missing authentication token. Please log in again.';
        this.loading = false;
        return;
      }

      try {
        const resp = await fetch('/users/2fa/verify', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ pre2faToken, code: this.code })
        });

        if (resp.status === 401) {
          this.error = 'Invalid 2FA code.';
        } else if (!resp.ok) {
          this.error = `Unexpected error (${resp.status}). Please try again.`;
        } else {
          // 200 OK → { token, user }
          const { token, user } = await resp.json();

          // Store JWT + user in localStorage
          localStorage.setItem('jwt', token);
          localStorage.setItem('currentUser', JSON.stringify(user));

          // Immediately update App.vue’s currentUser
          this.$root.currentUser = user;

          // Redirect to dashboard/home
          this.$router.push('/');
        }
      } catch (err) {
        this.error = `Network error: ${err.message}`;
      } finally {
        this.loading = false;
      }
    }
  }
};
</script>

<style scoped>
.fill-height {
  min-height: 100vh;
}
</style>
