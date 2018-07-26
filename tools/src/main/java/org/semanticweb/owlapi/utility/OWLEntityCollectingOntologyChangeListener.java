/* This file is part of the OWL API.
 * The contents of this file are subject to the LGPL License, Version 3.0.
 * Copyright 2014, The University of Manchester
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 * Alternatively, the contents of this file may be used under the terms of the Apache License, Version 2.0 in which case, the provisions of the Apache License Version 2.0 are applicable instead of those above.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License. */
package org.semanticweb.owlapi.utility;

import static org.semanticweb.owlapi.util.OWLAPIStreamUtils.asList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.OWLAxiomChange;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyChangeListener;

/**
 * A convenience class which is an ontology change listener which collects the entities which are
 * referenced in a set of ontology changes.
 *
 * @author Matthew Horridge, The University Of Manchester, Bio-Health Informatics Group
 * @since 2.0.0
 */
public abstract class OWLEntityCollectingOntologyChangeListener
    implements OWLOntologyChangeListener {

    private final Set<OWLEntity> entities = new HashSet<>();

    @Override
    public void ontologiesChanged(@Nonnull List<? extends OWLOntologyChange> changes) {
        entities.clear();
        for (OWLOntologyChange change : changes) {
            if (change.isAxiomChange()) {
                entities.addAll(asList(((OWLAxiomChange) change).signature()));
            }
        }
        ontologiesChanged();
    }

    /**
     * Called when a set of changes have been applied.
     */
    public abstract void ontologiesChanged();

    /**
     * @return the entities which were referenced in the last change set.
     */
    public Set<OWLEntity> getEntities() {
        return CollectionFactory.copyMutable(entities);
    }
}