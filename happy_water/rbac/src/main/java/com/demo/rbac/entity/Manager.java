package com.demo.rbac.entity;



import com.demo.rbac.entity.managerValidateGroup.AddAccountGroup;
import com.demo.rbac.entity.managerValidateGroup.LoginGroup;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * @Author: 快乐水 樱桃可乐
 * @Description:
 * @Date: Created in 21:28 2018/9/23
 * @Modified By:
 */
public class Manager {
    @Min(value = 1,message = "id必须是大于0的整数")
    private Long id;
    private String serialId;
    @NotNull(message = "账户不能为空",groups = { LoginGroup.class,AddAccountGroup.class})
    private String account;
    @NotNull(message = "密码不能为空",groups = { LoginGroup.class,AddAccountGroup.class})
    @Pattern(regexp = "(?!^[0-9]+$)(?!^[A-z]+$)(?!^[^A-z0-9]+$)^.{6,18}$",groups = {AddAccountGroup.class},message = "密码格式错误")
    private String password;
    private String salt;
    @NotNull(message = "冻结状态不能为空",groups = {AddAccountGroup.class})
    private Boolean locked;
    private String createdBy;
    private String updatedBy;
    private Long createdAt;
    private Long updatedAt;
    private List<Role> roles;

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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "Manager{" + "id=" + id + ", serialId='" + serialId + '\'' + ", account='" + account + '\'' + ", password='" + password + '\'' + ", salt='" + salt + '\'' + ", locked=" + locked + ", createdBy='" + createdBy + '\'' + ", updatedBy='" + updatedBy + '\'' + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", roles=" + roles + '}';
    }
}
