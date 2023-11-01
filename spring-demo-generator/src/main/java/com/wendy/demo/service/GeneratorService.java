package com.wendy.demo.service;

import com.wendy.demo.entity.ProjectReq;

/**
 * @Description TODO
 * @Author wendyma
 * @Date 2023/10/15 17:20
 * @Version 1.0
 */
public interface GeneratorService {
    byte[] generateProject(ProjectReq projectReq);
}