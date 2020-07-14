package com.example.kamrankhan.pstapplication;


public class ChildFolders implements Comparable<ChildFolders> {
    private long id;
    private String sender;
    private String subject;
    private String time;
    private String msgBody;

    //Default Constructor
    public ChildFolders() {

    }

    //Constructor
    public ChildFolders(long id, String sender, String subject, String time, String msgBody) {
        this.id=id;
        this.sender = sender;
        this.subject = subject;
        this.time = time;
        this.msgBody=msgBody;
    }

    //Setter, Getter


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int compareTo(ChildFolders other) {
        return time.compareTo(other.time);
    }
}
