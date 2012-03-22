/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ucam.kuabaSubsystem.repositories;

/**
 *
 * @author Bruno
 */
public class RepositoryLoadException extends Exception {

    public RepositoryLoadException(String url) {
        super("A problem has ocurred when trying to load: "+url);
    }
    
    public RepositoryLoadException(String url, String explanation) {
        super("A problem has ocurred when trying to load: "+url+"\n"+explanation);
    }
    
    
}
