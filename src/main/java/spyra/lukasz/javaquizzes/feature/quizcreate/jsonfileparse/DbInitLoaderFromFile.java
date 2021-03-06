package spyra.lukasz.javaquizzes.feature.quizcreate.jsonfileparse;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import spyra.lukasz.javaquizzes.shared.Quiz;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Loads {@link Quiz} from JSON file for database initialization
 */
@Component
@RequiredArgsConstructor
class DbInitLoaderFromFile {

    private static final Path FILES_PATH = Path.of("src/main/resources/quizcontent/");

    private final JsonReader jsonReader;

    private final QuizInitRepository quizRepo;


    /**
     * Called upon bean init completion, loads quizzes from all files in given {@link DbInitLoaderFromFile#FILES_PATH}
     * and saves in database
     *
     * @throws IOException when error reading from given file
     */
    @PostConstruct
    void loadQuizzesFromFile() throws IOException {
        try (Stream<Path> files = Files.walk(FILES_PATH)) {
            files
                    .filter(file -> !Files.isDirectory(file))
                    .map(this::getQuizFromJson)
                    .forEach(quizRepo::save);
        }
    }

    /**
     * Reads quizzes from given file path, uses @SneakyThrows for clear code in FP streams
     *
     * @param file to be read
     * @return Quiz entity
     */
    @SneakyThrows
    private Quiz getQuizFromJson(Path file) {
        return jsonReader.readJsonFile(file);
    }
}
