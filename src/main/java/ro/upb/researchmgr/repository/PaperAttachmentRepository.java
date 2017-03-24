package ro.upb.researchmgr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ro.upb.researchmgr.domain.PaperAttachment;

public interface PaperAttachmentRepository extends JpaRepository<PaperAttachment,Long> {

}
