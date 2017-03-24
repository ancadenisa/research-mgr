package ro.upb.researchmgr.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A PaperWork.
 */
@Entity
@Table(name = "paper_work")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PaperWork implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "subject", length = 100, nullable = false)
    private String subject;

    @Column(name = "description")
    private String description;

    @Column(name = "deadline_date")
    private LocalDate deadlineDate;

    @ManyToOne(optional = false)
    @NotNull
    private User coordinator;

    @ManyToOne(optional = false)
    @NotNull
    private User assignee;
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public PaperWork subject(String subject) {
        this.subject = subject;
        return this;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public PaperWork description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDeadlineDate() {
        return deadlineDate;
    }

    public PaperWork deadlineDate(LocalDate deadlineDate) {
        this.deadlineDate = deadlineDate;
        return this;
    }

    public void setDeadlineDate(LocalDate deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public User getCoordinator() {
        return coordinator;
    }

    public PaperWork coordinator(User user) {
        this.coordinator = user;
        return this;
    }

    public void setCoordinator(User user) {
        this.coordinator = user;
    }

    public User getAssignee() {
        return assignee;
    }

    public PaperWork assignee(User user) {
        this.assignee = user;
        return this;
    }

    public void setAssignee(User user) {
        this.assignee = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PaperWork paperWork = (PaperWork) o;
        if (paperWork.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, paperWork.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PaperWork{" +
            "id=" + id +
            ", subject='" + subject + "'" +
            ", description='" + description + "'" +
            ", deadlineDate='" + deadlineDate + "'" +
            '}';
    }
}
