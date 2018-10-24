package com.romanidze.asyncappdbstorage.security.handlers;

import com.romanidze.asyncappdbstorage.security.exceptions.EntityNotFoundException;
import com.romanidze.asyncappdbstorage.security.errors.Error;

import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

/**
 * 21.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@RestControllerAdvice
public class RESTExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers,
                                                                          HttpStatus status,
                                                                          WebRequest request){

        StringBuilder sb = new StringBuilder();

        sb.append("Отсутствует параметр ")
          .append(ex.getParameterName())
          .append(", тип ")
          .append(ex.getParameterType());

        Error error = new Error(HttpStatus.BAD_REQUEST, sb.toString(), ex);
        return new ResponseEntity<>(error, error.getStatus());

    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                     HttpHeaders headers,
                                                                     HttpStatus status,
                                                                     WebRequest request){

        StringBuilder sb = new StringBuilder();

        sb.append("Для данного запроса тип мультимедиа ").append(ex.getContentType()).append(" не поддерживается. ")
          .append("Поддерживаемые типы: ");

        ex.getSupportedMediaTypes()
          .forEach(mediaType -> sb.append(mediaType).append(", "));

        Error error = new Error(HttpStatus.UNSUPPORTED_MEDIA_TYPE, sb.substring(0, sb.length() - 2), ex);
        return new ResponseEntity<>(error, error.getStatus());

    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers,
                                                                         HttpStatus status,
                                                                         WebRequest request){

        StringBuilder sb = new StringBuilder();

        sb.append("В данном случае тип запроса ").append(ex.getMethod()).append(" не поддерживается. ")
          .append("Поддерживаемые типы: ");

        ex.getSupportedHttpMethods()
          .forEach(httpMethod -> sb.append(httpMethod).append(", "));

        Error error = new Error(HttpStatus.METHOD_NOT_ALLOWED, sb.substring(0, sb.length() - 2), ex);
        return new ResponseEntity<>(error, error.getStatus());

    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request){

        Error error = new Error(HttpStatus.BAD_REQUEST);

        error.setMessage("Ошибка валидации");
        error.addValidationErrors(ex.getBindingResult().getFieldErrors());
        error.addValidationError(ex.getBindingResult().getGlobalErrors());

        return new ResponseEntity<>(error, error.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status,
                                                         WebRequest request){

        Error error = new Error(HttpStatus.BAD_REQUEST);

        error.setMessage("Запрос подан к неверной сущности");
        error.addValidationErrors(ex.getBindingResult().getFieldErrors());
        error.addValidationError(ex.getBindingResult().getGlobalErrors());

        return new ResponseEntity<>(error, error.getStatus());

    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex){

        Error error = new Error(HttpStatus.NOT_FOUND);
        error.setMessage(ex.getMessage());
        return new ResponseEntity<>(error, error.getStatus());

    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request){

        String errorMessage = "Некорректное тело JSON-запроса";
        Error error = new Error(HttpStatus.BAD_REQUEST, errorMessage, ex);
        return new ResponseEntity<>(error, error.getStatus());

    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request){

        String errorMessage = "Ошибка при создании тела JSON";
        Error error = new Error(HttpStatus.BAD_REQUEST, errorMessage, ex);
        return new ResponseEntity<>(error, error.getStatus());

    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex,
                                                                  WebRequest request){

        if(ex.getCause() instanceof ConstraintViolationException){
            Error error = new Error(HttpStatus.CONFLICT, "Произошла ошибка в БД", ex.getCause());
            return new ResponseEntity<>(error, error.getStatus());
        }

        Error error = new Error(HttpStatus.INTERNAL_SERVER_ERROR, ex);
        return new ResponseEntity<>(error, error.getStatus());

    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> handleIllegalArguments(IllegalArgumentException ex,
                                                            WebRequest request){

        Error error = new Error(HttpStatus.BAD_REQUEST);
        error.setMessage(ex.getMessage());
        error.setDebugMessage("Получен неверный аргумент");
        return new ResponseEntity<>(error, error.getStatus());

    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                      WebRequest request){

        String errorMessage = String.format("Параметр '%s' типа '%s' не конвертируется в тип '%s'",
                ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

        Error error = new Error(HttpStatus.BAD_REQUEST);
        error.setMessage(errorMessage);
        error.setDebugMessage(ex.getMessage());
        return new ResponseEntity<>(error, error.getStatus());

    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
                                                        HttpStatus status, WebRequest request){

        StringBuilder sb = new StringBuilder();

        sb.append("Значение ")
          .append(ex.getValue())
          .append(" для ")
          .append(ex.getPropertyName())
          .append(" должно быть типа ")
          .append(ex.getRequiredType());

        Error error = new Error(HttpStatus.BAD_REQUEST);
        error.setMessage(sb.toString());
        error.setDebugMessage(ex.getMessage());
        return new ResponseEntity<>(error, error.getStatus());

    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex,
                                                                     HttpHeaders headers,
                                                                     HttpStatus status,
                                                                     WebRequest request){

        String errorMessage = "В запросе нет заголовка " + ex.getRequestPartName();

        Error error = new Error(HttpStatus.BAD_REQUEST);
        error.setMessage(errorMessage);
        error.setDebugMessage(ex.getMessage());
        return new ResponseEntity<>(error, error.getStatus());

    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                                   HttpHeaders headers,
                                                                   HttpStatus status,
                                                                   WebRequest request){

        StringBuilder sb = new StringBuilder();

        sb.append("Для ")
          .append(ex.getHttpMethod())
          .append(" ")
          .append(ex.getRequestURL())
          .append(" не найден нужный контроллер.");

        Error error = new Error(HttpStatus.NOT_FOUND);
        error.setMessage(sb.toString());
        error.setDebugMessage(ex.getMessage());
        return new ResponseEntity<>(error, error.getStatus());

    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request){

        Error error = new Error(HttpStatus.INTERNAL_SERVER_ERROR);
        error.setMessage("Произошла неизвестная ошибка");
        error.setDebugMessage(ex.getMessage());
        return new ResponseEntity<>(error, error.getStatus());

    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex,
                                                                WebRequest request){

        Error error = new Error(HttpStatus.FORBIDDEN);
        error.setMessage("Произошла ошибка авторизации");
        error.setDebugMessage(ex.getMessage());
        return new ResponseEntity<>(error, error.getStatus());

    }


}

