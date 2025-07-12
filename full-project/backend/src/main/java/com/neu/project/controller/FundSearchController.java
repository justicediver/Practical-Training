package com.neu.project.controller;

import com.neu.project.entity.Fund;
import com.neu.project.entity.FundFilterCriteria;
import com.neu.project.mapper.FOFMapper;
import com.neu.project.mapper.FundFilterMapper;
import com.neu.project.mapper.FundMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/fund")
public class FundSearchController {
    @Autowired
    private FundMapper fundMapper;
    @Autowired
    private FundFilterMapper fundFilterMapper;
    @Autowired
    private FOFMapper fofMapper;
    // 根据基金代码精确查询
    @GetMapping("/byCode")
    public Fund getByFundCode(@RequestParam int fundcode) {
        return fundMapper.findByFundCode(fundcode);
    }

    // 根据基金名称模糊查询
    @GetMapping("/byName")
    public List<Fund> getByFundName(@RequestParam String fundname) {
        return fundMapper.findByFundName(fundname);
    }

    // 根据公司名称模糊查询
    @GetMapping("/byCompany")
    public List<Fund> getByCompany(@RequestParam String company) {
        return fundMapper.findByCompany(company);
    }

    // 根据基金经理模糊查询
    @GetMapping("/byManager")
    public List<Fund> getByManager(@RequestParam String manager) {
        return fundMapper.findByManager(manager);
    }

    // 组合查询（所有参数都是可选的）
    @GetMapping("/search")
    public List<Fund> searchFunds(
            @RequestParam(required = false) Integer fundcode,
            @RequestParam(required = false) String fundname,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) String manager) {
        return fundMapper.findByCondition(fundcode, fundname, company, manager);
    }
    @RequestMapping("/findAll")
    public List<Fund> getAllFunds() {
            return fundMapper.findAll();
    }

    @GetMapping("/advancedSearch")
    public List<Fund> advancedSearch(
            @RequestParam(required = false) Integer fundcode,
            @RequestParam(required = false) String fundname,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) String manager,
            @RequestParam(required = false) String companySizeLevel,
            @RequestParam(required = false) String companyRatingLevel,
            @RequestParam(required = false) String companyRiskLevel,
            @RequestParam(required = false) String managerAssetLevel,
            @RequestParam(required = false) String managerReturnLevel,
            @RequestParam(required = false) String managerExpLevel) {

        FundFilterCriteria criteria = new FundFilterCriteria(
                fundcode, fundname, company, manager,
                companySizeLevel, companyRatingLevel, companyRiskLevel,
                managerAssetLevel, managerReturnLevel, managerExpLevel
        );

        return fundFilterMapper.filterFunds(criteria);
    }

    @PostMapping("/addToFOF")
    public String addToFOF(@RequestParam int fundcode, @RequestParam int id, @RequestParam String fundname) {
        if(fofMapper.existsFOF(id, fundcode) == 1) {
            return "基金已存在于FOF中";
        }
        fofMapper.insertFundIntoFOF(id, fundcode, fundname);
        return "添加成功";
    }

    // 删除FOF
    @DeleteMapping("/removeFromFOF")
    public String removeFromFOF(@RequestParam int fundcode, @RequestParam int id) {
        if(fofMapper.existsFOF(id, fundcode) == 0) {
            return "基金不存在于FOF中";
        }
        fofMapper.deleteFundIntoFOF(id, fundcode);
        return "删除成功";
    }
    @GetMapping("/checkInFOF")
    public int checkInFOF(@RequestParam int id, @RequestParam int fundcode) {
        return fofMapper.existsFOF(id, fundcode);
    }

}