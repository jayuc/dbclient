<template>
  <div class="publishIssueIndexMain">
    <div class="title">
      问题反馈：
    </div>
    <div class="header">
      <el-button type="primary"
                 @click="openIssue"
                 size="mini"
      >
        新 增
      </el-button>
      <el-button size="mini"
                 type="info"
                 style="float: right"
                 @click="gotoHome"
      >
        返 回
      </el-button>
    </div>
    <div ref="contentWrapper">
      <item v-for="item in list"
            v-bind:data="item"
            v-bind:key="item.index"
      />
    </div>
    <publish-issue-dialog ref="dialog"
                          v-on:publish-dialog="publishDialog"
    />
  </div>
</template>

<script>
    import PublishIssueDialog from './publishIssueDialog';
    import AjaxUtil from '@/utils/AjaxUtil';
    import $ from 'jquery';
    import Item from './item';

    export default {
      name: "publish-issue-index",
      data(){
        return {
          list: [],
        }
      },
      methods: {
        openIssue(){
          this.$refs.dialog.open();
        },
        publishDialog(data){
          //console.log(data);
          let item = data;
          item.index = this.list.length + 1;
          this.list.push(item);
        },
        gotoHome(){
          // 跳转到登陆页
          this.$router.push("/");
        }
      },
      components: {
        PublishIssueDialog,
        Item,
      },
      mounted() {
        let contentWrapper = this.$refs.contentWrapper;
        let that = this;
        //console.log(contentWrapper);
        //console.log(this.list);
        AjaxUtil.get('feedback/getList').then((data) => {
          //console.log(data);
          if(data.result.total === 0){
            $(contentWrapper).append('<div style="margin-top: 10px;color: #909399">暂无数据</div>')
          }else{
            that.list = data.result.rows;
          }
        });
      }
    }
</script>

<style>
  .publishIssueIndexMain{
    margin: 10px 20px;
  }
  .publishIssueIndexMain .header{
    padding-bottom: 5px;
    border-bottom: 1px solid #409EFF;
  }
  .publishIssueIndexMain .title{
    padding: 0 0 5px 0;
    font-size: 16px;
  }
</style>
