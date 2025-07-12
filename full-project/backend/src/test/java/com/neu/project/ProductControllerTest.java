package com.neu.project.controller;

import com.neu.project.entity.Product;
import com.neu.project.mapper.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void getAll() throws Exception {
        // 准备测试数据
        Product product1 = new Product("Fund1", "Desc1", 1, "未上架", "低风险", "描述1");
        Product product2 = new Product("Fund2", "Desc2", 1, "审核中", "中风险", "描述2");
        List<Product> products = Arrays.asList(product1, product2);

        // 模拟Mapper行为
        when(productMapper.findById(anyInt())).thenReturn(products);

        // 执行并验证
        mockMvc.perform(get("/product/all")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fofname").value("Fund1"))
                .andExpect(jsonPath("$[1].fofname").value("Fund2"));

        // 验证Mapper方法被调用
        verify(productMapper, times(1)).findById(1);
    }

    @Test
    void init() throws Exception {
        // 模拟Mapper行为 - init方法没有返回值，所以不需要when

        // 执行并验证
        mockMvc.perform(get("/product/init")
                        .param("fofname", "NewFund")
                        .param("dfname", "NewDescription")
                        .param("id", "1"))
                .andExpect(status().isOk());

        // 验证Mapper方法被调用
        verify(productMapper, times(1)).init("NewFund", "NewDescription", 1);
    }

    @Test
    void update() throws Exception {
        // 模拟Mapper行为 - update方法没有返回值，所以不需要when

        // 执行并验证
        mockMvc.perform(get("/product/update")
                        .param("fofname", "ExistingFund")
                        .param("id", "1")
                        .param("risk", "高风险")
                        .param("description", "更新后的描述"))
                .andExpect(status().isOk());

        // 验证Mapper方法被调用
        verify(productMapper, times(1)).update("ExistingFund", 1, "高风险", "更新后的描述");
    }
}