<template>
  <v-container fluid>
    <!-- Error banner -->
    <v-alert v-if="error" type="error" dense text class="mb-4">
      {{ error }}
    </v-alert>

    <!-- Posts grid -->
    <v-row>
      <v-col
          v-for="post in posts"
          :key="post.id"
          cols="12" sm="6" md="4"
      >
        <router-link
            :to="{ name: 'PostDetail', params: { id: post.id } }"
            class="text-decoration-none"
        >
          <v-card class="hoverable">
            <v-card-title>{{ post.title }}</v-card-title>
            <v-card-subtitle>
              {{ formatDate(post.created_on) }}
            </v-card-subtitle>
            <v-card-text>{{ post.content }}</v-card-text>
          </v-card>
        </router-link>
      </v-col>
    </v-row>

    <!-- Loading dialog -->
    <v-dialog v-model="loading" persistent width="300">
      <v-card>
        <v-card-text class="text-center">
          <v-progress-circular indeterminate size="40"></v-progress-circular>
          <div class="mt-2">Loading posts…</div>
        </v-card-text>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import axios from 'axios';

export default {
  name: 'PostsView',
  data() {
    return {
      posts: [],
      loading: false,
      error: ''
    };
  },
  created() {
    this.fetchPosts();
  },
  methods: {
    async fetchPosts() {
      this.loading = true;
      this.error = '';
      try {
        const token = localStorage.getItem('jwt');
        console.log('Fetching posts with JWT:', token);
        const response = await axios.get('/posts', { headers: { Authorization: `Bearer ${token}`  }});
        this.posts = response.data.map(p => ({
          ...p,
          createdOn: p.created_on
        }));
        this.posts = response.data;
      } catch (err) {
        console.error('Error fetching posts response:', err.response);
        if (err.response) {
          this.error = `Error ${err.response.status}: ${err.response.data || err.response.statusText}`;
        } else {
          this.error = `Network error: ${err.message}`;
        }
      } finally {
        this.loading = false;
      }
    },

    formatDate(dbString) {
      if (!dbString) return '';
      const [main] = dbString.split('.');
      const iso = main.replace(' ', 'T');
      const d = new Date(iso);
      if (isNaN(d)) return dbString;

      const dd  = String(d.getDate()).padStart(2, '0');
      const mm  = String(d.getMonth() + 1).padStart(2, '0');
      const yy  = String(d.getFullYear() % 100).padStart(2, '0');
      const hh  = String(d.getHours()).padStart(2, '0');
      const min = String(d.getMinutes()).padStart(2, '0');

      return `${dd}/${mm}/${yy} ${hh}:${min}`;
    }
  }
};
</script>

<style scoped>
.mb-4 {
  margin-bottom: 1rem;
}
</style>
