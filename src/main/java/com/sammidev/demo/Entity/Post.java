package com.sammidev.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@NoArgsConstructor
@Data

@Document(collection = "posts")
public class Post {

    @Id
    private String id;

    @NotBlank
    @Size(max = 200)
    private String text;

    @NotNull
    private Date createdAt =  new Date();

    public Post(String text) {
        this.id = id;
        this.text = text;
    }
}