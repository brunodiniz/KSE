/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.argouml.module.kuabaModule.ui;

import javax.swing.JFrame;
import org.argouml.module.kuabaModule.ui.MainFrame;
import org.argouml.util.ArgoFrame;

/**
 *
 * @author Bruno
 */
public class ActiveFrameController {
    
//    public static final int MAIN_FRAME = 0;
//    public static final int ARGO_FRAME = 1;
//    public static final int DR_UNION_FRAME = 2;
//    public static final int DR_DECISION_FRAME = 3;
    
    private static JFrame activeFrame;
    
    public static void initMainFrame() {
        activeFrame = MainFrame.getInstance();
        activeFrame.setVisible(true);
    }
    
//    public static void switchFrames() {
//        if(activeFrame == MAIN_FRAME) {
//            activeFrame = ARGO_FRAME;
//            ArgoFrame.getInstance().setVisible(true);
//            MainFrame.getInstance().setVisible(false);
//        }
//        else if (activeFrame == ARGO_FRAME) {
//            activeFrame = MAIN_FRAME;
//            ArgoFrame.getInstance().setVisible(false);
//            MainFrame.getInstance().setVisible(true);
//        }
//    }
    
    public static void setActive(JFrame frame) {
        activeFrame.setVisible(false);
        activeFrame = frame;
        activeFrame.setVisible(true);        
    }
    
//    private static void setAllInvisible() {
//        ArgoFrame.getInstance().setVisible(false);
//        MainFrame.getInstance().setVisible(false);
//        DRUnionFrame.getInstance().setVisible(false);
//    }
    
}
