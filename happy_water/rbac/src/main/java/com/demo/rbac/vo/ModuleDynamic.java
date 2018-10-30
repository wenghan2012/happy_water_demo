package com.demo.rbac.vo;


/**
 * @Author: 快乐水 樱桃可乐
 * @Description:
 * @Date: Created in 8:47 2018/10/4
 * @Modified By:
 */
public class ModuleDynamic {
    private String moduleId;
    private String moduleName;
    private String moduleFatherName;
    private String createdBy;

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleFatherName() {
        return moduleFatherName;
    }

    public void setModuleFatherName(String moduleFatherName) {
        this.moduleFatherName = moduleFatherName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String toString() {
        return "ModuleDynamic{" + "moduleId='" + moduleId + '\'' + ", moduleName='" + moduleName + '\'' + ", moduleFatherName='" + moduleFatherName + '\'' + ", createdBy='" + createdBy + '\'' + '}';
    }
}
