package com.mobcoder.exam.quiz;

import com.mobcoder.exam.base.AuditModel;
import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
@Entity(name = "quiz_status")
@Table(name = "quiz_status")
public class Quiz extends AuditModel {

    @Id
    public Long id;

    @NotNull
    public boolean quizStatus;
}
