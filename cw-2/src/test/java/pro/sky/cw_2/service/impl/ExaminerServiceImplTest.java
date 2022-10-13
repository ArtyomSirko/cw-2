package pro.sky.cw_2.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import pro.sky.cw_2.exception.NotEnoughQuestionsException;
import pro.sky.cw_2.model.Question;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExaminerServiceImplTest {

    @Mock
    private JavaQuestionService javaQuestionService;

    @Mock
    private MathQuestionService mathQuestionService;

    @InjectMocks
    private ExaminerServiceImpl examinerService;

    private final Set<Question> javaQuestions = new HashSet<>();
    private final Set<Question> mathQuestions = new HashSet<>();

    @BeforeEach
    public void beforeEach() {
        javaQuestions.clear();
        mathQuestions.clear();

        javaQuestions.addAll(
                Stream.of(
                        new Question("Вопрос по Java - 1", "Ответ по Java - 1"),
                        new Question("Вопрос по Java - 2", "Ответ по Java - 2"),
                        new Question("Вопрос по Java - 3", "Ответ по Java - 3")
                ).collect(Collectors.toSet())
        );
        mathQuestions.addAll(
                Stream.of(
                        new Question("Вопрос по Math - 1", "Ответ по Math - 1"),
                        new Question("Вопрос по Math - 2", "Ответ по Math - 2")
                ).collect(Collectors.toSet())
        );
        when(javaQuestionService.getAll()).thenReturn(javaQuestions);
        when(mathQuestionService.getAll()).thenReturn(mathQuestions);
    }

    @Test
    public void getQuestionsNegativeTest() {
        assertThatExceptionOfType(NotEnoughQuestionsException.class)
                .isThrownBy(() -> examinerService.getQuestions(-1));

        assertThatExceptionOfType(NotEnoughQuestionsException.class)
                .isThrownBy(() -> examinerService.getQuestions(javaQuestions.size() + mathQuestions.size() + 1));
    }

    @Test
    public void getQuestionsPositiveTest() {
        Random random = mock(MyRandom.class);
        when(random.nextInt(anyInt())).thenReturn(1, 0, 0, 0, 0, 0, 1, 1, 1);
        ReflectionTestUtils.setField(examinerService, "random", random);
        when(javaQuestionService.getRandomQuestion()).thenReturn(
                new Question("Вопрос по Java - 1", "Ответ по Java - 1"),
                new Question("Вопрос по Java - 2", "Ответ по Java - 2"),
                new Question("Вопрос по Java - 1", "Ответ по Java - 1"),
                new Question("Вопрос по Java - 1", "Ответ по Java - 1"),
                new Question("Вопрос по Java - 3", "Ответ по Java - 3")
        );
        when(mathQuestionService.getRandomQuestion()).thenReturn(
                new Question("Вопрос по Math - 2", "Ответ по Math - 2"),
                new Question("Вопрос по Math - 2", "Ответ по Math - 2"),
                new Question("Вопрос по Math - 2", "Ответ по Math - 2"),
                new Question("Вопрос по Math - 1", "Ответ по Math - 1")
        );

        assertThat(examinerService.getQuestions(5))
                .hasSize(5)
                .containsExactly(
                        new Question("Вопрос по Math - 2", "Ответ по Math - 2"),
                        new Question("Вопрос по Java - 1", "Ответ по Java - 1"),
                        new Question("Вопрос по Java - 2", "Ответ по Java - 2"),
                        new Question("Вопрос по Java - 3", "Ответ по Java - 3"),
                        new Question("Вопрос по Math - 1", "Ответ по Math - 1")
                );
    }

    @ParameterizedTest
    @MethodSource("params")
    public void getQuestionsPositiveTest(int amount) {
        Random random = new Random();
        ReflectionTestUtils.setField(javaQuestionService, "random", random);
        ReflectionTestUtils.setField(mathQuestionService, "random", random);
        lenient().when(javaQuestionService.getRandomQuestion()).thenCallRealMethod();
        lenient().when(mathQuestionService.getRandomQuestion()).thenCallRealMethod();

        assertThat(examinerService.getQuestions(amount))
                .hasSize(amount)
                .containsAnyElementsOf(Stream.concat(javaQuestions.stream(), mathQuestions.stream()).collect(Collectors.toSet()));
    }

    public static Stream<Arguments> params() {
        return Stream.of(
                Arguments.of(1),
                Arguments.of(2),
                Arguments.of(3),
                Arguments.of(4),
                Arguments.of(5)
        );
    }

    public static class MyRandom extends Random {
    }

}
