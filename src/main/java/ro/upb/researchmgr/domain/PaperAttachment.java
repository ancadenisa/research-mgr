package ro.upb.researchmgr.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * A PaperWork.
 */
@Entity
@Table(name = "paper_attachments")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PaperAttachment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "date")
    private Date date;

    @ManyToOne(optional = false)
    @NotNull
    @JsonBackReference
    private PaperWork paperWork;

    public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}


	public PaperWork getPaperWork() {
		return paperWork;
	}


	public void setPaperWork(PaperWork paperwork) {
		this.paperWork = paperwork;
	}

	@Override
    public String toString() {
        return "PaperWork{" +
            "id=" + id +
            ", path='" + path + "'" +
            ", date='" + date + "'" +
            '}';
    }
}
