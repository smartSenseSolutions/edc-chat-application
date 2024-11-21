package com.smartsense.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public interface ServiceUtil {
    ObjectMapper getObjectMapper();

    MessageSource getMessageSource();

    default <I, O> O toType(I data, Class<O> c) {
        return getObjectMapper().convertValue(data, c);
    }

    default <I, O> List<O> toListOf(Collection<I> data, Class<O> c) {
        return getObjectMapper().convertValue(data, TypeFactory.defaultInstance().constructParametricType(List.class, c));
    }

    default <I, O> List<O> toListOf(Collection<I> data, Function<I, O> converter) {
        return data.stream().map(converter).toList();
    }

    default <I> String convertToString(I data) {
        try {
            return getObjectMapper().writeValueAsString(data);
        } catch (JsonProcessingException e) {
            return null;
        }
    }


    default String resolveMessage(String key, String... arg) {
        try {
            return getMessageSource().getMessage(key, arg, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            return key;
        }
    }
    
    default void delete(File... files) {
        for (File file : files) {
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
