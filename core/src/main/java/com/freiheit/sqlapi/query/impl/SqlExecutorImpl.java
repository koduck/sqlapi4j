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
package com.freiheit.sqlapi.query.impl;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.Nonnull;

import com.freiheit.sqlapi.generate.SqlDialect;
import com.freiheit.sqlapi.generate.SqlGenerator;
import com.freiheit.sqlapi.generate.SqlQueryType;
import com.freiheit.sqlapi.generate.impl.SqlGeneratorImpl;
import com.freiheit.sqlapi.query.SelectListItem;
import com.freiheit.sqlapi.query.SelectResult;
import com.freiheit.sqlapi.query.SqlExecutor;
import com.freiheit.sqlapi.query.statements.CreateTableStatement;
import com.freiheit.sqlapi.query.statements.DeleteStatement;
import com.freiheit.sqlapi.query.statements.InsertStatement;
import com.freiheit.sqlapi.query.statements.SelectSequenceStatement;
import com.freiheit.sqlapi.query.statements.SelectStatement;
import com.freiheit.sqlapi.query.statements.UpdateStatement;

public class SqlExecutorImpl implements SqlExecutor {

    private final InsertStatementExecutor _insertStatementExecutor;
    private final UpdateStatementExecutor _updateStatementExecutor;
    private final DeleteStatementExecutor _deleteStatementExecutor;
    private final SelectStatementExecutor _selectStatementExecutor;
    private final SelectSequenceStatementExecutor _selectSequenceExecutor;
    private final CreateTableStatementExecutor _createTableExecutor;

	/**
	 * Standard constructor to create a QueryBuilder.
	 */
	public SqlExecutorImpl( final SqlDialect dialect) {
		this( dialect, new SqlGeneratorImpl(), SqlQueryType.PREPARED);
	}

	private SqlExecutorImpl( final SqlDialect dialect, final SqlGenerator generator, final SqlQueryType queryType) {
		_insertStatementExecutor = new InsertStatementExecutor(queryType, dialect, generator);
		_updateStatementExecutor = new UpdateStatementExecutor(queryType, dialect, generator);
		_deleteStatementExecutor = new DeleteStatementExecutor(queryType, dialect, generator);
		_selectStatementExecutor = new SelectStatementExecutor(queryType, dialect, generator);
		_selectSequenceExecutor = new SelectSequenceStatementExecutor(dialect, generator);
		_createTableExecutor = new CreateTableStatementExecutor(dialect, generator);
	}

	@Override
	public int execute(@Nonnull final Connection connection, @Nonnull final DeleteStatement query) throws SQLException {
	    return _deleteStatementExecutor.execute(connection, query);
	}

	@Override
	public int execute(@Nonnull final Connection connection, @Nonnull final InsertStatement statement) throws SQLException {
	    return _insertStatementExecutor.execute(connection, statement);
	}

	@Override
	public int execute(@Nonnull final Connection connection, @Nonnull final UpdateStatement statement) throws SQLException {
	    return _updateStatementExecutor.execute(connection, statement);
	}

	@Override
	public SelectResult execute(@Nonnull final Connection connection, @Nonnull final SelectStatement statement) throws SQLException {
	    return _selectStatementExecutor.execute(connection, statement);
	}

	@Override
	public long execute(@Nonnull final Connection connection, @Nonnull final SelectSequenceStatement statement) throws SQLException {
	    return _selectSequenceExecutor.execute(connection, statement);
	}

	@Override
	public <T> T execute(@Nonnull final Connection connection, @Nonnull final SelectSequenceStatement statement, final SelectListItem<T> targetColumn)
	        throws SQLException {
	    return _selectSequenceExecutor.execute(connection, statement, targetColumn);
	}

	@Override
	public void execute(@Nonnull final Connection connection, @Nonnull final CreateTableStatement statement) throws SQLException {
	    _createTableExecutor.execute(connection, statement);
	}

    @Override
    public String render(@Nonnull final CreateTableStatement statement) throws SQLException {
        return _createTableExecutor.toString(statement);
    }

    @Override
    public String render(@Nonnull final DeleteStatement statement) {
        return _deleteStatementExecutor.toString(statement);
    }

    @Override
    public String render(@Nonnull final SelectSequenceStatement statement) throws SQLException {
        return _selectSequenceExecutor.toString(statement);
    }

    @Override
    public String render(@Nonnull final InsertStatement statement) throws SQLException {
        return _insertStatementExecutor.toString(statement);
    }

    @Override
    public String render(@Nonnull final SelectStatement statement) {
        return _selectStatementExecutor.toString(statement);
    }

    @Override
    public String render(@Nonnull final UpdateStatement statement) {
        return _updateStatementExecutor.toString(statement);
    }
}
