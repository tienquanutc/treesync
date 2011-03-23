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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class SimpleNode {

  /** . */
  private static final AtomicInteger generator = new AtomicInteger();

  /** . */
  private SimpleNode parent;

  /** . */
  private final String id;

  /** . */
  private String state;

  /** . */
  private final List<SimpleNode> children;

  public SimpleNode() {
    this((String)null);
  }

  private SimpleNode(SimpleNode that) {

    ArrayList<SimpleNode> children = new ArrayList<SimpleNode>(that.children.size());
    for (SimpleNode thatChild : that.children) {
      SimpleNode child = new SimpleNode(thatChild);
      child.parent = this;
      children.add(child);
    }

    //
    this.id = that.id;
    this.state = that.state;
    this.children = children;
    this.parent = null;
  }

  public SimpleNode(String state)
  {
    this.id = "" + generator.incrementAndGet();
    this.children = new ArrayList<SimpleNode>();
    this.state = state;
    this.parent = null;
  }

  public String getId() {
    return id;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public SimpleNode addChild(String state) {
    SimpleNode child = new SimpleNode(state);
    children.add(child);
    child.parent = this;
    return child;
  }

  public SimpleNode getRoot() {
    return parent == null ? this : parent.getRoot();
  }

  public void addChild(SimpleNode child) {
    if (child.parent == null) {
      throw new AssertionError();
    }
    if (getRoot() != child.getRoot()) {
      throw new AssertionError();
    }
    for (Iterator<SimpleNode> i = child.parent.children.iterator();i.hasNext();) {
      SimpleNode sibling = i.next();
      if (sibling == child) {
        i.remove();
        child.parent = null;
        break;
      }
    }
    child.parent = this;
    children.add(child);
  }

  public SimpleNode getChild(String id) {
    for (SimpleNode child : children) {
      if (child.id.equals(id)) {
        return child;
      }
    }
    return null;
  }

  public void destroy() {
    if (parent != null) {
      for (Iterator<SimpleNode> i = parent.children.iterator();i.hasNext();) {
        SimpleNode sibling = i.next();
        if (sibling == this) {
          i.remove();
          parent = null;
        }
      }
    }
    for (SimpleNode child : children) {
      child.parent = null;
    }
  }

  public List<SimpleNode> getChildren() {
    return new ArrayList<SimpleNode>(children);
  }

  public SimpleNode clone() {
    return new SimpleNode(this);
  }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof SimpleNode) {
      SimpleNode that = (SimpleNode)o;
      if ((state != null && state.equals(that.state)) || (state == null && that.state == null)) {
        return children.equals(that.children);
      }
    }
    return false;
  }

  @Override
  public String toString() {
    return "SimpleNode[id=" + id + ",identity=" + System.identityHashCode(this) + "]";
  }
}
