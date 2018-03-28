package com.fleamarket.core.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "treasure_picture")
public class TreasurePicture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "treasure_id")
    private Integer treasureId;

    private String picture;

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
     * @return picture
     */
    public String getPicture() {
        return picture;
    }

    /**
     * @param picture
     */
    public void setPicture(String picture) {
        this.picture = picture;
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