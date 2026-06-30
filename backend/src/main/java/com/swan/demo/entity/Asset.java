package com.swan.demo.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Asset {

    private Long id;

    private String name;

    private String type;

    private BigDecimal amount;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
