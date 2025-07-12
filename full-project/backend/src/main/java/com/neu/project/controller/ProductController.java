package com.neu.project.controller;

import com.neu.project.entity.Product;
import com.neu.project.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductMapper productMapper;

    @GetMapping("/all")
    public List<Product> getAll(@RequestParam("id") int id){
        return productMapper.findById(id);
    }

    @GetMapping("/init")
    public void init(String fofname,String dfname,int id){
        productMapper.init(fofname,dfname,id);
    }

    @GetMapping("/update")
    public void update(String fofname,int id,String risk,String description){
        productMapper.update(fofname,id,risk,description);
    }
}