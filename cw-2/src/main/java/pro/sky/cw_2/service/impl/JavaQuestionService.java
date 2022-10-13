package pro.sky.cw_2.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pro.sky.cw_2.model.Question;
import pro.sky.cw_2.repository.QuestionRepository;
import pro.sky.cw_2.service.QuestionService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

@Service
public class JavaQuestionService implements QuestionService {

    private final Random random;
    private final QuestionRepository questionRepository;

    public JavaQuestionService(@Qualifier("javaQuestionRepository") QuestionRepository questionRepository) {
        this.random = new Random();
        this.questionRepository = questionRepository;
    }

    @Override
    public Question add(String question,
                        String answer) {
        return questionRepository.add(question, answer);
    }

    @Override
    public Question add(Question question) {
        return questionRepository.add(question);
    }

    @Override
    public Question remove(Question question) {
        return questionRepository.remove(question);
    }

    @Override
    public Collection<Question> getAll() {
        return questionRepository.getAll();
    }

    @Override
    public Question getRandomQuestion() {
        Collection<Question> questions = getAll();
        return new ArrayList<>(questions).get(random.nextInt(questions.size()));
    }
}
