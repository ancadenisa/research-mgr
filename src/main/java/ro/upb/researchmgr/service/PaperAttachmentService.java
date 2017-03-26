package ro.upb.researchmgr.service;

import java.io.InputStream;

import ro.upb.researchmgr.domain.PaperAttachment;

public interface PaperAttachmentService {

	void delete(Long id);

	InputStream getFileToDownloadAsInputstream(Long id);

	PaperAttachment findOne(Long id);

}
