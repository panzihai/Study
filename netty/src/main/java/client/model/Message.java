package client.model;

import java.util.Arrays;
import java.util.Map;


public class Message {

    private int contentLength;
    private byte flag;
    private byte type;
    private int  msgId;
    private Map<String, Object> params;
    private byte[] content;


    public byte getFlag() {
        return flag;
    }

    public void setFlag(byte flag) {
        this.flag = flag;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }


    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    @Override
    public String toString() {
        return "Message{" +
                "contentLength=" + contentLength +
                ", flag=" + flag +
                ", type=" + type +
                ", msgId=" + msgId +
                ", params=" + params +
                ", content=" + Arrays.toString(content) +
                '}';
    }
}
