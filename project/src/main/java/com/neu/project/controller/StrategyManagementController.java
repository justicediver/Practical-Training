package com.neu.project.controller;

import com.neu.project.entity.DerivedFactor;
import com.neu.project.entity.FOF;
import com.neu.project.mapper.FOFMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/strategy")
public class StrategyManagementController {

    @Autowired
    private FOFMapper fofMapper;

    // 获取用户的资产配置
    @GetMapping("/config")
    public List<FOF> getFundConfigurations(@RequestParam int id) {
        return fofMapper.findUserFundConfigurations(id);
    }

    // 保存资产配置
    @PostMapping("/save")
    public int saveFundConfigurations(@RequestBody List<FOF> configurations) {
        if (configurations == null || configurations.isEmpty()) {
            return 0;
        }

        int userId = configurations.get(0).getId(); // 假设所有配置项属于同一用户

        try {
            for (FOF config : configurations) {
                fofMapper.updateFundPercentage(userId, config.getFundcode(), config.getPercent());
            }
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 添加新基金到配置
    @PostMapping("/add")
    public int addFundToConfiguration(@RequestBody FOF fof) {
        if (fofMapper.existsFOF(fof.getId(), fof.getFundcode()) == 1) {
            return 0; // 基金已存在
        }

        try {
            fofMapper.insertFundIntoFOF(fof.getId(), fof.getFundcode(), fof.getFundname());
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 从配置中删除基金
    @PostMapping("/remove")
    public int removeFundFromConfiguration(@RequestBody FOF fof) {
        try {
            fofMapper.deleteFundIntoFOF(fof.getId(), fof.getFundcode());
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 获取用户的衍生因子配置
    @GetMapping("/derivedFactors")
    public List<DerivedFactor> getDerivedFactors(@RequestParam int id) {
        return fofMapper.findUserDerivedFactors(id);
    }
}