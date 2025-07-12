
package com.neu.project.controller;

import com.neu.project.entity.DerivedFactor;
import com.neu.project.entity.SingleFactor;
import com.neu.project.mapper.FactorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/factors")
public class FactorController {

    private final FactorMapper factorMapper;

    @Autowired
    public FactorController(FactorMapper factorMapper) {
        this.factorMapper = factorMapper;
    }

    @PostMapping("/add")
    public ResponseEntity<String> createDerivedFactor(@RequestBody DerivedFactor factor) {
        try {
            factorMapper.insertDerivedFactor(factor);
            return ResponseEntity.status(HttpStatus.CREATED).body("衍生因子创建成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("创建失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{dfname}/{id}")
    public ResponseEntity<String> deleteDerivedFactor(
            @PathVariable String dfname,
            @PathVariable int id) {
        int result = factorMapper.deleteDerivedFactor(dfname, id);
        return result > 0 ?
                ResponseEntity.ok("衍生因子删除成功") :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("未找到指定衍生因子");
    }

    @GetMapping("/derived/user/{id}")
    public ResponseEntity<List<DerivedFactor>> getDerivedFactorsByUser(
            @PathVariable int id) {
        List<DerivedFactor> factors = factorMapper.getDerivedFactorsByUser(id);
        return ResponseEntity.ok(factors);
    }

    @GetMapping("/derived/{dfname}/{id}")
    public ResponseEntity<DerivedFactor> getDerivedFactorByName(
            @PathVariable String dfname,
            @PathVariable int id) {
        DerivedFactor factor = factorMapper.getDerivedFactorByNameAndUser(dfname, id);
        return factor != null ?
                ResponseEntity.ok(factor) :
                ResponseEntity.notFound().build();
    }

    @GetMapping("/single/father/{fathername}")
    public ResponseEntity<List<SingleFactor>> getFactorsByFather(
            @PathVariable String fathername) {
        List<SingleFactor> factors = factorMapper.getSingleFactorsByFather(fathername);
        return ResponseEntity.ok(factors);
    }

    @GetMapping("/single/{factorname}")
    public ResponseEntity<SingleFactor> getSingleFactorByName(
            @PathVariable String factorname) {
        SingleFactor factor = factorMapper.getSingleFactorByName(factorname);
        return factor != null ?
                ResponseEntity.ok(factor) :
                ResponseEntity.notFound().build();
    }

    @GetMapping("/single/fathers")
    public ResponseEntity<List<String>> getAllFatherNames() {
        try {
            List<String> fatherNames = factorMapper.getAllfather();
            return ResponseEntity.ok(fatherNames);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}