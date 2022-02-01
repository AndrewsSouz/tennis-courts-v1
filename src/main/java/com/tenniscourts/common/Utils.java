package com.tenniscourts.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utils {
    public static <T> List<T> verifyEmptyList(List<T> list) {
        if (list.isEmpty()) {
            throw new ResponseStatusException(NO_CONTENT);
        } else {
            return list;
        }
    }
}
