package com.dp.spring.parallel.common.utils;

import com.dp.spring.springcore.exceptions.UnsupportedCallToPrivateConstructorException;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ResourcesUtils {
    private ResourcesUtils() {
        throw new UnsupportedCallToPrivateConstructorException();
    }

    public static String readFileAsString(final String pathFromClasspath) {
        try {
            File file = ResourceUtils.getFile("classpath:" + pathFromClasspath);
            return new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
