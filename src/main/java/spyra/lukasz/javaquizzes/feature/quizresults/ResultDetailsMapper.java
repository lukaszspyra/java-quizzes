package spyra.lukasz.javaquizzes.feature.quizresults;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spyra.lukasz.javaquizzes.shared.Question;
import spyra.lukasz.javaquizzes.shared.ScoreCounter;
import spyra.lukasz.javaquizzes.shared.TakeQuiz;
import spyra.lukasz.javaquizzes.shared.TakeQuizAnswer;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Maps {@link TakeQuiz} entity to {@link AttemptDetailsView} DTO
 */
@Component
@RequiredArgsConstructor
class ResultDetailsMapper {

    private final MarkedAnswerDetailsRepository uDetailAnsRepository;

    private final AnswerMapStructMapper ansMapStructMapper;

    private final ScoreCounter scoreCounter;


    AttemptDetailsView quizAttemptToDetailsView(final TakeQuiz takeQuiz) {
        AttemptDetailsView attemptDetailsView = new AttemptDetailsView();
        attemptDetailsView.setAttemptId(takeQuiz.getId());
        attemptDetailsView.setScore(takeQuiz.getScore());
        attemptDetailsView.setStart(localDateTimeToStringFormatter(takeQuiz.getStart()));
        attemptDetailsView.setFinish(localDateTimeToStringFormatter(takeQuiz.getFinish()));
        attemptDetailsView.setDuration(calcDuration(takeQuiz));
        attemptDetailsView.setQuizTitle(takeQuiz.getQuiz().getTitle());
        attemptDetailsView.setQuizMaxScore(takeQuiz.getQuiz().getMaxScore());
        attemptDetailsView.setDifficulty(takeQuiz.getQuiz().getDifficulty());
        attemptDetailsView.setQuestionsSolved(mapQuestToDetailsView(takeQuiz.getQuiz().getQuestions(), takeQuiz.getId()));
        return attemptDetailsView;
    }

    private String localDateTimeToStringFormatter(final LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return localDateTime.format(formatter);
    }

    private String calcDuration(final TakeQuiz takeQuiz) {
        var start = takeQuiz.getStart();
        var finish = takeQuiz.getFinish();
        var duration = Duration.between(start, finish);
        return duration.toMinutesPart() +
                "m : " +
                duration.toSecondsPart() +
                "s";
    }

    private QuestionDetailsView mapQuestToDetailsView(Question question, UUID takeQuizId) {
        QuestionDetailsView questDetails = new QuestionDetailsView();
        questDetails.setQuestionId(question.getId());
        questDetails.setQuestMaxScore(question.getScore());
        questDetails.setContent(question.getContent());
        questDetails.setDifficulty(questDetails.getDifficulty());
        questDetails.setPossibleAnswers(ansMapStructMapper.answersEntityToDetailsViews(question.getAnswers()));

        List<TakeQuizAnswer> ansMarkedAsTrue = uDetailAnsRepository.findTakeQuizAnswerByQuestionIdAndTakeQuizId(question.getId(), takeQuizId);
        List<AnswerDetailsView> markedEntitiesDetails = ansMapStructMapper.markedAnsEntitiesToDetailsViews(ansMarkedAsTrue);
        questDetails.setMarkedAnswers(markedEntitiesDetails);

        questDetails.setQuestAwardedScore(scoreCounter.count(ansMapStructMapper.detailsViewsToEntities(markedEntitiesDetails)));
        return questDetails;
    }

    private List<QuestionDetailsView> mapQuestToDetailsView(List<Question> questionList, UUID takeQuizId) {
        return questionList.stream()
                .map(e -> mapQuestToDetailsView(e, takeQuizId))
                .collect(Collectors.toUnmodifiableList());
    }
}
