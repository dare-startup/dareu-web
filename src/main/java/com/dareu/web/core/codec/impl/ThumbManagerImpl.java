package com.dareu.web.core.codec.impl;

import com.dareu.web.core.codec.ThumbManager;
import com.github.roar109.syring.annotation.ApplicationProperty;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import org.bytedeco.javacpp.opencv_core.Buffer;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;

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
        CanvasFrame 
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputFile); 
        try{
            grabber.start();
            Frame frame = grabber.grab(); 
            frame.image[0].
                    Buffer buffer = new Buffer(); 
            ImageIO.write()
        }catch(Exception ex){
            
        }
    }
    
}
