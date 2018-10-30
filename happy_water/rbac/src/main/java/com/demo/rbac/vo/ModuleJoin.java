package com.demo.rbac.vo;

import java.util.List;

/**
 * @Author: 快乐水 樱桃可乐
 * @Description:
 * @Date: Created in 16:28 2018/10/4
 * @Modified By:
 */
public class ModuleJoin {
    private Long id;
    private String moduleName;
    private String moduleUrl;
    private List<ModuleJoin> SonModule;
    private Boolean highlight=false;
    private Boolean select=false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<ModuleJoin> getSonModule() {
        return SonModule;
    }

    public void setSonModule(List<ModuleJoin> sonModule) {
        SonModule = sonModule;
    }

    public Boolean getHighlight() {
        return highlight;
    }

    public void setHighlight(Boolean highlight) {
        this.highlight = highlight;
    }

    public Boolean getSelect() {
        return select;
    }

    public void setSelect(Boolean select) {
        this.select = select;
    }

    @Override
    public String toString() {
        return "ModuleJoin{" + "id=" + id + ", moduleName='" + moduleName + '\'' + ", moduleUrl='" + moduleUrl + '\'' + ", SonModule=" + SonModule + ", highlight=" + highlight + ", select=" + select + '}';
    }
}

