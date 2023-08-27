package com.wendy.po;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @Description TODO
 * @Author wendyma
 * @Date 2023/8/27 14:30
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="url_map")
public class UrlMapPo {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    private String shortUrl;

    private String longUrl;
}
