package com.dburyak.example.jwt.lib.req.cfg;

import com.dburyak.example.jwt.lib.err.NotFoundException;
import com.dburyak.example.jwt.lib.req.EnumConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@AutoConfiguration
public class WebAutoConfiguration {

    @Configuration
    @RequiredArgsConstructor
    public class EnumConvertersCfg implements WebMvcConfigurer {
        private final List<EnumConverter<?>> enumConverters;

        @Override
        public void addFormatters(FormatterRegistry registry) {
            enumConverters.forEach(registry::addConverter);
        }
    }

    @ControllerAdvice
    public class GlobalExceptionHandlerAdvice {

        @ExceptionHandler(NotFoundException.class)
        public ResponseEntity<String> handleException(NotFoundException e) {
            // Log the exception and return a generic error response
            // This is a placeholder; you can customize the response as needed
            return ResponseEntity.status(NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}
