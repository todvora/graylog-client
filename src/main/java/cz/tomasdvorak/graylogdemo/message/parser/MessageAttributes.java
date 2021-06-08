package cz.tomasdvorak.graylogdemo.message.parser;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;
import java.util.stream.Collectors;

public class MessageAttributes {
    @JsonIgnore
    private final Map<String, Object> attributes;

    public MessageAttributes(final Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @JsonAnyGetter()
    public Map<String,Object> getMap() {
        return attributes.entrySet().stream().collect(Collectors.toMap(e -> withPrefix(e.getKey()), Map.Entry::getValue));
    }

    private String withPrefix(final String key) {
        if(!key.startsWith("_")) {
            return "_" + key;
        } else {
            return key;
        }
    }
}
