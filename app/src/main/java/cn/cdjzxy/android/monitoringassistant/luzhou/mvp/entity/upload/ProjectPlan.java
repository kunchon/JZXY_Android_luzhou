package cn.cdjzxy.monitoringassistant.mvp.model.entity.upload;

import java.util.List;

import lombok.Data;

@Data
public class ProjectPlan {
    private String               Id;
    private boolean              IsCompelSubmit;
    private List<ProjectContent> ProjectContents;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public boolean isCompelSubmit() {
        return IsCompelSubmit;
    }

    public void setIsCompelSubmit(boolean is){
        this.IsCompelSubmit=is;
    }

    public void setCompelSubmit(boolean compelSubmit) {
        IsCompelSubmit = compelSubmit;
    }

    public List<ProjectContent> getProjectContents() {
        return ProjectContents;
    }

    public void setProjectContents(List<ProjectContent> projectContents) {
        ProjectContents = projectContents;
    }
}
