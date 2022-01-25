package spyra.lukasz.javaquizzes.feature.quizattempt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import spyra.lukasz.javaquizzes.shared.TakeQuiz;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Controller
class AttemptController {

    @Autowired
    private AttemptService attemptService;


    @GetMapping("/quiz/start/{quiz_id}")
    ModelAndView startSingleQuiz(@PathVariable(name = "quiz_id") long quizId,
                                 Principal principal,
                                 HttpSession session) {
        TakeQuiz takeQuiz = attemptService.takeQuiz(quizId, principal.getName());
        List<QuestionView> questions = attemptService.getQuizQuestionsRandomOrder(quizId);
        session.setAttribute("questions", questions);
        return new ModelAndView("redirect:/quiz/attempt/" + quizId, "take_quiz_id", takeQuiz.getId());
    }


    @SuppressWarnings("unchecked cast")
    @GetMapping("/quiz/attempt/{quiz_id}")
    String progressQuiz(@ModelAttribute("take_quiz_id") long takeQuizId,
                        @PathVariable(name = "quiz_id") long quizId,
                        Model model,
                        HttpSession session) {
        List<QuestionView> questions = (List<QuestionView>) session.getAttribute("questions");
        if (questions.isEmpty()) {
            session.removeAttribute("questions");
            return "result";
        }
        model.addAttribute("quiz_id", quizId);
        model.addAttribute("take_quiz_id", takeQuizId);
        model.addAttribute("question", questions.remove(0));
        return "attempt";
    }

    @PostMapping("/quiz/answer/{quiz_id}")
    ModelAndView givenAnswers(@RequestParam(value = "given_answers", required = false) Long[] answerIds,
                              @RequestParam(value = "question_id") long questionId,
                              @RequestParam(value = "take_quiz_id") long takeQuizId,
                              @PathVariable(value = "quiz_id") long quizId) {
        attemptService.saveGivenAnswers(Arrays.asList(answerIds), takeQuizId, questionId);
        return new ModelAndView("redirect:/quiz/attempt/" + quizId, "take_quiz_id", takeQuizId);
    }

}
