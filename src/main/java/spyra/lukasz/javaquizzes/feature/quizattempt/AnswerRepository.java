package spyra.lukasz.javaquizzes.feature.quizattempt;

import org.springframework.data.jpa.repository.JpaRepository;
import spyra.lukasz.javaquizzes.shared.Answer;

import java.util.UUID;

interface AnswerRepository extends JpaRepository<Answer, UUID> {
}
