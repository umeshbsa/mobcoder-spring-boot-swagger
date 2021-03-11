package com.mobcoder.exam.question;

import com.mobcoder.exam.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepo extends CrudRepository<Question, Long> {

}
