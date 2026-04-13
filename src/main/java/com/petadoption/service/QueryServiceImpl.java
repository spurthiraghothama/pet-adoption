package com.petadoption.service;

import com.petadoption.dto.QueryDTO;
import com.petadoption.exception.ResourceNotFoundException;
import com.petadoption.model.Query;
import com.petadoption.model.User;
import com.petadoption.repository.QueryRepository;
import com.petadoption.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QueryServiceImpl implements QueryService {

    @Autowired
    private QueryRepository queryRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Query askQuery(QueryDTO queryDTO) {
        User user = userRepository.findById(queryDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Query query = new Query();
        query.setText(queryDTO.getText());
        query.setUser(user);
        return queryRepository.save(query);
    }

    @Override
    public Query respondQuery(Long questionId, String answer) {
        Query query = queryRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Query not found"));
        query.setAnswer(answer);
        return queryRepository.save(query);
    }

    @Override
    public java.util.List<Query> getAllQueries() {
        return queryRepository.findAll();
    }
}
