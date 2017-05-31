/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package research.mpl.backend.todo;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class DefaultToDoService implements ToDoService {

    private static final Logger logger = Logger
            .getLogger(DefaultToDoService.class.getName());

    @Inject
    private ToDoItemRepository repository;

    @Override
    public ToDoItem addToDoItem(String username, ToDoItem item) {
        item.setUsername(username);

        logger.log(Level.INFO, "Adding item: {0}", item);

        return repository.create(item);
    }

    @Override
    public void updateToDoItem(String username, ToDoItem item) {
        item.setUsername(username);

        logger.log(Level.INFO, "Updating item: {0}", item);

        repository.update(item);
    }

    @Override
    public void removeToDoItem(String username, Long id) {
        ToDoItem item = repository.find(id);

        logger.log(Level.INFO, "Removing item: {0}", item);

        repository.delete(item);
    }

    @Override
    public List<ToDoItem> findToDoItemsByUsername(String username) {
        logger.log(Level.INFO, "Getting all items for: {0}", username);

        return repository.findByUsername(username);
    }

    @Override
    public List<ToDoItem> findAllToDoItems() {
        logger.log(Level.INFO, "Getting all items for all users");

        return repository.findAll();
    }

    public void teste(){
//        EntityDAO dao = new EntityDAO();
//
//        GenericEntity entity = new GenericEntity();
//        Search<GenericEntity> searchEntity = new Search<>(entity);
//
//        List<GenericEntity> testeList = dao.createPagedResultList(searchEntity, 0, 5);
//        Integer testeCount = dao.createResultCount(searchEntity);
    }
}
