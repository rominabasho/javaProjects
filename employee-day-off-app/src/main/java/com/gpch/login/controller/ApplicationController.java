package com.gpch.login.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.gpch.login.model.Application;
import com.gpch.login.model.User;
import com.gpch.login.service.ApplicationService;
import com.gpch.login.service.UserService;


@RestController
public class ApplicationController {

	@Autowired
	@Qualifier("applicationService")
	private ApplicationService applicationService;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/approveApplication", method = RequestMethod.GET)
	public ModelAndView  approveApplication(@RequestParam(name="applicationId")String applicationId) {
		int appId = Integer.parseInt(applicationId);
		applicationService.approveApplication(appId);
		return new ModelAndView("redirect:/allapplications");
	}

	@RequestMapping(value = "/removeApplication", method = RequestMethod.GET)
	public ModelAndView removeApplication(@RequestParam(name="applicationId")String applicationId) {
		int appId = Integer.parseInt(applicationId);
		applicationService.removeApplication(appId);
		return new ModelAndView("redirect:/userapplications");
	}

	@GetMapping("/allapplications")
	public Model showallApplications(Model model) {
		model.addAttribute("applications", applicationService.getAllApplications());
		return model;
	}

	@GetMapping("/userapplications")
	public Model showUserApplications(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		int userId = user.getId();
		model.addAttribute("applications", applicationService.getUserApplications(userId));
		return model;
	}

	@RequestMapping(value="/createapplication", method = RequestMethod.GET)
	public ModelAndView createapplication(){
		ModelAndView modelAndView = new ModelAndView();
		Application application = new Application();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());// Get currently logged in user
		application.setUser(user);  
		modelAndView.addObject("application", application);
		modelAndView.setViewName("createapplication");
		return modelAndView;
	}

	@RequestMapping(value = "/createapplication", method = RequestMethod.POST)
	public ModelAndView createNewApplication(@Valid Application application, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();


		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());// Get currently logged in user
		application.setUser(user); // set the user
		modelAndView.addObject("application", application);
		modelAndView.setViewName("createapplication");

		int takenDays = calcTakenDays(application.getFromDate(), application.getToDate());
		int remainingDays = 20-takenDays;
		application.setNoOfTakenDays(takenDays);
		application.setNoOfLeftDays(remainingDays);

		applicationService.addApplication(application);
		modelAndView.addObject("successMessage", "Application has been sent successfully");

		return new ModelAndView("redirect:/userapplications");
	}

	@RequestMapping(value = "/updateapplication", method = RequestMethod.GET)
	public ModelAndView updateApplication(@Valid @RequestParam(name="applicationId",required = false)String applicationId, Application application, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();

		if(applicationId != null)
		{	
		int appId = Integer.parseInt(applicationId);
	    application = applicationService.getApplicationById(appId);	
		}
		modelAndView.addObject("application", application);
		modelAndView.setViewName("updateapplication");

		return modelAndView;
	}
	
	@RequestMapping(value = "/updateapplication", method = RequestMethod.POST)
	public ModelAndView updateExistingApplication(@Valid Application application, BindingResult bindingResult) {
		
		ModelAndView modelAndView = new ModelAndView();
		int takenDays = calcTakenDays(application.getFromDate(), application.getToDate());
		int remainingDays = 20-takenDays;
		application.setNoOfTakenDays(takenDays);
		application.setNoOfLeftDays(remainingDays);
		
		modelAndView.addObject("application", application);
		modelAndView.setViewName("updateapplication");
			
		applicationService.modifyApplication(application);
		modelAndView.addObject("successMessage", "Application has been updated successfully");

		return new ModelAndView("redirect:/userapplications");
	}

	@InitBinder
	public void initDateBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
	}

	public static int calcTakenDays(Date startDate, Date endDate) {
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);        

		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);

		int workDays = 0;

		//Return 0 if start and end are the same
		if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
			return 0;
		}

		if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
			startCal.setTime(endDate);
			endCal.setTime(startDate);
		}
		startCal.add(Calendar.DAY_OF_MONTH,1);

		do {
			//excluding start date
			startCal.add(Calendar.DAY_OF_MONTH, 1);
			if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
				++workDays;
			}
		} while (startCal.getTimeInMillis() < endCal.getTimeInMillis()); //excluding end date

		return workDays;
	}

}

