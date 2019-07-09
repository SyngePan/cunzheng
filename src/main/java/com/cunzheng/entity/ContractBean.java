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
    private String fileHash;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition="blob", nullable=true, length=10000000)
    private byte[] content;
    private int status;


    public ContractBean(String contractHash, Date uploadTime, String landlordSignature, String tenantSignature, String fileHash, byte[] content, int status) {
        this.contractHash = contractHash;
        this.uploadTime = uploadTime;
        this.landlordSignature = landlordSignature;
        this.tenantSignature = tenantSignature;
        this.fileHash = fileHash;
        this.content = content;
        this.status = status;
    }
}


