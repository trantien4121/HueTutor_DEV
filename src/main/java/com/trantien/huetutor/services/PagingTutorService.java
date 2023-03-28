package com.trantien.huetutor.services;

import com.trantien.huetutor.repositories.PagingTutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.trantien.huetutor.models.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class PagingTutorService {
    @Autowired
    PagingTutorRepository repository;

    public List<Tutor> getAllTutor(int pageNo, int pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        Page<Tutor> pagedResult = repository.findAll(paging);
        if(pagedResult.hasContent()){
            return pagedResult.getContent();
        }else{
            return new ArrayList<Tutor>();
        }
    }
}
