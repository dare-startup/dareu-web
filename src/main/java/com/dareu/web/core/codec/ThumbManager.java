package com.dareu.web.core.codec;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Alberto Rubalcaba <arubalcaba@24hourfit.com>
 */
public interface ThumbManager {
    public void createThumb(File inputFile, String thumbId)throws IOException; 
}
