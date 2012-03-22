/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ucam.kuabaSubsystem.rationaleProcessor.unionui;

import br.ucam.kuabaSubsystem.core.KuabaSubsystem;
import br.ucam.kuabaSubsystem.kuabaModel.Decision;
import br.ucam.kuabaSubsystem.repositories.KuabaRepository;
import br.ucam.kuabaSubsystem.repositories.OwlApiFileGateway;

/**
 *
 * @author Bruno
 */
public class WizardTest {
    public static void main(String args[]) {
        
        KuabaSubsystem.gateway = OwlApiFileGateway.getInstance();
        
        DRUnionWizard.NEXT_TEXT = "Próximo >";
        DRUnionWizard.BACK_TEXT = "< Voltar";
        DRUnionWizard.FINISH_TEXT = "Finalizar";
        DRUnionWizard.CANCEL_TEXT = "Cancelar";
        
        DRUnionWizard druWiz = new DRUnionWizard();
        
        
//        DRUnionWizardPanelDescriptor desc = new DRUnionWizardPanelDescriptor("teste", new DRUnionRepositoryChooserPanel());
        
        
//        WizardPanelDescriptor descriptor1 = new TestPanel1Descriptor();
//        wizard.registerWizardPanel(TestPanel1Descriptor.IDENTIFIER, descriptor1);
//
//        WizardPanelDescriptor descriptor2 = new TestPanel2Descriptor();
//        wizard.registerWizardPanel(TestPanel2Descriptor.IDENTIFIER, descriptor2);
//
//        WizardPanelDescriptor descriptor3 = new TestPanel3Descriptor();
//        wizard.registerWizardPanel(TestPanel3Descriptor.IDENTIFIER, descriptor3);
        
//        druWiz.setCurrentPanel(desc.getPanelDescriptorIdentifier());
        
        int ret = druWiz.showModalDialog();
        
        System.out.println("Dialog return code is (0=Finish,1=Cancel,2=Error): " + ret);
//        System.out.println("Second panel selection is: " + 
//            (((TestPanel2)descriptor2.getPanelComponent()).getRadioButtonSelected()));
        
        if(ret == 0) {
            KuabaRepository result = druWiz.getUnionResult();
            
            for (Decision d : result.getAllDecisions()) {
                System.out.println("Accepted: "+d.getIsAccepted()+" Idea: "+d.getConcludes().getHasText());
            }
        }
        
        System.exit(0);

    }
}
