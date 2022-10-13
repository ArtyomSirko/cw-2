package pro.sky.cw_2.repository;

import pro.sky.cw_2.model.Question;

import java.util.Collection;

public interface QuestionRepository {

    Question add(Question question);

    Question add(String question, String answer);

    Question remove(Question question);

    Collection<Question> getAll();
}
