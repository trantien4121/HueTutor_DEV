package com.trantien.huetutor.services;

import com.trantien.huetutor.models.Tutor;
import com.trantien.huetutor.repositories.ClassRepository;
import com.trantien.huetutor.repositories.PagingClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.trantien.huetutor.models.Class;
import java.util.ArrayList;
import java.util.List;

@Service
public class PagingClassService {

    @Autowired
    ClassRepository classRepository;
    @Autowired
    PagingClassRepository pagingClassRepository;

    public List<Class> getAllClasses(int pageNo, int pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        Page<Class> pagedResult = pagingClassRepository.findAll(paging);
        if(pagedResult.hasContent()){
            return pagedResult.getContent();
        }else{
            return new ArrayList<Class>();
        }
    }
}
