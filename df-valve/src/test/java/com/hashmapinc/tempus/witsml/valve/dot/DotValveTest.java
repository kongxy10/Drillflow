/**
 * Copyright © 2018-2018 Hashmap, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hashmapinc.tempus.witsml.valve.dot;

import com.hashmapinc.tempus.WitsmlObjects.AbstractWitsmlObject;
import com.hashmapinc.tempus.WitsmlObjects.v1311.ObjWell;
import com.hashmapinc.tempus.WitsmlObjects.v1311.ObjWellbore;
import com.hashmapinc.tempus.witsml.QueryContext;
import com.hashmapinc.tempus.witsml.valve.ValveAuthException;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mockito;

import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

public class DotValveTest {
	private DotClient mockClient;
    private DotDelegator mockDelegator;
    private DotValve valve;

	@Before
	public void doSetup() {
		this.mockClient = Mockito.mock(DotClient.class);
		this.mockDelegator = Mockito.mock(DotDelegator.class);
		this.valve = new DotValve(this.mockClient, this.mockDelegator); // inject mocks into valve
	}

	@Test
	public void shouldGetName() {
		assertEquals("DoT", this.valve.getName());
	}

	@Test
	public void shouldGetDescription() {
		assertEquals(
			"Valve for interaction with Drillops Town",
			this.valve.getDescription()
		);
	}

	@Test
	public void shouldGetSingleObject() throws Exception {
		// build well list
		ArrayList<AbstractWitsmlObject> witsmlObjects;
		witsmlObjects = new ArrayList<>();
		ObjWell well = new ObjWell();
		well.setName("well-1");
		well.setUid("well-1");
		witsmlObjects.add(well);

		// build query context
		QueryContext qc = new QueryContext(
			"1.3.1.1",
			"well",
			null,
			"",
			witsmlObjects,
			"goodUsername",
			"goodPassword",
			"shouldGetSingleObject" // exchange ID
		);

		// mock delegator behavior
		when(
			this.mockDelegator.getObject(well, qc.USERNAME, qc.PASSWORD, this.mockClient)
		).thenReturn(well);

		// test getObject
		String expected = well.getXMLString("1.3.1.1");
		String actual = this.valve.getObject(qc);
		assertEquals(expected, actual);
	}

	@Test
	public void shouldGetPluralObject() throws Exception {
		// build witsmlObjects list
		ArrayList<AbstractWitsmlObject> witsmlObjects;
		witsmlObjects = new ArrayList<>();

		ObjWellbore wellboreA = new ObjWellbore();
		wellboreA.setName("wellbore-A");
		wellboreA.setUid("wellbore-A");
		witsmlObjects.add(wellboreA);

		ObjWellbore wellboreB = new ObjWellbore();
		wellboreB.setName("wellbore-B");
		wellboreB.setUid("wellbore-B");
		witsmlObjects.add(wellboreB);


		// build query context
		QueryContext qc = new QueryContext(
			"1.3.1.1",
			"wellbore",
			null,
			"",
			witsmlObjects,
			"goodUsername",
			"goodPassword",
			"shouldGetPluralObject" // exchange ID
		);


		// mock delegator behavior
		when(
			this.mockDelegator.getObject(wellboreA, qc.USERNAME, qc.PASSWORD, this.mockClient)
		).thenReturn(wellboreA);
		when(
			this.mockDelegator.getObject(wellboreB, qc.USERNAME, qc.PASSWORD, this.mockClient)
		).thenReturn(wellboreB);


		// test getObject
		String expected = // expected = merge wellboreA and wellbore B
			wellboreA.getXMLString("1.3.1.1").replace("</wellbores>", "") +
			wellboreB.getXMLString("1.3.1.1").replace(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
				"<wellbores version=\"1.3.1.1\" xmlns=\"http://www.witsml.org/schemas/131\">",
				""
			);
		String actual = this.valve.getObject(qc);
		assertEquals(expected, actual);
	}

	@Test
	public void shouldCreateSingleObject() {
	}

	@Test
	public void shouldCreatePluralObject() {
	}

	@Test
	public void shouldDeleteSingleObject() {
	}

	@Test
	public void shouldDeletePluralObject() {
	}

	@Test
	public void shouldUpdateObject() {
	}

	@Test
	public void shouldSucceedAuthenticate() {
	}

	@Test(expected = ValveAuthException.class)
	public void shouldFailAuthenticate() throws ValveAuthException {
		// add mock behavior
		when(this.mockClient.getJWT("badUsername", "badPassword"))
			.thenThrow(new ValveAuthException(""));
		valve.authenticate("badUsername", "badPassword");
	}

	@Test
	public void shouldGetCap() {
	}
}








