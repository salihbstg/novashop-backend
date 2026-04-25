package com.bastug.novashop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

// Tüm controller'larda oluşan exception'ları merkezi olarak yakalayan yapı
@ControllerAdvice
public class ApplicationExceptionHandler {

    // Custom exception (ApplicationExceptionImpl) fırlatıldığında çalışır
    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleException(ApplicationExceptionImpl ex) {

        // Exception response objesi oluşturulur
        ExceptionResponse response = new ExceptionResponse();
        response.setMessage(ex.getMessage());
        response.setTimeStamp(System.currentTimeMillis());
        response.setStatus(HttpStatus.NOT_FOUND.value());

        // Eğer hata mesajında "şifre" geçiyorsa özel olarak UNAUTHORIZED dön
        if(ex.getMessage().contains("şifre")){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        // Diğer custom hatalarda BAD_REQUEST döner
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    // RuntimeException (beklenmeyen genel hatalar) için handler
    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> exceptionResponseResponseEntity(RuntimeException exc){

        ExceptionResponse response = new ExceptionResponse();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage(exc.getMessage());
        response.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // @Valid ile gelen validation hatalarını yakalar
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> exceptionResponseResponseEntity(MethodArgumentNotValidException exc){

        ExceptionResponse response = new ExceptionResponse();
        response.setStatus(HttpStatus.BAD_REQUEST.value());

        // İlk validation hatasının mesajını döner (örn: "email boş olamaz")
        response.setMessage(Objects.requireNonNull(exc.getFieldError()).getDefaultMessage());

        response.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}