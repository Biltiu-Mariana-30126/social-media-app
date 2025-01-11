<template>
  <v-container>
    <h2 class="text-h5">My posts</h2>
    <v-btn text="load posts" @click="fetchPosts"></v-btn>
    <v-list>
      <v-list-item
          class="postClass"
          variant="outlined"
          v-for="post in posts"
          :key="post.id"
          :subtitle="'posted on ' + post.createdOn"
          :title="post.title"
      >
        {{post.content}}
      </v-list-item>
    </v-list>
  </v-container>
</template>


<script>
import axios from 'axios';

export default {
  name: 'posts',
  data() {
    return {
      posts: []
    };
  },
  methods: {
    async fetchPosts() {
      try {
        const response = await axios.get('http://localhost:8082/post');
        this.posts = response.data;
      } catch (error) {
        console.error("Error fetching posts:", error);
      }
    }
  }
};
</script>

<style scoped>
.postClass {
  margin-bottom: 10px;
}
</style>