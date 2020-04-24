package adrianromanski.restschool.domain.base_entity.group;

import adrianromanski.restschool.domain.base_entity.person.Student;
import adrianromanski.restschool.domain.base_entity.person.Teacher;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class SportTeam extends Group {

    @ToString.Exclude
    @OneToMany(mappedBy = "sportTeam")
    private List<Student> students = new ArrayList<>();

}