package development.timetracker.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.springframework.cglib.core.Local;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "time_entries")
public class TimeEntry implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "time_entry_id")
    private int id;


    @Column(name = "user_id")
    private int userId;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "duration")
    private double duration;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;



    public TimeEntry(int time_entry_id, int user_id, LocalDateTime start_time, LocalDateTime end_time, double duration, LocalDateTime created_at, LocalDateTime updated_at) {
        this.id = time_entry_id;
        this.userId = user_id;
        this.startTime = start_time;
        this.endTime = end_time;
        this.duration = duration;
        this.createdAt = created_at;
        this.updatedAt = updated_at;
    }

    public TimeEntry() {
    }


}
