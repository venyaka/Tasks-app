package veniamin.tasksapp.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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
    private boolean status;

    @OneToMany
    @JoinColumn(name = "performer_id", referencedColumnName = "id")
    private Set<User> performers = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private User creator;

    @PrePersist
    public void prePersist() {
        this.date = LocalDate.now().plusDays(1);
        this.status = Boolean.FALSE;
    }

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
