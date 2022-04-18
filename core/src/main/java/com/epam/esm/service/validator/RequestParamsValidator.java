package com.epam.esm.service.validator;


import com.epam.esm.exception.ServiceErrorCode;
import com.epam.esm.exception.ServiceException;

import org.springframework.stereotype.Component;


@Component
public class RequestParamsValidator {

    private final String ASCENDING_SORT = "ASC";
    private final String DESCENDING_SORT = "DESC";
    private final String WITHOUT_SORT = "";
    
    private boolean isAllowedOrderDirection(String order){
        return order.equalsIgnoreCase(WITHOUT_SORT) || order.equalsIgnoreCase(ASCENDING_SORT) || order.equalsIgnoreCase(DESCENDING_SORT);
    }
    
    public boolean validateSortingOrders(String nameSortOrder,String dateSortOrder){
        if(!isAllowedOrderDirection(nameSortOrder) || !isAllowedOrderDirection(dateSortOrder)){
            throw new ServiceException(ServiceErrorCode.CERTIFICATE_BAD_REQUEST_PARAMS,"allowed values for sorting params are ASC and DESC");
        }
        return true;
    }
    

}
