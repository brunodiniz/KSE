package br.ucam.kuabaSubsystem.repositories;

import java.io.File;

import br.ucam.kuabaSubsystem.core.KuabaSubsystem;
import br.ucam.kuabaSubsystem.util.TemplateGenerator;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.protege.editor.owl.model.refactor.EntityFindAndReplaceURIRenamer;
import org.protege.editor.owl.model.refactor.ontology.OntologyMigrator;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.model.AddImport;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyAlreadyExistsException;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

public final class OwlApiFileGateway implements RepositoryGateway{

    private static OwlApiFileGateway instance;
    private OWLOntologyManager manager;
    private HashMap<IRI, KuabaRepository> repoMap;
    private HashMap<KuabaRepository, File> fileMap;
    private static final String ONTOLOGY_URL = KuabaSubsystem.ONTOLOGY_URL;

    private OwlApiFileGateway() {
            this.repoMap = new HashMap<IRI, KuabaRepository>();
            this.fileMap = new HashMap<KuabaRepository, File>();
            this.manager = OWLManager.createOWLOntologyManager();  
            
            File onto = new File("kuabaOntology/KuabaOntology.owl");
            try {
                if (onto.exists())
                    load("kuabaOntology/KuabaOntology.owl");
                else load(ONTOLOGY_URL);
                }
            catch (RepositoryLoadException rle) {
                JOptionPane.showMessageDialog(null, rle.getMessage());
            }
    }

    @Override
    public boolean save(KuabaRepository kr) {
        return save(kr, fileMap.get(kr));
    }
    
    @Override
    public boolean save(KuabaRepository kr, File destination) {
        return save(kr, destination, null);
    }
    
    public boolean save(KuabaRepository kr, File destination, String newBaseUrl) {
//        if(newBaseUrl != null)
//            manager.setOntologyDocumentIRI((OWLOntology) kr.getModel(), IRI.create(newBaseUrl));
        
        try {
            File previousLoc = fileMap.get(kr);
            if(destination != null) {
                manager.saveOntology((OWLOntology) kr.getModel(), IRI.create(destination));
                if(!destination.equals(previousLoc)) fileMap.put(kr, destination);
            } else {
                if (previousLoc != null)
                    manager.saveOntology((OWLOntology) kr.getModel(), IRI.create(previousLoc));
                else 
                    manager.saveOntology((OWLOntology) kr.getModel());
            }

            return true;
        } catch (OWLOntologyStorageException ex) {
            Logger.getLogger(OwlApiFileGateway.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    @Override
    public KuabaRepository load(String url) throws RepositoryLoadException{
        
        IRI iri;
        File f = new File(url);
        if(!url.contains(":/")) {
            iri = IRI.create(f);
        }
        else iri = IRI.create(url);
        
        try {
            OWLOntology inst = manager.loadOntologyFromOntologyDocument(iri);
            
            if (inst == null) throw new RepositoryLoadException(url, "Invalid Location.");
            if (inst.getOntologyID().getOntologyIRI() == null) {
                //An anonymous Ontology was loaded, and they are not supported by the Kuaba Subsystem
                manager.removeOntology(inst);
                throw new RepositoryLoadException(url, "It is probably an incompatible or malformed file.");
            }
            
            KuabaRepository repo = new OwlApiKuabaRepository(inst, manager.getOWLDataFactory());
            repoMap.put(inst.getOntologyID().getOntologyIRI(), repo);
            fileMap.put(repo, f);
            return repo;            
        } catch (OWLOntologyAlreadyExistsException ex) {
            OWLOntologyID id = ex.getOntologyID();
//            System.err.println("Ontologia "+id.getOntologyIRI().toString()+" já está carregada, esta será retornada.");
            return repoMap.get(id.getOntologyIRI());
            
        } catch (OWLOntologyCreationException ex) {
            throw new RepositoryLoadException(url, "Invalid Location.");
//            Logger.getLogger(OwlApiFileGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override 
    public KuabaRepository createNewRepository() {
        return this.createNewRepository(null, null);
    }

    public KuabaRepository createNewRepository(File destination) {
        return this.createNewRepository(null, destination);
    }

    public KuabaRepository createNewRepository(String url) {
        return this.createNewRepository(url, null);
    }
    
    public KuabaRepository createNewRepository(String url, File destination) {
        IRI iri;
        if (url == null)
            iri = IRI.generateDocumentIRI();
        else 
            iri = IRI.create(url);
        
        try {
            OWLOntology inst = manager.createOntology(iri);
            OWLImportsDeclaration imp = manager.getOWLDataFactory().getOWLImportsDeclaration(IRI.create(ONTOLOGY_URL));
            AddImport addi = new AddImport(inst, imp);
            manager.applyChange(addi);
            KuabaRepository repo = new OwlApiKuabaRepository(inst, manager.getOWLDataFactory());
            repoMap.put(inst.getOntologyID().getOntologyIRI(), repo);
            fileMap.put(repo, destination);
            
            TemplateGenerator.generateRootQuestion(repo);
            
            if (destination != null) this.save(repo);
            return repo;
        } catch (OWLOntologyCreationException ex) {
            Logger.getLogger(OwlApiFileGateway.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static OwlApiFileGateway getInstance() {
        if (instance == null) 
            instance = new OwlApiFileGateway();
        
        return instance;
    }
    
    public KuabaRepository getSourceRepository(IRI iri) {
        IRI repoIRI = IRI.create(iri.getStart().replaceAll("#", ""));
        KuabaRepository repo = repoMap.get(repoIRI);
        try {
            if (repo == null) repo = this.load(repoIRI.toString());
        }
        catch (Exception e) {
            System.err.println("Error when trying to get the source repository of "+ iri.toString());
            System.err.println("Source repository not found.");
        }
        
        return repo;
    }
    
    public OWLOntology getKuabaOntology() {
        return manager.getOntology(IRI.create(ONTOLOGY_URL));
    }
    
    public void dispose(KuabaRepository kr) {
        OWLOntology model = (OWLOntology) kr.getModel();
        repoMap.remove(model.getOntologyID().getOntologyIRI());
        fileMap.remove(kr);      
        model.getOWLOntologyManager().removeOntology(model);
    }
    
    //dispose all loaded repositories but the Kuaba Ontology
    public void disposeAll() {
        Set<IRI> keySet = repoMap.keySet();
        Iterator<IRI> it = keySet.iterator();
        
        while (it.hasNext()) {
            IRI key = it.next();
            if(IRI.create(ONTOLOGY_URL).equals(key)) continue;
            KuabaRepository repo = repoMap.get(key);
            it.remove();
            dispose(repo);
        }
    }
    
    public KuabaRepository copy(KuabaRepository kr) {
        return copy(kr,null,null);
    }
    
    public KuabaRepository copy(KuabaRepository kr, String url) {
        return copy(kr,url,null);
    }
    
    public KuabaRepository copy(KuabaRepository kr, String url, File destination) {
        IRI iri;
        if (url == null)
            iri = IRI.generateDocumentIRI();
        else 
            iri = IRI.create(url);
        
        try {
            OWLOntology model = (OWLOntology)kr.getModel();
            OWLOntology inst = manager.createOntology(iri);
            
            OntologyMigrator migrator = new OntologyMigrator(manager, model, inst);
            migrator.performMigration();
            
            EntityFindAndReplaceURIRenamer renamer = new EntityFindAndReplaceURIRenamer(manager, inst.getSignature(false), Collections.singleton(inst), model.getOntologyID().getOntologyIRI().toString(), iri.toString());
            
            if(renamer.hasErrors()) System.err.println("ERRO durante a cópia - rename");
            
            manager.applyChanges(renamer.getChanges());
            
            KuabaRepository repo = new OwlApiKuabaRepository(inst, manager.getOWLDataFactory());
            repoMap.put(inst.getOntologyID().getOntologyIRI(), repo);
            fileMap.put(repo, destination);
            
            
            return repo;
            
        } catch (OWLOntologyCreationException ex) {
            System.err.println("ERRO em copy");
            Logger.getLogger(OwlApiFileGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

}