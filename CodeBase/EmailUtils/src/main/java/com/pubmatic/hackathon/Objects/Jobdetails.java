package com.pubmatic.hackathon.Objects;

import java.io.InputStream;

/**
 * Created by vikrant on 24/4/15.
 */
public class Jobdetails {

    private String fromEmail;

    private String subject;

    private InputStream attachmentContent;

    private boolean isAttachmentPresent;

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public InputStream getAttachmentContent() {
        return attachmentContent;
    }

    public void setAttachmentContent(InputStream attachmentContent) {
        this.attachmentContent = attachmentContent;
    }

    public boolean isAttachmentPresent() {
        return isAttachmentPresent;
    }

    public void setAttachmentPresent(boolean isAttachmentPresent) {
        this.isAttachmentPresent = isAttachmentPresent;
    }
}
