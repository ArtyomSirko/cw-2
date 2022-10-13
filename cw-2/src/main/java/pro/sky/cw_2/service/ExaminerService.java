package pro.sky.cw_2.service;

import pro.sky.cw_2.model.Question;

import java.util.Collection;

public interface ExaminerService {

    Collection<Question> getQuestions(int amount);

}
