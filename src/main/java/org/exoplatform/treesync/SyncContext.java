/*
 * Copyright (C) 2010 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.exoplatform.treesync;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class SyncContext<N, H> {

   /** . */
   final SyncModel<N, H> model;

   /** . */
   final N root;

   public SyncContext(SyncModel<N, H> model, N root) throws NullPointerException {
      if (model == null) {
         throw new NullPointerException();
      }
      if (root == null) {
         throw new NullPointerException();
      }

      this.model = model;
      this.root = root;
   }

   public SyncModel<N, H> getModel() {
      return model;
   }

   public N getRoot() {
      return root;
   }

   public N findById(String id) {
      return findById(root, id);
   }

   private N findById(N node, String id) {
      N found;
      if (model.getId(node).equals(id)) {
         found = node;
      } else {
         found = null;
         for (H handle : model.getChildren(node)) {
            N child = model.getChild(node, handle);
            found = findById(child, id);
            if (found != null) {
               break;
            }
         }
      }
      return found;
   }

}
