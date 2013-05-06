/**
 * Copyright 2013 freiheit.com technologies gmbh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.freiheit.sqlapi.test.hsql;

import java.sql.Connection;
import java.sql.SQLException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.freiheit.sqlapi.query.InsertCommand;
import com.freiheit.sqlapi.query.SelectResult;
import com.freiheit.sqlapi.query.SqlCommand;
import com.freiheit.sqlapi.query.UpdateCommand;
import com.freiheit.sqlapi.query.statements.InsertStatement;
import com.freiheit.sqlapi.query.statements.SelectStatement;
import com.freiheit.sqlapi.test.hsql.TestDb.Address;
import com.freiheit.sqlapi.test.hsql.TestDb.Country;
import com.freiheit.sqlapi.test.hsql.TestDb.DbOperation;
import com.freiheit.sqlapi.test.hsql.TestDb.Person;

public class InsertUpdateTest extends TestBase {

	private static Object[][] RESULT_INSERT= new Object[][] { new Object[] { "Hamburg", "Strassenbahnring", Country.DE } };
	private static Object[][] RESULT_UPDATE_1= new Object[][] { new Object[] { "Hamburg", "Lehmweg", 1, Country.DE } };
	private static Object[][] RESULT_UPDATE_2= new Object[][] { new Object[] { "Hamburg", "Lehmweg", 2, Country.DE } };
	private static Object[][] RESULT_MULTI_INSERT= new Object[][] { new Object[] { 3l, "Hamburg", "Heimweg", 1, "20148", Country.DE }, new Object[] { 4l, "Hamburg", "Weidenallee", 3, "20357", Country.DE }, new Object[] { 5l, "Kiel", "Wiesenweg", 1, "24106", Country.DE },
			new Object[] { 6l, "Kiel", "Bismarkallee", 15, "24105", Country.DE } };

	@Test
	public void testInsert() {
		final InsertCommand cmd= insertAddress( 1l, "Hamburg", "Strassenbahnring", 22, "20251", Country.DE);

		final SqlCommand<SelectStatement> query= SQL.select( Address.CITY, Address.STREET, Address.COUNTRY).from( Address.TABLE).where( Address.ID.eq( 1l));

		TestDb.INSTANCE.executeSingle( new DbOperation<Void>() {
			@Override
			public Void execute( final Connection c) throws SQLException {
				final int insertCount= executeInsert(c, cmd);
				Assert.assertEquals( insertCount, 1, "Insert Count");
				final SelectResult res= executeQuery(c, query);
				assertResultSet( res, RESULT_INSERT, "inserted row");
				return null;
			}
		});
	}

	@Test
	public void testUpdate() {
		final InsertCommand cmd= insertAddress( 2l, "Hamburg", "Lehmweg", 1, "20251", Country.DE);

		final SqlCommand<SelectStatement> query= SQL.select( Address.CITY, Address.STREET, Address.NUMBER, Address.COUNTRY).from( Address.TABLE).where( Address.ID.eq( 2l));

		final UpdateCommand update= SQL.update( Address.TABLE).values( Address.NUMBER.set( 2)).where( Address.ID.eq( 2l));

		TestDb.INSTANCE.executeSingle( new DbOperation<Void>() {
			@Override
			public Void execute( final Connection c) throws SQLException {
				final int insertCount= executeInsert(c, cmd);
				Assert.assertEquals( insertCount, 1, "Insert Count");

				final SelectResult res1= executeQuery(c, query);
				assertResultSet( res1, RESULT_UPDATE_1, "before update");

				final int updateCount= executeUpdate(c, update);
				Assert.assertEquals( updateCount, 1, "Update Count");

				final SelectResult res2= executeQuery(c, query);
				assertResultSet( res2, RESULT_UPDATE_2, "after update");

				return null;
			}
		});
	}

	@Test
	public void testMultiInsert() {
		final InsertCommand cmd= insertAddress( 3l, "Hamburg", "Heimweg", 1, "20148", Country.DE);

		addInsertAddress( cmd, 4l, "Hamburg", "Weidenallee", 3, "20357", Country.DE);
		addInsertAddress( cmd, 5l, "Kiel", "Wiesenweg", 1, "24106", Country.DE);
		addInsertAddress( cmd, 6l, "Kiel", "Bismarkallee", 15, "24105", Country.DE);

		final SqlCommand<SelectStatement> query= SQL.select( Address.TABLE.getColumns()).from( Address.TABLE).where( Address.ID.in( 3l, 4l, 5l, 6l)).orderBy( Address.ID);

		TestDb.INSTANCE.executeSingle( new DbOperation<Void>() {
			@Override
			public Void execute( final Connection c) throws SQLException {
				final int insertCount = executeInsert(c, cmd);
				Assert.assertEquals( insertCount, 4, "Insert Count");
				final SelectResult res= executeQuery(c, query);
				assertResultSet( res, RESULT_MULTI_INSERT, "inserted row");
				return null;
			}
		});
	}

	@Test
	public void testInsertSelect() {
		final SqlCommand<InsertStatement> insert= SQL.insert( Address.TABLE).values( Address.ID.set( 20l), Address.CITY.set( "HH"), Address.STREET.set( Person.NAME), Address.NUMBER.set( Person.HEIGTH), Address.ZIP.set( "08150"), Address.COUNTRY.set( Country.DE)).from( Person.TABLE)
				.where( Person.ID.eq( 2l));

		System.out.println( insert);
		TestDb.INSTANCE.executeSingle( new DbOperation<Void>() {
			@Override
			public Void execute( final Connection c) throws SQLException {
				final int insertCount= executeInsert(c, insert);
				Assert.assertEquals( insertCount, 1, "Insert-Select Count");
				return null;
			}
		});
	}

	private static InsertCommand insertAddress( final long id, final String city, final String street, final int num, final String zip, final Country country) {
		return SQL.insert( Address.TABLE).values( Address.ID.set( id), Address.CITY.set( city), Address.STREET.set( street), Address.NUMBER.set( num), Address.ZIP.set( zip), Address.COUNTRY.set( country));
	}

	private static InsertCommand addInsertAddress( final InsertCommand insert, final long id, final String city, final String street, final int num, final String zip, final Country country) {
		return insert.values( Address.ID.set( id), Address.CITY.set( city), Address.STREET.set( street), Address.NUMBER.set( num), Address.ZIP.set( zip), Address.COUNTRY.set( country));
	}
}
