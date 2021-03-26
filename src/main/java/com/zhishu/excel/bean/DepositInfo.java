package com.zhishu.excel.bean;


/**
 * 交存信息
 */
public class DepositInfo {
    /**
     * 主键
     */
    private String id;

    /**
     * 支付顺序
     */
    private int serialNo;

    /**
     * 支付顺序名称
     */
    private String indexName;

    /**
     * 交存标准
     */
    private String depositStandard;


    /**
     * 交存金额
     */
    private String depositAmount;


    /**
     * 日期
     */
    private String depositDate;

    /**
     * 票据号
     */
    private String billNo;

    /**
     * 凭证号
     */
    private String certificateNo;

    /**
     * 跟项目关联
     */
    private String projectId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public int getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(int serialNo) {
        this.serialNo = serialNo;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getDepositStandard() {
        return depositStandard;
    }

    public String getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(String depositAmount) {
        this.depositAmount = depositAmount;
    }

    public void setDepositStandard(String depositStandard) {
        this.depositStandard = depositStandard;
    }


    public String getDepositDate() {
        return depositDate;
    }

    public void setDepositDate(String depositDate) {
        this.depositDate = depositDate;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
    }
}
