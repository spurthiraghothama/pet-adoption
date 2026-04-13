package com.petadoption.model;

import jakarta.persistence.*;

@Entity
public class Query {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;
    private String text;
    private String answer;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Query() {}

    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
