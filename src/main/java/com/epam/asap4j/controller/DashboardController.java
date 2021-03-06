package com.epam.asap4j.controller;

import com.epam.asap4j.dao.EventDao;
import com.epam.asap4j.dao.FeatureDao;
import com.epam.asap4j.dao.GroupDao;
import com.epam.asap4j.dao.UserDao;
import com.epam.asap4j.dto.Event;
import com.epam.asap4j.dto.Feature;
import com.epam.asap4j.dto.Person;
import com.epam.asap4j.dto.User;
import com.epam.asap4j.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/")
public class DashboardController {

    @Autowired
    private EventDao eventDao;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private FeatureDao featureDao;

    @Autowired
    private UserService userService;

    private static final Person DEFAULT_USER = new Person("4060741400006438769", "Aliaksei Zhynhiarouski");

    @RequestMapping(method = RequestMethod.GET)
    public String index(HttpSession session) {
        if (session.getAttribute("userBean") == null) {
            session.setAttribute("userBean", userService.loginUser(DEFAULT_USER.getPersonName()));
        }
        return "redirect:dashboard";
    }

    @RequestMapping(value = "dashboard", method = RequestMethod.GET)
    public String getDashboard(HttpSession session, ModelMap model) {
        List<Feature> features = featureDao.getEntitiesList();
        for (Feature feature : features) {
            model.addAttribute(feature.getFeatureName(), eventDao.getPersonEventsByFeature(getCurrentUser(session), feature));
        }

        return "dashboard/dashboard-main";
    }

    @RequestMapping(value = "accident", method = RequestMethod.GET)
    public String submitAccident( ModelMap model) {
        model.addAttribute("event", new Event());
        return "events/event-accident-main";
    }

    @RequestMapping(value = "wish", method = RequestMethod.GET)
    public String submitWish(HttpSession session, ModelMap model) {
        model.addAttribute("groupList", groupDao.getPersonGroups(getCurrentUser(session).getPersonId()));
        model.addAttribute("event", new Event());
        return "events/event-wishlist-main";
    }

    @RequestMapping(value = "groups", method = RequestMethod.GET)
    public String getGroups() {
        return "groups/groups-list-main";
    }

    @RequestMapping(value = "group", method = RequestMethod.GET)
    public String getGroup() {
        return "groups/groups-main";
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String loginPage(ModelMap model) {
        model.addAttribute("user", new User());
        return "login-main";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String login(HttpSession session, User user) {
        if (session.getAttribute("userBean") == null) {
            session.setAttribute("userBean", userService.loginUser(user.getPerson().getPersonName()));
        }
        return "redirect:dashboard";
    }

    private Person getCurrentUser(HttpSession session) {
        User user = (User) session.getAttribute("userBean");
        return user != null ? user.getPerson() : DEFAULT_USER;
    }
}
