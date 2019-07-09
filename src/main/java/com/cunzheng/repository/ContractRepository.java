package com.cunzheng.repository;

import com.cunzheng.entity.ContractBean;
import com.cunzheng.entity.UserBean;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContractRepository extends JpaRepository<ContractBean,Long> {

    ContractBean findByContractId(int contractId);

    List<ContractBean> findByStatus(String status);

}
