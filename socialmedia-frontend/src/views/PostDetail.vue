<template>
  <v-container fluid>
    <v-btn text @click="$router.back()">← Back</v-btn>

    <v-card class="my-4" v-if="post">
      <v-card-title>{{ post.title }}</v-card-title>
      <v-card-subtitle>
        {{ formatDate(post.created_on) }}
      </v-card-subtitle>
      <v-card-text>{{ post.content }}</v-card-text>
    </v-card>

    <h2>Comments</h2>
    <v-list two-line v-if="comments.length">
      <v-list-item
          v-for="c in comments"
          :key="c.idcomment"
      >
        <v-list-item-content>
          <v-list-item-title>{{ c.user.name }}</v-list-item-title>
          <v-list-item-subtitle>
            {{ formatDate(c.createOn || c.create_on) }}
          </v-list-item-subtitle>
          <div>{{ c.content }}</div>
        </v-list-item-content>
      </v-list-item>
    </v-list>
    <div v-else>No comments yet</div>

    <v-divider class="my-4"></v-divider>

    <v-textarea
        v-model="newComment"
        label="Add a comment"
        rows="2"
    />
    <v-btn
        color="primary"
        class="mt-2"
        :loading="posting"
        @click="postComment"
    >
      Post Comment
    </v-btn>

    <v-alert v-if="error" type="error" class="mt-4">{{ error }}</v-alert>
  </v-container>
</template>

<script>
import axios from 'axios';

export default {
  name: 'PostDetail',
  props: ['id'],
  data() {
    return {
      post: null,
      comments: [],
      newComment: '',
      loading: false,
      posting: false,
      error: ''
    };
  },
  mounted() {
    this.loadAll();
  },
  methods: {
    async loadAll() {
      this.loading = true;
      const token = localStorage.getItem('jwt');
      try {
        const [pRes, cRes] = await Promise.all([
          axios.get(`/posts/${this.id}`, {
            headers: { Authorization: `Bearer ${token}` }
          }),
          axios.get(`/posts/${this.id}/comments`, {
            headers: { Authorization: `Bearer ${token}` }
          })
        ]);
        this.post = pRes.data;
        this.comments = cRes.data;
      } catch (err) {
        console.error(err);
        this.error = 'Failed to load post or comments.';
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
      const dd  = String(d.getDate()).padStart(2,'0');
      const mm  = String(d.getMonth()+1).padStart(2,'0');
      const yy  = String(d.getFullYear()%100).padStart(2,'0');
      const hh  = String(d.getHours()).padStart(2,'0');
      const min = String(d.getMinutes()).padStart(2,'0');
      return `${dd}/${mm}/${yy} ${hh}:${min}`;
    },
    async postComment() {
      if (!this.newComment) return;
      this.posting = true;
      this.error = '';
      const token = localStorage.getItem('jwt');
      const user = JSON.parse(localStorage.getItem('currentUser'));
      try {
        await axios.post(
            `/posts/${this.id}/comments`,
            { userId: user.id, content: this.newComment },
            { headers: { Authorization: `Bearer ${token}` } }
        );
        this.newComment = '';
        await this.loadAll();
      } catch (err) {
        console.error(err);
        this.error = 'Failed to post comment.';
      } finally {
        this.posting = false;
      }
    }
  }
};
</script>
