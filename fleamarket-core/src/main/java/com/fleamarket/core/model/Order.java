package com.fleamarket.core.model;

import javax.persistence.*;
import java.util.Date;

@Table(name="`order`")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;


    private String address;
    @Column(name = "user_id")
    private Integer userId;
    private String phone;
    @Column(name = "treasure_id")
    private Integer treasureId;

    @Transient
    private Treasure treasure;

    @Column(name = "create_time")
    private Date createTime;

    private String note;

    /**
     * 【订单状态】0：退货、1：待发货、2：已发货、3：交易成功
     */
    private Integer status;

    /**
     * 物流单号，用于物流SDK
     */
    private String logistics;

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

    public Treasure getTreasure() {
        return treasure;
    }

    public void setTreasure(Treasure treasure) {
        this.treasure = treasure;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getPhone(){
        return phone;
    }
    public void setPhone(String phone){
        this.phone=phone;
    }
    public String getAddress(){
        return address;
    }
    public void setAddress(String address){
        this.address=address;
    }
    public void setUserId(Integer userId){
        this.userId=userId;
    }
    public Integer getUserId(){
        return userId;
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

    /**
     * @return note
     */
    public String getNote() {
        return note;
    }

    /**
     * @param note
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * 获取【订单状态】0：退货、1：待发货、2：已发货、3：交易成功
     *
     * @return status - 【订单状态】0：退货、1：待发货、2：已发货、3：交易成功
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置【订单状态】0：退货、1：待发货、2：已发货、3：交易成功
     *
     * @param status 【订单状态】0：退货、1：待发货、2：已发货、3：交易成功
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取物流单号，用于物流SDK
     *
     * @return logistics - 物流单号，用于物流SDK
     */
    public String getLogistics() {
        return logistics;
    }

    /**
     * 设置物流单号，用于物流SDK
     *
     * @param logistics 物流单号，用于物流SDK
     */
    public void setLogistics(String logistics) {
        this.logistics = logistics;
    }


}