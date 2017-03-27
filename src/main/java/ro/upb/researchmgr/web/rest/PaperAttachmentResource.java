package ro.upb.researchmgr.web.rest;

import com.codahale.metrics.annotation.Timed;

import ro.upb.researchmgr.config.Constants;
import ro.upb.researchmgr.domain.PaperAttachment;
import ro.upb.researchmgr.domain.PaperWork;
import ro.upb.researchmgr.domain.User;
import ro.upb.researchmgr.security.AuthoritiesConstants;
import ro.upb.researchmgr.security.SecurityUtils;
import ro.upb.researchmgr.service.PaperAttachmentService;
import ro.upb.researchmgr.service.PaperWorkService;
import ro.upb.researchmgr.service.UserService;
import ro.upb.researchmgr.web.rest.util.HeaderUtil;
import ro.upb.researchmgr.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.undertow.servlet.spec.ServletContextImpl;
import io.github.jhipster.web.util.ResponseUtil;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing PaperWork.
 */
@RestController
@RequestMapping("/api")
public class PaperAttachmentResource {

    private final Logger log = LoggerFactory.getLogger(PaperAttachmentResource.class);

    private static final String ENTITY_NAME = "paperAttachment";
        
    private final PaperAttachmentService paperAttachmentService;
    
    private final ServletContextImpl servletContextImpl;
    

    public PaperAttachmentResource(PaperAttachmentService paperAttachmentService,  ServletContextImpl servletContextImpl) {
        this.paperAttachmentService = paperAttachmentService;
        this.servletContextImpl = servletContextImpl;
    }

    /**
     * DELETE  /paper-attachments/:id : delete the "id" paperAttachment.
     *
     * @param id the id of the paperAttachments to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/paperAttachments/{id}")
    @Timed
    public ResponseEntity<Void> deletePaperAttachment(@PathVariable Long id) {
        log.debug("REST request to delete PaperAttachment : {}", id);
        paperAttachmentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    

    @ResponseBody
    @RequestMapping(value = "/paperAttachments/download/{id}", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
    @Timed
    public void downloadPaperWorkAttachments(@PathVariable Long id, HttpServletResponse response) throws URISyntaxException, IOException {
        log.debug("REST request to download a paper attachment to paper work with id: {}", id);
        PaperAttachment paperAttachment = paperAttachmentService.findOne(id);
        File file = new File(servletContextImpl.getRealPath(Constants.ROOT_FOLDER + Constants.UPLOAD_DIR + paperAttachment.getPath()));
        
         
        if(!file.exists()){
            String errorMessage = "Sorry. The file you are looking for does not exist";
            System.out.println(errorMessage);
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
            outputStream.close();
            return;
        }
         
        String mimeType= URLConnection.guessContentTypeFromName(file.getName());
        if(mimeType==null){
            System.out.println("mimetype is not detectable, will take default");
            mimeType = "application/octet-stream";
        }
         
        System.out.println("mimetype : "+mimeType);
         
        response.setContentType(mimeType);
         
        /* "Content-Disposition : inline" will show viewable types [like images/text/pdf/anything viewable by browser] right on browser 
            while others(zip e.g) will be directly downloaded [may provide save as popup, based on your browser setting.]*/
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() +"\""));
 
         
        /* "Content-Disposition : attachment" will be directly download, may provide save as popup, based on your browser setting*/
        //response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
         
        response.setContentLength((int)file.length());
 
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
 
        //Copy bytes from source to destination(outputstream in this example), closes both streams.
        FileCopyUtils.copy(inputStream, response.getOutputStream());
    }

}
