/*
 * Copyright 2017 Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.ufpr.gres.selection;

import br.ufpr.gres.core.MutationDetails;
import br.ufpr.gres.core.MutationIdentifier;
import java.util.List;

/**
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 */
public interface ISelectionStrategy {    
    boolean contains(MutationIdentifier id);
        
    void remove(MutationIdentifier id);
    
    void reset();
    
    void update(MutationDetails t);
    void updateIgnoreItem(MutationDetails t);
    void updateListStrategy(MutationDetails t);
    void updateListStrategy(List<MutationDetails> t);
    
    void setMaxSelection(int num);
    void setMinSelection(int num);
    
    List<MutationDetails> get();
    MutationDetails get(MutationIdentifier id);
    List<MutationDetails> getItemsIgnored();
    
    boolean allItemsSelected();
}
