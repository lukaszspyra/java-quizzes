package spyra.lukasz.javaquizzes.feature.quizattempt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PostAuthorize;
import spyra.lukasz.javaquizzes.shared.Quiz;

import java.util.UUID;

interface QuizProviderRepository extends JpaRepository<Quiz, UUID> {

    /**
     * Finds quiz by ID, checking for restricted object, which can be accessed only by admin roles.
     * @param id of the quiz to be found
     * @return authorized quiz
     */
    @PostAuthorize("returnObject.restricted == true ? hasAnyAuthority('ADMIN', 'SUPER_ADMIN') : true")
    Quiz findQuizById(UUID id);
}
