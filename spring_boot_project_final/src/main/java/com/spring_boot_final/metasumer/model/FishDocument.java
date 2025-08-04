package com.spring_boot_final.metasumer.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "fishinfo")  // 색인 이름
public class FishDocument {
	@Id
	private String fishNo;
	
	@Field(type = FieldType.Text)
    private String fishName;

    private String fishImg;
    private String fishCtgId;
    
	public String getFishNo() {
		return fishNo;
	}
	public void setFishNo(String fishNo) {
		this.fishNo = fishNo;
	}
	public String getFishName() {
		return fishName;
	}
	public void setFishName(String fishName) {
		this.fishName = fishName;
	}
	public String getFishImg() {
		return fishImg;
	}
	public void setFishImg(String fishImg) {
		this.fishImg = fishImg;
	}
	public String getFishCtgId() {
		return fishCtgId;
	}
	public void setFishCtgId(String fishCtgId) {
		this.fishCtgId = fishCtgId;
	}  

}
