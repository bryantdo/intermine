package org.flymine.web.results;

/*
 * Copyright (C) 2002-2003 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;


import servletunit.struts.MockStrutsTestCase;
import org.apache.struts.tiles.ComponentContext;

import org.flymine.objectstore.ObjectStore;
import org.flymine.objectstore.ObjectStoreFactory;
import org.flymine.objectstore.dummy.ObjectStoreDummyImpl;
import org.flymine.objectstore.query.Query;
import org.flymine.objectstore.query.fql.FqlQuery;
import org.flymine.objectstore.query.Results;
import org.flymine.objectstore.query.ResultsRow;

import org.flymine.model.testmodel.*;

public class ChangeResultsSizeActionTest extends MockStrutsTestCase
{
    public ChangeResultsSizeActionTest(String arg1) {
        super(arg1);
    }

    private Results results;
    private DisplayableResults dr;

    private Company company1, company2, company3;
    private Department department1, department2, department3;

    public void setUp() throws Exception {
        super.setUp();
        ObjectStoreDummyImpl os = new ObjectStoreDummyImpl();
        os.setResultsSize(15);
        FqlQuery fq = new FqlQuery("select c, d from Company as c, Department as d", "org.flymine.model.testmodel");
        results = os.execute(fq.toQuery());
        dr = new DisplayableResults(results);
        dr.setPageSize(5);

        // Set up some known objects in the first 3 results rows
        company1 = new Company();
        company1.setName("Company1");
        company2 = new Company();
        company2.setName("Company2");
        company3 = new Company();
        company3.setName("Company3");
        department1 = new Department();
        department1.setName("Department1");
        department2 = new Department();
        department2.setName("Department2");
        department3 = new Department();
        department3.setName("Department3");

        ResultsRow row = new ResultsRow();
        row.add(company1);
        row.add(department1);
        os.addRow(row);
        row = new ResultsRow();
        row.add(company2);
        row.add(department2);
        os.addRow(row);
        row = new ResultsRow();
        row.add(company3);
        row.add(department3);
        os.addRow(row);

    }


    public void testChangePageSize1() throws Exception {
        setRequestPathInfo("/changeResultsSize");
        addRequestParameter("pageSize","25");
        addRequestParameter("action", "Change");

        getSession().setAttribute("results", results);
        getSession().setAttribute("resultsTable", dr);

        dr.setStart(0);
        actionPerform();

        verifyForward("results");
        verifyNoActionErrors();
        assertEquals(0, dr.getStart());
        assertEquals(25, dr.getPageSize());
    }

    public void testChangePageSize2() throws Exception {
        setRequestPathInfo("/changeResultsSize");
        addRequestParameter("pageSize","10");
        addRequestParameter("action", "Change");

        getSession().setAttribute("results", results);
        getSession().setAttribute("resultsTable", dr);

        dr.setStart(12);
        actionPerform();

        verifyForward("results");
        verifyNoActionErrors();
        assertEquals(10, dr.getStart());
        assertEquals(10, dr.getPageSize());
    }

    public void testSaveNewBag() throws Exception {
        setRequestPathInfo("/changeResultsSize");
        // selectedObjects format: column,row
        addRequestParameter("selectedObjects", new String[] {"0,0", "1,2"});
        addRequestParameter("newBagName","testBag1");
        addRequestParameter("action", "Save selections in new collection");
        getSession().setAttribute("results", results);
        getSession().setAttribute("savedBags", new HashMap());

        actionPerform();
        verifyForward("results");
        verifyNoActionErrors();

        Map savedBags = (Map) getSession().getAttribute("savedBags");
        Collection objs = (Collection) savedBags.get("testBag1");

        assertEquals(2, objs.size());
        Iterator iter = objs.iterator();
        assertEquals(company1, iter.next());
        assertEquals(department3, iter.next());

    }

    public void testAddToExistingBag() throws Exception {
        setRequestPathInfo("/changeResultsSize");
        // selectedObjects format: column,row
        addRequestParameter("selectedObjects", new String[] {"0,1", "1,1"});
        addRequestParameter("bagName","testBag1");
        addRequestParameter("action", "Add selections to existing collection");
        getSession().setAttribute("results", results);

        Map savedBags = new HashMap();
        getSession().setAttribute("savedBags", savedBags);

        Collection objs = new LinkedHashSet();
        objs.add(company1);
        objs.add(department3);

        savedBags.put("testBag1", objs);

        actionPerform();
        verifyForward("results");
        verifyNoActionErrors();

        savedBags = (Map) getSession().getAttribute("savedBags");
        objs = (Collection) savedBags.get("testBag1");

        assertEquals(4, objs.size());
        Iterator iter = objs.iterator();
        assertEquals(company1, iter.next());
        assertEquals(department3, iter.next());
        assertEquals(company2, iter.next());
        assertEquals(department2, iter.next());

    }

    public void testAddSameToExistingBag() throws Exception {
        setRequestPathInfo("/changeResultsSize");
        // selectedObjects format: column,row
        addRequestParameter("selectedObjects", new String[] {"0,1", "1,1"});
        addRequestParameter("bagName","testBag1");
        addRequestParameter("action", "Add selections to existing collection");
        getSession().setAttribute("results", results);

        Map savedBags = new HashMap();
        getSession().setAttribute("savedBags", savedBags);

        Collection objs = new LinkedHashSet();
        objs.add(company1);
        objs.add(department2);

        savedBags.put("testBag1", objs);

        actionPerform();
        verifyForward("results");
        verifyNoActionErrors();

        savedBags = (Map) getSession().getAttribute("savedBags");
        objs = (Collection) savedBags.get("testBag1");

        assertEquals(3, objs.size());
        Iterator iter = objs.iterator();
        assertEquals(company1, iter.next());
        assertEquals(department2, iter.next());
        assertEquals(company2, iter.next());
    }

}
