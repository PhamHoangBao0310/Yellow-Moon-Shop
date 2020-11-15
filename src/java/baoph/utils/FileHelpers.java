/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baoph.utils;

import java.io.File;
import org.apache.commons.fileupload.FileItem;

/**
 *
 * @author DELL
 */
public class FileHelpers {
    
    public static void writeFile(FileItem item, String txtImageLink) throws Exception {
        File savedFile = new File(txtImageLink);
        item.write(savedFile);
    }
}
