// $Id: ProjectSettings.java 15611 2008-08-26 17:39:31Z penyaskito $
// Copyright (c) 2006-2008 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.kernel;

import java.awt.Font;
import java.beans.PropertyChangeEvent;

import org.argouml.application.api.Argo;
import org.argouml.application.events.ArgoDiagramAppearanceEvent;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoNotationEvent;
import org.argouml.configuration.Configuration;
import org.argouml.configuration.ConfigurationKey;
import org.argouml.notation.Notation;
import org.argouml.notation.NotationName;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.uml.diagram.DiagramAppearance;
import org.tigris.gef.undo.Memento;
import org.tigris.gef.undo.UndoManager;

/**
 * A datastructure for settings for a Project. <p>
 *
 * Most getters return a string, since they are used by "argo.tee".
 * This is also the reason all these attributes
 * are not part of a Map or something. <p>
 *
 * TODO: The header comment is curently not used - this function
 * is not completely implemented yet. How do we store this in the project?
 * Where should the user enter his header comment? See issue 4813.
 *
 * @author michiel
 */
public class ProjectSettings {

    /* The notation settings with project scope: */
    private String notationLanguage;
    private boolean showBoldNames;
    private boolean useGuillemots;
    private boolean showAssociationNames;
    private boolean showVisibility;
    private boolean showMultiplicity;
    private boolean showInitialValue;
    private boolean showProperties;
    private boolean showTypes;
    private boolean showStereotypes;
    private boolean showSingularMultiplicities;
    private int defaultShadowWidth;
    private int defaultStereotypeView;

    /* Diagram appearance settings with project scope: */
    private String fontName;
    private int fontSize;
    private boolean hideBidirectionalArrows;
    /* Keep some fonts around depending on the above settings: */
    private Font fontPlain;
    private Font fontItalic;
    private Font fontBold;
    private Font fontBoldItalic;

    /* Generation preferences: */
    private String headerComment =
        "Your copyright and other header comments";
    private String generationOutputDir;


    /**
     * Create a new set of project settings,
     * based on the application defaults. <p>
     *
     * The constructor is not public, since this
     * class is only created from the Project..
     */
    ProjectSettings() {
        super();

        notationLanguage =
            Notation.getConfiguredNotation().getConfigurationValue();
        NotationProviderFactory2.setCurrentLanguage(notationLanguage);
        showBoldNames = Configuration.getBoolean(
                Notation.KEY_SHOW_BOLD_NAMES);
        useGuillemots = Configuration.getBoolean(
                Notation.KEY_USE_GUILLEMOTS, false);
        /*
         * The next one defaults to TRUE, to stay compatible with older
         * ArgoUML versions that did not have this setting:
         */
        showAssociationNames = Configuration.getBoolean(
                Notation.KEY_SHOW_ASSOCIATION_NAMES, true);
        showVisibility = Configuration.getBoolean(
                Notation.KEY_SHOW_VISIBILITY);
        showMultiplicity = Configuration.getBoolean(
                Notation.KEY_SHOW_MULTIPLICITY);
        showInitialValue = Configuration.getBoolean(
                Notation.KEY_SHOW_INITIAL_VALUE);
        showProperties = Configuration.getBoolean(
                Notation.KEY_SHOW_PROPERTIES);
        /*
         * The next ones defaults to TRUE, to stay compatible with older
         * ArgoUML versions that did not have this setting:
         */
        showTypes = Configuration.getBoolean(Notation.KEY_SHOW_TYPES, true);
        hideBidirectionalArrows = Configuration.getBoolean(
                Notation.KEY_HIDE_BIDIRECTIONAL_ARROWS, true);
        
        showStereotypes = Configuration.getBoolean(
                Notation.KEY_SHOW_STEREOTYPES);
        /*
         * The next one defaults to TRUE, despite that this is
         * NOT compatible with older ArgoUML versions
         * (before 0.24) that did
         * not have this setting - see issue 1395 for the rationale:
         */
        showSingularMultiplicities = Configuration.getBoolean(
                Notation.KEY_SHOW_SINGULAR_MULTIPLICITIES, true);
        defaultShadowWidth = Configuration.getInteger(
                Notation.KEY_DEFAULT_SHADOW_WIDTH, 1);
        defaultStereotypeView = Configuration.getInteger(
                ProfileConfiguration.KEY_DEFAULT_STEREOTYPE_VIEW,
                DiagramAppearance.STEREOTYPE_VIEW_TEXTUAL);

        /*
         * Diagram appearance settings:
         */
        fontName = DiagramAppearance.getInstance().getConfiguredFontName();
        fontSize = Configuration.getInteger(DiagramAppearance.KEY_FONT_SIZE);
        /* And initialise some fonts: */
        initFonts();

        /* Generation preferences: */
        if (System.getProperty("file.separator").equals("/")) {
            generationOutputDir = "/tmp";
        } else {
            generationOutputDir = System.getProperty("java.io.tmpdir");
        }
        generationOutputDir = Configuration.getString(
                Argo.KEY_MOST_RECENT_EXPORT_DIRECTORY, generationOutputDir);
    }

    /**
     * Send all events... <p>
     * This code fixes the following problem when loading a project:
     * In the ProjectBrowser, the new project is loaded, 
     * and only when successful, the old one is removed.
     * While loading, the Figs read the notation settings to be able
     * to draw themselves correctly from the wrong project,
     * since the old project is still the "current" one.
     * Hence, after the ProjectBrowser finished loading,
     * it sets the current project to the new one, removes the
     * old project, and then calls this init() method, to set 
     * all notation settings right - which refreshes the screen. <p>
     * TODO: Find a better way, 
     * which does not cause the diagrams to be drawn wrongly
     * first, and then corrected.  
     */
    public void init() {
        /* Since this is (hopefully) a temporary solution,
         * and nobody ever looks at the type of notation event,
         * we can simplify from sending every existing event
         * to one event only. But since there is no 
         * catch-all event defined, we just make one up.
         * Rationale: reduce the number of total 
         * refreshes of the drawing. */
        init(true, Configuration.makeKey("notation", "all"));

        /* The above hence replaces the following statements. 
         * If ever someone needs to dissect notation events, 
         * we may need to add these again. */
//        init(notationLanguage, 
//                Notation.KEY_DEFAULT_NOTATION);
//        init(showBoldNames, 
//                Notation.KEY_SHOW_BOLD_NAMES);
//        init(useGuillemots, 
//                Notation.KEY_USE_GUILLEMOTS);
//        init(showAssociationNames, 
//                Notation.KEY_SHOW_ASSOCIATION_NAMES);
//        init(showVisibility,
//                Notation.KEY_SHOW_VISIBILITY);
//        init(showMultiplicity,
//                Notation.KEY_SHOW_MULTIPLICITY);
//        init(showInitialValue, 
//                Notation.KEY_SHOW_INITIAL_VALUE);
//        init(showProperties,
//                Notation.KEY_SHOW_PROPERTIES);
//        init(showTypes, 
//                Notation.KEY_SHOW_TYPES);
//        init(showStereotypes, 
//                Notation.KEY_SHOW_STEREOTYPES);
//        init(showSingularMultiplicities, 
//                Notation.KEY_SHOW_SINGULAR_MULTIPLICITIES);
//        init(defaultShadowWidth,
//                Notation.KEY_DEFAULT_SHADOW_WIDTH);

        /* Since this is (hopefully) a temporary solution,
         * and nobody ever looks at the type of the
         * diagram appearance event,
         * we can simplify from sending every existing event
         * to one event only. But since there is no 
         * catch-all event defined, we just use one.
         * Rationale: reduce the number of total 
         * refreshes of the drawing. */
        fireDiagramAppearanceEvent(
                Configuration.makeKey("diagramappearance", "all"), 
                0, 0);

        /* The above hence replaces the following statements. 
         * If ever someone needs to dissect 
         * diagram-appearance events, 
         * we may need to add these again. */
//        fireDiagramAppearanceEvent(DiagramAppearance.KEY_FONT_NAME, 
//                fontName, fontName);
//        fireDiagramAppearanceEvent(DiagramAppearance.KEY_FONT_SIZE, 
//                fontSize, fontSize);
    }

    @SuppressWarnings("unused")
    private void init(String value, ConfigurationKey key) {
        fireNotationEvent(key, value, value);
    }

    private void init(boolean value, ConfigurationKey key) {
        fireNotationEvent(key, value, value);
    }

    @SuppressWarnings("unused")
    private void init(int value, ConfigurationKey key) {
        fireNotationEvent(key, value, value);
    }

    /**
     * Used by "argo.tee".
     *
     * @return Returns the notation language.
     */
    public String getNotationLanguage() {
        return notationLanguage;
    }

    /**
     * @return Returns the notation language.
     */
    public NotationName getNotationName() {
        return Notation.findNotation(notationLanguage);
    }

    /**
     * @param newLanguage the notation language.
     * @return true if the notation is set - false if it does not exist
     */
    public boolean setNotationLanguage(final String newLanguage) {
        if (notationLanguage.equals(newLanguage)) return true;
        if (Notation.findNotation(newLanguage) == null) {
            /* This Notation is not available! */
            return false;
        }

        final String oldLanguage = notationLanguage;

        Memento memento = new Memento() {
            private final ConfigurationKey key = Notation.KEY_DEFAULT_NOTATION;

            public void redo() {
                notationLanguage = newLanguage;
                NotationProviderFactory2.setCurrentLanguage(newLanguage);
                fireNotationEvent(key, oldLanguage, newLanguage);
            }

            public void undo() {
                notationLanguage = oldLanguage;
                NotationProviderFactory2.setCurrentLanguage(oldLanguage);
                fireNotationEvent(key, newLanguage, oldLanguage);
            }
        };
        if (UndoManager.getInstance().isGenerateMementos()) {
            UndoManager.getInstance().addMemento(memento);
        }
        memento.redo();
        ProjectManager.getManager().setSaveEnabled(true);
        return true;
    }

    /**
     * @param nn the new notation language
     */
    public void setNotationLanguage(NotationName nn) {
        setNotationLanguage(nn.getConfigurationValue());
    }

    /**
     * Used by "argo.tee".
     *
     * @return Returns "true" if we show bold names.
     */
    public String getShowBoldNames() {
        return Boolean.toString(showBoldNames);
    }

    /**
     * @return Returns <code>true</code> if we show bold names.
     */
    public boolean getShowBoldNamesValue() {
        return showBoldNames;
    }

    /**
     * @param showbold <code>true</code> if names are to be shown in bold font.
     */
    public void setShowBoldNames(String showbold) {
        setShowBoldNames(Boolean.valueOf(showbold).booleanValue());
    }

    /**
     * @param showem <code>true</code> if names are to be shown in bold font.
     */
    public void setShowBoldNames(final boolean showem) {
        if (showBoldNames == showem) return;

        Memento memento = new Memento() {
            private final ConfigurationKey key = Notation.KEY_SHOW_BOLD_NAMES;

            public void redo() {
                showBoldNames = showem;
                fireNotationEvent(key, !showem, showem);
            }

            public void undo() {
                showBoldNames = !showem;
                fireNotationEvent(key, showem, !showem);
            }
        };
        if (UndoManager.getInstance().isGenerateMementos()) {
            UndoManager.getInstance().addMemento(memento);
        }
        memento.redo();
        ProjectManager.getManager().setSaveEnabled(true);
    }

    /**
     * Used by "argo.tee".
     *
     * @return Returns "true" if we show guillemots.
     */
    public String getUseGuillemots() {
        return Boolean.toString(useGuillemots);
    }

    /**
     * @return Returns <code>true</code> if we show guillemots.
     */
    public boolean getUseGuillemotsValue() {
        return useGuillemots;
    }
    

    /**
     * @param showem <code>true</code> if guillemots are to be shown.
     */
    public void setUseGuillemots(String showem) {
        setUseGuillemots(Boolean.valueOf(showem).booleanValue());
    }

    /**
     * @param showem <code>true</code> if guillemots are to be shown.
     */

    public void setUseGuillemots(final boolean showem) {
        if (useGuillemots == showem) return;

        Memento memento = new Memento() {
            private final ConfigurationKey key = Notation.KEY_USE_GUILLEMOTS;

            public void redo() {
                useGuillemots = showem;
                fireNotationEvent(key, !showem, showem);
            }

            public void undo() {
                useGuillemots = !showem;
                fireNotationEvent(key, showem, !showem);
            }
        };
        if (UndoManager.getInstance().isGenerateMementos()) {
            UndoManager.getInstance().addMemento(memento);
        }
        memento.redo();
        ProjectManager.getManager().setSaveEnabled(true);
    }

    /**
     * @return the left pointing guillemot, i.e. << or the one-character symbol
     */
    public String getLeftGuillemot() {
        return useGuillemots ? "\u00ab" : "<<";
    }

    /**
     * @return the right pointing guillemot, i.e. >> or the one-character symbol
     */
    public String getRightGuillemot() {
        return useGuillemots ? "\u00bb" : ">>";
    }

    /**
     * Used by "argo.tee".
     *
     * @return Returns "true" if we show association names.
     */
    public String getShowAssociationNames() {
        return Boolean.toString(showAssociationNames);
    }

    /**
     * @return Returns <code>true</code> if we show association names.
     */
    public boolean getShowAssociationNamesValue() {
        return showAssociationNames;
    }

    /**
     * @param showem <code>true</code> if association names are to be shown.
     */
    public void setShowAssociationNames(String showem) {
        setShowAssociationNames(Boolean.valueOf(showem).booleanValue());
    }

    /**
     * @param showem <code>true</code> if association names are to be shown.
     */
    public void setShowAssociationNames(final boolean showem) {
        if (showAssociationNames == showem) return;

        Memento memento = new Memento() {
            private final ConfigurationKey key = 
                Notation.KEY_SHOW_ASSOCIATION_NAMES;

            public void redo() {
                showAssociationNames = showem;
                fireNotationEvent(key, !showem, showem);
            }

            public void undo() {
                showAssociationNames = !showem;
                fireNotationEvent(key, showem, !showem);
            }
        };
        if (UndoManager.getInstance().isGenerateMementos()) {
            UndoManager.getInstance().addMemento(memento);
        }
        memento.redo();
        ProjectManager.getManager().setSaveEnabled(true);
    }

    /**
     * Used by "argo.tee".
     *
     * @return Returns "true" if we show visibilities.
     */
    public String getShowVisibility() {
        return Boolean.toString(showVisibility);
    }

    /**
     * @return Returns <code>true</code> if we show visibilities.
     */
    public boolean getShowVisibilityValue() {
        return showVisibility;
    }

    /**
     * @param showem <code>true</code> if visibilities are to be shown.
     */
    public void setShowVisibility(String showem) {
        setShowVisibility(Boolean.valueOf(showem).booleanValue());
    }

    /**
     * @param showem <code>true</code> if visibilities are to be shown.
     */
    public void setShowVisibility(final boolean showem) {
        if (showVisibility == showem) return;

        Memento memento = new Memento() {
            private final ConfigurationKey key = Notation.KEY_SHOW_VISIBILITY;

            public void redo() {
                showVisibility = showem;
                fireNotationEvent(key, !showem, showem);
            }

            public void undo() {
                showVisibility = !showem;
                fireNotationEvent(key, showem, !showem);
            }
        };
        if (UndoManager.getInstance().isGenerateMementos()) {
            UndoManager.getInstance().addMemento(memento);
        }
        memento.redo();
        ProjectManager.getManager().setSaveEnabled(true);
    }

    /**
     * Used by "argo.tee".
     *
     * @return Returns "true" if we show multiplicities.
     */
    public String getShowMultiplicity() {
        return Boolean.toString(showMultiplicity);
    }

    /**
     * @return Returns <code>true</code> if we show multiplicities.
     */
    public boolean getShowMultiplicityValue() {
        return showMultiplicity;
    }

    /**
     * @param showem <code>true</code> if multiplicity is to be shown.
     */
    public void setShowMultiplicity(String showem) {
        setShowMultiplicity(Boolean.valueOf(showem).booleanValue());
    }

    /**
     * @param showem <code>true</code> if the multiplicity is to be shown.
     */
    public void setShowMultiplicity(final boolean showem) {
        if (showMultiplicity == showem) return;

        Memento memento = new Memento() {
            private final ConfigurationKey key = Notation.KEY_SHOW_MULTIPLICITY;

            public void redo() {
                showMultiplicity = showem;
                fireNotationEvent(key, !showem, showem);
            }

            public void undo() {
                showMultiplicity = !showem;
                fireNotationEvent(key, showem, !showem);
            }
        };
        if (UndoManager.getInstance().isGenerateMementos()) {
            UndoManager.getInstance().addMemento(memento);
        }
        memento.redo();
        ProjectManager.getManager().setSaveEnabled(true);
    }

    /**
     * Used by "argo.tee".
     *
     * @return Returns "true" if we show initial values.
     */
    public String getShowInitialValue() {
        return Boolean.toString(showInitialValue);
    }

    /**
     * @return Returns <code>true</code> if we show initial values.
     */
    public boolean getShowInitialValueValue() {
        return showInitialValue;
    }

    /**
     * @param showem <code>true</code> if initial values are to be shown.
     */
    public void setShowInitialValue(String showem) {
        setShowInitialValue(Boolean.valueOf(showem).booleanValue());
    }

    /**
     * @param showem <code>true</code> if initial values are to be shown.
     */
    public void setShowInitialValue(final boolean showem) {
        if (showInitialValue == showem) return;

        Memento memento = new Memento() {
            private final ConfigurationKey key =
                Notation.KEY_SHOW_INITIAL_VALUE;

            public void redo() {
                showInitialValue = showem;
                fireNotationEvent(key, !showem, showem);
            }

            public void undo() {
                showInitialValue = !showem;
                fireNotationEvent(key, showem, !showem);
            }
        };
        if (UndoManager.getInstance().isGenerateMementos()) {
            UndoManager.getInstance().addMemento(memento);
        }
        memento.redo();
        ProjectManager.getManager().setSaveEnabled(true);
    }

    /**
     * Used by "argo.tee".
     *
     * @return Returns "true" if we show properties.
     */
    public String getShowProperties() {
        return Boolean.toString(showProperties);
    }

    /**
     * @return Returns <code>true</code> if we show properties.
     */
    public boolean getShowPropertiesValue() {
        return showProperties;
    }

    /**
     * @param showem <code>true</code> if properties are to be shown.
     */
    public void setShowProperties(String showem) {
        setShowProperties(Boolean.valueOf(showem).booleanValue());
    }

    /**
     * @param showem <code>true</code> if properties are to be shown.
     */
    public void setShowProperties(final boolean showem) {
        if (showProperties == showem) return;

        Memento memento = new Memento() {
            private final ConfigurationKey key =
                Notation.KEY_SHOW_PROPERTIES;

            public void redo() {
                showProperties = showem;
                fireNotationEvent(key, !showem, showem);
            }

            public void undo() {
                showProperties = !showem;
                fireNotationEvent(key, showem, !showem);
            }
        };
        if (UndoManager.getInstance().isGenerateMementos()) {
            UndoManager.getInstance().addMemento(memento);
        }
        memento.redo();
        ProjectManager.getManager().setSaveEnabled(true);
    }

    /**
     * Used by "argo.tee".
     *
     * @return Returns "true" if we show types.
     */
    public String getShowTypes() {
        return Boolean.toString(showTypes);
    }

    /**
     * @return Returns <code>true</code> if we show types.
     */
    public boolean getShowTypesValue() {
        return showTypes;
    }

    /**
     * @param showem <code>true</code> if types are to be shown.
     */
    public void setShowTypes(String showem) {
        setShowTypes(Boolean.valueOf(showem).booleanValue());
    }

    /**
     * @param showem <code>true</code> if types are to be shown.
     */
    public void setShowTypes(final boolean showem) {
        if (showTypes == showem) return;

        Memento memento = new Memento() {
            private final ConfigurationKey key = Notation.KEY_SHOW_TYPES;

            public void redo() {
                showTypes = showem;
                fireNotationEvent(key, !showem, showem);
            }

            public void undo() {
                showTypes = !showem;
                fireNotationEvent(key, showem, !showem);
            }
        };
        if (UndoManager.getInstance().isGenerateMementos()) {
            UndoManager.getInstance().addMemento(memento);
        }
        memento.redo();
        ProjectManager.getManager().setSaveEnabled(true);
    }


    /**
     * Used by "argo.tee".
     *
     * @return Returns "true" if we show stereotypes.
     */
    public String getShowStereotypes() {
        return Boolean.toString(showStereotypes);
    }

    /**
     * @return Returns <code>true</code> if we show stereotypes.
     */
    public boolean getShowStereotypesValue() {
        return showStereotypes;
    }

    /**
     * @param showem <code>true</code> if stereotypes are to be shown.
     */
    public void setShowStereotypes(String showem) {
        setShowStereotypes(Boolean.valueOf(showem).booleanValue());
    }

    /**
     * @param showem <code>true</code> if stereotypes are to be shown.
     */
    public void setShowStereotypes(final boolean showem) {
        if (showStereotypes == showem) return;

        Memento memento = new Memento() {
            private final ConfigurationKey key = Notation.KEY_SHOW_STEREOTYPES;

            public void redo() {
                showStereotypes = showem;
                fireNotationEvent(key, !showem, showem);
            }

            public void undo() {
                showStereotypes = !showem;
                fireNotationEvent(key, showem, !showem);
            }
        };
        if (UndoManager.getInstance().isGenerateMementos()) {
            UndoManager.getInstance().addMemento(memento);
        }
        memento.redo();
        ProjectManager.getManager().setSaveEnabled(true);
    }

    /**
     * Used by "argo.tee".
     *
     * @return Returns "true" if we show "1" Multiplicities.
     */
    public String getShowSingularMultiplicities() {
        return Boolean.toString(showSingularMultiplicities);
    }

    /**
     * @return Returns <code>true</code> if we show  "1" Multiplicities.
     */
    public boolean getShowSingularMultiplicitiesValue() {
        return showSingularMultiplicities;
    }

    /**
     * @param showem <code>true</code> if "1" Multiplicities are to be shown.
     */
    public void setShowSingularMultiplicities(String showem) {
        setShowSingularMultiplicities(Boolean.valueOf(showem).booleanValue());
    }

    /**
     * @param showem <code>true</code> if "1" Multiplicities are to be shown.
     */
    public void setShowSingularMultiplicities(final boolean showem) {
        if (showSingularMultiplicities == showem) return;

        Memento memento = new Memento() {
            private final ConfigurationKey key =
                Notation.KEY_SHOW_SINGULAR_MULTIPLICITIES;

            public void redo() {
                showSingularMultiplicities = showem;
                fireNotationEvent(key, !showem, showem);
            }

            public void undo() {
                showSingularMultiplicities = !showem;
                fireNotationEvent(key, showem, !showem);
            }
        };
        if (UndoManager.getInstance().isGenerateMementos()) {
            UndoManager.getInstance().addMemento(memento);
        }
        memento.redo();
        ProjectManager.getManager().setSaveEnabled(true);
    }

    /**
     * Used by "argo.tee".
     *
     * @return Returns "true" if we show the arrows when
     * both association ends of an association are navigable.
     */
    public String getHideBidirectionalArrows() {
        return Boolean.toString(hideBidirectionalArrows);
    }

    /**
     * @return Returns <code>true</code> if we show the arrows when
     * both association ends of an association are navigable.

     */
    public boolean getHideBidirectionalArrowsValue() {
        return hideBidirectionalArrows;
    }

    /**
     * @param hideem <code>true</code> if both arrows are to be shown when
     * both association ends of an association are navigable.
     */
    public void setHideBidirectionalArrows(String hideem) {
        setHideBidirectionalArrows(Boolean.valueOf(hideem).booleanValue());
    }

    /**
     * @param hideem <code>true</code> if both arrows are to be shown when
     * both association ends of an association are navigable.

     */
    public void setHideBidirectionalArrows(final boolean hideem) {
        if (hideBidirectionalArrows == hideem) return;

        Memento memento = new Memento() {
            private final ConfigurationKey key =
                Notation.KEY_HIDE_BIDIRECTIONAL_ARROWS;

            public void redo() {
                hideBidirectionalArrows = hideem;
                fireNotationEvent(key, !hideem, hideem);
            }

            public void undo() {
                hideBidirectionalArrows = !hideem;
                fireNotationEvent(key, hideem, !hideem);
            }
        };
        if (UndoManager.getInstance().isGenerateMementos()) {
            UndoManager.getInstance().addMemento(memento);
        }
        memento.redo();
        ProjectManager.getManager().setSaveEnabled(true);
    }
    
    /**
     * Used by "argo.tee".
     *
     * @return Returns the shadow width.
     */
    public String getDefaultShadowWidth() {
        return Integer.valueOf(defaultShadowWidth).toString();
    }

    /**
     * @return Returns the shadow width.
     */
    public int getDefaultShadowWidthValue() {
        return defaultShadowWidth;
    }

    /**
     * @param newWidth The Shadow Width.
     */
    public void setDefaultShadowWidth(final int newWidth) {
        if (defaultShadowWidth == newWidth) return;

        final int oldValue = defaultShadowWidth;

        Memento memento = new Memento() {
            private final ConfigurationKey key =
                Notation.KEY_DEFAULT_SHADOW_WIDTH;

            public void redo() {
                defaultShadowWidth = newWidth;
                fireNotationEvent(key, oldValue, newWidth);
            }

            public void undo() {
                defaultShadowWidth = oldValue;
                fireNotationEvent(key, newWidth, oldValue);
            }
        };
        if (UndoManager.getInstance().isGenerateMementos()) {
            UndoManager.getInstance().addMemento(memento);
        }
        memento.redo();
        ProjectManager.getManager().setSaveEnabled(true);
    }

    /**
     * @param width The shadow width to set.
     */
    public void setDefaultShadowWidth(String width) {
        setDefaultShadowWidth(Integer.parseInt(width));
    }

    /**
     * Used by "argo.tee".
     *
     * @return Returns the default stereotype view
     */
    public String getDefaultStereotypeView() {
        return Integer.valueOf(defaultStereotypeView).toString();
    }

    /**
     * @return Returns the default stereotype view
     */
    public int getDefaultStereotypeViewValue() {
        return defaultStereotypeView;
    }


    /**
     * Used by "argo.tee".
     *
     * @return the output directory name
     */
    public String getGenerationOutputDir() {
        return generationOutputDir;
    }

    /**
     * @param od the output directory name
     */
    public void setGenerationOutputDir(String od) {
        generationOutputDir = od;
        Configuration.setString(Argo.KEY_MOST_RECENT_EXPORT_DIRECTORY, od);
    }

    /**
     * @return the header comment string
     */
    public String getHeaderComment() {
        return headerComment;
    }

    /**
     * @param newView the default stereotype view
     */
    public void setDefaultStereotypeView(final int newView) {

        if (defaultStereotypeView == newView) {
            return;
        }

        final int oldValue = defaultStereotypeView;

        Memento memento = new Memento() {
            private final ConfigurationKey key =
                ProfileConfiguration.KEY_DEFAULT_STEREOTYPE_VIEW;

            public void redo() {
                defaultStereotypeView = newView;
                fireNotationEvent(key, oldValue, newView);
            }

            public void undo() {
                defaultStereotypeView = oldValue;
                fireNotationEvent(key, newView, oldValue);
            }
        };
        if (UndoManager.getInstance().isGenerateMementos()) {
            UndoManager.getInstance().addMemento(memento);
        }
        memento.redo();
        ProjectManager.getManager().setSaveEnabled(true);
    }

    /**
     * @param c the header comment string
     */
    public void setHeaderComment(String c) { headerComment = c; }


    /**
     * Convenience methods to fire notation configuration change events.
     *
     * @param key the ConfigurationKey that is related to the change
     * @param oldValue the old value
     * @param newValue the new value
     */
    private void fireNotationEvent(
            ConfigurationKey key, int oldValue, int newValue) {
        fireNotationEvent(key, Integer.toString(oldValue),
                Integer.toString(newValue));
    }

    /**
     * Convenience methods to fire notation configuration change events.
     *
     * @param key the ConfigurationKey that is related to the change
     * @param oldValue the old value
     * @param newValue the new value
     */
    private void fireNotationEvent(ConfigurationKey key, boolean oldValue,
            boolean newValue) {
        fireNotationEvent(key, Boolean.toString(oldValue),
                Boolean.toString(newValue));
    }

    /**
     * Convenience methods to fire notation configuration change events.
     *
     * @param key the ConfigurationKey that is related to the change
     * @param oldValue the old value
     * @param newValue the new value
     */
    private void fireNotationEvent(ConfigurationKey key, String oldValue,
            String newValue) {
        ArgoEventPump.fireEvent(new ArgoNotationEvent(
                ArgoEventTypes.NOTATION_CHANGED, new PropertyChangeEvent(this,
                        key.getKey(), oldValue, newValue)));
    }

    /**
     * Convenience methods to fire diagram appearance
     * configuration change events.
     *
     * @param key the ConfigurationKey that is related to the change
     * @param oldValue the old value
     * @param newValue the new value
     */
    private void fireDiagramAppearanceEvent(ConfigurationKey key, int oldValue,
            int newValue) {
        fireDiagramAppearanceEvent(key, Integer.toString(oldValue), Integer
                .toString(newValue));
    }


    /**
     * Convenience methods to fire diagram appearance
     * configuration change events.
     *
     * @param key the ConfigurationKey that is related to the change
     * @param oldValue the old value
     * @param newValue the new value
     */
    private void fireDiagramAppearanceEvent(ConfigurationKey key,
            String oldValue, String newValue) {
        ArgoEventPump.fireEvent(new ArgoDiagramAppearanceEvent(
                ArgoEventTypes.DIAGRAM_FONT_CHANGED, new PropertyChangeEvent(
                        this, key.getKey(), oldValue, newValue)));
    }

    /**
     * Diagram font name. <p>
     *
     * Used by "argo.tee".
     *
     * @return diagram font name.
     */
    public String getFontName() {
        return fontName;
    }

    /**
     * Diagram font name.
     * @param newFontName diagram font name.
     */
    public void setFontName(String newFontName) {
        String old = fontName;
        fontName = newFontName;
        initFonts();

        fireDiagramAppearanceEvent(DiagramAppearance.KEY_FONT_NAME, old,
                this.fontName);
    }

    /**
     * Diagram font size. <p>
     *
     * Used by "argo.tee".
     *
     * @return diagram font size.
     */
    public int getFontSize() {
        return fontSize;
    }

    /**
     * Diagram font size.
     * @param newFontSize diagram font size.
     */
    public void setFontSize(int newFontSize) {
        int old = fontSize;
        fontSize = newFontSize;
        initFonts();

        fireDiagramAppearanceEvent(DiagramAppearance.KEY_FONT_SIZE, old,
                this.fontSize);
    }

    private void initFonts() {
        fontPlain = new Font(fontName, Font.PLAIN, fontSize);
        fontItalic = new Font(fontName, Font.ITALIC, fontSize + 2);
        fontBold = new Font(fontName, Font.BOLD, fontSize + 2);
        fontBoldItalic = new Font(fontName,
                Font.BOLD | Font.ITALIC, fontSize + 2);
    }

    /**
     * Returns the Plain diagram font which corresponds
     * to selected parameters.
     *
     * @return plain diagram font
     */
    public Font getFontPlain() {
        return fontPlain;
    }

    /**
     * Returns the Italic diagram font which corresponds
     * to selected parameters.
     *
     * @return italic diagram font
     */
    public Font getFontItalic() {
        return fontItalic;
    }

    /**
     * Returns the Bold diagram font which corresponds
     * to selected parameters.
     *
     * @return bold diagram font
     */
    public Font getFontBold() {
        return fontBold;
    }

    /**
     * Returns the Bold-Italic diagram font which corresponds
     * to selected parameters.
     *
     * @return bold-italic diagram font
     */
    public Font getFontBoldItalic() {
        return fontBoldItalic;
    }

    /**
     * Utility function to convert a font style integer into a Font.
     *
     * @param fontStyle the style; see the predefined constants in Font
     * @return the Font that corresponds to the style
     */
    public Font getFont(int fontStyle) {
        if ((fontStyle & Font.ITALIC) != 0) {
            if ((fontStyle & Font.BOLD) != 0) {
                return fontBoldItalic;
            } else {
                return fontItalic;
            }
        } else {
            if ((fontStyle & Font.BOLD) != 0) {
                return fontBold;
            } else {
                return fontPlain;
            }
        }
    }
}
