package com.epam.esm.service.validation;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.epam.esm.exception.ErrorCode;
import com.epam.esm.exception.ServiceException;

import org.springframework.util.MultiValueMap;

public class RequestParamsValidator {

    private static final String ASCENDING_SORT = "ASC";
    private static final String DESCENDING_SORT = "DESC";
    private static final String NAME_PART = "namePart";
    private static final String DESCRIPTION_PART = "descriptionPart";
    private static final String NAME_SORT_ORDER = "nameSortOrder";
    private static final String DATE_SORT_ORDER = "dateSortOrder";
    private static final String TAG_NAME = "tagName";
    
    private static final Set<String> allowedRequestParams = new HashSet<>();

    static{
        allowedRequestParams.add(NAME_PART);
        allowedRequestParams.add(DESCRIPTION_PART);
        allowedRequestParams.add(NAME_SORT_ORDER);
        allowedRequestParams.add(DATE_SORT_ORDER);
        allowedRequestParams.add(TAG_NAME);
    }
    
    public static boolean isAllowedOrderDirection(String order){
        return order.equalsIgnoreCase(ASCENDING_SORT) || order.equalsIgnoreCase(DESCENDING_SORT);
    }
    
    public static void validateParams(MultiValueMap<String,String> params){
        deleteUnallowedParams(params);
        checkInvalidValuedParams(params);
        checkDuplicatedQueryParams(params);
    }

    private static void checkInvalidValuedParams(MultiValueMap<String,String> params){
        if((params.containsKey(NAME_SORT_ORDER) && !RequestParamsValidator.isAllowedOrderDirection(params.getFirst(NAME_SORT_ORDER))) ||
                (params.containsKey(DATE_SORT_ORDER) && !RequestParamsValidator.isAllowedOrderDirection(params.getFirst(DATE_SORT_ORDER)))){
            throw new ServiceException(ErrorCode.CERTIFICATE_BAD_REQUEST_PARAMS,"allowed values for sorting params are ASC and DESC");
        }
    }
    




    private static void checkDuplicatedQueryParams(MultiValueMap<String,String> params){
        List<Map.Entry<String,List<String>>> unallowedDuplicates = params.entrySet().stream().filter(entry -> 
        entry.getValue().size() > 1 && !entry.getKey().equals("tagName")).collect(Collectors.toList());
        if(!unallowedDuplicates.isEmpty()){
            StringBuilder duplicatesInfo = new StringBuilder();
            unallowedDuplicates.stream().forEach(entry -> duplicatesInfo.append(entry.getKey() + " can't have several values|"));
            throw new ServiceException(ErrorCode.CERTIFICATE_BAD_REQUEST_PARAMS,duplicatesInfo.toString());
        }
    }

    private static void deleteUnallowedParams(MultiValueMap<String,String> params){
        Set<String> unallowedParams = new HashSet<>();
        params.forEach((key,value) -> {
            if(!allowedRequestParams.contains(key)){
                unallowedParams.add(key);
            }
        });
        unallowedParams.forEach(param->params.remove(param));
    }

}
