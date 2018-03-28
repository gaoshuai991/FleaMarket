package com.fleamarket.core.model;

import javax.persistence.*;

@Table(name = "delivery_addr")
public class DeliveryAddr {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    private String city;

    private String address;

    /**
     * 收件人
     */
    private String consignee;

    private String tel;

    @Column(name = "isDefault")
    private Byte isdefault;

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
     * @return city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取收件人
     *
     * @return consignee - 收件人
     */
    public String getConsignee() {
        return consignee;
    }

    /**
     * 设置收件人
     *
     * @param consignee 收件人
     */
    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    /**
     * @return tel
     */
    public String getTel() {
        return tel;
    }

    /**
     * @param tel
     */
    public void setTel(String tel) {
        this.tel = tel;
    }

    /**
     * @return isDefault
     */
    public Byte getIsdefault() {
        return isdefault;
    }

    /**
     * @param isdefault
     */
    public void setIsdefault(Byte isdefault) {
        this.isdefault = isdefault;
    }
}