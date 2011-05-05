package com.dineshv.XmlDoclet.test;

import java.util.List;

/**
 * An abstract class.
 *
 * @Author: dineshv
 * Date: 4/26/11
 * Time: 10:23 AM
 *
 * Copyright (c) 2011, Dinesh Visweswaraiah. dinesh (at) dineshv com
  *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
public abstract class AbstractClass implements anInterface {

    /**
     * anAbstractMethod - an abstract method to be implemented by sub classes.
     * @param iParam
     */
    public abstract void anAbstractMethod(int iParam);

    /**
     * Implements the method sampleMethod from the interface.
     */
    @Override
    public void sampleMethod() {

    }

    /**
     * takesArray method takes an array of String
     * @param a
     */
    public void takesArray(String[] a) {}

    /**
     * returnsArray method takes a 2 dimension array of String and returns an array of string
     * @param a
     * @return an Array of String.
     */
    public String[] returnsArray(String[][] a) { return a[0];}

    /**
     * returns a parametrized Type
     * @param a
     * @return
     */
    public List<anInterface> constructAList(String[][] a) { return null;}
}
