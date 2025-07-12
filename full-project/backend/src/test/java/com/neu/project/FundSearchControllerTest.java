package com.neu.project.controller;

import com.neu.project.entity.Fund;
import com.neu.project.entity.FundFilterCriteria;
import com.neu.project.entity.FOF;
import com.neu.project.mapper.FOFMapper;
import com.neu.project.mapper.FundFilterMapper;
import com.neu.project.mapper.FundMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FundSearchControllerTest {

    @Mock private FundMapper fundMapper;
    @Mock private FundFilterMapper fundFilterMapper;
    @Mock private FOFMapper fofMapper;

    @InjectMocks private FundSearchController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // 测试方法：getByFundCode
    @Test
    void getByFundCode_Success() {
        Fund expected = new Fund(12345, "Test Fund", "Company", "Manager", 100, 10, 50, "Info");
        when(fundMapper.findByFundCode(12345)).thenReturn(expected);

        Fund result = controller.getByFundCode(12345);

        assertEquals(expected, result);
    }

    @Test
    void getByFundCode_NotFound() {
        when(fundMapper.findByFundCode(99999)).thenReturn(null);

        Fund result = controller.getByFundCode(99999);

        assertNull(result);
    }

    // 测试方法：getByFundName
    @Test
    void getByFundName_Success() {
        List<Fund> expected = Arrays.asList(
                new Fund(1, "Test Fund 1", "Company 1", "Manager 1", 100, 10, 50, "Info 1"),
                new Fund(2, "Test Fund 2", "Company 2", "Manager 2", 200, 15, 60, "Info 2")
        );
        when(fundMapper.findByFundName("Test")).thenReturn(expected);

        List<Fund> result = controller.getByFundName("Test");

        assertEquals(2, result.size());
    }

    @Test
    void getByFundName_Empty() {
        when(fundMapper.findByFundName("")).thenReturn(Collections.emptyList());

        List<Fund> result = controller.getByFundName("");

        assertTrue(result.isEmpty());
    }

    // 测试方法：getByCompany
    @Test
    void getByCompany_Success() {
        List<Fund> expected = Arrays.asList(
                new Fund(1, "Fund 1", "Test Company", "Manager 1", 100, 10, 50, "Info 1"),
                new Fund(2, "Fund 2", "Test Company", "Manager 2", 200, 15, 60, "Info 2")
        );
        when(fundMapper.findByCompany("Test Company")).thenReturn(expected);

        List<Fund> result = controller.getByCompany("Test Company");

        assertEquals(2, result.size());
    }

    // 测试方法：getByManager
    @Test
    void getByManager_Success() {
        List<Fund> expected = Arrays.asList(
                new Fund(1, "Fund 1", "Company 1", "Test Manager", 100, 10, 50, "Info 1"),
                new Fund(2, "Fund 2", "Company 2", "Test Manager", 200, 15, 60, "Info 2")
        );
        when(fundMapper.findByManager("Test Manager")).thenReturn(expected);

        List<Fund> result = controller.getByManager("Test Manager");

        assertEquals(2, result.size());
    }

    // 测试方法：searchFunds
    @Test
    void searchFunds_Success() {
        List<Fund> expected = Collections.singletonList(
                new Fund(12345, "Test Fund", "Test Company", "Test Manager", 100, 10, 50, "Info")
        );
        when(fundMapper.findByCondition(12345, "Test Fund", "Test Company", "Test Manager"))
                .thenReturn(expected);

        List<Fund> result = controller.searchFunds(12345, "Test Fund", "Test Company", "Test Manager");

        assertEquals(1, result.size());
    }

    @Test
    void searchFunds_EmptyCondition() {
        List<Fund> expected = Arrays.asList(
                new Fund(1, "Fund 1", "Company 1", "Manager 1", 100, 10, 50, "Info 1"),
                new Fund(2, "Fund 2", "Company 2", "Manager 2", 200, 15, 60, "Info 2")
        );
        when(fundMapper.findByCondition(null, null, null, null)).thenReturn(expected);

        List<Fund> result = controller.searchFunds(null, null, null, null);

        assertEquals(2, result.size());
    }

    // 测试方法：getAllFunds
    @Test
    void getAllFunds_Success() {
        List<Fund> expected = Arrays.asList(
                new Fund(1, "Fund 1", "Company 1", "Manager 1", 100, 10, 50, "Info 1"),
                new Fund(2, "Fund 2", "Company 2", "Manager 2", 200, 15, 60, "Info 2")
        );
        when(fundMapper.findAll()).thenReturn(expected);

        List<Fund> result = controller.getAllFunds();

        assertEquals(2, result.size());
    }

    // 测试方法：advancedSearch
    @Test
    void advancedSearch_Success() {
        List<Fund> expected = Collections.singletonList(
                new Fund(12345, "Test Fund", "Test Company", "Test Manager", 100, 10, 50, "Info")
        );
        when(fundFilterMapper.filterFunds(any(FundFilterCriteria.class))).thenReturn(expected);

        List<Fund> result = controller.advancedSearch(
                12345, "Test Fund", "Test Company", "Test Manager",
                "LARGE", "HIGH", "MEDIUM", "LARGE", "HIGH", "SENIOR"
        );

        assertEquals(1, result.size());
    }

    // 测试方法：addToFOF
    @Test
    void addToFOF_Success() {
        when(fofMapper.existsFOF(1, 12345)).thenReturn(0);

        String result = controller.addToFOF(12345, 1, "Test Fund");

        assertEquals("添加成功", result);
    }

    @Test
    void addToFOF_AlreadyExists() {
        when(fofMapper.existsFOF(1, 12345)).thenReturn(1);

        String result = controller.addToFOF(12345, 1, "Test Fund");

        assertEquals("基金已存在于FOF中", result);
    }

    // 测试方法：removeFromFOF
    @Test
    void removeFromFOF_Success() {
        when(fofMapper.existsFOF(1, 12345)).thenReturn(1);

        String result = controller.removeFromFOF(12345, 1);

        assertEquals("删除成功", result);
    }

    @Test
    void removeFromFOF_NotExists() {
        when(fofMapper.existsFOF(1, 12345)).thenReturn(0);

        String result = controller.removeFromFOF(12345, 1);

        assertEquals("基金不存在于FOF中", result);
    }

    // 测试方法：checkInFOF
    @Test
    void checkInFOF_Exists() {
        when(fofMapper.existsFOF(1, 12345)).thenReturn(1);

        int result = controller.checkInFOF(1, 12345);

        assertEquals(1, result);
    }

    @Test
    void checkInFOF_NotExists() {
        when(fofMapper.existsFOF(1, 12345)).thenReturn(0);

        int result = controller.checkInFOF(1, 12345);

        assertEquals(0, result);
    }
}