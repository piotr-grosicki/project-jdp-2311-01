package com.kodilla.ecommercee.domain;
import lombok.Getter;


@Getter
public class Group {
    private long id;
    private String name;
    private String description;

    public Group(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
