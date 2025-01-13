<template>
  <v-app>
    <v-main>
      <!-- Posts View and Button to Create Post -->
      <div style="max-width: 1000px; margin-left: 200px">
        <!-- Posts View will fetch and display posts -->
        <posts-view />

        <v-btn style="align-content: center" @click="openDialog">
          Create new post
        </v-btn>
      </div>

      <!-- Dialog for creating a post -->
      <add-post
          :currentUser="username"
          ref="addPostDialog"
      ></add-post>
    </v-main>
  </v-app>
</template>

<script>
import PostsView from "@/components/PostsView.vue";
import AddPost from "@/components/AddPost.vue";

export default {
  name: "App",

  components: {
    PostsView,
    AddPost
  },

  data() {
    return {
      username: "Nume utilizator"
    };
  },

  methods: {
    openDialog() {
      // Show the AddPost dialog when clicked
      this.$refs.addPostDialog.showDialog = true;
    }
  }
};
</script>