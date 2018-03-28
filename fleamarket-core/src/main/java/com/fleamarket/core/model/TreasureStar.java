package com.fleamarket.core.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "treasure_star")
public class TreasureStar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "treasure_id")
    private Integer treasureId;

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
     * @return user_id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return treasure_id
     */
    public Integer getTreasureId() {
        return treasureId;
    }

    /**
     * @param treasureId
     */
    public void setTreasureId(Integer treasureId) {
        this.treasureId = treasureId;
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