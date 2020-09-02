package com.openpayd.rest.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@Getter
@JsonIgnoreProperties({"httpStatus"})
public class Result {

    private HttpStatus httpStatus;

    private Map<String, Object> fields;

    private Result(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        fields = new HashMap<>();
    }

    public static Result Error(HttpStatus status) {
        return new Result(status).add("error", status);
    }

    public static Result Success() {
        return new Result(HttpStatus.OK);
    }

    public Result add(String key, Object value) {
        this.fields.put(key, value);
        return this;
    }

    public Result message(Object value) {
        this.fields.put("message", value);
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getFields() {
        return fields;
    }

    public ResponseEntity<Result> build() {
        return new ResponseEntity<>(this, this.httpStatus);
    }
}
