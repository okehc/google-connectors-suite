package org.mule.module.google.spreadsheet.automation.testcases;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.api.MuleEvent;
import org.mule.api.processor.MessageProcessor;
import org.mule.module.google.spreadsheet.model.Cell;
import org.mule.module.google.spreadsheet.model.Row;
import org.mule.module.google.spreadsheet.model.Worksheet;

public class PurgeWorksheetTestCases extends GoogleSpreadsheetsTestParent {
	
	@Before
	public void setUp() {
		try {
			testObjects = (Map<String, Object>) context.getBean("purgeWorksheet");
			
			String spreadsheetTitle = (String) testObjects.get("spreadsheet");
			createSpreadsheet(spreadsheetTitle);

			String title = (String) testObjects.get("title");
			int rowCount = (Integer) testObjects.get("rowCount");
			int colCount = (Integer) testObjects.get("colCount");
			
			Worksheet worksheet = createWorksheet(spreadsheetTitle, title, rowCount, colCount);
			testObjects.put("worksheetObject", worksheet);			
		}
		catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Category({RegressionTests.class})
	@Test
	public void testPurgeWorksheet() {
		try {
			String spreadsheetTitle = (String) testObjects.get("spreadsheet");
			Worksheet worksheet = (Worksheet) testObjects.get("worksheetObject");
			testObjects.put("worksheet", worksheet.getTitle());
			
			MessageProcessor flow = lookupFlowConstruct("purge-worksheet");
			MuleEvent response = flow.process(getTestEvent(testObjects));
			
			List<Row> allRows = getAllCells(spreadsheetTitle, worksheet.getTitle());
			assertTrue(allRows.isEmpty());
		}
		catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@After
	public void tearDown() {
		try {
			String spreadsheet = (String) testObjects.get("spreadsheet");
			deleteSpreadsheet(spreadsheet);
		}
		catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}
