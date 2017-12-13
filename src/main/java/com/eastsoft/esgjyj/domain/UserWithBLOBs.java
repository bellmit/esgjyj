package com.eastsoft.esgjyj.domain;

public class UserWithBLOBs extends User {
	private static final long serialVersionUID = -9205471376576699120L;

	private byte[] qmtp;

    private byte[] photo;

    private byte[] signature;

    public byte[] getQmtp() {
        return qmtp;
    }

    public void setQmtp(byte[] qmtp) {
        this.qmtp = qmtp;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }
}