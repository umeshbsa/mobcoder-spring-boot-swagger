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
    @Email
    @Size(max = 200)
    public String question;

    @NotNull
    public String answer1;

    @NotNull
    public String answer2;

    @NotNull
    public String answer3;

    @NotNull
    public String answer4;

    public int finalAnswer;
}
