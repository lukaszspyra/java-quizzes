package spyra.lukasz.javaquizzes.feature.demo;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO for {@link spyra.lukasz.javaquizzes.shared.Answer} entity
 */
@Getter
@Setter
final class AnswerDTO {

    private long id;

    private boolean correct;

    private String content;

}
