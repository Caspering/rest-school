package adrianromanski.restschool.domain.base_entity.person;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Data
@Entity
@NoArgsConstructor
public class Guardian extends Person {

    private String telephoneNumber;
    private String email;

}