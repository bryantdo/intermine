package org.intermine.web.config;

/*
 * Copyright (C) 2002-2004 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Configuration object for displaying a class
 *
 * @author Andrew Varley
 */
public class Type
{
    // if fieldName is null it's ignored and the webapp will use 
    private String fieldName;
    private String className;
    private List shortDisplayers = new ArrayList();
    private List longDisplayers = new ArrayList();

    /**
     * Set the fully-qualified class name for this Type
     *
     * @param name the name of the Type
     */
    public void setClassName(String name) {
        this.className = className;
    }

    /**
     * Get the class name
     *
     * @return the name
     */
    public String getClassName() {
        return this.className;
    }

    /**
     * Set the field name
     *
     * @param name the field name of the Type
     */
    public void setFieldName(String name) {
        this.fieldName = fieldName;
    }

    /**
     * Get the field name
     *
     * @return the field name
     */
    public String getFieldName() {
        return this.fieldName;
    }

    /**
     * Add a short displayer for this Type
     *
     * @param disp the Displayer to add
     */
    public void addShortDisplayer(Displayer disp) {
        shortDisplayers.add(disp);
    }

    /**
     * Get the List of short Displayers
     *
     * @return the List of short Displayers
     */
    public List getShortDisplayers() {
        return this.shortDisplayers;
    }

   /**
     * Add a long displayer for this Type
     *
     * @param disp the Displayer to add
     */
    public void addLongDisplayer(Displayer disp) {
        longDisplayers.add(disp);
    }

    /**
     * Get the List of long Displayers
     *
     * @return the List of long Displayers
     */
    public List getLongDisplayers() {
        return this.longDisplayers;
    }

    /**
     * @see Object#equals
     *
     * @param obj the Object to compare with
     * @return true if this is equal to obj
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof Type)) {
            return false;
        }

        return shortDisplayers.equals(((Type) obj).shortDisplayers)
            && longDisplayers.equals(((Type) obj).longDisplayers);
    }

    /**
     * @see Object#hashCode
     *
     * @return the hashCode for this WebConfig object
     */
    public int hashCode() {
        return shortDisplayers.hashCode() + 3 * longDisplayers.hashCode();
    }

    /**
     * Return an XML String of this Type object
     *
     * @return a String version of this WebConfig object
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<class name=\"" + className + "\"");
        if (fieldName != null) {
            sb.append(" fieldName=\"" + fieldName + "\"");
        }
        sb.append(">");
        sb.append("<shortdisplayers>");
        Iterator iter = shortDisplayers.iterator();
        while (iter.hasNext()) {
            sb.append(iter.next().toString());
        }
        sb.append("</shortdisplayers>");
        sb.append("<longdisplayers>");
        iter = longDisplayers.iterator();
        while (iter.hasNext()) {
            sb.append(iter.next().toString());
        }
        sb.append("</longdisplayers>");
        sb.append("</class>");

        return sb.toString();
    }


}

