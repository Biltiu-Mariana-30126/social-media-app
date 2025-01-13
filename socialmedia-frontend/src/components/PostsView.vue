<template>
  <v-container>
    <v-row>
      <v-col v-for="post in posts" :key="post.id" cols="12" sm="6" md="4">
        <v-card>
          <v-card-title>{{ post.title }}</v-card-title>
          <v-card-subtitle>{{ post.created_on }}</v-card-subtitle>
          <v-card-text>{{ post.content }}</v-card-text>
        </v-card>
      </v-col>
    </v-row>

    <!-- Loading indicator -->
    <v-dialog v-model="loading" persistent>
      <v-card>
        <v-card-title class="headline">Loading Posts...</v-card-title>
        <v-card-actions>
          <v-btn text @click="loading = false">Close</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
// Import Axios
import axios from 'axios';

export default {
  name: "PostsView",

  data() {
    return {
      posts: [], // Array to store fetched posts
      loading: true, // Flag to show loading dialog
    };
  },

  created() {
    this.fetchPosts();
  },

  methods: {
    async fetchPosts() {
      try {
        const response = await axios.get("http://localhost:8082/posts"); // Make GET request to backend
        this.posts = response.data; // Store the posts in the data property
      } catch (error) {
        console.error("Error fetching posts:", error);
      } finally {
        this.loading = false; // Hide loading dialog once posts are fetched
      }
    }
  }
};
</script>