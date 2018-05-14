package com.fleamarket.core.model;

import javax.persistence.*;
import java.util.Date;

@Table(name = "treasure_view")
public class TreasureView {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "treasure_id")
    private Integer treasureId;

    @Column(name = "view_time")
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTreasureId() {
        return treasureId;
    }

    public void setTreasureId(Integer treasureId) {
        this.treasureId = treasureId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
