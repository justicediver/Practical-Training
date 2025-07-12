package com.neu.project.controller;

import com.neu.project.entity.DerivedFactor;
import com.neu.project.entity.SingleFactor;
import com.neu.project.mapper.FactorMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FactorControllerTest {

    @Mock
    private FactorMapper factorMapper;

    @InjectMocks
    private FactorController factorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // 测试方法：createDerivedFactor
    @Test
    void createDerivedFactor_Success() {
        DerivedFactor factor = new DerivedFactor("testFactor", 1, "factor1,factor2", "0.5,0.5");
        when(factorMapper.insertDerivedFactor(factor)).thenReturn(1);

        ResponseEntity<String> response = factorController.createDerivedFactor(factor);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("衍生因子创建成功", response.getBody());
    }

    @Test
    void createDerivedFactor_DatabaseError() {
        DerivedFactor factor = new DerivedFactor("testFactor", 1, "factor1,factor2", "0.5,0.5");
        when(factorMapper.insertDerivedFactor(factor)).thenThrow(new RuntimeException());

        ResponseEntity<String> response = factorController.createDerivedFactor(factor);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void createDerivedFactor_EmptyFactors() {
        DerivedFactor factor = new DerivedFactor("emptyFactor", 1, "", "");
        when(factorMapper.insertDerivedFactor(factor)).thenReturn(1);

        ResponseEntity<String> response = factorController.createDerivedFactor(factor);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    // 测试方法：deleteDerivedFactor
    @Test
    void deleteDerivedFactor_Success() {
        when(factorMapper.deleteDerivedFactor("testFactor", 1)).thenReturn(1);

        ResponseEntity<String> response = factorController.deleteDerivedFactor("testFactor", 1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteDerivedFactor_NotFound() {
        when(factorMapper.deleteDerivedFactor("nonexistent", 1)).thenReturn(0);

        ResponseEntity<String> response = factorController.deleteDerivedFactor("nonexistent", 1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteDerivedFactor_EmptyName() {
        ResponseEntity<String> response = factorController.deleteDerivedFactor("", 1);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // 测试方法：getDerivedFactorsByUser
    @Test
    void getDerivedFactorsByUser_Success() {
        List<DerivedFactor> expected = Arrays.asList(
                new DerivedFactor("factor1", 1, "a,b", "0.3,0.7"),
                new DerivedFactor("factor2", 1, "c,d", "0.5,0.5")
        );
        when(factorMapper.getDerivedFactorsByUser(1)).thenReturn(expected);

        ResponseEntity<List<DerivedFactor>> response = factorController.getDerivedFactorsByUser(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void getDerivedFactorsByUser_Empty() {
        when(factorMapper.getDerivedFactorsByUser(1)).thenReturn(Collections.emptyList());

        ResponseEntity<List<DerivedFactor>> response = factorController.getDerivedFactorsByUser(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    // 测试方法：getDerivedFactorByName
    @Test
    void getDerivedFactorByName_Success() {
        DerivedFactor expected = new DerivedFactor("testFactor", 1, "a,b", "0.5,0.5");
        when(factorMapper.getDerivedFactorByNameAndUser("testFactor", 1)).thenReturn(expected);

        ResponseEntity<DerivedFactor> response = factorController.getDerivedFactorByName("testFactor", 1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
    }

    @Test
    void getDerivedFactorByName_NotFound() {
        when(factorMapper.getDerivedFactorByNameAndUser("nonexistent", 1)).thenReturn(null);

        ResponseEntity<DerivedFactor> response = factorController.getDerivedFactorByName("nonexistent", 1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // 测试方法：getFactorsByFather
    @Test
    void getFactorsByFather_Success() {
        List<SingleFactor> expected = Arrays.asList(
                new SingleFactor("child1", "parent"),
                new SingleFactor("child2", "parent")
        );
        when(factorMapper.getSingleFactorsByFather("parent")).thenReturn(expected);

        ResponseEntity<List<SingleFactor>> response = factorController.getFactorsByFather("parent");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    // 测试方法：getSingleFactorByName
    @Test
    void getSingleFactorByName_Success() {
        SingleFactor expected = new SingleFactor("testFactor", "parent");
        when(factorMapper.getSingleFactorByName("testFactor")).thenReturn(expected);

        ResponseEntity<SingleFactor> response = factorController.getSingleFactorByName("testFactor");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
    }

    @Test
    void getSingleFactorByName_NotFound() {
        when(factorMapper.getSingleFactorByName("nonexistent")).thenReturn(null);

        ResponseEntity<SingleFactor> response = factorController.getSingleFactorByName("nonexistent");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // 测试方法：getAllFatherNames
    @Test
    void getAllFatherNames_Success() {
        List<String> expected = Arrays.asList("father1", "father2");
        when(factorMapper.getAllfather()).thenReturn(expected);

        ResponseEntity<List<String>> response = factorController.getAllFatherNames();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void getAllFatherNames_Error() {
        when(factorMapper.getAllfather()).thenThrow(new RuntimeException());

        ResponseEntity<List<String>> response = factorController.getAllFatherNames();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}