package events;

import java.util.EventObject;

public class Event extends EventObject {
    private final String type;
    private final Object data;

    public Event(Object source, String type, Object data) {
        super(source);
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public Object getData() {
        return data;
    }
}