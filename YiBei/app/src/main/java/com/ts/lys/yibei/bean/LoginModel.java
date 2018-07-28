package com.ts.lys.yibei.bean;

/**
 * 登录信息
 */

public class LoginModel {
    /**
     * data : {"user":{"firstLogin":"true","accessToken":"cb0c6cbc24a21f53b21420246ff7197716822824171d29235736e35b325d2a2853ac55c148474c363a6b4cce527fd32a7f37cd4435e61e2dce4c5529b05aee2d","type":0,"userId":36}}
     * err_code : 0
     * err_msg : successe
     */

    private DataBean data;
    private String err_code;
    private String err_msg;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getErr_code() {
        return err_code;
    }

    public void setErr_code(String err_code) {
        this.err_code = err_code;
    }

    public String getErr_msg() {
        return err_msg;
    }

    public void setErr_msg(String err_msg) {
        this.err_msg = err_msg;
    }

    public static class DataBean {
        /**
         * user : {"firstLogin":"true","accessToken":"cb0c6cbc24a21f53b21420246ff7197716822824171d29235736e35b325d2a2853ac55c148474c363a6b4cce527fd32a7f37cd4435e61e2dce4c5529b05aee2d","type":0,"userId":36}
         */

        private UserBean user;

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean {
            /**
             * firstLogin : true
             * accessToken : cb0c6cbc24a21f53b21420246ff7197716822824171d29235736e35b325d2a2853ac55c148474c363a6b4cce527fd32a7f37cd4435e61e2dce4c5529b05aee2d
             * type : 0
             * userId : 36
             */

            private boolean firstLogin;
            private String accessToken;
            private int type;
            private int userId;
            private String inviteCode;

            public boolean isFirstLogin() {
                return firstLogin;
            }

            public void setFirstLogin(boolean firstLogin) {
                this.firstLogin = firstLogin;
            }

            public String getAccessToken() {
                return accessToken;
            }

            public void setAccessToken(String accessToken) {
                this.accessToken = accessToken;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public String getInviteCode() {
                return inviteCode;
            }

            public void setInviteCode(String inviteCode) {
                this.inviteCode = inviteCode;
            }
        }
    }
}
