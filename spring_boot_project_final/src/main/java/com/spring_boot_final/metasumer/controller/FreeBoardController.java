package com.spring_boot_final.metasumer.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.spring_boot_final.metasumer.model.FreeBoardVO;
import com.spring_boot_final.metasumer.service.FreeBoardService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class FreeBoardController {

	@Autowired
	FreeBoardService fbService;

	// 자유게시판, 공지사항, 자주묻는 질문, 고객센터 전체 보기
	@RequestMapping("/freeboard/freeboardView/{boardCtgId}")
	public String freeboardView(@PathVariable String boardCtgId,
			@RequestParam(value = "page", defaultValue = "1") int page, Model model) {
		
		try {
        
			if (boardCtgId.equals("3")) {
				ArrayList<FreeBoardVO> qnaList = fbService.qnaList(boardCtgId);
				model.addAttribute("qnaList", qnaList);
				return "freeboard/qnaView";
			}

            int pageSize = 10;

            if (page < 1) {
                page = 1;
            }

            int totalItems = fbService.countTotalItems(boardCtgId);

            int totalPages = (int) Math.ceil((double) totalItems / pageSize);

            if (page > totalPages) {
                page = totalPages;
            }

            int startPage = (page - 1) / 10 * 10 + 1;
            int endPage = Math.min(startPage + 9, totalPages);
            int offset = (page - 1) * pageSize;

            ArrayList<FreeBoardVO> fbList = fbService.selectItemsForPage(offset, pageSize, boardCtgId);

            model.addAttribute("fbList", fbList);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
            model.addAttribute("boardCtgId", boardCtgId);

            return "freeboard/freeboardView";

        } catch (Exception e) {
            e.printStackTrace();
            return "freeboard/freeboardView";
        }
	}

	// 벼룩시장
	@RequestMapping("/freeboard/fleamarketList/{completed}")
	public String fleamarketList(@PathVariable int completed, Model model, @RequestParam(value = "page", defaultValue = "1") String pageParam) {

		String boardCtgId = "5"; // 카테고리 ID

        // 전체 레코드 조회
        List<FreeBoardVO> fbList = fbService.fleamarketList(boardCtgId, completed);

        // 페이지 네비게이션 설정
        int recordsPerPage = 20; // 페이지당 레코드 수
        int totalRecords = fbList.size(); // 전체 레코드 수
        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage); // 총 페이지 수

        int currentPage;
        try {
            currentPage = Integer.parseInt(pageParam.split("\\.")[0]);
        } catch (NumberFormatException e) {
            currentPage = 1;
        }

        if (currentPage < 1) {
            currentPage = 1;
        } else if (currentPage > totalPages) {
            currentPage = totalPages;
        }

        int startIndex = (currentPage - 1) * recordsPerPage;
        int endIndex = Math.min(startIndex + recordsPerPage, totalRecords);

        List<FreeBoardVO> recordsForPage;
        if (startIndex >= totalRecords) {
            recordsForPage = Collections.emptyList();
        } else {
            recordsForPage = fbList.subList(startIndex, endIndex);
        }

        int startPage = (currentPage - 1) / 10 * 10 + 1;
        int endPage = Math.min(startPage + 9, totalPages);

        // 모델에 데이터 추가
        model.addAttribute("fbList", recordsForPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

		return "freeboard/fleamarketList";
	}

	// 벼룩시장
	@RequestMapping("/freeboard/fleamarketListcompleted/{completed}")
	public String fleamarketcompleted(@PathVariable int completed, @RequestParam String boardPostNo) {

		if (completed == 1) {
			completed = 0;
		} else {
			completed = 1;
		}

		fbService.fleamarketcompleted(boardPostNo, completed);

		return "redirect:/freeboard/fleamarketList/" + completed;
	}

	@RequestMapping("/freeboard/newfreeboardForm/{boardCtgId}")
	public String newfreeboradForm(@PathVariable String boardCtgId, HttpServletRequest request) {

		HttpSession session = request.getSession();

		String memNickname = (String) session.getAttribute("memNickname");
		String memId = (String) session.getAttribute("memId");

		request.setAttribute("memNickname", memNickname);
		request.setAttribute("memId", memId);
		request.setAttribute("boardCtgId", boardCtgId);

		return "freeboard/newfreeboardForm";
	}

	// 자유게시판 글 등록
	@RequestMapping(value = "/freeboard/insertFreeBoard", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> insertFreeBoard(@RequestParam("title") String title, @RequestParam(value = "completed", required = false) int completed,
			@RequestParam("content") String content, @RequestParam("memId") String memId,
			@RequestParam(value = "price", required = false) String price, @RequestParam("boardCtgId") int boardCtgId,
			@RequestParam(value = "uploadFile", required = false) MultipartFile file, Model model) {

		Map<String, String> response = new HashMap<>();

		try {
			FreeBoardVO vo = new FreeBoardVO();
			vo.setTitle(title);
			vo.setContent(content);
			vo.setBoardCtgId(boardCtgId);
			vo.setCompleted(completed);
			vo.setMemId(memId);

			if (price != null && !price.isEmpty()) {
				vo.setPrice(price);
			}

			if (file != null && !file.isEmpty()) {
				String savedFileName = saveFile(file);
				vo.setUploadFile(savedFileName);
			}

			fbService.insertFreeBoard(vo);

			String redirectUrl;
			
			if (boardCtgId == 5) {
				redirectUrl = "/freeboard/fleamarketList/" + completed; 
			} else {
				redirectUrl = "/freeboard/freeboardView/" + boardCtgId;
			}

			response.put("status", "success");
			response.put("redirectUrl", redirectUrl);

		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", "fail");
		}

		return response;
	}

	@RequestMapping("/freeboard/detailViewFreeBoard/{boardPostNo}/{sidmemId}")
	public String detailViewFreeBoard(@PathVariable String boardPostNo, @PathVariable String sidmemId, Model model) {

		FreeBoardVO fb = fbService.detailViewFreeBoard(boardPostNo);
		fbService.countViews(sidmemId, boardPostNo);
		

		model.addAttribute("fb", fb);

		return "freeboard/detailViewFreeBoard";
	}

	// 다운로드
	@RequestMapping("/downloadFile/{uploadFile}")
	public void FileDownload(@PathVariable String uploadFile, HttpServletResponse response) throws IOException {
		
	    File f = new File("D:/springWorkspace/metasumer_images", uploadFile);
		//File f = new File("/usr/local/project/metasumer_images/", uploadFile);
		String encodedFileName = new String(uploadFile.getBytes("UTF-8"), "ISO-8859-1");

		response.setContentType("application/download");
		response.setContentLength((int) f.length());
		response.setHeader("Content-Disposition", "attatchment;filename=\"" + encodedFileName + "\"");

		FileInputStream fis = new FileInputStream(f);

		OutputStream os = response.getOutputStream();

		FileCopyUtils.copy(fis, os);

	}

	// 수정
	@RequestMapping("/freeboard/updateBoardForm/{boardPostNo}")
	public String updateFreeBoardForm(@PathVariable String boardPostNo, Model model) {
		FreeBoardVO fb = fbService.detailViewFreeBoard(boardPostNo);
		model.addAttribute("fb", fb);
		return "freeboard/updateFreeBoardForm";
	}

	@RequestMapping("freeboard/updateFreeBoard")
	public String updateFreeBoard(@RequestParam("boardPostNo") String boardPostNo, @RequestParam("title") String title,
			@RequestParam("boardCtgId") int boardCtgId, @RequestParam("content") String content, 
			@RequestParam(value = "price", required = false) String price, @RequestParam(value = "completed", required = false) int completed,
			@RequestParam("uploadFile") MultipartFile file) {

		FreeBoardVO fb = fbService.detailViewFreeBoard(boardPostNo);

		FreeBoardVO vo = new FreeBoardVO();
		vo.setTitle(title);
		vo.setContent(content);
		vo.setBoardPostNo(boardPostNo);

		if (price != null && !price.isEmpty()) {
			vo.setPrice(price);
		}

		try {
			if (file != null && !file.isEmpty()) {
				String savedFileName = saveFile(file);
				vo.setUploadFile(savedFileName);
			} else {
				vo.setUploadFile(fb.getUploadFile());
			}

			fbService.updateFreeBoard(vo);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (boardCtgId == 5) {
			return "redirect:/freeboard/fleamarketList/" + completed;
		} else {
			return "redirect:/freeboard/freeboardView/" + boardCtgId;
		}
	}

	// 삭제
	@RequestMapping("/freeboard/deleteBoard")
	public String deleteFreeBoard(@RequestParam("boardPostNo") String boardPostNo, @RequestParam("completed") int completed,
			@RequestParam("boardCtgId") int boardCtgId) {
		
		fbService.deleteFreeBoardComment(boardPostNo);
		fbService.deleteFreeBoard(boardPostNo);

		if (boardCtgId == 5) {
			return "redirect:/freeboard/fleamarketList/" + completed;
		} else {
			return "redirect:/freeboard/freeboardView/" + boardCtgId;
		}
	}

	private String saveFile(MultipartFile file) throws IOException {
		String uploadPath = "D:/springWorkspace/metasumer_images/";
		//String uploadPath = "/usr/local/project/upload/";

		String originalFileName = file.getOriginalFilename();
		originalFileName = originalFileName.replace("[", "_").replace("]", "_");

		UUID uuid = UUID.randomUUID();
		String savedFileName = uuid.toString() + "_" + originalFileName;
		File uploadFile = new File(uploadPath + savedFileName);

		file.transferTo(uploadFile);

		return savedFileName;
	}

}
