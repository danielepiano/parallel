package com.dp.spring.parallel.hermes.utils;

import com.dp.spring.springcore.exceptions.UnsupportedCallToPrivateConstructor;
import lombok.AllArgsConstructor;

import java.util.Map;

public class EmailMessageParser {
    @AllArgsConstructor
    public enum Keyword {
        FIRST_NAME("firstName"),
        LAST_NAME("lastName"),
        EMAIL("email"),
        PASSWORD("password"),

        COMPANY_NAME("companyName"),
        HEADQUARTERS_CITY("headquartersCity"),
        HEADQUARTERS_ADDRESS("headquartersAddress"),
        BOOKING_DATE("bookingDate"),
        WORKSPACE_NAME("workspaceName"),
        WORKSPACE_FLOOR("workspaceFloor"),
        WORKPLACE_NAME("workplaceName");


        private final String keyword;

        public String get() {
            return keyword;
        }

        public String getTemplate() {
            return "{" + keyword + "}";
        }
    }


    private EmailMessageParser() {
        throw new UnsupportedCallToPrivateConstructor();
    }

    /**
     * Parsing a {@link String} message on given associations keyword-value.<br>
     * Given: "Lorem ipsum {x} sit amet", "x" -> "dolor"<br>
     * Then: "Lorem ipsum dolor sit amet"
     *
     * @param message   the message to parse
     * @param keyValues pairs of keyword to be searched and values replacing them
     * @return the parsed message
     */
    public static String parse(String message, final Map<Keyword, String> keyValues) {
        for (final Map.Entry<Keyword, String> keyValue : keyValues.entrySet()) {
            message = message.replace(
                    keyValue.getKey().getTemplate(),
                    keyValue.getValue()
            );
        }
        return message;
    }
}
