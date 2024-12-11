package veniamin.tasksapp.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String details;

    @Column
    private LocalDate date;

    @Column
    private Boolean isComplete;

    @Column
    private String priority;

    @Column
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performer_id", referencedColumnName = "id")
    private User performer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private User creator;

//    @PrePersist
//    public void prePersist() {
//        this.date = LocalDate.now().plusDays(1);
//        this.status = Boolean.FALSE;
//    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Task)) {
            return false;
        }

        Task otherTask = (Task) obj;
        return this.getId().equals(otherTask.getId());
    }
}
