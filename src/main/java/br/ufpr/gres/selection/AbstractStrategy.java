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
import br.ufpr.gres.util.comparator.AlphanumComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 *
 * @author Jackson Antonio do Prado Lima <jacksonpradolima at gmail.com>
 * @version 1.0
 */
public class AbstractStrategy implements ISelectionStrategy {

    /**
     * Pattern order comparator
     */
    protected final Comparator comparatorAlphanum;

    /**
     * Maximum selection
     */
    protected int maxSelection;

    /**
     * Minimum selection
     */
    protected int minSelection;

    protected final JMetalRandom randomGenerator;

    /**
     * The original list
     */
    protected ArrayList<MutationDetails> originalList;

    /**
     * The items from which the strategy can choose from and remove
     */
    protected final ArrayList<MutationDetails> listStrategy;

    /**
     * The items were selected by the strategy
     */
    protected Map<MutationIdentifier, MutationDetails> selectedItems;

    /**
     * Items ignored by the strategy
     */
    protected Map<MutationIdentifier, MutationDetails> ignoredItems;

    public AbstractStrategy(ArrayList<MutationDetails> list) {
        this.comparatorAlphanum = new AlphanumComparator();
        this.originalList = new ArrayList<>(list);
        this.listStrategy = new ArrayList<>(list);
        this.randomGenerator = JMetalRandom.getInstance();

        this.maxSelection = 2;
        this.minSelection = 2;

        this.ignoredItems = new HashMap<>();
        this.selectedItems = new HashMap<>();

        this.originalList.sort(this.comparatorAlphanum);
        this.listStrategy.sort(this.comparatorAlphanum);
    }

    @Override
    public boolean allItemsSelected() {
        return !this.listStrategy.isEmpty();
    }

    @Override
    public MutationDetails get(MutationIdentifier id) {
        return this.selectedItems.get(id);
    }

    @Override
    public List<MutationDetails> get() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected Comparator getComparator() {
        return this.comparatorAlphanum;
    }

    @Override
    public boolean contains(MutationIdentifier id) {
        return this.selectedItems.containsKey(id);
    }

    @Override
    public List<MutationDetails> getItemsIgnored() {
        return new ArrayList<>(this.ignoredItems.values());
    }

    /**
     * Get a random order between the maximum and minimum order with uniform probability
     *
     * @return
     */
    protected int getRandomNumberSelection() {
        if (minSelection == maxSelection) {
            return maxSelection;
        }

        return randomGenerator.nextInt(minSelection, maxSelection);
    }

    @Override
    public void setMaxSelection(int num) {
        this.maxSelection = num;
    }

    @Override
    public void setMinSelection(int num) {
        this.minSelection = num;
    }

    /**
     * Reset the strategy
     */
    @Override
    public void reset() {

        // By default the initial mutants are the second order mutants
        this.maxSelection = 2;
        this.minSelection = 2;

        this.ignoredItems = new HashMap<>();
        this.selectedItems = new HashMap<>();

        this.originalList = new ArrayList<>(this.originalList);
        this.originalList.sort(this.comparatorAlphanum);
    }

    @Override
    public void remove(MutationIdentifier id) {
        this.selectedItems.remove(id);
    }

    @Override
    public void update(MutationDetails t) {
        this.selectedItems.put(t.getId(), t);
    }

    @Override
    public void updateIgnoreItem(MutationDetails t) {
        this.ignoredItems.put(t.getId(), t);
    }

    @Override
    public void updateListStrategy(MutationDetails t) {
        this.listStrategy.remove(t);
    }
    
     @Override
    public void updateListStrategy(List<MutationDetails> t) {
         for (MutationDetails mutationDetails : t) {
             updateListStrategy(mutationDetails);
         }
    }
}
