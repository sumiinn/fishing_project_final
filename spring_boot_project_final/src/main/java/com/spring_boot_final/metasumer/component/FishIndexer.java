package com.spring_boot_final.metasumer.component;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;

import com.spring_boot_final.metasumer.dao.IFishDAO;
import com.spring_boot_final.metasumer.elasticsearch.dao.IFishSearchDAO;
import com.spring_boot_final.metasumer.model.FishDocument;
import com.spring_boot_final.metasumer.model.FishVO;

//@Component
public class FishIndexer implements CommandLineRunner {
	@Autowired
    IFishDAO fishDAO; // 기존 MyBatis DAO

    @Autowired
    IFishSearchDAO fishSearchDAO; // Elasticsearch DAO

    @Override
    public void run(String... args) throws Exception {
        List<FishVO> fishList = fishDAO.listAllFish(0, 999);

        List<FishDocument> docs = fishList.stream().map(f -> {
            FishDocument doc = new FishDocument();
            doc.setFishNo(f.getFishNo());
            doc.setFishName(f.getFishName());
            doc.setFishImg(f.getFishImg());
            doc.setFishCtgId(f.getFishCtgId());
            return doc;
        }).collect(Collectors.toList());

        fishSearchDAO.saveAll(docs);
        System.out.println("🐟 색인 완료! Elasticsearch에 어종 데이터가 등록되었습니다.");
    }

}
