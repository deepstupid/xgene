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
package br.ufpr.gres.selection.strategy;

import br.ufpr.gres.core.MutationDetails;
import br.ufpr.gres.selection.AbstractStrategy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * First To Last strategy
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 */
public class First2LastStrategy extends AbstractStrategy {

    public First2LastStrategy(ArrayList<MutationDetails> list) {
        super(list);
    }

    @Override
    public List<MutationDetails> get() {
        ArrayList<MutationDetails> result = new ArrayList<>();

        int numSelection = getRandomNumberSelection();

        ArrayList<MutationDetails> itemsAvailable = new ArrayList(this.listStrategy);

        for (int i = 0; i < numSelection; i++) {
            MutationDetails mutationDetails = null;

            if (itemsAvailable.isEmpty()) {
                itemsAvailable = new ArrayList(this.originalList);

                itemsAvailable.removeAll(result);
            }

            if ((i % 2) == 0) {
                // get the first from list
                mutationDetails = itemsAvailable.get(0);
            } else {
                // get the last from list
                mutationDetails = itemsAvailable.get(itemsAvailable.size() - 1);
            }

            itemsAvailable.remove(mutationDetails);

            result.add(mutationDetails);
        }

        updateListStrategy(result);
        result.sort(getComparator());

        return result;
    }

    @Override
    public String toString() {
        return "First2LastStrategy";
    }
}
