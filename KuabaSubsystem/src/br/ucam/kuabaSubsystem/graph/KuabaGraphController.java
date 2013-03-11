/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ucam.kuabaSubsystem.graph;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxICell;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxGraph;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Bruno
 */
public class KuabaGraphController implements mxEventSource.mxIEventListener {

    private KuabaGraph kg;
    private boolean isHorizontal;

    public KuabaGraphController(KuabaGraph kg, boolean isHorizontal) {
        this.kg = kg;
        this.isHorizontal = isHorizontal;
    }
    
    public void invoke(Object sender, mxEventObject evt) {
        if (evt.getName().equals(mxEvent.CELLS_MOVED)) {
            updateLinksRouting(sender, evt);
        }
        
        if (evt.getName().equals(mxEvent.CELLS_FOLDED)) {
            toggleNames(sender, evt);
        }
    }
    
    private void toggleNames(Object sender, mxEventObject evt) {
        
        mxGraph graph = (mxGraph) sender;
        
        Object[] cells =  (Object[]) evt.getProperties().get("cells");
        
        for (int x=0; x<cells.length;x++) {
            mxCell cell = (mxCell) cells[x];
            
            if (cell.getStyle().contains(KuabaGraph.TREE_BOX_STYLE)) {
                if(cell.getStyle().contains("noLabel")) {
                    cell.setStyle(KuabaGraph.TREE_BOX_STYLE);
                }
                else {
                    cell.setStyle(KuabaGraph.TREE_BOX_STYLE+";noLabel=1");
                }
            }
        }
    }
    
    private void updateLinksRouting(Object sender, mxEventObject evt) {

        mxGraph graph = (mxGraph) sender;
        
        Object[] cells =  (Object[]) evt.getProperties().get("cells");
        
        HashSet<mxCell> cellSet = new HashSet<mxCell>();

        for (int x=0; x<cells.length;x++) {
            mxCell cell = (mxCell) cells[x];
            
            if (!cell.isVertex()) continue;
            
            cellSet.add(cell);
            
            for (int y=0; y< cell.getChildCount(); y++) {
                cellSet.add((mxCell) cell.getChildAt(y));
            }
        }
        
        for (mxCell cell : cellSet) {

            for (int x=0; x<cell.getEdgeCount();x++) {
                mxCell edge = (mxCell) cell.getEdgeAt(x);
                
                if (edge.getGeometry().getPoints()!=null) {
                    
                    List<mxPoint> points = edge.getGeometry().getPoints();
                    
                    mxPoint closest = null;
                    
                    if (edge.getSource().equals(cell)) {
                        closest = points.get(0);
                    } else {
                        closest = points.get(points.size()-1);
                    }
                    
                    if (isHorizontal) {
                        closest.setY(closest.getY()+Double.parseDouble(evt.getProperties().get("dy").toString()));
                    }
                    else {
                        closest.setX(closest.getX()+Double.parseDouble(evt.getProperties().get("dx").toString()));
                    }
                }
            }
        }
    }
    
}
