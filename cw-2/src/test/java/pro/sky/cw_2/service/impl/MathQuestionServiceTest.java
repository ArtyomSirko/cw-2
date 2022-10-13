package pro.sky.cw_2.service.impl;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.cw_2.exception.QuestionAlreadyExistException;
import pro.sky.cw_2.exception.QuestionNotFoundException;
import pro.sky.cw_2.model.Question;
import pro.sky.cw_2.repository.impl.MathQuestionRepository;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class  MathQuestionServiceTest {

    @Mock
    private MathQuestionRepository mathQuestionRepository;

    @InjectMocks
    private MathQuestionService mathQuestionService;

    @ParameterizedTest
    @MethodSource("question1")
    public void add1Test(Question question) {
        when(mathQuestionRepository.add(question)).thenReturn(question);
        mathQuestionService.add(question);

        when(mathQuestionRepository.add(question)).thenThrow(new QuestionAlreadyExistException());
        assertThatExceptionOfType(QuestionAlreadyExistException.class)
                .isThrownBy(() -> mathQuestionService.add(question));

        when(mathQuestionRepository.getAll()).thenReturn(Collections.singleton(question));
        assertThat(mathQuestionService.getAll()).containsExactlyInAnyOrder(question);
    }

    @ParameterizedTest
    @MethodSource("question2")
    public void add2Test(String question, String answer) {
        Question q = new Question(question, answer);
        when(mathQuestionRepository.add(question, answer)).thenReturn(q);
        mathQuestionService.add(question, answer);

        when(mathQuestionRepository.add(question, answer)).thenThrow(new QuestionAlreadyExistException());
        assertThatExceptionOfType(QuestionAlreadyExistException.class)
                .isThrownBy(() -> mathQuestionService.add(question, answer));

        when(mathQuestionRepository.getAll()).thenReturn(Collections.singleton(q));
        assertThat(mathQuestionService.getAll()).containsExactlyInAnyOrder(q);
    }

    @ParameterizedTest
    @MethodSource("question1")
    public void removeTest(Question question) {
        when(mathQuestionRepository.add(question)).thenReturn(question);
        mathQuestionService.add(question);

        when(mathQuestionRepository.remove(question)).thenReturn(question);
        mathQuestionService.remove(question);

        when(mathQuestionRepository.remove(question)).thenThrow(new QuestionNotFoundException());
        assertThatExceptionOfType(QuestionNotFoundException.class).isThrownBy(() -> mathQuestionService.remove(question));
    }

    @ParameterizedTest
    @MethodSource("questions")
    public void getRandomQuestionTest(Set<Question> questions) {
        when(mathQuestionRepository.getAll()).thenReturn(questions);

        assertThat(mathQuestionService.getRandomQuestion()).isIn(mathQuestionService.getAll());
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

    public static Stream<Arguments> questions() {
        return Stream.of(
                Arguments.of(
                        Set.of(
                                new Question("Question1", "Answer1"),
                                new Question("Question2", "Answer2"),
                                new Question("Question3", "Answer3")
                        )
                )
        );
    }

}
