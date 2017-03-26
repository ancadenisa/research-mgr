package ro.upb.researchmgr.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import io.undertow.servlet.spec.ServletContextImpl;
import ro.upb.researchmgr.config.Constants;
import ro.upb.researchmgr.domain.PaperAttachment;
import ro.upb.researchmgr.domain.PaperWork;
import ro.upb.researchmgr.repository.PaperAttachmentRepository;
import ro.upb.researchmgr.service.PaperAttachmentService;

/**
 * Service Implementation for managing PaperWork.
 */
@Service
@Transactional
public class PaperAttachmentServiceImpl implements PaperAttachmentService{
	 private final Logger log = LoggerFactory.getLogger(PaperAttachmentServiceImpl.class);
	 
    private final PaperAttachmentRepository paperAttachmentRepository;
    
    
    private final ServletContextImpl servletContextImpl;

    public PaperAttachmentServiceImpl(PaperAttachmentRepository paperAttachmentRepository, ServletContextImpl servletContextImpl) {
        this.paperAttachmentRepository = paperAttachmentRepository;
        this.servletContextImpl = servletContextImpl;
    }
    
    @Override
    public InputStream getFileToDownloadAsInputstream(Long id) {
    	PaperAttachment paperAttachment = paperAttachmentRepository.findOne(id);
    	InputStream is;

    	try {
    	    is = new FileInputStream(servletContextImpl.getRealPath(paperAttachment.getPath()));
    	} catch (FileNotFoundException e) {
    		throw new RuntimeException(e);
    	}

    	return is;
    }

	@Override
	public void delete(Long id) {
		paperAttachmentRepository.delete(id);
	}

	@Override
	public PaperAttachment findOne(Long id) {
		return paperAttachmentRepository.findOne(id);
	}
	
}
	
