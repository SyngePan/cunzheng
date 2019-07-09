package com.cunzheng.configuration;

import lombok.Getter;

@Getter
public enum Status {

    CONTRACT_INVALID,
    WITH_TENANT_SIGNATURE,
    WITH_LANDLORD_SIGNATURE,
    NEW,
    CLOSED,
    CONTRACT_NOT_EXIST;


}
