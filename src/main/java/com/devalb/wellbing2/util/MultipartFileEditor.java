package com.devalb.wellbing2.util;

import org.springframework.web.multipart.MultipartFile;
import java.beans.PropertyEditorSupport;

public class MultipartFileEditor extends PropertyEditorSupport {
    @Override
    public void setAsText(String text) {
        setValue(text);
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof MultipartFile) {
            MultipartFile file = (MultipartFile) value;
            setValue(file.getOriginalFilename());
        } else {
            super.setValue(value);
        }
    }
}
