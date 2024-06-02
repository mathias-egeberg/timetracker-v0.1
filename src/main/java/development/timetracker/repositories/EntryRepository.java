package development.timetracker.repositories;

import development.timetracker.entities.TimeEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface EntryRepository extends JpaRepository<TimeEntry, Integer> {

    @Query("SELECT COALESCE(SUM(te.duration), 0) FROM TimeEntry te WHERE te.userId = :userId AND YEAR(te.startTime) = :year")
    Double findTotalHoursByYear(@Param("userId") int userId, @Param("year") int year);

    @Query("SELECT COALESCE(SUM(te.duration), 0) FROM TimeEntry te WHERE te.userId = :userId AND YEAR(te.startTime) = :year AND MONTH(te.startTime) = :month")
    Double findTotalHoursByMonth(@Param("userId") int userId, @Param("year") int year, @Param("month") int month);

    @Query("SELECT COALESCE(SUM(te.duration), 0) FROM TimeEntry te WHERE te.userId = :userId AND YEAR(te.startTime) = :year AND MONTH(te.startTime) = :month AND DAY(te.startTime) = :day")
    Double findTotalHoursByDay(@Param("userId") int userId, @Param("year") int year, @Param("month") int month, @Param("day") int day);
}

