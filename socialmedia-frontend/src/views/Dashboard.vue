<template>
  <v-container>
    <v-row align="center" justify="space-between" class="mb-4">
      <v-col cols="auto">
        <h1 v-if="currentUser">Welcome, {{ currentUser.name }}!</h1>
      </v-col>
      <v-col cols="auto">
        <v-btn color="secondary" @click="$router.push('/2fa/setup')">
          Enable Two-Factor Auth
        </v-btn>
        <v-btn color="primary" class="ml-2" @click="showAdd = true">
          Create New Post
        </v-btn>
      </v-col>
    </v-row>

    <posts-view ref="postsView" />

    <add-post
        v-model="showAdd"
        :currentUser="currentUser"
        @postCreated="onPostCreated"
    />
  </v-container>
</template>

<script>
import axios from 'axios';
import PostsView from '@/components/PostsView.vue';
import AddPost from '@/components/AddPost.vue';

export default {
  name: 'Dashboard',
  components: { PostsView, AddPost },
  props: {
    currentUser: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      posts: [],
      loading: true,
      showAdd: false
    };
  },
  created() {
    this.fetchPosts();
  },
  methods: {
    async fetchPosts() {
      this.loading = true;
      try {
        const token = localStorage.getItem('jwt');
        const response = await axios.get('/posts', {
          headers: { Authorization: `Bearer ${token}` }
        });
        this.posts = response.data;
      } catch (error) {
        console.error('Error fetching posts:', error);
      } finally {
        this.loading = false;
      }
    },

    onPostCreated() {
      this.showAdd = false;
      this.$refs.postsView.fetchPosts();
    }
  }
};
</script>

<style scoped>
.mb-4 {
  margin-bottom: 1rem;
}
</style>
