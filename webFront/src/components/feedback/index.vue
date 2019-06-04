<template>
  <div class="publishIssueIndexMain">
    <div class="header">
      <el-button type="primary"
                 @click="openIssue"
                 size="small"
      >
        新增
      </el-button>
    </div>
    <div>
      <p v-for="item in list"
         :pt="item.id"
         @click="getDetails"
      >
        {{item.index}}：{{item.title}}
      </p>
    </div>
    <publish-issue-dialog ref="dialog"
    />
  </div>
</template>

<script>
    import PublishIssueDialog from './publishIssueDialog';

    export default {
      name: "publish-issue-index",
      data(){
        return {
          list: [
            {title: '第一个问题', id: 'a', index: 1},
            {title: '第一个问题', id: 'v', index: 2},
          ]
        }
      },
      methods: {
        getDetails(e){
          console.log(e.target);
        },
        openIssue(){
          this.$refs.dialog.open();
        }
      },
      components: {
        PublishIssueDialog
      }
    }
</script>

<style>
  .publishIssueIndexMain{
    margin: 20px;
  }
  .publishIssueIndexMain .header{
    padding-bottom: 5px;
    border-bottom: 1px solid #409EFF;
  }
  .publishIssueIndexMain p{
    line-height: 16px;
    padding-bottom: 7px;
    border-bottom: 1px solid #EBEEF5;
    cursor: pointer;
  }
</style>
