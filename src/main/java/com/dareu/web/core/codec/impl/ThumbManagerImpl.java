package com.dareu.web.core.codec.impl;

import com.dareu.web.core.codec.ThumbManager;
import com.github.roar109.syring.annotation.ApplicationProperty;
import java.io.File;
import java.io.IOException;
import javax.inject.Inject;

/**
 *
 * @author Alberto Rubalcaba <arubalcaba@24hourfit.com>
 */
public class ThumbManagerImpl implements ThumbManager{

    
    @ApplicationProperty(name = "com.dareu.web.video.thumb.directory", type = ApplicationProperty.Types.SYSTEM)
    @Inject
    private String thumbDirectory; 
    
    static final int FRAME = 100;
    public void createThumb(File inputFile, String thumbId) throws IOException {

    }
    
}
