package com.epam.esm.exception;

import com.epam.esm.repository.exception.RepositoryException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class ExceptionProcessor  {

    private static final int ERROR_CODE_START = 0;
    private static final int ERROR_CODE_END = 3;

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorDescriptor> serviceProcessor(ServiceException e){
        HttpStatus status = HttpStatus.resolve(Integer.parseInt(e.getErrorCode().substring(ERROR_CODE_START,ERROR_CODE_END)));
        return new  ResponseEntity<>(new ErrorDescriptor(e.getErrorCode(),e.getErrorMsg()),status);
    }

    @ExceptionHandler(RepositoryException.class)
    public ResponseEntity<ErrorDescriptor> repositoryProcessor(RepositoryException e){
        HttpStatus status = HttpStatus.resolve(Integer.parseInt(e.getErrorCode().substring(ERROR_CODE_START,ERROR_CODE_END)));
        return new ResponseEntity<>(new ErrorDescriptor(e.getErrorCode(),e.getErrorMsg()),status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<String> validationExceptionHandler(MethodArgumentNotValidException e){
        StringBuilder builder = new StringBuilder();
        for(FieldError error:e.getFieldErrors()){
            builder.append(error.getDefaultMessage());
            builder.append("\n");
        }
        return new ResponseEntity<>(builder.toString(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<String> violationExceptionHandler(ConstraintViolationException e){
        StringBuilder builder = new StringBuilder();
        e.getConstraintViolations().forEach(violation->{
            builder.append(violation.getMessage());
            builder.append("\n");
        });
        return new ResponseEntity<>(builder.toString(),HttpStatus.BAD_REQUEST);
    }
}
