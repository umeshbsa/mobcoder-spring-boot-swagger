package com.mobcoder.exam.question;

import com.mobcoder.exam.base.AuditModel;
import com.sun.istack.NotNull;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Entity(name = "question")
@Table(name = "question")
public class Question extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotNull
    public String question;

    @NotNull
    public String answera;

    @NotNull
    public String answerb;

    @NotNull
    public String answerc;

    @NotNull
    public String answerd;

    public String finalAnswer;
}
