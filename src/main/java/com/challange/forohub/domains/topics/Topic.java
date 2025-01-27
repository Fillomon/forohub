package com.challange.forohub.domains.topics;

import com.challange.forohub.domains.answers.Answer;
import com.challange.forohub.domains.courses.Course;
import com.challange.forohub.domains.users.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Table(name = "topics")
@Entity(name = "Topic")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Topic {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Setter
    private String title;
    @Setter
    private String message;
    private LocalDateTime creation;
    @Enumerated(EnumType.STRING)
    private Status status;
    @JoinColumn(name = "course_id")
    @ManyToOne
    private Course course;
    @JoinColumn(name = "author_id")
    @ManyToOne
    private User author;
    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Answer> answers;

    public Topic(User user, Course course, String message, String title) {
        this.author = user;
        this.course = course;
        this.message = message;
        this.title = title;
        this.creation = LocalDateTime.now();
        this.status = Status.CREADO;
    }

    public void delete(){
        this.status = Status.ELIMINADO;
    }

    public void update(){
        this.status = Status.MODIFICADO;
    }
}
