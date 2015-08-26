package com.vkclient.entities;

public class Dialog extends AbstractContentEntity {
    private final String title;
    private final String chatId;

    public Dialog(int id, long date, int user_id, boolean readState, String title, String body, String chatId) {
        this.id = id;
        this.date = date;
        this.user_id = user_id;
        this.readState = readState;
        this.title = title;
        this.body = body;
        this.chatId = chatId;
    }

    public String getTitle() {
        return this.title;
    }

    public int getChatId() {
        if (chatId.isEmpty()) return 0;
        return Integer.parseInt(chatId);
    }
}
