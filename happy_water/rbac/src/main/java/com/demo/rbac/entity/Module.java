package com.demo.rbac.entity;


import com.demo.rbac.entity.moduleValidateGroup.AddModuleGroup;
import javax.validation.constraints.NotNull;

/**
 * @Author: 快乐水 樱桃可乐
 * @Description:
 * @Date: Created in 10:30 2018/9/24
 * @Modified By:
 */
public class Module {
    private Long id;
    private String serialId;
    private Integer moduleLevel;
    @NotNull(message = "模块名不能为空",groups = {AddModuleGroup.class})
    private String moduleName;
    private String moduleUrl;
    private String url;
    @NotNull(message = "父模块不能为空",groups = {AddModuleGroup.class})
    private Long fatherModule;
    private String moduleType;
    private String createdBy;
    private String updatedBy;
    private Long createdAt;
    private Long updatedAt;
    private Boolean highlight=false;
    private Boolean select=false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialId() {
        return serialId;
    }

    public void setSerialId(String serialId) {
        this.serialId = serialId;
    }

    public Integer getModuleLevel() {
        return moduleLevel;
    }

    public void setModuleLevel(Integer moduleLevel) {
        this.moduleLevel = moduleLevel;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleUrl() {
        return moduleUrl;
    }

    public void setModuleUrl(String moduleUrl) {
        this.moduleUrl = moduleUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getFatherModule() {
        return fatherModule;
    }

    public void setFatherModule(Long fatherModule) {
        this.fatherModule = fatherModule;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isHighlight() {
        return highlight;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    @Override
    public String toString() {
        return "Module{" + "id=" + id + ", serialId='" + serialId + '\'' + ", moduleLevel=" + moduleLevel + ", moduleName='" + moduleName + '\'' + ", moduleUrl='" + moduleUrl + '\'' + ", url='" + url + '\'' + ", fatherModule=" + fatherModule + ", moduleType='" + moduleType + '\'' + ", createdBy='" + createdBy + '\'' + ", updatedBy='" + updatedBy + '\'' + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", highlight=" + highlight + ", select=" + select + '}';
    }
}
