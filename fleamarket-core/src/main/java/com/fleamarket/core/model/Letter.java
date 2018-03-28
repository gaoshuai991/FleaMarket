package com.fleamarket.core.model;

import java.util.Date;
import javax.persistence.*;

public class Letter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "source_user_id")
    private Integer sourceUserId;

    @Column(name = "target_user_id")
    private Integer targetUserId;

    private String content;

    @Column(name = "create_time")
    private Date createTime;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return source_user_id
     */
    public Integer getSourceUserId() {
        return sourceUserId;
    }

    /**
     * @param sourceUserId
     */
    public void setSourceUserId(Integer sourceUserId) {
        this.sourceUserId = sourceUserId;
    }

    /**
     * @return target_user_id
     */
    public Integer getTargetUserId() {
        return targetUserId;
    }

    /**
     * @param targetUserId
     */
    public void setTargetUserId(Integer targetUserId) {
        this.targetUserId = targetUserId;
    }

    /**
     * @return content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}