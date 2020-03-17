<template>
  <div>welcome{{ this.username }}</div>
</template>

<script>
export default {
  data() {
    return {
      username: ''
    }
  },
  mounted() {
    let Base64 = require('js-base64').Base64
    let token = JSON.parse(Base64.decode(localStorage.getItem('token')))
    this.username = token.username
  }
}
</script>

<style>
html {
  background-color: rgb(246, 241, 228);
}
</style>
