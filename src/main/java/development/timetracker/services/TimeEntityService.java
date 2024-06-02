package development.timetracker.services;

import development.timetracker.repositories.EntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeEntityService {

    @Autowired
    private EntryRepository entryRepository;

    public Double getTotalHoursByYear(int userId, int year) {
        return entryRepository.findTotalHoursByYear(userId, year);
    }

    public Double getTotalHoursByMonth(int userId, int year, int month) {
        return entryRepository.findTotalHoursByMonth(userId, year, month);
    }

    public Double getTotalHoursByDay(int userId, int year, int month, int day) {
        return entryRepository.findTotalHoursByDay(userId, year, month, day);
    }


}
