package com.spring_boot_final.metasumer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring_boot_final.metasumer.elasticsearch.dao.IFishSearchDAO;
import com.spring_boot_final.metasumer.model.FishDocument;

@Service
public class FishSearchService implements IFishSearchService {
	@Autowired
	IFishSearchDAO dao;

	@Override
	public List<FishDocument> searchFish(String keyword) {
		return dao.findByFishNameContaining(keyword);
	}

}
