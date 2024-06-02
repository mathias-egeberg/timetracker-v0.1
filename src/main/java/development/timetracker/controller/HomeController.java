package development.timetracker.controller;


import development.timetracker.entities.TimeEntry;
import development.timetracker.entities.User;
import development.timetracker.repositories.EntryRepository;
import development.timetracker.repositories.UserRepository;
import development.timetracker.services.TimeEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.ui.Model;
import development.timetracker.services.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private EntryRepository entryRepo;

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


        // Pass the username to the view
        model.addAttribute("username", userDetails.getFirstname());
        model.addAttribute("role", userDetails.getAuthorities());
        model.addAttribute("yearlySummary", yearlySummary);
        model.addAttribute("monthlySummary", monthlySummary);
        model.addAttribute("dailySummary", dailySummary);
        return "home";
    }


    @GetMapping("/new-entry")
    public String newEntryPage(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        model.addAttribute("id", userDetails.getId());

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


}
