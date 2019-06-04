<template>
    <el-dialog title="新增问题或建议"
               :visible.sync="visible"
               width="600px"
    >
      <el-form :model="formData"
               label-width="65px"
               ref="form"
               :rules="rules"
      >
        <el-form-item label="标题：" prop="title">
          <el-input v-model="formData.title" size="small"></el-input>
        </el-form-item>
        <el-form-item label="内容：" prop="content">
          <el-input v-model="formData.content"
                    type="textarea"
                    rows="10"
                    :draggable="textareaDrag"
          ></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="publish">提 交</el-button>
      </span>
    </el-dialog>
</template>

<script>
  import AjaxUtil from '@/utils/AjaxUtil';

    export default {
      name: "publishIssueDialog",
      data(){
        return {
          visible: false,
          textareaDrag: false,
          formData: {
            title: '',
            content: ''
          },
          rules: {
            title: [{required: true, message: '标题不能为空', trigger: 'blur'}],
            content: [{required: true, message: '内容不能为空', trigger: 'blur'}]
          }
        }
      },
      methods: {
        publish() {
          let param = this.formData;
          let that = this;
          this.$refs.form.validate((valid) => {
            if (valid) {   //验证成功
              AjaxUtil.post('feedback/publish', param).then((data) => {
                console.log(data);
              });
            }
          });
          console.log(this.formData);
          //this.close();
        },
        cancel(){
          this.close();
        },
        close(){
          this.resetFrom();
          this.visible = false;
        },
        open(){
          this.visible = true;
        },
        resetFrom(){  //重置表单
          this.$refs.form.resetFields();
        },
      }
    }
</script>

<style scoped>

</style>
