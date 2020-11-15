/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baoph.utils;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

/**
 *
 * @author DELL
 */
public class MultipartHelper implements Serializable {

    static Logger logger = Logger.getLogger(MultipartHelper.class);
    private Hashtable<String, String> params;
    private FileItem fileItem;

    /**
     * @return the params
     */
    public Hashtable<String, String> getParams() {
        return params;
    }

    /**
     * @return the fileItem
     */
    public FileItem getFileItem() {
        return fileItem;
    }

    public boolean operateMultipartConfig(HttpServletRequest request) {
        boolean isMultiPart = ServletFileUpload.isMultipartContent(request);
        if (isMultiPart) {
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            List items = null;
            try {
                items = upload.parseRequest(request);
            } catch (Exception e) {
                logger.error("MultipartHelper-RequestParse : " + e.getMessage());
                isMultiPart = false;
            }
            Iterator iter = items.iterator();
            params = new Hashtable();
            fileItem = null;
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();
                if (item.isFormField()) {
                    params.put(item.getFieldName(), item.getString());
                } else {
                    try {
                        fileItem = item;
                    } catch (Exception e) {
                        logger.error("MultipartHelper-LoadItemName : " + e.getMessage());
                        isMultiPart = false;
                    }
                }
            }
        }
        return isMultiPart;
    }

}
