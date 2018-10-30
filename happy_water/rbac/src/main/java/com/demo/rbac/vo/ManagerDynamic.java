package com.demo.rbac.vo;

/**
 * @Author: 快乐水 樱桃可乐
 * @Description:
 * @Date: Created in 10:00 2018/9/27
 * @Modified By:
 */
public class ManagerDynamic {
    private Long id;
    private String serialId;
    private String account;
    private String password;
    private String salt;
    private Boolean locked;
    private String createdBy;
    private String updatedBy;
    private Long createdAt;
    private Long updatedAt;
    private String roleName;

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

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "ManagerDynamic{" + "id=" + id + ", serialId='" + serialId + '\'' + ", account='" + account + '\'' + ", password='" + password + '\'' + ", salt='" + salt + '\'' + ", locked=" + locked + ", createdBy='" + createdBy + '\'' + ", updatedBy='" + updatedBy + '\'' + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", roleName='" + roleName + '\'' + '}';
    }
}
