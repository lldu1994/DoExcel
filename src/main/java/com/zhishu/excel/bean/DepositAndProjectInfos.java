package com.zhishu.excel.bean;

/**
 * 交存和项目信息
 */
public class DepositAndProjectInfos {
    /**
     * excel上序号
     */
    private String index;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 楼幢号
     */
    private String buildNum;

    /**
     * 房屋编号，唯一编号
     */
    private String roomId;


    /**
     * 房屋层数
     */
    private String floor;

    /**
     * 室号
     */
    private String roomNO;


    /**
     * 用途
     */
    private String purpose;

    /**
     * 建筑面积（实测）
     */
    private String actualArea;


    /**
     * 购房人
     */
    private String buyHouse;

    /**
     * 备注
     */
    private String remark;


    /**
     * 支付顺序
     */
    private int payIndex;

    /**
     * 支付顺序名称
     */
    private String payIndexName;

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

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getBuildNum() {
        return buildNum;
    }

    public void setBuildNum(String buildNum) {
        this.buildNum = buildNum;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getRoomNO() {
        return roomNO;
    }

    public void setRoomNO(String roomNO) {
        this.roomNO = roomNO;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getActualArea() {
        return actualArea;
    }

    public void setActualArea(String actualArea) {
        this.actualArea = actualArea;
    }

    public String getBuyHouse() {
        return buyHouse;
    }

    public void setBuyHouse(String buyHouse) {
        this.buyHouse = buyHouse;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getPayIndex() {
        return payIndex;
    }

    public void setPayIndex(int payIndex) {
        this.payIndex = payIndex;
    }

    public String getPayIndexName() {
        return payIndexName;
    }

    public void setPayIndexName(String payIndexName) {
        this.payIndexName = payIndexName;
    }

    public String getDepositStandard() {
        return depositStandard;
    }

    public void setDepositStandard(String depositStandard) {
        this.depositStandard = depositStandard;
    }

    public String getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(String depositAmount) {
        this.depositAmount = depositAmount;
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
