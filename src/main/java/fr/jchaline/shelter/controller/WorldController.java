package fr.jchaline.shelter.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import fr.jchaline.shelter.domain.MapCell;
import fr.jchaline.shelter.domain.World;
import fr.jchaline.shelter.service.WorldService;

@RestController
@RequestMapping(value = "/world", method = RequestMethod.GET)
public class WorldController {

	private static final Logger LOGGER = LoggerFactory.getLogger(WorldController.class);

	@Autowired
	private WorldService service;

	@RequestMapping("/get")
	public World get(@AuthenticationPrincipal User activeUser) {
		return service.get(activeUser.getUsername());
	}

	@RequestMapping("/cell/{id}")
	public MapCell cell(@PathVariable long cellId) {
		return service.findCell(cellId);
	}

	@ExceptionHandler(Exception.class)
	public ModelAndView handleError(HttpServletRequest req, Exception exception) {
		LOGGER.error("Request: " + req.getRequestURL() + " raised " + exception);

		ModelAndView mav = new ModelAndView();
		mav.addObject("exception", exception);
		mav.addObject("url", req.getRequestURL());
		mav.setViewName("error");
		return mav;
	}
}
