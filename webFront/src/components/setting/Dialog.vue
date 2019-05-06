<template>
  <el-dialog title="设置"
             :visible.sync="visible"
             width="400px"
  >
    <el-form :model="formData"
             label-width="115px"
             :rules="rules"
             ref="the_form"
    >
      <el-form-item label="默认查询个数：" prop="limit">
        <el-input v-model.number="formData.limit"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="cancel">取 消</el-button>
      <el-button type="primary" @click="setting">设 置</el-button>
    </span>
  </el-dialog>
</template>

<script>
    import AjaxUtil from '@/utils/AjaxUtil';
    import ResultUtil from '@/utils/ResultUtil';

    export default {
      name: "setting-dialog",
      data(){
        return {
          visible: false,
          formData: {
            limit: 10
          },
          rules: {
            limit: [{type: 'number', message: '必须为数字', trigger: 'change'}]
          }
        }
      },
      methods: {
        cancel(){
          this.close();
        },
        open(){
          this.visible = true;
        },
        close(){
          this.visible = false;
        },
        setting(){
          this.$refs.the_form.validate((valid) => {
            if (valid) {   //验证成功
              let that = this;
              AjaxUtil.post('user/setting/doset', {limit: this.formData.limit}).then((data) => {
                //console.log(data);
                ResultUtil.handle(data, null, that);
              });
            }
          });
          this.close();
        }
      },
      mounted() {
        let that = this;
        AjaxUtil.get('user/setting/get').then((data) => {
          //console.log(data);
          ResultUtil.handle(data, () => {
            let userData = data.attributes.userData;
            if(userData){
              that.formData.limit = userData.limit;
            }
          }, that);
        });
      }
    }
</script>

<style>

</style>
