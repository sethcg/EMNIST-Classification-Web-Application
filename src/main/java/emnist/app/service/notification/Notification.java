package emnist.app.service.notification;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = NotificationSerializer.class)
public class Notification {

    public int epochNum = 0;
    public int batchNum = 0;
    public int steps = 0;
    public String loss = "";
    public String accuracy = "";

    public Notification(int epochNum, int batchNum) {
        this.epochNum = epochNum;
        this.batchNum = batchNum;
    }

}