package com.cunzheng.entity;

import com.cunzheng.configuration.Status;
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
    @GeneratedValue
    private int contractId;
    private String contractHash;
    private Date uploadTime;
    private String landlordSignature;
    private String tenantSignature;
    private String fileHash;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name=" contract_file", columnDefinition="blob", nullable=true)
    private byte[] contractFile;
    private String status;

    public ContractBean(String contractHash, Date uploadTime, String landlordSignature, String tenantSignature, String fileHash, byte[] contractFile, String status) {
        this.contractHash = contractHash;
        this.uploadTime = uploadTime;
        this.landlordSignature = landlordSignature;
        this.tenantSignature = tenantSignature;
        this.fileHash = fileHash;
        this.contractFile = contractFile;
        this.status = status;
    }
}
