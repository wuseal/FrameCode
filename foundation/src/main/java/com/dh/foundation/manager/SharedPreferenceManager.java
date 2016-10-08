package com.dh.foundation.manager;


import com.dh.foundation.utils.SharedPreferenceUtils;

/**
 * APP和用户信息配置管理类
 * Created By: Seal.Wu
 * Date: 2015/4/14
 * Time: 18:40
 */
public class SharedPreferenceManager {

    private final String userIdKey = "user_id";
    private final String appTokenKey = "app_token";
    private final String userNameKey = "user_name";
    private final String userPasswordKey = "user_password";
    private final String userPhoneKey = "user_phone";
    private final String isLoginKey = "user_login";
    private final String roleCodeKey = "role_code";

    public void setUserId(String userId) {
        SharedPreferenceUtils.STRING_CONTROLLER.set(userIdKey, userId);
    }

    public String getUserId() {
        return SharedPreferenceUtils.STRING_CONTROLLER.get(userIdKey, "");
    }

    public void setUserName(String userName) {
        SharedPreferenceUtils.STRING_CONTROLLER.set(userNameKey, userName);
    }

    public String getUserName() {
        return SharedPreferenceUtils.STRING_CONTROLLER.get(userNameKey, "");
    }

    public void setAppToken(String appToken) {
        SharedPreferenceUtils.STRING_CONTROLLER.set(appTokenKey, appToken);
    }

    public String getAppToken() {
        return SharedPreferenceUtils.STRING_CONTROLLER.get(appTokenKey, "");
    }

    public void setUserPassword(String userPassword) {
        SharedPreferenceUtils.STRING_CONTROLLER.set(userPasswordKey, userPassword);
    }

    public String getUserPassword() {
        return SharedPreferenceUtils.STRING_CONTROLLER.get(userPasswordKey, "");
    }

    public void setIsLogin(boolean isLogin) {
        SharedPreferenceUtils.BOOLEAN_CONTROLLER.set(isLoginKey, isLogin);
    }

    public boolean isLogin() {
        return SharedPreferenceUtils.BOOLEAN_CONTROLLER.get(isLoginKey, false);
    }

    public void setUserPhone(String userPhone) {
        SharedPreferenceUtils.STRING_CONTROLLER.set(userPhoneKey, userPhone);
    }

    public String getUserPhone() {
        return SharedPreferenceUtils.STRING_CONTROLLER.get(userPhoneKey, "");
    }

    public int getRoleCode() {
        return SharedPreferenceUtils.INTEGER_CONTROLLER.get(roleCodeKey, 3);
    }

    public void setRoleCode(int roleCode) {
        SharedPreferenceUtils.INTEGER_CONTROLLER.set(roleCodeKey, roleCode);
    }

}
