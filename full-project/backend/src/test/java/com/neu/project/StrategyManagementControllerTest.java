package com.neu.project.controller;

import com.neu.project.entity.DerivedFactor;
import com.neu.project.entity.FOF;
import com.neu.project.mapper.FOFMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StrategyManagementControllerTest {

    @Mock
    private FOFMapper fofMapper;

    @InjectMocks
    private StrategyManagementController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getFundConfigurations() {
        // 准备测试数据
        int userId = 1;
        List<FOF> expectedConfigurations = new ArrayList<>();
        expectedConfigurations.add(new FOF(1, "name", null, userId, 1001, "基金A", 30));
        expectedConfigurations.add(new FOF(2, "name", null, userId, 1002, "基金B", 70));

        // 模拟Mapper行为
        when(fofMapper.findUserFundConfigurations(userId)).thenReturn(expectedConfigurations);

        // 调用测试方法
        List<FOF> actualConfigurations = controller.getFundConfigurations(userId);

        // 验证结果
        assertEquals(expectedConfigurations.size(), actualConfigurations.size());
        assertEquals(expectedConfigurations.get(0).getFundcode(), actualConfigurations.get(0).getFundcode());
        verify(fofMapper, times(1)).findUserFundConfigurations(userId);
    }

    @Test
    void saveFundConfigurations() {
        // 准备测试数据
        List<FOF> configurations = new ArrayList<>();
        configurations.add(new FOF(1, "name", null, 1, 1001, "基金A", 30));
        configurations.add(new FOF(2, "name", null, 1, 1002, "基金B", 70));

        // 调用测试方法
        int result = controller.saveFundConfigurations(configurations);

        // 验证结果
        assertEquals(1, result);
        verify(fofMapper, times(2)).updateFundPercentage(anyInt(), anyInt(), anyInt());
    }

    @Test
    void saveFundConfigurations_EmptyList() {
        // 准备空列表
        List<FOF> configurations = new ArrayList<>();

        // 调用测试方法
        int result = controller.saveFundConfigurations(configurations);

        // 验证结果
        assertEquals(0, result);
        verify(fofMapper, never()).updateFundPercentage(anyInt(), anyInt(), anyInt());
    }

    @Test
    void addFundToConfiguration() {
        // 准备测试数据
        FOF newFund = new FOF(0, "name", null, 1, 1003, "基金C", 0);

        // 模拟Mapper行为
        when(fofMapper.existsFOF(1, 1003)).thenReturn(0);

        // 调用测试方法
        int result = controller.addFundToConfiguration(newFund);

        // 验证结果
        assertEquals(1, result);
        verify(fofMapper, times(1)).insertFundIntoFOF(1, 1003, "基金C");
    }

    @Test
    void addFundToConfiguration_FundAlreadyExists() {
        // 准备测试数据
        FOF existingFund = new FOF(0, "name", null, 1, 1001, "基金A", 0);

        // 模拟Mapper行为
        when(fofMapper.existsFOF(1, 1001)).thenReturn(1);

        // 调用测试方法
        int result = controller.addFundToConfiguration(existingFund);

        // 验证结果
        assertEquals(0, result);
        verify(fofMapper, never()).insertFundIntoFOF(anyInt(), anyInt(), anyString());
    }

    @Test
    void removeFundFromConfiguration() {
        // 准备测试数据
        FOF fundToRemove = new FOF(0, "name", null, 1, 1001, "基金A", 0);

        // 调用测试方法
        int result = controller.removeFundFromConfiguration(fundToRemove);

        // 验证结果
        assertEquals(1, result);
        verify(fofMapper, times(1)).deleteFundIntoFOF(1, 1001);
    }

    @Test
    void getDerivedFactors() {
        // 准备测试数据
        int userId = 1;
        List<DerivedFactor> expectedFactors = new ArrayList<>();
        expectedFactors.add(new DerivedFactor("策略1", userId, "因子A,因子B", "50,50"));

        // 模拟Mapper行为
        when(fofMapper.findUserDerivedFactors(userId)).thenReturn(expectedFactors);

        // 调用测试方法
        List<DerivedFactor> actualFactors = controller.getDerivedFactors(userId);

        // 验证结果
        assertEquals(expectedFactors.size(), actualFactors.size());
        assertEquals(expectedFactors.get(0).getDfname(), actualFactors.get(0).getDfname());
        verify(fofMapper, times(1)).findUserDerivedFactors(userId);
    }

    @Test
    void saveDfnameToFundConfigurations() {
        // 准备测试数据
        DerivedFactor derivedFactor = new DerivedFactor("新策略", 1, null, null);

        // 调用测试方法
        int result = controller.saveDfnameToFundConfigurations(derivedFactor);

        // 验证结果
        assertEquals(1, result);
        verify(fofMapper, times(1)).updateDfnameForAllFunds(1, "新策略");
    }

    @Test
    void updateFofname() {
        // 准备测试数据
        FOF fof = new FOF();
        fof.setId(1);
        fof.setFofname("新配置名称");

        // 调用测试方法
        int result = controller.updateFofname(fof);

        // 验证结果
        assertEquals(1, result);
        verify(fofMapper, times(1)).updateFofnameForAllFunds(1, "新配置名称");
    }
}