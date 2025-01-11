<template>
  <v-dialog v-model="showDialog" max-width="400px" class="bg-transparent">
    <div class="dialogClass">
      Welcome, {{currentUser}}!
      <v-text-field
          label="Post title"
          v-model="post.title"
      ></v-text-field>
      <v-textarea
          label="Post content"
          v-model="post.content"
      ></v-textarea>
      <v-btn color="green" @click="createPost">Create</v-btn>
      <v-btn color="red" @click="closeDialog">Cancel</v-btn>
    </div>
  </v-dialog>
</template>

<script>
import axios from 'axios';

export default {

  name: 'posts',
  props: {
    currentUser: String
  },
  data() {
    return {
      showDialog: Boolean,
      post: {
        title: "",
        content: "",
      }
    }
  },
  methods: {
    async createPost() {
      try {
        const response = await axios.post('http://localhost:8082/post', this.post)
        console.log("post created successfully:", response.data);
        this.showDialog = false
      } catch (error) {
        console.error("Error creating post:", error);
      }
    },
    closeDialog() {
      this.showDialog = false;
      this.post = { title: "", content: "" };
    },
  }
};
</script>

<style scoped>
.dialogClass {
  padding: 20px;
  background-color: white;
}
</style>