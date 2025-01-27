package com.challange.forohub.domains.topics;

import com.challange.forohub.domains.courses.CourseRepository;
import com.challange.forohub.domains.topics.validations.ValidatorNewTopical;
import com.challange.forohub.domains.topics.validations.ValidatorUpdateTopical;
import com.challange.forohub.domains.users.User;
import com.challange.forohub.domains.users.UserRepository;
import com.challange.forohub.infra.errors.IntegrityValidation;
import com.challange.forohub.infra.security.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ServiceNewTopic {
    
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private List<ValidatorNewTopical> validations;
    @Autowired
    private List<ValidatorUpdateTopical> updateTopicals;
    @Autowired
    private TokenService tokenService;

    public Topic add(DataTopic data, HttpServletRequest request){
        var user = getAuthenticatedUser(request);
        if(userRepository.findById(user.getId()).isEmpty()){
            throw new IntegrityValidation("Este id para usurio no existe");
        }
        if(!courseRepository.existsById(data.courseId())){
            throw new IntegrityValidation("Este id para el curso no existe");
        }

        validations.forEach(v -> v.validate(data));

        user = userRepository.findById(user.getId()).get();
        var course = courseRepository.findById(data.courseId()).orElse(null);

        return topicRepository.save(new Topic(user, course,data.message(),data.title()));
    }


    public TopicalDetail update(TopicDataUpdate data, HttpServletRequest request) {
        if (data.title() == null && data.message() ==null){
            throw new IntegrityValidation("No hay nada para editar");
        }
        if(!topicRepository.existsById(data.id())){
            throw new IntegrityValidation("No hay topicos con ese id");
        }

        var user = getAuthenticatedUser(request);
        DataTopic dataTopic = new DataTopic(data.title(),data.message(),null);
        validations.forEach(v -> v.validate(dataTopic));
        updateTopicals.forEach(v -> v.validate(data,user));

        var topic = topicRepository.getReferenceById(data.id());
        if(data.title() != null && data.message() !=null){
            topic.setMessage(data.message());
            topic.setTitle(data.title());
        } else if (data.title() == null) {
            topic.setMessage(data.message());
        }
        else {
            topic.setTitle(data.title());
        }
        topic.update();
        return new TopicalDetail(topic);
    }

    public void delete(Long id,HttpServletRequest request) {
        if(!topicRepository.existsById(id)){
            throw new IntegrityValidation("No exite el topico");
        }
        var user = getAuthenticatedUser(request);
        TopicDataUpdate data = new TopicDataUpdate(id,null,null);
        updateTopicals.forEach(v -> v.validate(data,user));
        var topic = topicRepository.getReferenceById(id);
        topic.delete();
    }

    private User getAuthenticatedUser(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.replace("Bearer ", "");
        String subject = tokenService.getSubject(token);
        return (User) userRepository.findByMail(subject);
    }
}
