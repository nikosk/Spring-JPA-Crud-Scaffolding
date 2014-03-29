package gr.dsigned.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import gr.dsigned.security.SecurityUtils;


/**
 * Created by IntelliJ IDEA.
 * User: nk
 * Date: 11/16/13
 * Time: 2:21 AM
 */
@MappedSuperclass
public abstract class AuditedEntity extends BaseEntity {

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    @Column(name = "updated_by")
    private String updateBy;

    @Column(name = "created_by")
    private String createdBy;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @PrePersist
    public void setCreationDate() {
        this.createdAt = new Date();
        this.createdBy = SecurityUtils.getCurrentLogin();
    }

    @PreUpdate
    public void setChangeDate() {
        this.updatedAt = new Date();
        this.updateBy = SecurityUtils.getCurrentLogin();
    }


}
