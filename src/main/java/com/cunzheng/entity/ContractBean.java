package com.cunzheng.entity;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ContractBean {
    private int contractId;
    private String contractHash;
    private Date uploadTime;
    private String landlordSignature;
    private String tenantSignature;
}
