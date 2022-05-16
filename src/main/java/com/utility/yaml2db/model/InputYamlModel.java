package com.utility.yaml2db.model;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class InputYamlModel {
    public InputYamlModel(){}
    private BigInteger osmId;
    private BigInteger[] seorenId;

    public BigInteger getOsmId() {
        return osmId;
    }

    public void setOsmId(BigInteger osmId) {
        this.osmId = osmId;
    }

    public BigInteger[] getSeorenId() {
        return seorenId;
    }

    public void setSeorenId(BigInteger[] seorenId) {
        this.seorenId = seorenId;
    }

    @Override
    public String toString() {
        return "InputYamlModel{" +
                "osmId=" + osmId +
                ", seorenId=" + Arrays.toString(seorenId) +
                '}';
    }
}
