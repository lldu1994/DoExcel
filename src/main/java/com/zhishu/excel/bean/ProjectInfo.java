package com.zhishu.excel.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 保存楼盘的项目信息
 */
public class ProjectInfo {

    /**
     * id
     */
    private String id;

    /**
     * excel上序号
     */
    private String serialNo;

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
     * 支付信息
     */
    private List<DepositInfo> depositInfos =new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
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

    public List<DepositInfo> getDepositInfos() {
        return depositInfos;
    }

    public void setDepositInfos(List<DepositInfo> depositInfos) {
        this.depositInfos = depositInfos;
    }
}
