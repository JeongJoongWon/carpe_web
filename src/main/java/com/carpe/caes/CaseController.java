package com.carpe.caes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.carpe.common.CarpeConfig;
import com.carpe.common.Consts;

@Controller
public class CaseController {

	@Inject
	private CaseService service;

	@RequestMapping(value = "/case.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView caseView(@RequestParam HashMap<String, String> map, HttpSession session, HttpServletRequest requst, Model model) throws Exception {
		ModelAndView mav = new ModelAndView();

		mav.setViewName("carpe/case/case");
		return mav;
	}

	@RequestMapping(value = "/map.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView mapView(@RequestParam HashMap<String, String> map, HttpSession session, HttpServletRequest requst, Model model) throws Exception {
		ModelAndView mav = new ModelAndView();

		mav.setViewName("carpe/case/map");
		return mav;
	}

	@RequestMapping(value = "/case_list.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView getCaseList(Locale locale, @RequestParam HashMap<String, String> map, Model model) throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("jsonView");

		Map<String, Object> paramMap = new HashMap<String, Object>();
		List<Map> caseList = service.selectCaseList(paramMap);

		mav.addObject("list", caseList);
		mav.addObject("totalcount", caseList.size());

		return mav;
	}

	/**
	 * Main 페이지 Case 선택
	 * @param map
	 * @param session
	 * @param requst
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/select_case.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView selectCase(@RequestParam HashMap<String, String> map, HttpSession session, HttpServletRequest requst, Model model) throws Exception {
		ModelAndView mav = new ModelAndView();

		String caseId = map.get("id");
		String caseName = map.get("name");
		
		session.setAttribute(Consts.SESSION_CASE_ID, caseId);
		session.setAttribute(Consts.SESSION_CASE_NAME, caseName);

		mav.setViewName("forward:/overview.do");

		return mav;
	}

	/**
	 * Case 추가
	 * @param map
	 * @param session
	 * @param requst
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/add_case.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView addcase(@RequestParam HashMap<String, String> map, HttpSession session, HttpServletRequest requst, Model model) throws Exception {
		ModelAndView mav = new ModelAndView();

		Map<String, Object> paramMap = new HashMap<String, Object>();

		String caseId = CarpeConfig.getCaseCode() + UUID.randomUUID().toString().replace("-", "");

		paramMap.put("id", caseId);
		paramMap.put("name", map.get("name"));
		paramMap.put("admin", map.get("admin"));
		paramMap.put("desc", map.get("desc"));

		int affected = 0;
		try {
			affected = service.insertCase(paramMap);
		} catch (Exception e) {
			e.printStackTrace();
		}

		mav.addObject("affected", affected);
		mav.setViewName("jsonView");

		return mav;
	}
	
	/**
	 * Case 삭제
	 * @param map
	 * @param session
	 * @param requst
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete_case.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView delCase(@RequestParam HashMap<String, String> map, HttpSession session, HttpServletRequest requst, Model model) throws Exception {
		ModelAndView mav = new ModelAndView();

		Map<String, Object> paramMap = new HashMap<String, Object>();

//		String caseId = CarpeConfig.getCaseCode() + UUID.randomUUID().toString().replace("-", "");
		String delimiter = ",";
		List<String> split = Arrays.asList(map.get("caseId").split(delimiter));
		ArrayList<String> caseList = new ArrayList<String>();
		caseList.addAll(split);
		paramMap.put("caseList", caseList);


		int affected = 0;
		try {
			// case list delete
			affected = service.deleteCaseList(paramMap);
		} catch (Exception e) {
			e.printStackTrace();
		}

		mav.addObject("affected", affected);
		mav.setViewName("jsonView");

		return mav;
	}
}