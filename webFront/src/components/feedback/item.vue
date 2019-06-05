<template>
    <div class="feedback-index-item">
      <div class="feedback-index-title"
           :title="titleTip"
           @click="showDetail"
      >
        <el-badge :is-dot="status">
          {{index}}：{{title}}&nbsp;
        </el-badge>
        <el-button type="mini"
                   style="position: absolute;top: -1px;right: 0"
                   v-show="status"
                   @click="solve"
        >
          解 决
        </el-button>
      </div>
      <div class="feedback-index-content"
           v-show="show"
      >
        {{content}}
      </div>
    </div>
</template>

<script>
    import AjaxUtil from '@/utils/AjaxUtil';

    export default {
      name: "feedback-main-item",
      data(){
        //console.log(this.$attrs);
        let data = this.$attrs.data;
        return {
          title: data.title,
          content: data.content,
          index: data.index,
          id: data.id,
          status: data.status !== '1',
          titleTip: data.status !== '1' ? '未解决' : '已解决',
          show: false,
        };
      },
      methods: {
        showDetail(e){
          let show = this.show;
          //console.log(show);
          //console.log(e.target.className);
          let targetClassName = e.target.className;
          if(targetClassName === 'feedback-index-title' || targetClassName === 'el-badge'){
            if(!show && !this.content){
              let that = this;
              let param = {
                id: this.id,
              };
              AjaxUtil.get('feedback/getFeedbackById', param).then((data) => {
                //console.log(data);
                let content = data.content;
                if(content && content.length > 0){
                  that.content = content;
                  that.show = true;
                }else{
                  that.$message.warning('无数据');
                }
              }, () => {
                that.$message.error('请求出错');
              });
              return;
            }
            this.show = !show;
          }
        },
        solve(){
          let that = this;
          let param = {
            id: this.id,
            status: '1'
          };
          AjaxUtil.post('feedback/modifyStatus', param).then((data) => {
            //console.log(data);
            if(data === 1){
              that.status = false;
              that.titleTip = '已解决';
            }else{
              that.$message.warning('无需要修改的状态');
            }
          }, () => {
            that.$message.error('请求出错');
          });
        }
      }
    }
</script>

<style scoped>
  .feedback-index-item{
    font-size: 12px;
    border-bottom: 1px solid #EBEEF5;
    padding: 7px 0 5px 0;
    line-height: 24px;
  }
  .feedback-index-title{
    line-height: 16px;
    padding: 4px 0;
    cursor: pointer;
    position: relative;
  }
  .feedback-index-content{
    text-indent: 1.5em;
    color: #606266;
  }
</style>
