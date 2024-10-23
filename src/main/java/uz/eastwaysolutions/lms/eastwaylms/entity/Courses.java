package uz.eastwaysolutions.lms.eastwaylms.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Courses {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
