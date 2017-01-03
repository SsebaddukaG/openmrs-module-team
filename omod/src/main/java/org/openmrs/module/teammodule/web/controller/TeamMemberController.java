/**
 * 
 */
package org.openmrs.module.teammodule.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openmrs.api.context.Context;
import org.openmrs.module.teammodule.Team;
import org.openmrs.module.teammodule.TeamLead;
import org.openmrs.module.teammodule.TeamMember;
import org.openmrs.module.teammodule.api.TeamLeadService;
import org.openmrs.module.teammodule.api.TeamMemberService;
import org.openmrs.module.teammodule.api.TeamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.RequestParam;
//import java.util.Date;
//import org.openmrs.PersonName;
//import org.openmrs.module.teammodule.Team;
//import org.openmrs.module.teammodule.TeamLead;
//import org.openmrs.module.teammodule.api.TeamLeadService;
//import org.openmrs.module.teammodule.api.TeamService;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

/**
 * @author Muhammad Safwan
 * 
 */
@Controller
@RequestMapping(value = "module/teammodule/teamMember/")
public class TeamMemberController {
	/** Logger for this class and subclasses */
	protected final Log log = LogFactory.getLog(getClass());

	/** Success form views name */
	private final String SHOW = "/module/teammodule/teamMember";

	/**
	 * Initially called after the formBackingObject method to get the landing
	 * form name
	 * 
	 * @return String form view name
	 */
	@RequestMapping(method = RequestMethod.GET, value = "list.form")
	public String showForm(Model model, HttpServletRequest request) {
		List<TeamMember> teamMember;
		// Person person;
		// List<Person> personList = new ArrayList<Person>();
		String teamId = request.getParameter("teamId");
		Team team = Context.getService(TeamService.class).getTeam(Integer.parseInt(teamId));
		String memberName = request.getParameter("member");
		String changeLead = request.getParameter("changeLead");
		System.out.println(memberName);
		String caption = "";
		// int teamIden = Integer.parseInt(teamId);
		List<String> genderList = new ArrayList<String>();
		List<String> joinDate = new ArrayList<String>();
		TeamLead teamLead = Context.getService(TeamLeadService.class).getTeamLead(team);

		if (Context.isAuthenticated()) {
			if (memberName == null) {
				teamMember = Context.getService(TeamMemberService.class).getTeamMembers(team, null, null, null);
			} else {
				teamMember = Context.getService(TeamMemberService.class).searchMemberByTeam(memberName, Integer.parseInt(teamId));
			}
			for (int i = 0; i < teamMember.size(); i++) {
				/*
				 * person =
				 * Context.getPersonService().getPerson(teamMember.get(i
				 * ).getPersonId()); personList.add(person);
				 */
				if (teamMember.get(i).getJoinDate() != null) {
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					String date = sdf.format(teamMember.get(i).getJoinDate());
					joinDate.add(date);
				} else {
					joinDate.add("");
				}

				/*
				 * date = teamMember.get(i).getLeaveDate(); leaveDate.add(date);
				 */
			}
			if(changeLead == null){
				caption = team.getTeamName() + " Members";
				model.addAttribute("edit", caption);			
			}else{
				caption = "Change Team Lead";
				model.addAttribute("edit",caption);
			}
			model.addAttribute("teamMember", teamMember);
			// model.addAttribute("name", personList);
			model.addAttribute("team", team);
			// model.addAttribute("teamIden", teamIden);
			model.addAttribute("gender", genderList);
			model.addAttribute("join", joinDate);
			model.addAttribute("teamLead", teamLead);
			model.addAttribute("member", memberName);
			// model.addAttribute("leave", leaveDate);
		}

		return SHOW;
	}
	
	@RequestMapping(value = "user.form", method = RequestMethod.GET)
	public String getUser(
			@RequestParam(required = false, value = "userName") String userName, ModelMap model) {
		
		// the formBackingObject method above sets up user, depending on userId and personId parameters   
		
		model.addAttribute("isNewUser", isNewUser(user));
		if (isNewUser(user) || Context.hasPrivilege(PrivilegeConstants.EDIT_USER_PASSWORDS)) {
			model.addAttribute("modifyPasswords", true);
		}
		
		if (createNewPerson != null) {
			model.addAttribute("createNewPerson", createNewPerson);
		}
		
		if (!isNewUser(user)) {
			model.addAttribute("changePassword", new UserProperties(user.getUserProperties()).isSupposedToChangePassword());
			
			model.addAttribute("secretQuestion", userService.getSecretQuestion(user));
		}
		
		if (user.getPerson().getId() != null && !Context.getProviderService().getProvidersByPerson(user.getPerson()).isEmpty()) {
			model.addAttribute("isProvider", true);
			model.addAttribute("providerList", Context.getProviderService().getProvidersByPerson(user.getPerson()));
		} else {
			model.addAttribute("isProvider", false);
		}
		
		// not using the default view name because I'm converting from an existing form
		return "module/legacyui/admin/users/userForm";
	}
	
	@RequestMapping(value = "userRoleList.form", method = RequestMethod.GET)
	public String Role() {
		
	}
	

	@RequestMapping(method = RequestMethod.GET, value = "listPopup.form")
	@ResponseBody
	public ArrayList showFormPopup(Model model, HttpServletRequest request) throws JSONException {
		List<TeamMember> teamMember;
		// Person person;
		// List<Person> personList = new ArrayList<Person>();
		String teamId = request.getParameter("teamId");
		Team team = Context.getService(TeamService.class).getTeam(Integer.parseInt(teamId));
		String memberName = request.getParameter("member");
		String changeLead = request.getParameter("changeLead");
		System.out.println(memberName);
		String caption = "";
		// int teamIden = Integer.parseInt(teamId);
		List<String> genderList = new ArrayList<String>();
		List<String> joinDate = new ArrayList<String>();
		TeamLead teamLead = Context.getService(TeamLeadService.class).getTeamLead(team);
		ArrayList arr = new ArrayList();
		Map<String, String>m=new HashedMap();
		
		if (Context.isAuthenticated()) {
			if (memberName == null) {
				teamMember = Context.getService(TeamMemberService.class).getTeamMembers(team, null, null, null);
			} else {
				teamMember = Context.getService(TeamMemberService.class).searchMemberByTeam(memberName, Integer.parseInt(teamId));
			}
			for (int i = 0; i < teamMember.size(); i++) {
				if (teamMember.get(i).getJoinDate() != null) {
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					String date = sdf.format(teamMember.get(i).getJoinDate());
					joinDate.add(date);
				} else {
					joinDate.add("");
				}
			}
			if(changeLead == null){
				caption = team.getTeamName() + " Members";
				m.put("edit", caption);			
			}else{
				caption = "Change Team Lead";
				m.put("edit",caption);
			}
			
			for(int i=0;i<teamMember.size();i++)
			{	m=new HashedMap();
				m.put("teamMemberId", String.valueOf(teamMember.get(i).getTeamMemberId()));
				m.put("personName", teamMember.get(i).getPerson().getGivenName() + teamMember.get(i).getPerson().getFamilyName());
				m.put("join", joinDate.get(i));
				m.put("gender", String.valueOf(genderList));
				m.put("teamId",teamId);
				arr.add(m);
			}
		}
		
		return arr;
	}
	/**
	 * All the parameters are optional based on the necessity
	 * 
	 * @param httpSession
	 * @param anyRequestObject
	 * @param errors
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "list.form")
	public String onSubmit(HttpSession httpSession, @ModelAttribute("anyRequestObject") Object anyRequestObject, BindingResult errors,HttpServletRequest request,@ModelAttribute("searchMember") TeamMember searchMember) {

		String teamId = request.getParameter("teamId");
		String member = request.getParameter("memberName");
		System.out.println(member);
		if (errors.hasErrors()) {
			// return error view
		}

		return "redirect:/module/teammodule/teamMember/list.form?teamId="+teamId+"&member="+member;
	}

	@RequestMapping(method = RequestMethod.GET, value = "changeTeamLead.form")
	public String showFormEdit(Model model, HttpServletRequest request) {
		String teamMemberId = request.getParameter("teamMemberId");
		String teamId = request.getParameter("teamId");
		String teamLeadId = request.getParameter("teamLeadId");

		List<String> joinDate = new ArrayList<String>();
		// Person person;
		// List<Person> personList = new ArrayList<Person>();
		TeamMember tm = null;
		Team team = Context.getService(TeamService.class).getTeam(Integer.parseInt(teamId));
		TeamLead teamLead = Context.getService(TeamLeadService.class).getTeamLead(team);
		//TeamMember tm = Context.getService(TeamMemberService.class).getMember(Integer.parseInt(teamMemberId));
		if (teamLead != null) {
			teamLead.setVoided(true);
			teamLead.setVoidReason("Team Lead Changed");
			teamLead.setLeaveDate(new Date());
			teamLead.setDateVoided(new Date());
			Context.getService(TeamLeadService.class).update(teamLead);
			tm = Context.getService(TeamMemberService.class).getMember(Integer.parseInt(teamLeadId));
			tm.setIsTeamLead(false);
			Context.getService(TeamMemberService.class).update(tm);
		}
		tm = Context.getService(TeamMemberService.class).getMember(Integer.parseInt(teamMemberId));
		teamLead = new TeamLead();
		teamLead.setTeam(team);
		teamLead.setJoinDate(new Date());
		teamLead.setTeamMember(tm);
		if(teamLead.getUuid() == null){
			teamLead.setUuid(UUID.randomUUID().toString());
		}	
		Context.getService(TeamLeadService.class).save(teamLead);
		teamLead = Context.getService(TeamLeadService.class).getTeamLead(team);	
		tm.setIsTeamLead(true);
		Context.getService(TeamMemberService.class).update(tm);
		List<TeamMember> teamMember = Context.getService(TeamMemberService.class).getTeamMembers(team, null, null, false);

		for (int i = 0; i < teamMember.size(); i++) {
			// person =
			// Context.getPersonService().getPerson(teamMember.get(i).getPersonId());
			// personList.add(person);
			if (teamMember.get(i).getJoinDate() != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String date = sdf.format(teamMember.get(i).getJoinDate());
				joinDate.add(date);
			} else {
				joinDate.add("");
			}
		}
		model.addAttribute("teamMember", teamMember);
		// model.addAttribute("name", personList);
		model.addAttribute("join", joinDate);
		model.addAttribute("teamLead", teamLead);
		model.addAttribute("team", team);
		return SHOW;
	}

	@RequestMapping(method = RequestMethod.POST, value = "changeTeamLead.form")
	public String onSubmitEdit(HttpSession httpSession, @ModelAttribute("anyRequestObject") Object anyRequestObject, BindingResult errors) {

		if (errors.hasErrors()) {
			// return error view
		}
		return SHOW;
	}

}
