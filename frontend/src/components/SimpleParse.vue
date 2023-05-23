<template>
  <div>
    <h1>Simple Parsing</h1>
      <input v-model="uri">
      <button @click="sentToParse">sent</button>
      <pre>
          <code>{{ parseResult }}</code>
      </pre>
  </div>
</template>

<script>

export default {
    name: 'SimpleParse',
    data() {
        return {
            uri: '',
            parseResult: ''
        }
    },
    methods: {
        sentToParse() {
            this.axios.get(this.backUrl + 'main/parse/simple?uri=' + this.uri)
                .then(response => this.parseResult = response.data)
                .catch(error => this.parseResult = error.message);
            console.log(this.parseResult);
        }
    }
}
</script>
