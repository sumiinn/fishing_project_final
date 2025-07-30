package com.spring_boot_final.metasumer.elasticsearch.dao;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.spring_boot_final.metasumer.model.FishDocument;

public interface IFishSearchDAO extends ElasticsearchRepository<FishDocument, String> {
	// 어종 검색 (메서드 이름 분석해서 spring에서 쿼리 만들어줌)
	public List<FishDocument> findByFishNameContaining(String keyword);
}
