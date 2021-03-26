package com.zhishu.excel.dao;

import com.zhishu.excel.bean.DepositInfo;
import com.zhishu.excel.bean.ProjectInfo;
import com.zhishu.excel.bean.TempInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DealExcelDao {


    /**
     * 批量新增(项目信息)
     */
    void batchProjectInserts(List<ProjectInfo> projectInfoLists);

    /**
     * 批量新增(交存信息)
     */
    void batchDepositInserts(List<DepositInfo> depositInfoList);

    /**
     * 批量新增(项目信息)
     */
    void projectInsert(ProjectInfo projectInfoList);

    /**
     * 批量新增(交存信息)
     */
    void depositInserts(DepositInfo depositInfoList);


    /**
     * 临时数据保存
     *
     */
    void saveTempInf(TempInfo tempInfo);

    /**
     * 临时数据查询
     *
     */
    String selectTempInf(String id);
}
