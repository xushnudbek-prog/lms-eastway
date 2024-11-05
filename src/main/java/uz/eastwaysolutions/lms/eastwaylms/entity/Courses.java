package uz.eastwaysolutions.lms.eastwaylms.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "courses")
public class Courses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @Column(name = "media_url")
    private String mediaUrl;

    @Column(name = "created_by")
    private Long createdBy;

    @ManyToMany(mappedBy = "courses")
    @JsonBackReference
    private List<User> users = new ArrayList<>();

    public List<User> getUsers() {
        return users;
    }





}
