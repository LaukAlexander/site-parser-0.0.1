<template>
    This is site-parser!
    <SimpleParse/>
    <HardParse/>
</template>

<script>
import SimpleParse from './components/SimpleParse.vue'
import HardParse from './components/HardParse.vue'

export default {
    name: 'App',
    components: {
        SimpleParse,
        HardParse
    }
}
</script>
