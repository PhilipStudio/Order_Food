package com.philip.studio.orderfood.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Comment {
    private String avatar;
    private String nickname;
    private String content;
    private String time;
    private float star;

    public Comment() {
    }

    public Comment(String avatar, String nickname, String content, String time, float star) {
        this.avatar = avatar;
        this.nickname = nickname;
        this.content = content;
        this.time = time;
        this.star = star;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public float getStar() {
        return star;
    }
}
