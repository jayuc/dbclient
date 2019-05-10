<template>
  <div id="app">
    <span>
      <div class="__app-header__"></div>
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
  .__app-header__{
    height: 10px;
    background: black;
  }
</style>
