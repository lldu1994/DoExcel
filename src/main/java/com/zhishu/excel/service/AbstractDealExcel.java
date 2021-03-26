package com.zhishu.excel.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zhishu.excel.bean.DepositInfo;
import com.zhishu.excel.bean.ProjectInfo;
import com.zhishu.excel.bean.TempInfo;
import com.zhishu.excel.dao.DealExcelDao;
import com.zhishu.excel.util.POIUtil;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 票据信息处理
 */
@Service
public class AbstractDealExcel {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDealExcel.class);


    /**
     * 读取Excel文件位置
     */
    private String srcExcelPath;

    /**
     * 写到Excel文件位置
     */
    private String desExcelPath;

    /**
     * 文件后缀格式
     */
    private final String fileSuffix = ".xlsx";


    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    private DealExcelDao dealExcelDao;

    /**
     * String 代表唯一的键格式为（roomID）
     */
    private Map<String, ProjectInfo> allData = new ConcurrentHashMap<>();


    /**
     * String 代表唯一的键格式为（roomID）
     */
    //private Map<String, ProjectInfo> incorrectData = new ConcurrentHashMap<>();


    @Transactional
    public Boolean dealExcel(String save) {

        //第一步 获取文件得地址 判断文件存不存在
        Boolean containExcelFile = isContainExcelFile();
        //先从表里面获取数据
        String s = dealExcelDao.selectTempInf("1");
        if (!StringUtils.isEmpty(s)) {
            allData = JSON.parseObject(s, new TypeReference<Map<String, ProjectInfo>>() {
            });
        }

        if (allData.isEmpty()) {
            //第二步 解析原文件，并且封装成对象;并且对已经完成解析的数据进行处理，（筛选出支取的数据为负数的）
            parseSrcExcel();
        }

        //第三步 写入到db里面
        storeDb();


        return true;
    }

    /**
     * 获取文件得地址 判断文件存不存在
     *
     * @return
     */
    public Boolean isContainExcelFile() {

        if (StringUtils.isEmpty(srcExcelPath) && StringUtils.isEmpty(desExcelPath)) {
            this.srcExcelPath = "C:\\Users\\86131\\Downloads\\已核对数据";
            this.desExcelPath = "C:\\Users\\86131\\Downloads\\新数据";
        }

        //原文件路径
        File srcFile = new File(srcExcelPath);
        if (srcFile.exists() && srcFile.setReadable(true)) {
            String[] list = srcFile.list();
            //找出不合法得路径
            List<String> srcInvalidPaths = Arrays.stream(list).filter(srcPath -> !srcPath.endsWith(fileSuffix)).collect(Collectors.toList());

            srcInvalidPaths.forEach(srcInvalidPath -> {
                LOGGER.info("存在不合法得路径，请确认{}", srcInvalidPath);
            });

            if (!srcInvalidPaths.isEmpty()) {
                return false;
            }

        }
        //创建目标文件
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyymmdd");
        File descExcelFile = new File(this.desExcelPath);

        if (!descExcelFile.getParentFile().exists()) {
            descExcelFile.getParentFile().mkdirs();
        }

        String fileName = "大丰物管数据合并 " + sdf.format(date) + fileSuffix;
        desExcelPath = this.desExcelPath + File.separator + fileName;
        return true;
    }


    /**
     * 解析原文件，并且封装成对象
     */
    public void parseSrcExcel() {
        File srcFile = new File(srcExcelPath);
        List<String> srcFilePaths = Arrays.asList(srcFile.list());

        for (int i = 0; i < srcFilePaths.size(); i++) {

            if (srcFilePaths.get(i).contains("~$")) {
                continue;
            }

            //开始解析Excel文件
            LOGGER.info("第{}个文件，名称为{}", i + 1, srcFilePaths.get(i));
            List<ProjectInfo> excelData = getExcelData(srcFile.getAbsolutePath() + File.separator + srcFilePaths.get(i));
            LOGGER.info("增量的房屋数量为：{}", excelData.size());

        }
        if (!allData.isEmpty()) {
            TempInfo tempInfo = new TempInfo();
            tempInfo.setId("1");
            tempInfo.setTempInfo(JSON.toJSONString(allData));
            dealExcelDao.saveTempInf(tempInfo);
        }

    }

    public static Workbook getWorkbook(String filePath) {
        Workbook workbook = null;
        try {
            InputStream inputStream = new FileInputStream(filePath);
            if (filePath.endsWith(".xls")) {
                workbook = new HSSFWorkbook(inputStream);
            } else if (filePath.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(inputStream);
            }
            inputStream.close();
        } catch (IOException e) {
            LOGGER.info("获取文件失败{}", filePath, e);
        }
        return workbook;
    }


    /**
     * 获取每一个Excel里面的数据
     *
     * @return
     */
    public List<ProjectInfo> getExcelData(String srcFilePath) {
        List<ProjectInfo> list = new ArrayList<>();

        Workbook sheets = getWorkbook(srcFilePath);

        /* 读EXCEL文字内容 */
        // 获取第一个sheet表，也可使用sheet表名获取
        Sheet sheet = sheets.getSheetAt(0);
        try {
            sheets.close();

            // 获取行
            Iterator<Row> rows = sheet.rowIterator();

            Row row;

            while (rows.hasNext()) {
                row = rows.next();
                int rowNum = row.getRowNum();
                if (rowNum <= 1) {
                    continue;
                }

                ProjectInfo projectInfo = new ProjectInfo();

                //第一笔支取
                DepositInfo depositInfo1 = new DepositInfo();
                depositInfo1.setSerialNo(1);
                depositInfo1.setIndexName("第1笔");

                //第二笔支取
                DepositInfo depositInfo2 = new DepositInfo();
                depositInfo2.setSerialNo(2);
                depositInfo2.setIndexName("第2笔");

                short lastCellNum = row.getLastCellNum();
                for (int i = 0; i < lastCellNum - 1; i++) {
                    Cell value = row.getCell(i);
                    String cellValue = POIUtil.getCellValue(value);

                    switch (i) {
                        case 0:
                            projectInfo.setSerialNo(cellValue);
                            break;
                        case 1:
                            projectInfo.setProjectName(cellValue);
                            break;
                        case 2:
                            projectInfo.setBuildNum(cellValue);
                            break;
                        case 3:
                            projectInfo.setRoomId(cellValue);
                            break;
                        case 4:
                            projectInfo.setFloor(cellValue);
                            break;
                        case 5:
                            projectInfo.setRoomNO(cellValue);
                            break;
                        case 6:
                            projectInfo.setPurpose(cellValue);
                            break;
                        case 7:
                            projectInfo.setActualArea(cellValue);
                            break;
                        case 8:
                            projectInfo.setBuyHouse(cellValue);
                            break;
                        case 9:
                            projectInfo.setRemark(cellValue);
                            break;
                    }

                    //第一次支取
                    switch (i) {
                        case 10:
                            depositInfo1.setDepositStandard(cellValue);
                            break;
                        case 11:
                            depositInfo1.setDepositAmount(cellValue);
                            break;
                        case 12:
                            depositInfo1.setDepositDate(cellValue);
                            break;
                        case 13:
                            depositInfo1.setBillNo(cellValue);
                            break;
                        case 14:
                            depositInfo1.setCertificateNo(cellValue);
                            break;
                    }
                    //第二次支取
                    switch (i) {
                        case 15:
                            depositInfo2.setDepositStandard(cellValue);
                            break;
                        case 16:
                            depositInfo2.setDepositAmount(cellValue);
                            break;
                        case 17:
                            depositInfo2.setDepositDate(cellValue);
                            break;
                        case 18:
                            depositInfo2.setBillNo(cellValue);
                            break;
                        case 19:
                            depositInfo2.setCertificateNo(cellValue);
                            break;
                    }

                    if (lastCellNum - 2 == i) {
                        ProjectInfo alreadyProjectInfo = allData.get(projectInfo.getRoomId().trim());
                        if (alreadyProjectInfo == null) {
                            allData.put(projectInfo.getRoomId().trim(), projectInfo);
                            list.add(projectInfo);
                        } else {
                            if (!StringUtils.isEmpty(alreadyProjectInfo.getRoomId())) {
                                //log.info("已存在的业主信息{}" , alreadyProjectInfo.getRoomId());
                            }
                        }

                        ProjectInfo finalProjectInfo = allData.get(projectInfo.getRoomId().trim());
                        List<DepositInfo> depositInfos = new ArrayList<>();
                        if ((!StringUtils.isEmpty(depositInfo1.getDepositStandard()) ||
                                !StringUtils.isEmpty(depositInfo1.getDepositAmount()) ||
                                !StringUtils.isEmpty(depositInfo1.getDepositDate()) ||
                                !StringUtils.isEmpty(depositInfo1.getBillNo()) ||
                                !StringUtils.isEmpty(depositInfo1.getCertificateNo()))) {
                            depositInfos.add(depositInfo1);
                        }
                        if ((!StringUtils.isEmpty(depositInfo2.getDepositStandard()) ||
                                !StringUtils.isEmpty(depositInfo2.getDepositAmount()) ||
                                !StringUtils.isEmpty(depositInfo2.getDepositDate()) ||
                                !StringUtils.isEmpty(depositInfo2.getBillNo()) ||
                                !StringUtils.isEmpty(depositInfo2.getCertificateNo()))) {
                            depositInfos.add(depositInfo2);
                        }
                        finalProjectInfo.getDepositInfos().addAll(depositInfos);

                        //保存支取为负数的记录

                        /*if ((depositInfo1.getDepositAmount() != null && depositInfo1.getDepositAmount().contains("-"))) {
                            //先从Map里面获取
                            ProjectInfo incorrectProjectInfo1 = incorrectData.get(projectInfo.getRoomId().trim());
                            if (incorrectProjectInfo1 == null) {
                                ProjectInfo newIncorrectProjectInfo1 = new ProjectInfo();
                                newIncorrectProjectInfo1.setSerialNo(projectInfo.getSerialNo());
                                newIncorrectProjectInfo1.setProjectName(projectInfo.getProjectName());
                                newIncorrectProjectInfo1.setBuildNum(projectInfo.getBuildNum());
                                newIncorrectProjectInfo1.setRoomId(projectInfo.getRoomId());
                                newIncorrectProjectInfo1.setFloor(projectInfo.getFloor());
                                newIncorrectProjectInfo1.setRoomNO(projectInfo.getRoomNO());
                                newIncorrectProjectInfo1.setPurpose(projectInfo.getPurpose());
                                newIncorrectProjectInfo1.setActualArea(projectInfo.getActualArea());
                                newIncorrectProjectInfo1.setBuyHouse(projectInfo.getBuyHouse());
                                newIncorrectProjectInfo1.setRemark(projectInfo.getRemark());
                                incorrectData.put(projectInfo.getRoomId().trim(), newIncorrectProjectInfo1);

                                List<DepositInfo> incorrectDepositInfos = new ArrayList<>();
                                incorrectDepositInfos.add(depositInfo1);
                                newIncorrectProjectInfo1.setDepositInfos(incorrectDepositInfos);
                            } else {
                                incorrectProjectInfo1.getDepositInfos().add(depositInfo1);
                            }
                        }
                    }*/

                    /*if ((depositInfo2.getDepositAmount() != null && depositInfo2.getDepositAmount().contains("-"))) {
                        //先从Map里面获取
                        ProjectInfo incorrectProjectInfo2 = incorrectData.get(projectInfo.getRoomId().trim());
                        if (incorrectProjectInfo2 == null) {
                            ProjectInfo newIncorrectProjectInfo2 = new ProjectInfo();
                            newIncorrectProjectInfo2.setSerialNo(projectInfo.getSerialNo());
                            newIncorrectProjectInfo2.setProjectName(projectInfo.getProjectName());
                            newIncorrectProjectInfo2.setBuildNum(projectInfo.getBuildNum());
                            newIncorrectProjectInfo2.setRoomId(projectInfo.getRoomId());
                            newIncorrectProjectInfo2.setFloor(projectInfo.getFloor());
                            newIncorrectProjectInfo2.setRoomNO(projectInfo.getRoomNO());
                            newIncorrectProjectInfo2.setPurpose(projectInfo.getPurpose());
                            newIncorrectProjectInfo2.setActualArea(projectInfo.getActualArea());
                            newIncorrectProjectInfo2.setBuyHouse(projectInfo.getBuyHouse());
                            newIncorrectProjectInfo2.setRemark(projectInfo.getRemark());
                            incorrectData.put(projectInfo.getRoomId().trim(), newIncorrectProjectInfo2);

                            List<DepositInfo> incorrectDepositInfos = new ArrayList<>();
                            incorrectDepositInfos.add(depositInfo2);
                            newIncorrectProjectInfo2.setDepositInfos(incorrectDepositInfos);
                        } else {
                            incorrectProjectInfo2.getDepositInfos().add(depositInfo2);
                        }*/
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * 封装后的数据保存到DB里面
     */
    public void storeDb() {
        List<ProjectInfo> projectInfoList = new ArrayList<>();
        List<DepositInfo> depositInfoList = new ArrayList<>();

        Set<String> strings = allData.keySet();
        for (String string : strings) {
            ProjectInfo projectInfo = allData.get(string);
            projectInfo.setId(getUUID());
            String index = projectInfo.getSerialNo();
            if (StringUtils.isEmpty(index)) {
                continue;
            }
            String projectName = projectInfo.getProjectName();
            if (StringUtils.isEmpty(projectName)) {
                LOGGER.info("项目为空的id为：{}", index);
            }
            projectInfoList.add(projectInfo);
            if (projectInfo != null) {
                List<DepositInfo> depositInfos = projectInfo.getDepositInfos();
                if (!depositInfos.isEmpty()) {
                    for (DepositInfo depositInfo : depositInfos) {
                        depositInfo.setProjectId(projectInfo.getRoomId());
                        depositInfo.setId(getUUID());
                        depositInfoList.add(depositInfo);
                    }
                }
            }

        }
        mybatisSqlSession(projectInfoList);
        mybatisSqlSession(depositInfoList);
    }


    public static boolean saveWorkbook(String filePath, Workbook workbook) {
        try {
            File file = new File(filePath);
            FileOutputStream fileoutputStream = new FileOutputStream(file);
            workbook.write(fileoutputStream);
            fileoutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 随机生成UUID
     *
     * @return
     */
    public synchronized String getUUID() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        String uuidStr = str.replace("-", "");
        return uuidStr;
    }

    public void mybatisSqlSession(List<?> data) {

        SqlSession batchSqlSession = null;
        try {
            batchSqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
            int batchCount = 500;//每批commit的个数
            for (int index = 0; index < data.size(); index++) {
                Object object = data.get(index);
                if (object instanceof ProjectInfo) {
                    batchSqlSession.getMapper(DealExcelDao.class).projectInsert((ProjectInfo) object);
                } else if (object instanceof DepositInfo) {
                    batchSqlSession.getMapper(DealExcelDao.class).depositInserts((DepositInfo) object);
                }
                if (index != 0 && index % batchCount == 0) {
                    batchSqlSession.commit();
                }
            }
            batchSqlSession.commit();
        } catch (Exception e) {
            batchSqlSession.rollback();
            e.printStackTrace();
        } finally {
            if (batchSqlSession != null) {
                batchSqlSession.close();
            }
        }
    }

    /**
     * 获取要读取得excel文件地址，和要写到得excel文件地址
     */
    //public abstract void getSrcAndDesExcelPath();
    public static void main(String[] args) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext("com.zhishu");
        AbstractDealExcel bean = (AbstractDealExcel) annotationConfigApplicationContext.getBean("abstractDealExcel");
        bean.dealExcel(null);
    }
}
