/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ucam.kuabaSubsystem.rationaleProcessor;

import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.swing.DefaultListModel;

/**
 *
 * @author Bruno
 */
public class EquivalenceRuleManager {
    
    private SortedMap<String, SortedSet<String>> equivalenceMap = new TreeMap<String, SortedSet<String>>();
    private SortedSet<String> baseDomainIdeas;

    public EquivalenceRuleManager(SortedSet<String> baseDomainIdeas) {
        this.baseDomainIdeas = baseDomainIdeas;
    }
    
    public EquivalenceRuleManager(Collection<Idea> baseDomainIdeas) {
        SortedSet<String> baseDomainIdeasSet = new TreeSet<String>();
        for (Idea i : baseDomainIdeas) {
            if (i.hasHasText())
                baseDomainIdeasSet.add(i.getHasText());
        }
        
        this.baseDomainIdeas = baseDomainIdeasSet;
    }
    
    public boolean isEquivalent(String idea1, String idea2) {
        Set<String> eq1 = equivalenceMap.get(idea1);
        Set<String> eq2 = equivalenceMap.get(idea2);
        
        if(eq1 == null || eq2 == null) return false;
        if(eq1.contains(idea2) || eq2.contains(idea1)) return true;
        else return false;
    }
    
    public void addEquivalenceRule(SortedSet<String> newRule) throws Exception {
        normalizeRule(newRule);
        
        if (!isValidRule(newRule)) throw new Exception("You can't have two base domain ideas in the same rule!");
        
        for (String key : newRule) {
                TreeSet<String> newEqSet = new TreeSet<String>();
                for(String eqValue : newRule){
                    if(!eqValue.equals(key)) newEqSet.add(eqValue);
                }
                equivalenceMap.put(key, newEqSet);
        }
    }
    
    private void normalizeRule(SortedSet<String> equivalenceRule) {
        for (String key : equivalenceMap.keySet()) {
            for (String value : equivalenceMap.get(key)) {
                if (equivalenceRule.contains(value)) {
                    equivalenceRule.add(key);
                }
            }
        }
    }
    
    private boolean isValidRule(SortedSet<String> newRule) {
        boolean containsBaseIdea = false;
        for (String key : newRule) {
            if (baseDomainIdeas.contains(key)) {
                if(containsBaseIdea) {
                    return false;
                } else {
                    containsBaseIdea = true;
                }                
            }
        }
        return true;
    }
    
    public void removeEquivalenceRule(SortedSet<String> rule) {
        for (String key : rule) {
                TreeSet<String> tempEqSet = new TreeSet<String>();
                for(String eqValue : rule){
                    if(!eqValue.equals(key)) tempEqSet.add(eqValue);
                }
                if(equivalenceMap.containsKey(key)) {
                    equivalenceMap.get(key).removeAll(tempEqSet);
                    if(equivalenceMap.get(key).isEmpty()) equivalenceMap.remove(key);
                }
        }
    }
    
    public void updateEquivalenceRule(SortedSet<String> oldRule, SortedSet<String> newRule) throws Exception {
        removeEquivalenceRule(oldRule);
        addEquivalenceRule(newRule);
    }
    
    //generates an updated view of the rules in this manager to be showed in a JList
    public void updateListModel(DefaultListModel<String> listModel, boolean clear) {
        if(clear) listModel.clear();
        
        Set<String> keys = equivalenceMap.keySet();

        Set<String> visitedKeys = new HashSet<String>();

        for (String k : keys) {
            if (!visitedKeys.contains(k)) {
                visitedKeys.add(k);
                String newRule = k;
                Set<String> eqSet = equivalenceMap.get(k);

                for (String s : eqSet) {
                    newRule += " = " + s;
                    visitedKeys.add(s);
                }

                if (!eqSet.isEmpty()) {
                    listModel.addElement(newRule);
                }
            }
        }
    }
    
    public void removeAllEquivalenceRules() {
        equivalenceMap.clear();
    }
}
