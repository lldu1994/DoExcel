package com.zhishu.excel.controller;

import com.zhishu.excel.service.AbstractDealExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExcelController {

    @Autowired
    private AbstractDealExcel abstractDealExcel;


    @ResponseBody
    @RequestMapping("/doExcel")
    public String doExcel() {
        abstractDealExcel.dealExcel(null);
        return "导入成功";
    }


    @ResponseBody
    @RequestMapping("/saveDB")
    public String saveDB() {
        abstractDealExcel.dealExcel("save");
        return "导入成功";
    }

}
