package spyra.lukasz.javaquizzes.feature.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import spyra.lukasz.javaquizzes.shared.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Controls progress during Demo quiz attempt.
 *
 * As it serves for demo purpose, results are not saved in database, only {@link TakeQuiz} instance is updated.
 */
@Component
@RequiredArgsConstructor
class DemoProgresser {

    private final ScoreCounter scoreCounter;

    private final TakeDemoMapper mapper;

    /**
     * Make progress of demo quiz attempt by updating {@link TakeQuiz}
     * <p>
     * As it is demo feature, answers are not saved, but score is updated. Returned instance is mapped to DTO.
     *
     * @param questionDTO during attempt
     * @param markedIds   selected answers Ids
     * @param takeDemoDTO current {@link TakeQuiz} attempt instance
     * @return demo DTO with updated score
     */
    TakeDemoDTO progressQuiz(final QuestionDTO questionDTO, final List<UUID> markedIds, final TakeDemoDTO takeDemoDTO) {
        final Question question = mapper.toQuestion(questionDTO);
        final TakeQuiz currentDemo = mapper.fromDTO(takeDemoDTO);

        final List<Answer> answered = question.selectAnswers(markedIds);
        final TakeQuiz updatedDemo = currentDemo.progressQuizAttempt(scoreCounter.count(answered), LocalDateTime.now());
        return mapper.toDTO(updatedDemo);
    }

}
