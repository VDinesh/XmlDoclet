package com.dineshv.XmlDoclet.test;

/**
 * This is a simple class to test Doclet.
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
public class SimpleClass extends AbstractClass {
    /**
     * Simple default constructor
     */
    public SimpleClass() {}

    /**
     * iField - Protected field
     */
    int iField;

    /**
     * fField - A public field.
     */
    public float fField;

    /**
     * bField - A private field.
     */
    private boolean bField;

    /**
     * A Static method of default visibility.
     * @param aI
     * @param aF
     * @param aB
     */
    static void aStaticMethod (int aI, float aF, boolean aB) {

    }

    /**
     * A public static method.
     * @param aI
     * @param aF
     * @param aB
     * @return
     */
    public static boolean aPublicStaticMethod (int aI, float aF, boolean aB) {
        return true;
    }

    private void aPrivateMethodThatThrowsException () throws NullPointerException {
        throw new NullPointerException("An obligatory exception thrown.");
    }

    /**
     * Implementation of an anAbstractMethod.
     * @param iParam
     */
    @Override
    public void anAbstractMethod(int iParam) {

    }
}
