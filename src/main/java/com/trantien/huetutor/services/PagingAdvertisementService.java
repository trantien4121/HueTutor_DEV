package com.trantien.huetutor.services;

import com.trantien.huetutor.models.Tutor;
import com.trantien.huetutor.repositories.PagingAdvertisementRepository;
import com.trantien.huetutor.repositories.PagingTutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import com.trantien.huetutor.models.*;

@Service
public class PagingAdvertisementService {
    @Autowired
    PagingAdvertisementRepository repository;

    public List<Advertisement> getAllAds(int pageNo, int pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        Page<Advertisement> pagedResult = repository.findAll(paging);
        if(pagedResult.hasContent()){
            return pagedResult.getContent();
        }else{
            return new ArrayList<Advertisement>();
        }
    }
}
