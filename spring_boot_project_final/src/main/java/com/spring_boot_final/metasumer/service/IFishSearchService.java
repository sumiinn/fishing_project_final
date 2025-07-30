package com.spring_boot_final.metasumer.service;

import java.util.List;

import com.spring_boot_final.metasumer.model.FishDocument;

public interface IFishSearchService {
	public List<FishDocument> searchFish(String keyword);
}
