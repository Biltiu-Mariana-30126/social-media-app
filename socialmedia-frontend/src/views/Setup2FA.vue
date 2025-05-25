<template>
  <v-container>
    <v-btn @click="fetchQr">Generate QR</v-btn>
    <div v-if="qr">
      <img :src="`data:image/png;base64,${qr}`" />
      <p><strong>Manual key:</strong> {{ secret }}</p>
      <v-text-field
          v-model="code"
          label="Enter code to confirm"
          maxlength="6"
          required
      />
      <v-btn color="primary" @click="confirm">Confirm</v-btn>
    </div>
    <v-alert v-if="error" type="error">{{ error }}</v-alert>
  </v-container>
</template>

<script>
export default {
  name: 'Setup2FA',
  data: () => ({
    qr: '',
    secret: '',
    code: '',
    error: ''
  }),
  methods: {
    parseJwt(token) {
      try {
        return JSON.parse(atob(token.split('.')[1]))
      } catch {
        return {}
      }
    },

    async fetchQr() {
      this.error = ''
      const jwt = localStorage.getItem('jwt')
      if (!jwt) return (this.error = 'Not authenticated')

      const { userId } = this.parseJwt(jwt)
      if (!userId) return (this.error = 'Cannot determine user ID')

      const resp = await fetch(`/users/${userId}/2fa/setup`, {
        method: 'POST',
        headers: { Authorization: `Bearer ${jwt}` }
      })
      if (!resp.ok) {
        this.error = `Setup failed (${resp.status})`
        return
      }
      const { qr, secret } = await resp.json()
      this.qr = qr
      this.secret = secret
    },

    async confirm() {
      const jwt = localStorage.getItem('jwt')
      if (!jwt) {
        this.error = 'Not authenticated—you must log in first.'
        return
      }

      const resp = await fetch('/users/2fa/confirm', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${jwt}`    // ← this must be the full JWT, not the pre2faToken
        },
        body: JSON.stringify({ code: this.code })
      })

      if (resp.status === 200) {
        // success!
        this.$router.push('/')
      } else if (resp.status === 400) {
        this.error = 'Invalid 2FA code'
      } else if (resp.status === 401) {
        this.error = 'Unauthorized—invalid or expired token'
      } else {
        this.error = `Error (${resp.status})`
      }
    }

  }
}
</script>
