package pro.sky.cw_2.repository.impl;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pro.sky.cw_2.exception.QuestionAlreadyExistException;
import pro.sky.cw_2.exception.QuestionNotFoundException;
import pro.sky.cw_2.model.Question;
import pro.sky.cw_2.repository.QuestionRepository;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class  MathQuestionRepositoryTest {

    private final QuestionRepository questionRepository = new MathQuestionRepository();

    @ParameterizedTest
    @MethodSource("question1")
    public void add1Test(Question question) {
        questionRepository.add(question);
        assertThatExceptionOfType(QuestionAlreadyExistException.class)
                .isThrownBy(() -> questionRepository.add(question));
        assertThat(questionRepository.getAll()).containsExactlyInAnyOrder(question);
    }

    @ParameterizedTest
    @MethodSource("question2")
    public void add2Test(String question, String answer) {
        questionRepository.add(question, answer);
        assertThatExceptionOfType(QuestionAlreadyExistException.class)
                .isThrownBy(() -> questionRepository.add(question, answer));
        assertThat(questionRepository.getAll()).containsExactlyInAnyOrder(new Question(question, answer));
    }

    @ParameterizedTest
    @MethodSource("question1")
    public void removeTest(Question question) {
        questionRepository.add(question);
        questionRepository.remove(question);
        assertThat(questionRepository.getAll()).isEmpty();
        assertThatExceptionOfType(QuestionNotFoundException.class).isThrownBy(() -> questionRepository.remove(question));
    }

    public static Stream<Arguments> question1() {
        return Stream.of(
                Arguments.of(new Question("Question", "Answer"))
        );
    }

    public static Stream<Arguments> question2() {
        return Stream.of(
                Arguments.of("Question", "Answer")
        );
    }

}
