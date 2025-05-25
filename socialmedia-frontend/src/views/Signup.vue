<template>
  <v-container class="fill-height" fluid>
    <v-row align="center" justify="center">
      <v-col cols="12" sm="8" md="4">
        <v-card>
          <v-card-title class="text-h5">Sign Up</v-card-title>
          <v-card-text>
            <!-- Display errors here -->
            <v-alert
                v-if="error"
                type="error"
                dense
                text
                class="mb-4"
            >
              {{ error }}
            </v-alert>

            <v-form ref="form" @submit.prevent="submit">
              <v-text-field
                  v-model="name"
                  label="Name"
                  required
              />
              <v-text-field
                  v-model="email"
                  label="Email"
                  type="email"
                  required
              />
              <v-text-field
                  v-model="password"
                  label="Password"
                  type="password"
                  required
              />
              <v-text-field
                  v-model="confirm"
                  label="Confirm Password"
                  type="password"
                  :rules="[matchPassword]"
                  required
              />
              <v-btn
                  :disabled="!valid"
                  type="submit"
                  color="primary"
                  class="mt-4"
                  block
              >
                Create Account
              </v-btn>
            </v-form>
          </v-card-text>
          <v-card-actions>
            <v-spacer/>
            <v-btn text @click="$router.push('/login')">
              Already have an account?
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  name: 'Signup',
  data() {
    return {
      name: '',
      email: '',
      password: '',
      confirm: '',
      error: ''          // <-- track any API error here
    };
  },
  computed: {
    valid() {
      return (
          this.name &&
          this.email &&
          this.password &&
          this.confirm === this.password
      );
    },
    matchPassword() {
      return v =>
          v === this.password || 'Passwords must match';
    },
  },
  methods: {
    async submit() {
      this.error = '';
      try {
        const resp = await fetch('/users/signup', {
          method: 'POST',
          headers: {'Content-Type': 'application/json'},
          body: JSON.stringify({
            name: this.name,
            email: this.email,
            password: this.password
          })
        });

        if (resp.status === 201) {
          this.$router.push('/login');
          return;
        } else {
          // try to read an error message from body
          const text = await resp.text();
          this.error = text || `Signup failed (${resp.status})`;
        }
      } catch (err) {
        this.error = err.message;
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
