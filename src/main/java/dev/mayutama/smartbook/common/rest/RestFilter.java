package dev.mayutama.smartbook.common.rest;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestFilter {
    public enum Operator {
        EQ,
        NE,
        LE,
        LT,
        GE,
        GT,
        LIKE
    }

    private String path;
    private Operator operator;
    private Object value;

    @SuppressWarnings("unchecked")
    public <T> T getValue() {
        return (T) this.value;
    }
}
