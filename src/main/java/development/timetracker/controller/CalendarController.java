package development.timetracker.controller;


import development.timetracker.entities.TimeEntry;
import development.timetracker.repositories.EntryRepository;
import development.timetracker.services.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/home/calendar")
public class CalendarController {

    private static final Logger logger = LoggerFactory.getLogger(CalendarController.class);

    @Autowired
    private EntryRepository timeEntryRepository;


    @GetMapping
    public String calendar(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        model.addAttribute("username", userDetails.getFirstname());
        model.addAttribute("role", userDetails.getAuthorities());
        model.addAttribute("userId", userDetails.getId());
        return "calendar";
    }

    @GetMapping("/calendar-entries")
    public ResponseEntity<List<LocalDate>> getCalendarEntries(@RequestParam int userId) {
        try {
            List<TimeEntry> entries = timeEntryRepository.findByUserId(userId);
            List<LocalDate> entryDates = entries.stream()
                    .map(entry -> {
                        LocalDateTime startTime = entry.getStartTime();
                        return startTime != null ? startTime.toLocalDate() : null;
                    })
                    .filter(Objects::nonNull) // Filter out null entries
                    .distinct()
                    .collect(Collectors.toList());
            return ResponseEntity.ok(entryDates);
        } catch (Exception e) {
            logger.error("Error fetching calendar entries for userId: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
