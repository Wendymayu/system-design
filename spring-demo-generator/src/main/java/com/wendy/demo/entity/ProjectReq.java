package com.wendy.demo.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description TODO
 * @Author wendyma
 * @Date 2023/10/15 17:24
 * @Version 1.0
 */
@Data
public class ProjectReq {
    @Length(min = 1, max = 256)
    private String name;

    @Length(min = 3, max = 256)
    private String groupId;

    @Length(min = 1, max = 256)
    private String artifactId;

    @Length(max = 256)
    private String description;

    private Dependency parent;

    private List<Dependency> dependencies;

}
