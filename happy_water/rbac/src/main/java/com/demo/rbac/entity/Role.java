package com.demo.rbac.entity;

import com.demo.rbac.entity.roleValidateGroup.AddRoleGroup;
import com.demo.rbac.entity.roleValidateGroup.UpRoleGroup;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: 快乐水 樱桃可乐
 * @Description:
 * @Date: Created in 9:20 2018/9/24
 * @Modified By:
 */
public class Role {
    private Long id;
    private String serialId;
    @Min(value = 0,message = "roleLevel必须是正整数",groups ={ AddRoleGroup.class,UpRoleGroup.class})
    @NotNull(message = "roleLevel不能为空",groups = AddRoleGroup.class)
    private Integer roleLevel;
    @NotNull(groups = AddRoleGroup.class)
    private String roleName;
    private String createdBy;
    private String updatedBy;
    private Long createdAt;
    private Long updatedAt;
    private List<Module> modules;

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

    public Integer getRoleLevel() {
        return roleLevel;
    }

    public void setRoleLevel(Integer roleLevel) {
        this.roleLevel = roleLevel;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
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

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    @Override
    public String toString() {
        return "Role{" + "id=" + id + ", serialId='" + serialId + '\'' + ", roleLevel=" + roleLevel + ", roleName='" + roleName + '\'' + ", createdBy='" + createdBy + '\'' + ", updatedBy='" + updatedBy + '\'' + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", modules=" + modules + '}';
    }
}
