package com.cunzheng.entity;

import lombok.*;

import javax.persistence.*;
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
    private int contractId;
    private String contractHash;
    private Date uploadTime;
    private String landlordSignature;
    private String tenantSignature;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition="blob", nullable=true, length=10000000)
    private byte[] content;
}

