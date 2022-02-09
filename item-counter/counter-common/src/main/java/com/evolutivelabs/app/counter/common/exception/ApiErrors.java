package com.evolutivelabs.app.counter.common.exception;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

@ApiModel("檢核錯誤資訊")
public class ApiErrors {
    @ApiModelProperty("錯誤訊息")
    private List<ContentBody> messages = new ArrayList<>();

    public ApiErrors(List<ObjectError> errors) {
        for (ObjectError error : errors) {
            ContentBody body = new ContentBody();
            body.setMessage(error.getDefaultMessage());
            body.setDescription(error.getCode());
            if (error instanceof FieldError) {
                body.setName(((FieldError) error).getField());
            }
            messages.add(body);
        }
    }

    public ApiErrors(Errors errors) {
        for (ObjectError error : errors.getAllErrors()) {
            ContentBody body = new ContentBody();
            body.setMessage(error.getDefaultMessage());
            body.setDescription(error.getCode());
            if (error instanceof FieldError) {
                body.setName(((FieldError) error).getField());
            }
            messages.add(body);
        }
    }

    public List<ContentBody> getMessages() {
        return messages;
    }

    public void setMessages(List<ContentBody> messages) {
        this.messages = messages;
    }

    @Data
    private class ContentBody {
        private String name;
        private String description;
        private String message;
    }
}
