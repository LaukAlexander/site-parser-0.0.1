<template>
  <div>
    <h1>Hard Parsing</h1>
      <input v-model="uri">
      <button @click="sentToParse">sent</button>
      <p>{{this.parseResult}}</p>
  </div>
</template>

<script>
import axios from "axios";

export default {
    name: 'HardParse',
    data() {
        return {
            uri: '',
            parseResult: '',
            backUrl: 'http://localhost:15151'
        }
    },
    methods: {
        sentToParse() {
            axios.get(this.backUrl + '/main/parse/hard?uri=' + this.uri).then(response => this.parseResult = response);
            console.log(this.parseResult);
            console.log(this.backUrl);
        }
    }
}
</script>
