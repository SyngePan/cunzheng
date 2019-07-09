package com.cunzheng.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class ContractBean {
    @Id
    @GeneratedValue
    private int contractId;
    private String contractHash;
    private Date uploadTime;
    private String landlordSignature;
    private String tenantSignature;
    private String fileHash;
}
