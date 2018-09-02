package commons.model;

import java.util.Date;

public class Message {
    private String from;
    private Integer ChatRoomId;
    private String content;
    private String type;
    private Date date;

    public Message(String from, String content, String type, Date date) {
        this.from = from;
        this.content = content;
        this.type = type;
        this.date = date;
    }

    public Message() {

    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
