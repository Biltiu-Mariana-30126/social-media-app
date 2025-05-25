<template>
  <v-dialog v-model="showDialog" max-width="400px">
    <v-card>
      <v-card-title>
        <span class="headline">New Post</span>
        <v-spacer/>
      </v-card-title>

      <v-card-text>
        <!-- Show any error here -->
        <v-alert
            v-if="error"
            type="error"
            dense
            text
            class="mb-4"
        >
          {{ error }}
        </v-alert>

        <v-text-field
            v-model="post.title"
            label="Post title"
            required
        />
        <v-textarea
            v-model="post.content"
            label="Post content"
            required
        />
      </v-card-text>

      <v-card-actions>
        <v-spacer/>
        <v-btn
            color="green darken-1"
            text
            @click="createPost"
            :loading="loading"
            :disabled="loading"
        >
          Create
        </v-btn>
        <v-btn
            color="red darken-1"
            text
            @click="closeDialog"
            :disabled="loading"
        >
          Cancel
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import axios from 'axios';

export default {
  name: 'AddPost',
  props: {
    currentUser: {
      type: Object,
      required: true
    },
    modelValue: {
      type: Boolean,
      default: false
    }
  },
  emits: ['update:modelValue', 'postCreated'],
  data() {
    return {
      post: {
        title: '',
        content: ''
      },
      loading: false,
      error: ''
    };
  },
  computed: {
    showDialog: {
      get() {
        return this.modelValue;
      },
      set(val) {
        this.$emit('update:modelValue', val);
      }
    }
  },
  methods: {
    async createPost() {
      this.error   = '';
      this.loading = true;

      try {
        const token = localStorage.getItem('jwt');
        if (!token) throw new Error('Not authenticated');

        // Only send title+content—no userId
        const payload = {
          title:   this.post.title,
          content: this.post.content
        };

        const { data } = await axios.post(
            '/posts',
            payload,
            { headers: { Authorization: `Bearer ${token}` } }
        );

        this.$emit('postCreated', data);
        this.resetPost();
        this.showDialog = false;

      } catch (err) {
        console.error('Error creating post:', err);
        if (err.response) {
          const msg = err.response.data?.message
              || err.response.data
              || err.response.statusText;
          this.error = `Error ${err.response.status}: ${msg}`;
        } else {
          this.error = err.message;
        }
      } finally {
        this.loading = false;
      }
    },

    closeDialog() {
      this.resetPost();
      this.error = '';
      this.showDialog = false;
    },

    resetPost() {
      this.post.title = '';
      this.post.content = '';
    }
  }
};
</script>

<style scoped>
.v-card {
  padding: 16px;
}

.mb-4 {
  margin-bottom: 1rem;
}
</style>
