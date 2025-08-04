package com.spring_boot_final.metasumer.service;

import java.util.List;

public interface IFishSuggestService {
	public List<String> getSuggestions(String keyword);
}
