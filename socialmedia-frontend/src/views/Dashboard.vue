<template>
  <v-container>
    <h1 v-if="currentUser">Welcome, {{ currentUser.name }}!</h1>

    <v-row>
      <v-col v-for="post in posts" :key="post.id" cols="12" sm="6" md="4">
        <v-card>
          <v-card-title>{{ post.title }}</v-card-title>
          <v-card-subtitle>{{ post.created_on }}</v-card-subtitle>
          <v-card-text>{{ post.content }}</v-card-text>
        </v-card>
      </v-col>
    </v-row>

    <v-dialog v-model="loading" persistent>
      <v-card>
        <v-card-title class="headline">Loading Posts...</v-card-title>
        <v-card-actions>
          <v-btn text @click="loading = false">Close</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-btn @click="openDialog" style="align-content: center">
      Create new post
    </v-btn>

    <add-post :currentUser="username" ref="addPostDialog"></add-post>
  </v-container>
</template>

<script>
import axios from 'axios';
import AddPost from '@/components/AddPost.vue';

export default {
  name: 'Dashboard',
  components: {
    AddPost
  },
  data() {
    return {
      posts: [],
      loading: true,
      username: 'Nume utilizator'
    };
  },
  created() {
    this.fetchPosts();
  },
  methods: {
    async fetchPosts() {
      try {
        const response = await axios.get('http://localhost:8082/posts');
        this.posts = response.data;
      } catch (error) {
        console.error('Error fetching posts:', error);
      } finally {
        this.loading = false;
      }
    },
    openDialog() {
      this.$refs.addPostDialog.showDialog = true;
    }
  }
};
</script>
