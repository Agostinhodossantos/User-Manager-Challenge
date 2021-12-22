package user.app.com.models;

import java.io.Serializable;

public class User implements Serializable {
    private String id, name, imgUrl, biography;

    public User(String id, String name, String imgUrl, String biography) {
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
        this.biography = biography;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", biography='" + biography + '\'' +
                '}';
    }
}
