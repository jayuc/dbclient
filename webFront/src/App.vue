<template>
  <div id="app">
    <span>
      <div class="header"></div>
    </span>
    <span>
      <router-view/>
    </span>
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
  .header{
    height: 20px;
    background: black;
  }
</style>
