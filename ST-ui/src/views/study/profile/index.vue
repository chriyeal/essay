<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!-- 用户信息卡片 -->
      <el-col :span="8">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>个人信息</span>
          </div>
          <div class="user-profile">
            <div class="avatar-container">
              <img :src="avatar" class="user-avatar" @click="openAvatarDialog">
              <div class="avatar-tip">点击更换头像</div>
            </div>
            <div class="user-info">
              <h2>{{ user.nickName }}</h2>
              <p class="user-signature">{{ user.signature || '这个人很懒，什么都没写~' }}</p>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <!-- 修改信息表单 -->
      <el-col :span="16">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>基本资料</span>
          </div>
          <el-tabs v-model="activeTab">
            <el-tab-pane label="基本资料" name="userinfo">
              <el-form ref="userForm" :model="user" :rules="rules" label-width="100px">
                <el-form-item label="用户昵称" prop="nickName">
                  <el-input v-model="user.nickName" placeholder="请输入用户昵称" />
                </el-form-item>
                <el-form-item label="手机号码" prop="phonenumber">
                  <el-input v-model="user.phonenumber" placeholder="请输入手机号码" maxlength="11" />
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                  <el-input v-model="user.email" placeholder="请输入邮箱" />
                </el-form-item>
                <el-form-item label="性别">
                  <el-radio-group v-model="user.sex">
                    <el-radio label="0">男</el-radio>
                    <el-radio label="1">女</el-radio>
                  </el-radio-group>
                </el-form-item>
                <el-form-item label="个性签名">
                  <el-input v-model="user.signature" type="textarea" :rows="3" placeholder="请输入个性签名" maxlength="200" show-word-limit />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="submitUserInfo">保存修改</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>
            
            <el-tab-pane label="修改密码" name="resetPwd">
              <el-form ref="pwdForm" :model="pwdForm" :rules="pwdRules" label-width="100px">
                <el-form-item label="旧密码" prop="oldPassword">
                  <el-input v-model="pwdForm.oldPassword" placeholder="请输入旧密码" type="password" show-password />
                </el-form-item>
                <el-form-item label="新密码" prop="newPassword">
                  <el-input v-model="pwdForm.newPassword" placeholder="请输入新密码" type="password" show-password />
                </el-form-item>
                <el-form-item label="确认密码" prop="confirmPassword">
                  <el-input v-model="pwdForm.confirmPassword" placeholder="请确认新密码" type="password" show-password />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="submitPwd">保存修改</el-button>
                  <el-button type="danger" @click="close">关闭</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>

    <!-- 头像上传对话框 -->
    <el-dialog :title="title" :visible.sync="openAvatar" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :action="uploadUrl"
        :headers="headers"
        :show-file-list="false"
        :on-success="handleAvatarSuccess"
        :before-upload="beforeAvatarUpload"
        :on-change="handleAvatarChange"
        :auto-upload="false"
        :data="uploadData"
        name="avatarfile"
        class="avatar-uploader"
      >
        <img v-if="imageUrl" :src="imageUrl" class="avatar">
        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openAvatar = false">取 消</el-button>
        <el-button type="primary" @click="submitAvatar" :loading="uploading">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getUserProfile, updateUserProfile, updateUserPwd } from "@/api/login";
import { getToken } from "@/utils/auth";

export default {
  name: "Profile",
  data() {
    const equalToPassword = (rule, value, callback) => {
      if (this.pwdForm.newPassword !== value) {
        callback(new Error("两次输入的密码不一致"));
      } else {
        callback();
      }
    };
    return {
      user: {},
      activeTab: "userinfo",
      openAvatar: false,
      title: "修改头像",
      imageUrl: "",
      uploadUrl: process.env.VUE_APP_BASE_API + "/system/user/profile/avatar",
      headers: {
        Authorization: "Bearer " + getToken()
      },
      uploadData: {},
      pwdForm: {
        oldPassword: undefined,
        newPassword: undefined,
        confirmPassword: undefined
      },
      uploading: false,
      selectedFile: null,
      rules: {
        nickName: [
          { required: true, message: "用户昵称不能为空", trigger: "blur" }
        ],
        email: [
          { required: true, message: "邮箱地址不能为空", trigger: "blur" },
          { type: "email", message: "'请输入正确的邮箱地址", trigger: ["blur", "change"] }
        ],
        phonenumber: [
          { required: true, message: "手机号码不能为空", trigger: "blur" },
          { pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: "请输入正确的手机号码", trigger: "blur" }
        ]
      },
      pwdRules: {
        oldPassword: [
          { required: true, message: "旧密码不能为空", trigger: "blur" }
        ],
        newPassword: [
          { required: true, message: "新密码不能为空", trigger: "blur" },
          { min: 6, max: 20, message: "长度在 6 到 20 个字符", trigger: "blur" }
        ],
        confirmPassword: [
          { required: true, message: "确认密码不能为空", trigger: "blur" },
          { required: true, validator: equalToPassword, trigger: "blur" }
        ]
      }
    };
  },
  computed: {
    avatar() {
      return this.user.avatar || require("@/assets/images/profile.jpg");
    }
  },
  created() {
    this.getUser();
  },
  methods: {
    getUser() {
      getUserProfile().then(response => {
        this.user = response.data;
      });
    },
    submitUserInfo() {
      this.$refs["userForm"].validate(valid => {
        if (valid) {
          updateUserProfile(this.user).then(response => {
            this.$modal.msgSuccess("修改成功");
            this.getUser();
          });
        }
      });
    },
    submitPwd() {
      this.$refs["pwdForm"].validate(valid => {
        if (valid) {
          updateUserPwd(this.pwdForm.oldPassword, this.pwdForm.newPassword).then(response => {
            this.$modal.msgSuccess("密码修改成功，请重新登录");
            this.$store.dispatch("LogOut").then(() => {
              location.href = "/index";
            });
          });
        }
      });
    },
    openAvatarDialog() {
      this.openAvatar = true;
      this.imageUrl = this.user.avatar;
      this.selectedFile = null;
    },
    handleAvatarChange(file) {
      // 选择文件后预览
      this.selectedFile = file;
      this.imageUrl = URL.createObjectURL(file.raw);
    },
    submitAvatar() {
      if (!this.selectedFile) {
        this.$modal.msgWarning("请先选择头像图片");
        return;
      }
      this.uploading = true;
      this.$refs.upload.submit();
    },
    handleAvatarSuccess(res, file) {
      this.uploading = false;
      if (res.code === 200) {
        // 后端已经自动更新了头像，直接刷新用户信息
        this.$modal.msgSuccess("头像修改成功");
        this.openAvatar = false;
        this.getUser();
      } else {
        this.$modal.msgError(res.msg || "上传失败");
      }
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === "image/jpeg" || file.type === "image/png";
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isJPG) {
        this.$message.error("上传头像图片只能是 JPG/PNG 格式!");
      }
      if (!isLt2M) {
        this.$message.error("上传头像图片大小不能超过 2MB!");
      }
      return isJPG && isLt2M;
    },
    close() {
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.push({ path: "/index" });
    }
  }
};
</script>

<style lang="scss" scoped>
.user-profile {
  text-align: center;
  
  .avatar-container {
    position: relative;
    display: inline-block;
    cursor: pointer;
    
    .user-avatar {
      width: 120px;
      height: 120px;
      border-radius: 50%;
      object-fit: cover;
    }
    
    .avatar-tip {
      margin-top: 10px;
      font-size: 12px;
      color: #409EFF;
    }
  }
  
  .user-info {
    margin-top: 20px;
    
    h2 {
      margin: 0 0 10px 0;
      font-size: 20px;
    }
    
    .user-signature {
      color: #909399;
      font-size: 14px;
    }
  }
}

.avatar-uploader {
  text-align: center;
  
  ::v-deep .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    
    &:hover {
      border-color: #409EFF;
    }
  }
  
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
    object-fit: cover;
  }
}
</style>
