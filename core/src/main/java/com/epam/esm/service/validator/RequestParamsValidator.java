package com.epam.esm.service.validator;


import com.epam.esm.exception.ServiceErrorCode;
import com.epam.esm.exception.ServiceException;


//TODO static to component
public class RequestParamsValidator {

    private static final String ASCENDING_SORT = "ASC";
    private static final String DESCENDING_SORT = "DESC";
    
    public static boolean isAllowedOrderDirection(String order){
        return order.equalsIgnoreCase(ASCENDING_SORT) || order.equalsIgnoreCase(DESCENDING_SORT);
    }
    
    public static void validateSortingOrders(String nameSortOrder,String dateSortOrder){
        if(!isAllowedOrderDirection(nameSortOrder) || !isAllowedOrderDirection(dateSortOrder)){
            throw new ServiceException(ServiceErrorCode.CERTIFICATE_BAD_REQUEST_PARAMS,"allowed values for sorting params are ASC and DESC");
        }
    }
    

}
