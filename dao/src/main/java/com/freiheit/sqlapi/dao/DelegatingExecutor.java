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
package com.freiheit.sqlapi.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import com.freiheit.sqlapi.query.SelectListItem;
import com.freiheit.sqlapi.query.SelectResult;
import com.freiheit.sqlapi.query.SqlExecutor;
import com.freiheit.sqlapi.query.statements.CreateTableStatement;
import com.freiheit.sqlapi.query.statements.DeleteStatement;
import com.freiheit.sqlapi.query.statements.InsertStatement;
import com.freiheit.sqlapi.query.statements.SelectSequenceStatement;
import com.freiheit.sqlapi.query.statements.SelectStatement;
import com.freiheit.sqlapi.query.statements.UpdateStatement;

@ParametersAreNonnullByDefault
public class DelegatingExecutor implements SqlExecutor {

    private final SqlExecutor _executor;

    public DelegatingExecutor(final SqlExecutor executor) {
        super();
        _executor = executor;
    }

    @Override
    public void execute(@Nonnull final Connection connection, @Nonnull final CreateTableStatement statement) throws SQLException {
        _executor.execute(connection, statement);
    }

    @Override
    public int execute(@Nonnull final Connection connection, @Nonnull final DeleteStatement statement) throws SQLException {
        return _executor.execute(connection, statement);
    }

    @Override
    public int execute(@Nonnull final Connection connection, @Nonnull final InsertStatement statement) throws SQLException {
        return _executor.execute(connection, statement);
    }

    @Override
    public SelectResult execute(@Nonnull final Connection connection, @Nonnull final SelectStatement statement) throws SQLException {
        return _executor.execute(connection, statement);
    }

    @Override
    public int execute(@Nonnull final Connection connection, @Nonnull final UpdateStatement statement) throws SQLException {
        return _executor.execute(connection, statement);
    }

    @Override
    public long execute(@Nonnull final Connection connection, @Nonnull final SelectSequenceStatement statement) throws SQLException {
        return _executor.execute(connection, statement);
    }

    @Override
    public <T> T execute(@Nonnull final Connection connection, @Nonnull final SelectSequenceStatement statement, final SelectListItem<T> targetColumn)
            throws SQLException {
        return _executor.execute(connection, statement, targetColumn);
    }

    @Override
    public String render(@Nonnull final SelectSequenceStatement statement) throws SQLException {
        return _executor.render(statement);
    }

    @Override
    public String render(@Nonnull final CreateTableStatement statement) throws SQLException {
        return _executor.render(statement);
    }

    @Override
    public String render(@Nonnull final DeleteStatement statement) {
        return _executor.render(statement);
    }

    @Override
    public String render(@Nonnull final InsertStatement statement) throws SQLException {
        return _executor.render(statement);
    }

    @Override
    public String render(@Nonnull final SelectStatement statement) {
        return _executor.render(statement);
    }

    @Override
    public String render(@Nonnull final UpdateStatement statement) {
        return _executor.render(statement);
    }
}
