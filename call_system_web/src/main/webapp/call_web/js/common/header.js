//封装nav和header组件
Vue.component('header-list', {
    data: function () {
        return {
            customerServerName: ''
        }
    },
    template: `
      <el-col :span="24" class="header">
      <el-col :span="15" class="logo">
        <img src="images/logo.png" alt="logo">
        外呼平台
      </el-col>
      <el-col :span="9" class="userinfo">
        <el-dropdown trigger="click">
            <span class="el-dropdown-link userinfo-inner">
              {{customerServerName}}
              <img src="images/user.png"  alt="头像">
            </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </el-col>
    `,
    methods: {
        logout: function () {
            var _this = this;
            this.$confirm('确认退出吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消'
                //type: 'warning'
            }).then(function () {
                window.location.href = '/call_web/login.html';
                axios.get('/user/logout').then(function (res) {
                    //console.log(res);
                });
            }).catch(function () {

            });
        }
    },
    mounted: function () {
        this.customerServerName = customerServerName || '';
    }
});
/*template:'<el-col :span="24" class="header"><el-col :span="15" class="logo"><img src="images/Sunlight-Logo.png" alt="logo"></el-col><el-col :span="9" class="userinfo"><el-dropdown trigger="click"><span class="el-dropdown-link userinfo-inner">sales1<img src="images/user.png"  alt="头像"></span><el-dropdown-menu slot="dropdown"><el-dropdown-item @click.native="">退出登录</el-dropdown-item></el-dropdown-menu></el-dropdown></el-col>'
 */