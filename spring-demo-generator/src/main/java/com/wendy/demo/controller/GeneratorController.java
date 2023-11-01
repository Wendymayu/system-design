package com.wendy.demo.controller;

import com.wendy.demo.entity.ProjectReq;
import com.wendy.demo.service.GeneratorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Description TODO
 * @Author wendyma
 * @Date 2023/10/15 17:20
 * @Version 1.0
 */
@RestController
@RequestMapping("/demo-generator/v1/")
public class GeneratorController {
    @Resource
    private GeneratorService generatorService;

    @PostMapping("/project")
    public ResponseEntity<byte[]> generateProject(@RequestBody ProjectReq projectReq) {
        byte[] bytes = generatorService.generateProject(projectReq);
        return new ResponseEntity<>(bytes, HttpStatus.OK);
    }

}
