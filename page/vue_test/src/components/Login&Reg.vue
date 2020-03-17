<template>
  <div id="lr-ctn">
    <div id="head"></div>

    <div id="main">
      <div id="left">
        <!-- <img src="../assets/images/qt.jpg" width="80%" height="100%" /> -->
      </div>
      <div id="right">
        <div id="lr_inner">
          <div id="right_top">
            <div id="login" @click="change_log()" ref="login">登录</div>
            <div id="register" @click="change_reg()" ref="reg">注册</div>
          </div>
          <div class="tip" style="margin-left:3.5rem" ref="tip"></div>
          <div id="input_log" v-if="if_login">
            <el-input v-model="username" class="lr_input" placeholder="请输入用户名或手机号"></el-input>
            <el-input v-model="password" class="lr_input" placeholder="请输入密码" type="password"></el-input>
            <div v-show="verifyCode_show" style="display:flex">
              <img
                ref="img_click"
                width="100px"
                height="60px"
                alt="验证码"
                onclick="this.src='http://localhost:8082/user/defaultKaptcha?d='+new Date()*1"
                src="http://localhost:8082/user/defaultKaptcha"
              />
              <el-input v-model="verifyCode" style="width:6.5rem;margin-left:1rem" class="lr_input" placeholder="输入验证码"></el-input>
            </div>
            <el-switch style="width:68%;margin: 0.8rem 0" v-model="auto_login" active-text="记住我"> </el-switch>
            <el-button style="width:68%;opacity:0.7" type="primary" @click="login()">登录</el-button>
            <div id="fgt" @click="fgtpwd()">忘记密码?</div>
            <div id="other">第三方登录</div>
          </div>
          <div id="input_reg" v-if="if_reg">
            <el-input v-model="username2" class="lr_input" placeholder="用户名(4-16位,字母数字下划线)"></el-input>
            <el-input v-model="password2" class="lr_input" placeholder="请输入密码" type="password" show-password></el-input>
            <el-input v-model="tel_no" class="lr_input" placeholder="请输入手机号码"></el-input>
            <div style="margin-left:3.2rem;display:flex">
              <el-input style="width:40%" v-model="validateMsg" class="lr_input" placeholder="验证码"></el-input>
              <el-button style="margin-left:2.5rem;height:2.4rem;margin-top:1rem;opacity:0.7" type="primary" @click="getSmscode()">获取</el-button>
            </div>

            <el-button style="width:68%;opacity:0.7" type="primary" @click="reg()">注册</el-button>
          </div>
        </div>
      </div>
    </div>
    <div id="foot"></div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      // 登录参数
      username: '',
      password: '',
      verifyCode_show: false,
      auto_login: true,
      verifyCode: '',
      // 注册参数
      username2: '',
      password2: '',
      tel_no: '',
      validateMsg: '',

      if_login: true,
      if_reg: false
    }
  },
  methods: {
    change_log() {
      // this.$refs.tip.innerHTML = ''
      this.if_login = true
      this.if_reg = false
      this.$refs.login.style.color = 'black'
      this.$refs.reg.style.color = '#999999'
    },
    change_reg() {
      // this.$refs.tip.innerHTML = ''
      this.if_login = false
      this.if_reg = true
      this.$refs.reg.style.color = 'black'
      this.$refs.login.style.color = '#999999'
    },
    login() {
      if (this.username == '' || this.password == '') {
        this.$notify({
          title: '提示',
          message: '用户名或密码不能为空',
          type: 'warning',
          duration: 3000
        })
        return
      }
      if (this.verifyCode_show && this.verifyCode == '') {
        this.$notify({
          title: '提示',
          message: '请输入验证码',
          type: 'warning',
          duration: 3000
        })
        return
      }
      if (this.verifyCode_show && this.verifyCode != '') {
        this.$http.get('/user/verifyKaptcha?verifyCode=' + this.verifyCode).then(res => {
          if (!res.data) {
            this.$notify({
              title: '提示',
              message: '验证码错误',
              type: 'warning',
              duration: 3000
            })
          } else {
            {
              this.$http.post('/user/login', { username: this.username, password: this.password, rememberMe: this.auto_login }).then(res => {
                console.log(res.data)
                if (res.data.code === '1000') {
                  this.verifyCode_show = false
                  let Base64 = require('js-base64').Base64
                  let tokenBase64 = Base64.encode(
                    JSON.stringify({
                      exp: JSON.stringify(new Date().getTime() + 1000 * 60 * 60),
                      username: this.username,
                      rem: JSON.stringify(this.auto_login)
                    })
                  )

                  localStorage.setItem('token', tokenBase64) //1h token过期
                  this.$router.push({ name: 'mine' })
                } else if (res.data.code === '1001') {
                  this.verifyCode_show = true
                  this.$refs.img_click.click()
                  this.$notify({
                    title: '提示',
                    message: '用户名或密码错误',
                    type: 'warning',
                    duration: 3000
                  })
                } else {
                  this.$notify({
                    title: '提示',
                    message: '服务异常',
                    type: 'warning',
                    duration: 3000
                  })
                }
              })
            }
          }
        })
      }
      if (!this.verifyCode_show) {
        this.$http.post('/user/login', { username: this.username, password: this.password, rememberMe: this.auto_login }).then(res => {
          console.log(res.data)
          if (res.data.code === '1000') {
            this.verifyCode_show = false
            let Base64 = require('js-base64').Base64
            let tokenBase64 = Base64.encode(
              JSON.stringify({
                exp: JSON.stringify(new Date().getTime() + 1000 * 60 * 60),
                username: this.username,
                rem: JSON.stringify(this.auto_login)
              })
            )

            localStorage.setItem('token', tokenBase64) //1h token过期
            this.$router.push({ name: 'mine' })
          } else if (res.data.code === '1001') {
            this.verifyCode_show = true
            this.$refs.img_click.click()
            this.$notify({
              title: '提示',
              message: '用户名或密码错误',
              type: 'warning',
              duration: 3000
            })
          } else {
            this.$notify({
              title: '提示',
              message: '服务异常',
              type: 'warning',
              duration: 3000
            })
          }
        })
      }
    },
    reg() {
      this.$http
        .get('/user/validateMsg', {
          params: { validateMsg: this.validateMsg }
        })
        .then(res => {
          console.log(res.data.data.result)
          if (res.data.data.result === true) {
            //4到16位（字母，数字，下划线，减号）
            if (this.username2 == '' || this.password2 == '' || !this.username2.match(/^[a-zA-Z0-9_-]{4,12}$/)) {
              this.$notify({
                title: '提示',
                message: '请输入合法用户名',
                type: 'warning',
                duration: 3000
              })
              return
            }
            if (this.tel_no == '' || !this.tel_no.match(/^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\d{8}$/)) {
              this.$notify({
                title: '提示',
                message: '请输入正确手机号码',
                type: 'warning',
                duration: 3000
              })
              return
            }
            this.$http.post('/user/register', { username: this.username2, password: this.password2, telNo: this.tel_no }).then(res => {
              console.log(res.data)
              if (res.data.code === '1000') {
                this.$notify({
                  title: '恭喜，注册成功',
                  message: '快去登录吧！',
                  type: 'success',
                  duration: 2000
                })

                this.if_login = true
                this.if_reg = false
                this.$refs.login.style.color = 'black'
                this.$refs.reg.style.color = '#999999'

                var that = this
                setTimeout(function() {
                  that.if_login = true
                  that.if_reg = false
                }, 3000)
              } else if (res.data.code == '1001') {
                // that2.$refs.tip.innerHTML = res.data.msg
                this.$notify({
                  title: '提示',
                  message: res.data.msg,
                  type: 'warning',
                  duration: 3000
                })
              }
            })
          }
        })
    },
    getSmscode() {
      this.$notify({
        title: '提示',
        message: '服务测试中,暂可不输入验证码',
        type: 'warning',
        duration: 3000
      })
    },
    fgtpwd() {
      this.$notify({
        title: '提示',
        message: '可联系管理员 qq1436621819',
        type: 'warning',
        duration: 3000
      })
    }
  },

  mounted() {
    this.$refs.login.style.color = 'black'
  }
}
</script>

<style>
#lr-ctn {
  min-width: 1200px;
  min-height: 600px;
  height: 100vh;
  display: flex;
  flex-direction: column;
}
#head {
  background-color: rgb(237, 244, 250);
  width: 100vw;
  flex-grow: 8;
}
#main {
  background-color: mintcream;
  border-top: 1px solid rgb(247, 248, 248);
  border-bottom: 1px solid rgb(248, 241, 228);
  display: flex;
  /* background: url('../assets/images/main_bg.png'); */
  flex-grow: 5;
  /* align-content: center; */
  /* align-items: center; */
  justify-content: center;
}
#left {
  background-color: mintcream;
  /* background: url('../assets/images/qt.jpg'); */
  text-align: center;
  /* background-size: 100%; */
  /* background-repeat: no-repeat; */
  min-height: 28rem;
  margin: 4rem 0;
  justify-content: space-around;
  height: 60%;
  width: 35%;
}
#right {
  /* filter: alpha(opacity=80);
  -moz-opacity: 0.8;
  opacity: 0.8; */
  /* background: url('../assets/images/main_bg.png'); */
  /* background-size: 0%; */
  min-height: 28rem;
  justify-content: center;
  display: flex;
  margin: 4rem 0;
  height: 60%;
  background-color: mintcream;
  width: 35%;
}
#right_top {
  padding: 0 1rem;
  display: flex;
  justify-content: space-around;
  font-size: 1.5rem;
  color: #999999;
}
#right_top div:hover {
  cursor: pointer;
  color: black;
}
#lr_inner {
  background: url('../assets/images/main_bg.png');
  align-self: center;
  width: 20rem;
  height: 22rem;
  border: 0.01rem solid lightblue;
  padding: 6% 5%;
  border-radius: 0.5rem;
  box-shadow: 0 0 1rem 0 rgba(0, 0, 0, 0.4);
}
#input_log,
#input_reg {
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 5rem;
}

.lr_input {
  display: block;
  margin: 1rem 0;
  width: 68%;
}
.tip {
  margin-left: 1rem;
  margin-top: 0.5rem;
  margin-bottom: -0.8rem;
  height: 1.5rem;
  display: block;
  width: 68%;
  color: red;
  font-size: 0.8rem;
}
#fgt {
  align-self: flex-end;
  margin-top: 1.5rem;
  width: 68%;
  text-align: right;
  font-size: 0.5rem;
  color: rgb(0, 160, 255);
  cursor: pointer;
}
#other {
  align-self: flex-start;
  margin-top: 2.5rem;
  font-size: 0.5rem;
  color: black;
}

#foot {
  background-color: rgb(229, 239, 244);
  flex-grow: 40;
}

body {
  overflow: hidden;
  padding: 0;
  margin: 0;
}
</style>
