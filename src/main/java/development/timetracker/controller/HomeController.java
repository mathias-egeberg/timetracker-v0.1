package development.timetracker.controller;


import development.timetracker.entities.TimeEntry;
import development.timetracker.entities.User;
import development.timetracker.repositories.EntryRepository;
import development.timetracker.services.TimeEntityService;
import development.timetracker.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import development.timetracker.services.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private EntryRepository entryRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private TimeEntityService timeEntityService;




    @GetMapping
    public String homePage(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        int userId = userDetails.getId();
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();
        int day = LocalDate.now().getDayOfMonth();

        double yearlySummary = timeEntityService.getTotalHoursByYear(userId,year);
        double monthlySummary = timeEntityService.getTotalHoursByMonth(userId, year, month);
        double dailySummary = timeEntityService.getTotalHoursByDay(userId, year, month, day);
        double overtimeYearly = timeEntityService.findTotalOvertimeHoursByYear(userId, year);
        double overtimeMonthly = timeEntityService.findTotalOvertimeHoursByMonth(userId, year, month);
        double overtimeDaily = timeEntityService.findTotalOvertimeHoursByDay(userId, year, month, day);

        // Pass the username to the view
        model.addAttribute("username", userDetails.getFirstname());
        model.addAttribute("role", userDetails.getAuthorities());
        model.addAttribute("yearlySummary", yearlySummary);
        model.addAttribute("monthlySummary", monthlySummary);
        model.addAttribute("dailySummary", dailySummary);
        model.addAttribute("overtimeYearly", overtimeYearly);
        model.addAttribute("overtimeMonthly", overtimeMonthly);
        model.addAttribute("overtimeDaily", overtimeDaily);
        model.addAttribute("userId", userDetails.getId());
        return "home";
    }


    @GetMapping("/new-entry")
    public String newEntryPage(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        model.addAttribute("userId", userDetails.getId());
        model.addAttribute("username", userDetails.getFirstname());

        return "new-entry";
    }

    @PostMapping("/save")
    public String saveEntry(@ModelAttribute("timeEntry") TimeEntry timeEntry){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        timeEntry.setUserId(userDetails.getId()); // Set the userId from the authenticated user

        try {
            // Set the createdAt and updatedAt timestamps
            LocalDateTime now = LocalDateTime.now();
            timeEntry.setCreatedAt(now);
            timeEntry.setUpdatedAt(now);

            // Save the time entry
            entryRepo.save(timeEntry);
            return "redirect:/home"; // Redirect to home page
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/home/new-entry"; // Redirect back to form page
        }
    }

    @GetMapping("/new-user")
    public String newUserPage(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        model.addAttribute("username", userDetails.getFirstname());
        model.addAttribute("role", userDetails.getAuthorities());
        model.addAttribute("userId", userDetails.getId());
        return "new-user";
    }


    @PostMapping("/saveuser")
    public String registerUser(@ModelAttribute("user")User user){
        userService.saveUser(user);
        return "redirect:/home";
    }

    @GetMapping("/settings")
    public String settingsPage(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        model.addAttribute("username", userDetails.getFirstname());
        model.addAttribute("role", userDetails.getAuthorities());
        model.addAttribute("userId", userDetails.getId());
        return "settings";
    }


    @GetMapping("/time-entries/week")
    public List<TimeEntry> getTimeEntriesForWeek(
            @RequestParam int userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {

        LocalDate endDate = startDate.plusDays(6);
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        return entryRepo.findByUserIdAndStartTimeBetween(userId, startDateTime, endDateTime);
    }

}
