package com.yjxxt.query;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.yjxxt.base.BaseQuery;

public class SaleChanceQuery  extends BaseQuery {
    private String customerName;
    private String creatMan;
    private Integer state;

    public SaleChanceQuery() {
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCreatMan() {
        return creatMan;
    }

    public void setCreatMan(String creatMan) {
        this.creatMan = creatMan;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "SaleChanceQuery{" +
                "customerName='" + customerName + '\'' +
                ", creatMan='" + creatMan + '\'' +
                ", state=" + state +
                '}';
    }
}
