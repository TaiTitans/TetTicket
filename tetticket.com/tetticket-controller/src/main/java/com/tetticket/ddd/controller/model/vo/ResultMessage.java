package com.tetticket.ddd.controller.model.vo;


import lombok.Data;

import java.io.Serializable;

/**
 * VO tương tác giữa frontend và backend
 */
public class ResultMessage<T> implements Serializable {
         private static final long serialVersionUID = 1L;

         private boolean success;
         private String message;
         private Integer code;
         private long timestamp = System.currentTimeMillis();
         private T result;

         public boolean isSuccess() {
             return success;
         }

         public String getMessage() {
             return message;
         }

         public Integer getCode() {
             return code;
         }

         public long getTimestamp() {
             return timestamp;
         }

         public T getResult() {
             return result;
         }

         public ResultMessage<T> setSuccess(boolean success) {
             this.success = success;
             return this;
         }

         public ResultMessage<T> setMessage(String message) {
             this.message = message;
             return this;
         }

         public ResultMessage<T> setCode(Integer code) {
             this.code = code;
             return this;
         }

         public ResultMessage<T> setTimestamp(long timestamp) {
             this.timestamp = timestamp;
             return this;
         }

         public ResultMessage<T> setResult(T result) {
             this.result = result;
             return this;
         }
     }