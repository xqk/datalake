<template>
  <div>
    <!--基础配置表单-->
    <el-form
      ref="formInline"
      v-loading="loading"
      :model="formInline"
      :rules="rules"
      class="demo-form-inline"
      :disabled="show"
      size="small"
    >
      <el-row>
        <el-col>
          <el-form-item :label="$t('system_parameter_setting.front_time_out')" prop="frontTimeOut">
            <el-input
              v-model="formInline.frontTimeOut"
              :placeholder="$t('system_parameter_setting.empty_front')"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col>
          <el-form-item :label="$t('system_parameter_setting.msg_time_out')" prop="msgTimeOut">
            <el-input
              v-model="formInline.msgTimeOut"
              :placeholder="$t('system_parameter_setting.empty_msg')"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row v-if="loginTypes.length > 1">
        <el-col>
          <el-form-item :label="$t('system_parameter_setting.login_type')" prop="loginType">
            <el-radio-group v-model="formInline.loginType">
              <el-radio :label="0" size="mini">{{ $t('login.default_login') }}</el-radio>
              <el-radio v-if="loginTypes.includes(1)" :label="1" size="mini">LDAP</el-radio>
              <el-radio v-if="loginTypes.includes(2)" :label="2" size="mini">OIDC</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col>
          <el-form-item :label="$t('display.openHomePage')">
            <el-checkbox v-model="formInline.openHomePage" true-label="true" false-label="false" />
          </el-form-item>
        </el-col>
      </el-row>

    </el-form>

    <div>

      <el-button v-if="showEdit" size="small" @click="edit">{{ $t('commons.edit') }}</el-button>
      <el-button v-if="showSave" type="success" :disabled="disabledSave" size="small" @click="save('formInline')">
        {{ $t('commons.save') }}
      </el-button>
      <el-button v-if="showCancel" type="info" size="small" @click="cancel">{{ $t('commons.cancel') }}</el-button>
    </div>
  </div>
</template>

<script>

import { basicInfo, updateInfo } from '@/api/system/basic'
import { ldapStatus, oidcStatus } from '@/api/user'

export default {
  name: 'EmailSetting',
  data() {
    return {
      formInline: {},
      input: '',
      visible: true,
      showEdit: true,
      showSave: false,
      showCancel: false,
      show: true,
      disabledSave: false,
      loading: false,
      loginTypes: [0],
      rules: {
        frontTimeOut: [
          {
            pattern: '^([0-9]|\\b[1-9]\\d\\b|\\b[1-2]\\d\\d\\b|\\b300\\b)$', //修改了正则表达式，让其正确匹配0-300的数值
            message: this.$t('system_parameter_setting.front_error'),
            trigger: 'blur'
          }
        ],
        msgTimeOut: [
          {
            pattern: '^([1-9]|[1-9]\\d|365)$',
            message: this.$t('system_parameter_setting.msg_error'),
            trigger: 'blur'
          }
        ]
      }
    }
  },
  beforeCreate() {
    ldapStatus().then(res => {
      if (res.success && res.data) {
        this.loginTypes.push(1)
      }
    })

    oidcStatus().then(res => {
      if (res.success && res.data) {
        this.loginTypes.push(2)
      }
    })
  },
  created() {
    this.query()
  },
  methods: {

    query() {
      basicInfo().then(response => {
        this.formInline = response.data

        if (this.formInline && !this.formInline.loginType) {
          this.formInline.loginType = 0
        }

        this.$nextTick(() => {
          this.$refs.formInline.clearValidate()
        })
      })
    },

    edit() {
      this.showEdit = false
      this.showSave = true
      this.showCancel = true
      this.show = false
    },
    save(formInline) {
      const param = [
        { paramKey: 'basic.frontTimeOut', paramValue: this.formInline.frontTimeOut, type: 'text', sort: 1 },
        { paramKey: 'basic.msgTimeOut', paramValue: this.formInline.msgTimeOut, type: 'text', sort: 2 },
        { paramKey: 'basic.loginType', paramValue: this.formInline.loginType, type: 'text', sort: 3 },
        { paramKey: 'ui.openHomePage', paramValue: this.formInline.openHomePage, type: 'text', sort: 13 }

      ]

      this.$refs[formInline].validate(valid => {
        if (valid) {
          updateInfo(param).then(response => {
            const flag = response.success
            if (flag) {
              this.$success(this.$t('commons.save_success'))
              this.showEdit = true
              this.showCancel = false
              this.showSave = false
              this.show = true
              window.location.reload()
            } else {
              this.$message.error(this.$t('commons.save_failed'))
            }
          })
        } else {
          // this.result = false
        }
      })
    },
    cancel() {
      this.showEdit = true
      this.showCancel = false
      this.showSave = false
      this.show = true
      this.query()
    }

  }
}
</script>

<style scoped>

</style>
