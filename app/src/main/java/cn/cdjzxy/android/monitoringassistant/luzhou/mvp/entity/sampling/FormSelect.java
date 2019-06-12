package cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Keep;

import java.util.List;

import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;


@Entity
public class FormSelect {
    private String         FormId;
    private String         FormCode;
    private String         FormName;
    private String         TagParentId;
    private String         TagId;
    private String         Path;
    @Transient
    private List<FormFlow> FormFlows;

    @Keep()
    public FormSelect(String FormId, String FormCode, String FormName, String TagParentId, String TagId, String Path) {
        this.FormId = FormId;
        this.FormCode = FormCode;
        this.FormName = FormName;
        this.TagParentId = TagParentId;
        this.TagId = TagId;
        this.Path = Path;
    }

    @Keep()
    public FormSelect() {
    }

    public String getFormId() {
        return this.FormId;
    }

    public void setFormId(String FormId) {
        this.FormId = FormId;
    }

    public String getFormCode() {
        return this.FormCode;
    }

    public void setFormCode(String FormCode) {
        this.FormCode = FormCode;
    }

    public String getFormName() {
        return this.FormName;
    }

    public void setFormName(String FormName) {
        this.FormName = FormName;
    }

    public String getTagParentId() {
        return this.TagParentId;
    }

    public void setTagParentId(String TagParentId) {
        this.TagParentId = TagParentId;
    }

    public String getTagId() {
        return this.TagId;
    }

    public void setTagId(String TagId) {
        this.TagId = TagId;
    }

    public String getPath() {
        return this.Path;
    }

    public void setPath(String Path) {
        this.Path = Path;
    }

    public List<FormFlow> getFormFlows() {
        return FormFlows;
    }

    public void setFormFlows(List<FormFlow> formFlows) {
        FormFlows = formFlows;
    }


}
