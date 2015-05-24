package com.sinontech.project.web.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.sinontech.framework.common.DateUtil;
import com.sinontech.framework.web.BaseAction;
import com.sinontech.framework.webService.ReadUrl;
import com.sinontech.project.entity.activity.DiyMatchApply;
import com.sinontech.project.entity.activity.DiyMatchColorRing;
import com.sinontech.project.entity.activity.DiyMatchGetTicket;
import com.sinontech.project.entity.activity.DiyMatchLogin;
import com.sinontech.project.entity.activity.DiyMatchOpenDiy;
import com.sinontech.project.entity.activity.DiyMatchPwd;
import com.sinontech.project.entity.activity.DiyMatchTicket;
import com.sinontech.project.entity.activity.DiyMatchVote;
import com.sinontech.project.service.activity.DiyMatchApplyService;
import com.sinontech.project.service.activity.DiyMatchColorRingService;
import com.sinontech.project.service.activity.DiyMatchGetTicketService;
import com.sinontech.project.service.activity.DiyMatchLoginService;
import com.sinontech.project.service.activity.DiyMatchOpenDiyService;
import com.sinontech.project.service.activity.DiyMatchPwdService;
import com.sinontech.project.service.activity.DiyMatchTicketService;
import com.sinontech.project.service.activity.DiyMatchVoteService;

/**
 * 自定义action
 * 
 * @author lyj
 * 
 */
@ParentPackage("cubeDefault")
@Namespace(value = "")
@Results({ @Result(name = "index", type = "freemarker", location = "/index.html"), @Result(name = "registration", type = "freemarker", location = "/registration.html"), @Result(name = "exception", type = "freemarker", location = "/exception.html"), @Result(name = "preliminaries", type = "freemarker", location = "/preliminaries.html") })
public class DiyMatchAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 首页点击我要报名到达报名页面
	@Action(value = "registration", interceptorRefs = { @InterceptorRef("Logger"), @InterceptorRef("defaultStack") })
	public String index() throws Exception {
		return "registration";
	}

	// 首页点击我要报名到达报名页面
	@Action(value = "exception", interceptorRefs = { @InterceptorRef("Logger"), @InterceptorRef("defaultStack") })
	public String exception() throws Exception {
		HttpServletRequest request = getRequest();
		String flag = request.getParameter("flag");
		request.setAttribute("flag", flag);
		return "exception";
	}

	// 登录
	@Action(value = "login", interceptorRefs = { @InterceptorRef("Logger"), @InterceptorRef("defaultStack") })
	public void login() throws Exception {
		HttpServletRequest request = getRequest();
		HttpServletResponse response = getResponse();
		HttpSession session = getSession();
		String result = "0";// AJAX请求返回码:0登陆失败；1登陆成功;2登陆异常
		String nowTime = DateUtil.getFormatDate("yyyy-MM-dd HH:mm:ss");// 获取当前时间
		String phone = request.getParameter("phone");
		String pwd = request.getParameter("pwd");
		try {
			DiyMatchPwdService diyMatchPwdService = (DiyMatchPwdService) getBean("diyMatchPwdService");
			DiyMatchPwd diyMatchPwd = diyMatchPwdService.getByPhone(phone);
			if (diyMatchPwd != null && pwd.equals(diyMatchPwd.getPwd())) {
				DiyMatchLoginService diyMatchLoginService = (DiyMatchLoginService) getBean("diyMatchLoginService");
				DiyMatchTicketService diyMatchTicketService = (DiyMatchTicketService) getBean("diyMatchTicketService");
				DiyMatchApplyService diyMatchApplyService = (DiyMatchApplyService) getBean("diyMatchApplyService");
				// 记录用户登录
				DiyMatchLogin diyMatchLogin = new DiyMatchLogin();
				diyMatchLogin.setCreateTime(nowTime);
				diyMatchLogin.setModifyTime(nowTime);
				diyMatchLogin.setPhone(phone);
				diyMatchLogin.setPwd(pwd);
				diyMatchLoginService.save(diyMatchLogin);

				int count = 0;// 投票剩余次数
				if (diyMatchTicketService.getByPhone(phone) != null) {
					count = diyMatchTicketService.getByPhone(phone).getCount();
				}
				Long score = 0L;// 我的票数
				String opusName = "";// 作品名称
				String opusUrl = "";// 作品地址
				DiyMatchApply diyMatchApply = diyMatchApplyService.getByPhone(phone);
				if (diyMatchApply != null) {
					score = Math.round(diyMatchApply.getScoreWeb() + diyMatchApply.getScoreWeixin() * 0.3);// 修改取整方式为四舍五入Math.round,同时修改score为Long
					opusName = diyMatchApply.getOpus();
					opusUrl = diyMatchApply.getRingPath();
				}
				int ranking = diyMatchApplyService.getRanking(phone) + 1;// 我的排行
				session.setAttribute("diyMatchLogin", diyMatchLogin);
				session.setAttribute("opusName", opusName);
				session.setAttribute("opusUrl", opusUrl);
				session.setAttribute("count", count);
				session.setAttribute("score", score);
				session.setAttribute("ranking", ranking);
				result = "1";
			}
		} catch (Exception e) {
			result = "2";
			e.printStackTrace();
		} finally {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().print(result);
			response.getWriter().flush();
		}
	}

	// 退出登录
	@Action(value = "logout", interceptorRefs = { @InterceptorRef("Logger"), @InterceptorRef("defaultStack") })
	public void logout() throws Exception {
		HttpSession session = getSession();
		session.removeAttribute("diyMatchLogin");
		session.invalidate();
	}

	// 获取验证码
	@Action(value = "getPwd", interceptorRefs = { @InterceptorRef("Logger"), @InterceptorRef("defaultStack") })
	public void getPwd() throws Exception {
		HttpServletRequest request = getRequest();
		String nowTime = DateUtil.getFormatDate("yyyy-MM-dd HH:mm:ss");// 获取当前时间
		String phone = request.getParameter("phone");
		int min = 100000;
		int max = 999999;
		Random random = new Random();
		String pwd = String.valueOf((random.nextInt(max) % (max - min + 1) + min));
		DiyMatchPwdService diyMatchPwdService = (DiyMatchPwdService) getBean("diyMatchPwdService");
		DiyMatchPwd diyMatchPwd = diyMatchPwdService.getByPhone(phone);
		if (diyMatchPwd == null) {
			diyMatchPwd = new DiyMatchPwd();
			diyMatchPwd.setCreateTime(nowTime);
			diyMatchPwd.setModifyTime(nowTime);
			diyMatchPwd.setPhone(phone);
			diyMatchPwd.setPwd(pwd);
			diyMatchPwdService.save(diyMatchPwd);
		} else {
			diyMatchPwd.setModifyTime(nowTime);
			diyMatchPwd.setPwd(pwd);
			diyMatchPwdService.modify(diyMatchPwd);
		}
		// 2015-05-17修改发送验证码的内容
		String msg = "验证码：" + pwd + " ，请确认本人操作。[彩铃DIY设计大赛]";
		ReadUrl.sendMess(phone, msg);
	}

	// 记录开通DIY for WX
	@Action(value = "saveOpenDiy", interceptorRefs = { @InterceptorRef("Logger"), @InterceptorRef("defaultStack") })
	public void saveOpenDiy() throws Exception {
		HttpServletRequest requet = getRequest();
		String phone = requet.getParameter("phone");
		String nowTime = DateUtil.getFormatDate("yyyy-MM-dd HH:mm:ss");// 获取当前时间
		DiyMatchOpenDiyService diyMatchOpenDiyService = (DiyMatchOpenDiyService) getBean("diyMatchOpenDiyService");
		DiyMatchOpenDiy diyMatchOpenDiy = new DiyMatchOpenDiy();
		diyMatchOpenDiy.setCreateTime(nowTime);
		diyMatchOpenDiy.setModifyTime(nowTime);
		diyMatchOpenDiy.setPhone(phone);
		diyMatchOpenDiyService.save(diyMatchOpenDiy);
	}

	// 从微信报名，生成报名
	@Action(value = "createApplyFromWX", interceptorRefs = { @InterceptorRef("Logger"), @InterceptorRef("defaultStack") })
	public void createApplyFromWX() throws Exception {
		HttpServletRequest requet = getRequest();
		HttpServletResponse response = getResponse();
		response.setContentType("text/html;charset=UTF-8");
		String nowTime = DateUtil.getFormatDate("yyyy-MM-dd HH:mm:ss");// 获取当前时间
		String phone = requet.getParameter("phone");
		String name = requet.getParameter("name");
		DiyMatchApplyService diyMatchApplyService = (DiyMatchApplyService) getBean("diyMatchApplyService");
		DiyMatchApply diyMatchApply = diyMatchApplyService.getByPhone(phone);
		if (diyMatchApply == null) {
			diyMatchApply = new DiyMatchApply();
			diyMatchApply.setCreateTime(nowTime);
			diyMatchApply.setModifyTime(nowTime);
			diyMatchApply.setPhone(phone);
			diyMatchApply.setName(name);
			diyMatchApply.setScoreWeb(0);
			diyMatchApply.setScoreWeixin(0);
			diyMatchApply.setType("1");
			diyMatchApply.setState("0");// 没有完成报名
			diyMatchApplyService.save(diyMatchApply);
		} else {
			diyMatchApply.setModifyTime(nowTime);
			diyMatchApply.setPhone(phone);
			diyMatchApply.setName(name);
			diyMatchApply.setType("1");
			diyMatchApplyService.modify(diyMatchApply);
		}
		response.getWriter().flush();
	}

	// 从微信报名,成功
	@Action(value = "applyFromWX", interceptorRefs = { @InterceptorRef("Logger"), @InterceptorRef("defaultStack") })
	public void applyFromWX() throws Exception {
		String result = "0";
		HttpServletRequest requet = getRequest();
		HttpServletResponse response = getResponse();
		response.setContentType("text/html;charset=UTF-8");
		String nowTime = DateUtil.getFormatDate("yyyy-MM-dd HH:mm:ss");// 获取当前时间
		String phone = requet.getParameter("phone");
		DiyMatchApplyService diyMatchApplyService = (DiyMatchApplyService) getBean("diyMatchApplyService");
		DiyMatchApply diyMatchApply = diyMatchApplyService.getByPhone(phone);
		if (diyMatchApply != null) {
			diyMatchApply.setModifyTime(nowTime);
			diyMatchApply.setType("1");
			diyMatchApply.setState("1");
			diyMatchApplyService.modify(diyMatchApply);
			result = "1";// 报名成功
		}
		response.getWriter().print(result);
		response.getWriter().flush();
	}

	// 根据号码获取报名记录
	@Action(value = "getApplyStateByPhone", interceptorRefs = { @InterceptorRef("Logger"), @InterceptorRef("defaultStack") })
	public void getApplyStateByPhone() throws Exception {
		String result = "0";
		HttpServletRequest requet = getRequest();
		HttpServletResponse response = getResponse();
		response.setContentType("text/html;charset=UTF-8");
		String phone = requet.getParameter("phone");
		DiyMatchApplyService diyMatchApplyService = (DiyMatchApplyService) getBean("diyMatchApplyService");
		DiyMatchApply diyMatchApply = diyMatchApplyService.getByPhone(phone);
		if (diyMatchApply != null) {
			result = diyMatchApply.getState();
		}
		response.getWriter().print(result);
		response.getWriter().flush();
	}

	// 是否可以报名
	@Action(value = "isApply", interceptorRefs = { @InterceptorRef("Logger"), @InterceptorRef("defaultStack") })
	public void isApply() throws Exception {
		String result = "0";// 0没有登陆，1已经报名过了，2可以
		HttpServletResponse response = getResponse();
		response.setContentType("text/html;charset=UTF-8");
		HttpSession session = getSession();
		DiyMatchLogin diyMatchLogin = (DiyMatchLogin) session.getAttribute("diyMatchLogin");
		if (diyMatchLogin != null) {
			DiyMatchApplyService diyMatchApplyService = (DiyMatchApplyService) getBean("diyMatchApplyService");
			DiyMatchApply diyMatchApply = diyMatchApplyService.getByPhone(diyMatchLogin.getPhone());
			if (diyMatchApply != null && "1".equals(diyMatchApply.getState())) {
				result = "1";
			} else {
				result = "2";
			}
		}
		response.getWriter().print(result);
		response.getWriter().flush();
	}

	// ------------------ 二期 -----------------------//
	// 进入
	@Action(value = "preliminaries", interceptorRefs = { @InterceptorRef("Logger"), @InterceptorRef("defaultStack") })
	public String preliminaries() throws Exception {
		HttpSession session = getSession();
		DiyMatchApplyService diyMatchApplyService = (DiyMatchApplyService) getBean("diyMatchApplyService");
		// 选手列表
		@SuppressWarnings("unchecked")
		List<DiyMatchApply> listApplyOld = (List<DiyMatchApply>) session.getAttribute("listApply");
		if (listApplyOld == null) {
			Long id = 0L;// list中最大的id
			int count = 12;// 页面显示条数
			String state = "1";// 1表示报名成功

			int min = 1;
			int max = diyMatchApplyService.getMaxId() == 0 ? 1 : diyMatchApplyService.getMaxId();
			Random random = new Random();
			int ran = random.nextInt(max - min + 1) + min;
			id = Long.valueOf(ran + "");

			List<DiyMatchApply> listApply = diyMatchApplyService.getSomeApplyByState(state, count, id);
			if (listApply == null) {
				listApply = new ArrayList<DiyMatchApply>();
			}
			if ((listApply == null ? 0 : listApply.size()) < count) {
				List<DiyMatchApply> listTemp = diyMatchApplyService.getSomeApplyByState(state, count - (listApply == null ? 0 : listApply.size()), 0L);
				if (listTemp == null) {
					listTemp = new ArrayList<DiyMatchApply>();
				}
				listApply.addAll(listTemp);
			}
			String ids = "DiyMatchApply 数据库ID=>";
			Logger log = Logger.getLogger("DiyMatch");
			for (DiyMatchApply object : listApply) {
				ids += object.getId() + " ";
			}
			log.info(ids);
			session.setAttribute("listApply", listApply);
		}
		// 歌曲和铃音盒列表
		@SuppressWarnings({ "unchecked" })
		List<DiyMatchColorRing> listColorRing1Old = (List<DiyMatchColorRing>) session.getAttribute("listColorRing1");
		@SuppressWarnings({ "unchecked" })
		List<DiyMatchColorRing> listColorRing2Old = (List<DiyMatchColorRing>) session.getAttribute("listColorRing2");
		if (listColorRing1Old == null && listColorRing2Old == null) {
			DiyMatchColorRingService diyMatchColorRingService = (DiyMatchColorRingService) getBean("diyMatchColorRingService");
			List<DiyMatchColorRing> listColorRing1 = diyMatchColorRingService.getByType("1");
			List<DiyMatchColorRing> listColorRing2 = diyMatchColorRingService.getByType("2");
			if (listColorRing1 != null && listColorRing2 != null) {
				session.setAttribute("listColorRing1", listColorRing1);
				session.setAttribute("listColorRing2", listColorRing2);
			}
		}
		// 选手排行列表
		int rankinkCount = 15;
		List<DiyMatchApply> listRankingApply = diyMatchApplyService.getRankingApply(rankinkCount);
		session.setAttribute("listRankingApply", listRankingApply);

		return "preliminaries";
	}

	// 获取部分apply
	@Action(value = "getSomeApply", interceptorRefs = { @InterceptorRef("Logger"), @InterceptorRef("defaultStack") })
	public void getSomeApply() throws Exception {
		HttpSession session = getSession();
		Long id = 0L;// list中最大的id
		int count = 12;// 页面显示条数
		String state = "1";// 1表示报名成功
		@SuppressWarnings("unchecked")
		List<DiyMatchApply> listApplyOld = (List<DiyMatchApply>) session.getAttribute("listApply");
		DiyMatchApplyService diyMatchApplyService = (DiyMatchApplyService) getBean("diyMatchApplyService");
		if (listApplyOld == null) {
			int min = 1;
			int max = diyMatchApplyService.getMaxId() == 0 ? 1 : diyMatchApplyService.getMaxId();
			Random random = new Random();
			int ran = random.nextInt(max - min + 1) + min;
			id = Long.valueOf(ran + "");
		} else {
			id = listApplyOld.get(listApplyOld.size() - 1).getId();
		}
		List<DiyMatchApply> listApply = diyMatchApplyService.getSomeApplyByState(state, count, id);
		if (listApply == null) {
			listApply = new ArrayList<DiyMatchApply>();
		}
		if ((listApply == null ? 0 : listApply.size()) < count) {
			listApply.addAll(diyMatchApplyService.getSomeApplyByState(state, count - (listApply == null ? 0 : listApply.size()), 0L));
		}
		// 控制台打印获取到的apply
		String ids = "DiyMatchApply 数据库ID=>";
		Logger log = Logger.getLogger("DiyMatch");
		for (DiyMatchApply object : listApply) {
			ids += object.getId() + " ";
		}
		log.info(ids);
		session.setAttribute("listApply", listApply);
	}

	// 订购铃音
	@Action(value = "subRing", interceptorRefs = { @InterceptorRef("Logger"), @InterceptorRef("defaultStack") })
	public void subRing() throws Exception {
		String result = "0";
		HttpServletRequest request = getRequest();
		HttpServletResponse response = getResponse();
		HttpSession session = getSession();
		String nowTime = DateUtil.getFormatDate("yyyy-MM-dd HH:mm:ss");// 获取当前时间

		DiyMatchTicketService diyMatchTicketService = (DiyMatchTicketService) getBean("diyMatchTicketService");
		DiyMatchGetTicketService diyMatchGetTicketService = (DiyMatchGetTicketService) getBean("diyMatchGetTicketService");

		DiyMatchLogin diyMatchLogin = (DiyMatchLogin) session.getAttribute("diyMatchLogin");// 获取当前登陆用户
		if (diyMatchLogin != null) {// else的话，没有登陆或登陆失效
			String phone = diyMatchLogin.getPhone();// 登陆用户的phone
			String id = request.getParameter("id");// 从页面获取，订购的铃音编号
			String reCode = ReadUrl.downRing(diyMatchLogin.getPhone(), id);
			Logger log = Logger.getLogger("subRing");
			log.info("订购铃音返回码：" + reCode);
			if ("0".equals(reCode)) {// 订购成功
				// 票获取记录
				DiyMatchGetTicket diyMatchGetTicket = new DiyMatchGetTicket();
				diyMatchGetTicket.setCreateTime(nowTime);
				diyMatchGetTicket.setModifyTime(nowTime);
				diyMatchGetTicket.setPhone(phone);
				diyMatchGetTicket.setScore(2);
				diyMatchGetTicket.setType("1");// 1：订购获取票
				diyMatchGetTicketService.save(diyMatchGetTicket);

				DiyMatchTicket diyMatchTicket = diyMatchTicketService.getByPhone(phone);
				if (diyMatchTicket == null) {
					diyMatchTicket = new DiyMatchTicket();
					diyMatchTicket.setCount(2);
					diyMatchTicket.setCreateTime(nowTime);
					diyMatchTicket.setModifyTime(nowTime);
					diyMatchTicket.setPhone(phone);
					diyMatchTicketService.save(diyMatchTicket);
				} else {
					diyMatchTicket.setCount(diyMatchTicket.getCount() + 2);
					diyMatchTicket.setModifyTime(nowTime);
					diyMatchTicketService.modify(diyMatchTicket);
				}
				session.setAttribute("count", diyMatchTicket.getCount());// 修改session里面count的值
				result = "1" + diyMatchTicket.getCount();
			} else if ("7174".equals(reCode) || "9019".equals(reCode)) {// 已经有这个铃音了
				result = "2";
			} else {// 订购失败
				result = "3";
			}
		}
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().print(result);
		response.getWriter().flush();
	}

	// 订购铃音盒
	@Action(value = "subRingBox", interceptorRefs = { @InterceptorRef("Logger"), @InterceptorRef("defaultStack") })
	public void subRingBox() throws Exception {
		String result = "0";
		HttpServletRequest request = getRequest();
		HttpServletResponse response = getResponse();
		HttpSession session = getSession();
		String nowTime = DateUtil.getFormatDate("yyyy-MM-dd HH:mm:ss");// 获取当前时间

		DiyMatchTicketService diyMatchTicketService = (DiyMatchTicketService) getBean("diyMatchTicketService");
		DiyMatchGetTicketService diyMatchGetTicketService = (DiyMatchGetTicketService) getBean("diyMatchGetTicketService");

		DiyMatchLogin diyMatchLogin = (DiyMatchLogin) session.getAttribute("diyMatchLogin");// 获取当前登陆用户
		if (diyMatchLogin != null) {// else的话，没有登陆或登陆失效
			String phone = diyMatchLogin.getPhone();// 登陆用户的phone
			String id = request.getParameter("id");// 从页面获取，订购的铃音编号
			String reCode = ReadUrl.downRing(diyMatchLogin.getPhone(), id);
			Logger log = Logger.getLogger("subRing");
			log.info("订购音乐盒返回码：" + reCode);
			if ("0".equals(reCode)) {// 订购成功
				// 票获取记录
				DiyMatchGetTicket diyMatchGetTicket = new DiyMatchGetTicket();
				diyMatchGetTicket.setCreateTime(nowTime);
				diyMatchGetTicket.setModifyTime(nowTime);
				diyMatchGetTicket.setPhone(phone);
				diyMatchGetTicket.setScore(5);
				diyMatchGetTicket.setType("1");// 1：订购获取票
				diyMatchGetTicketService.save(diyMatchGetTicket);

				DiyMatchTicket diyMatchTicket = diyMatchTicketService.getByPhone(phone);
				if (diyMatchTicket == null) {
					diyMatchTicket = new DiyMatchTicket();
					diyMatchTicket.setCount(5);
					diyMatchTicket.setCreateTime(nowTime);
					diyMatchTicket.setModifyTime(nowTime);
					diyMatchTicket.setPhone(phone);
					diyMatchTicketService.save(diyMatchTicket);
				} else {
					diyMatchTicket.setCount(diyMatchTicket.getCount() + 5);
					diyMatchTicket.setModifyTime(nowTime);
					diyMatchTicketService.modify(diyMatchTicket);
				}
				session.setAttribute("count", diyMatchTicket.getCount());// 修改session里面count的值
				result = "1" + diyMatchTicket.getCount();
			} else if ("7174".equals(reCode)) {// 已经有这个铃音了
				result = "2";
			} else {// 订购失败
				result = "3";
			}
		}
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().print(result);
		response.getWriter().flush();
	}

	// 试听获取票
	@Action(value = "audition", interceptorRefs = { @InterceptorRef("Logger"), @InterceptorRef("defaultStack") })
	public void audition() throws Exception {
		String result = "0";
		HttpServletResponse response = getResponse();
		HttpSession session = getSession();
		String nowTime = DateUtil.getFormatDate("yyyy-MM-dd HH:mm:ss");// 获取当前时间
		DiyMatchTicketService diyMatchTicketService = (DiyMatchTicketService) getBean("diyMatchTicketService");
		DiyMatchGetTicketService diyMatchGetTicketService = (DiyMatchGetTicketService) getBean("diyMatchGetTicketService");
		DiyMatchLogin diyMatchLogin = (DiyMatchLogin) session.getAttribute("diyMatchLogin");// 获取当前登陆用户
		if (diyMatchLogin != null) {// else的话，没有登陆或登陆失效
			String nowDay = DateUtil.getFormatDate("yyyy-MM-dd");// 获取当前时间
			// 试听type=2
			DiyMatchGetTicket diyMatchGetTicket2 = diyMatchGetTicketService.getByPhoneType(diyMatchLogin.getPhone(), "2", nowDay);
			if (diyMatchGetTicket2 == null) {
				String phone = diyMatchLogin.getPhone();// 登陆用户的phone
				// 票获取记录
				DiyMatchGetTicket diyMatchGetTicket = new DiyMatchGetTicket();
				diyMatchGetTicket.setCreateTime(nowTime);
				diyMatchGetTicket.setModifyTime(nowTime);
				diyMatchGetTicket.setPhone(phone);
				diyMatchGetTicket.setScore(1);
				diyMatchGetTicket.setType("2");// 2：试听获取票
				diyMatchGetTicketService.save(diyMatchGetTicket);

				DiyMatchTicket diyMatchTicket = diyMatchTicketService.getByPhone(phone);
				if (diyMatchTicket == null) {
					diyMatchTicket = new DiyMatchTicket();
					diyMatchTicket.setCount(1);
					diyMatchTicket.setCreateTime(nowTime);
					diyMatchTicket.setModifyTime(nowTime);
					diyMatchTicket.setPhone(phone);
					diyMatchTicketService.save(diyMatchTicket);
				} else {
					diyMatchTicket.setCount(diyMatchTicket.getCount() + 1);
					diyMatchTicket.setModifyTime(nowTime);
					diyMatchTicketService.modify(diyMatchTicket);
				}
				session.setAttribute("count", diyMatchTicket.getCount());// 修改session里面count的值
				result = "1" + diyMatchTicket.getCount();
			}
		}
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().print(result);
		response.getWriter().flush();
	}

	// 分享获取票
	@Action(value = "share", interceptorRefs = { @InterceptorRef("Logger"), @InterceptorRef("defaultStack") })
	public void share() throws Exception {
		String result = "0";
		HttpServletResponse response = getResponse();
		HttpSession session = getSession();
		String nowTime = DateUtil.getFormatDate("yyyy-MM-dd HH:mm:ss");// 获取当前时间
		DiyMatchTicketService diyMatchTicketService = (DiyMatchTicketService) getBean("diyMatchTicketService");
		DiyMatchGetTicketService diyMatchGetTicketService = (DiyMatchGetTicketService) getBean("diyMatchGetTicketService");
		DiyMatchLogin diyMatchLogin = (DiyMatchLogin) session.getAttribute("diyMatchLogin");// 获取当前登陆用户
		if (diyMatchLogin != null) {// else的话，没有登陆或登陆失效
			String nowDay = DateUtil.getFormatDate("yyyy-MM-dd");// 获取当前时间
			DiyMatchGetTicket diyMatchGetTicket3 = diyMatchGetTicketService.getByPhoneType(diyMatchLogin.getPhone(), "3", nowDay);
			if (diyMatchGetTicket3 == null) {
				String phone = diyMatchLogin.getPhone();// 登陆用户的phone
				// 票获取记录
				DiyMatchGetTicket diyMatchGetTicket = new DiyMatchGetTicket();
				diyMatchGetTicket.setCreateTime(nowTime);
				diyMatchGetTicket.setModifyTime(nowTime);
				diyMatchGetTicket.setPhone(phone);
				diyMatchGetTicket.setScore(2);
				diyMatchGetTicket.setType("3");// 3：分享获取票
				diyMatchGetTicketService.save(diyMatchGetTicket);

				DiyMatchTicket diyMatchTicket = diyMatchTicketService.getByPhone(phone);
				if (diyMatchTicket == null) {
					diyMatchTicket = new DiyMatchTicket();
					diyMatchTicket.setCount(2);
					diyMatchTicket.setCreateTime(nowTime);
					diyMatchTicket.setModifyTime(nowTime);
					diyMatchTicket.setPhone(phone);
					diyMatchTicketService.save(diyMatchTicket);
				} else {
					diyMatchTicket.setCount(diyMatchTicket.getCount() + 2);
					diyMatchTicket.setModifyTime(nowTime);
					diyMatchTicketService.modify(diyMatchTicket);
				}
				session.setAttribute("count", diyMatchTicket.getCount());// 修改session里面count的值
				result = "1" + diyMatchTicket.getCount();
			}
		}
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().print(result);
		response.getWriter().flush();
	}

	// PC端投票
	@Action(value = "vote", interceptorRefs = { @InterceptorRef("Logger"), @InterceptorRef("defaultStack") })
	public void vote() throws Exception {
		String result = "0";
		HttpServletRequest request = getRequest();
		HttpServletResponse response = getResponse();
		HttpSession session = getSession();
		String nowTime = DateUtil.getFormatDate("yyyy-MM-dd HH:mm:ss");// 获取当前时间

		DiyMatchLogin diyMatchLogin = (DiyMatchLogin) session.getAttribute("diyMatchLogin");// 获取当前登陆用户
		if (diyMatchLogin != null) {// else的话，没有登陆或登陆失效
			DiyMatchTicketService diyMatchTicketService = (DiyMatchTicketService) getBean("diyMatchTicketService");
			DiyMatchApplyService diyMatchApplyService = (DiyMatchApplyService) getBean("diyMatchApplyService");
			DiyMatchVoteService diyMatchVoteService = (DiyMatchVoteService) getBean("diyMatchVoteService");

			int count = 0;// 投票剩余次数
			DiyMatchTicket diyMatchTicket = diyMatchTicketService.getByPhone(diyMatchLogin.getPhone());
			if (diyMatchTicket != null) {
				count = diyMatchTicket.getCount();
			}
			if (count != 0) {// 没有可投的票
				diyMatchTicket.setCount(count - 1);// 用户投票，那么用户持票数当然减去一
				diyMatchTicket.setModifyTime(nowTime);
				diyMatchTicketService.modify(diyMatchTicket);

				Long id = Long.valueOf(request.getParameter("id"));// 从页面获取，投票给哪个ID的apply
				// 投票增加得分
				DiyMatchApply diyMatchApply = diyMatchApplyService.get(id);
				diyMatchApply.setScoreWeb(diyMatchApply.getScoreWeb() + 1);
				diyMatchApply.setModifyTime(nowTime);
				diyMatchApplyService.modify(diyMatchApply);

				// 记录本次投票
				DiyMatchVote diyMatchVote = new DiyMatchVote();
				diyMatchVote.setAccount(diyMatchLogin.getPhone());
				diyMatchVote.setApplyId(diyMatchApply.getId().toString());
				diyMatchVote.setCreateTime(nowTime);
				diyMatchVote.setModifyTime(nowTime);
				diyMatchVote.setType("1");// 1:是网站投票，区别于微信投票
				diyMatchVoteService.save(diyMatchVote);
				
				int ranking = diyMatchApplyService.getRanking(diyMatchLogin.getPhone()) + 1;// 我的排行
				session.setAttribute("ranking", ranking);
				result = "1" + diyMatchTicket.getCount()+ "," + ranking;
				
				if (id.equals(diyMatchApplyService.getByPhone(diyMatchLogin.getPhone()).getId())) {// 给自己投票了
					session.setAttribute("score", (Long) session.getAttribute("score") + 1);
					result = "2" + diyMatchTicket.getCount() + "," + ranking;
				}
				@SuppressWarnings("unchecked")
				List<DiyMatchApply> listApply = (List<DiyMatchApply>) session.getAttribute("listApply");
				if (listApply != null) {
					for (int i = 0; i < listApply.size(); i++) {
						if (id.equals(listApply.get(i).getId())) {
							listApply.get(i).setScoreWeb(diyMatchApply.getScoreWeb());
							break;
						}
					}
				}
				
				
				
				// 选手排行列表
				int rankinkCount = 15;
				List<DiyMatchApply> listRankingApply = diyMatchApplyService.getRankingApply(rankinkCount);
				session.setAttribute("listRankingApply", listRankingApply);
				
				session.setAttribute("listApply", listApply);
				session.setAttribute("count", count - 1);
			}
		}
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().print(result);
		response.getWriter().flush();
	}

	// WX端获取作品列表
	@Action(value = "getSomeApplyFromWX", interceptorRefs = { @InterceptorRef("Logger"), @InterceptorRef("defaultStack") })
	public void getSomeApplyFromWX() throws Exception {
		// 选手列表
		HttpServletResponse response = getResponse();
		DiyMatchApplyService diyMatchApplyService = (DiyMatchApplyService) getBean("diyMatchApplyService");
		Long id = 0L;// list中最大的id
		int count = 5;// 页面显示条数
		String state = "1";// 1表示报名成功

		int min = 1;
		int max = diyMatchApplyService.getMaxId() == 0 ? 1 : diyMatchApplyService.getMaxId();
		Random random = new Random();
		int ran = random.nextInt(max - min + 1) + min;
		id = Long.valueOf(ran + "");

		List<DiyMatchApply> listApply = diyMatchApplyService.getSomeApplyByState(state, count, id);
		if (listApply == null) {
			listApply = new ArrayList<DiyMatchApply>();
		}
		if ((listApply == null ? 0 : listApply.size()) < count) {
			List<DiyMatchApply> listTemp = diyMatchApplyService.getSomeApplyByState(state, count - (listApply == null ? 0 : listApply.size()), 0L);
			if (listTemp == null) {
				listTemp = new ArrayList<DiyMatchApply>();
			}
			listApply.addAll(listTemp);
		}
		
		String ids = "DiyMatchApply 数据库ID=>";
		Logger log = Logger.getLogger("DiyMatch");
		for (DiyMatchApply object : listApply) {
			ids += object.getId() + " ";
		}
		log.info(ids);
		
		JSONArray jsonArr = JSONArray.fromObject(listApply);
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().print(jsonArr);
		response.getWriter().flush();
	}
}
