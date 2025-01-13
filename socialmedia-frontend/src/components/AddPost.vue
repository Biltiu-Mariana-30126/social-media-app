<template>
  <v-dialog v-model="showDialog" max-width="400px" class="bg-transparent">
    <div class="dialogClass">
      Welcome, {{ currentUser }}!
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
  name: 'AddPost',
  props: {
    currentUser: String, // You could use the username or ID of the logged-in user
  },
  data() {
    return {
      showDialog: false, // Dialog visibility
      post: {
        title: "", // Initialize title as an empty string
        content: "", // Initialize content as an empty string
      },
    };
  },
  methods: {
    // Method to create a post
    async createPost() {
      try {
        // Send the POST request to the backend to create a post
        const response = await axios.post('http://localhost:8082/posts', this.post);
        console.log("Post created successfully:", response.data);

        // Refresh the page
        window.location.reload();
        this.showDialog = false; // Close the dialog after successful creation
        this.resetPost(); // Optionally reset the post data for next use
      } catch (error) {
        console.error("Error creating post:", error);
      }
    },
    // Method to close the dialog
    closeDialog() {
      this.showDialog = false;
      this.resetPost(); // Reset post when dialog is closed
    },
    // Method to reset the post data
    resetPost() {
      this.post = {
        title: "",
        content: "",
      };
    },
  },
};
</script>

<style scoped>
.dialogClass {
  padding: 20px;
  background-color: white;
}
</style>
