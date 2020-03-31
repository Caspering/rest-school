package adrianromanski.restschool.model.base_entity;

import adrianromanski.restschool.domain.base_entity.BaseEntity;
import adrianromanski.restschool.model.base_entity.event.ExamDTO;
import adrianromanski.restschool.model.base_entity.person.StudentDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDTO extends BaseEntity {

    private String name;
    private Long value;

    private List<StudentDTO> studentsDTO = new ArrayList<>();
    private List<ExamDTO> examsDTO = new ArrayList<>();

}