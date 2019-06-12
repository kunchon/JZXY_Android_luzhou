package cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class SamplingUser {
    /**
     * UserId : 61bb48f0-5d8f-49a1-989d-7d6d7033fd91
     * UserName : Admin
     */

    private String UserId;
    private String UserName;
    @Keep()
    public SamplingUser(String UserId, String UserName) {
        this.UserId = UserId;
        this.UserName = UserName;
    }
    @Keep()
    public SamplingUser() {
    }
    public String getUserId() {
        return this.UserId;
    }
    public void setUserId(String UserId) {
        this.UserId = UserId;
    }
    public String getUserName() {
        return this.UserName;
    }
    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

}
