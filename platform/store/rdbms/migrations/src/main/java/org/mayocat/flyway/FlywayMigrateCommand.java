/*
 * Copyright (c) 2012, Mayocat <hello@mayocat.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mayocat.flyway;

import com.googlecode.flyway.core.Flyway;

import io.dropwizard.Configuration;
import io.dropwizard.cli.ConfiguredCommand;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.DatabaseConfiguration;
import io.dropwizard.setup.Bootstrap;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * @version $Id$
 */
public class FlywayMigrateCommand<T extends Configuration> extends ConfiguredCommand<T>
{
    public static final String MAYOAPP_MIGRATIONS = "mayoapp/migrations";

    private final DatabaseConfiguration<T> strategy;

    private final Class<T> configurationClass;

    public FlywayMigrateCommand(DatabaseConfiguration<T> strategy, Class<T> configurationClass)
    {
        super("flyway-migrate", "Migrate database with Flyway");

        this.strategy = strategy;
        this.configurationClass = configurationClass;
    }

    @Override
    protected Class<T> getConfigurationClass()
    {
        return configurationClass;
    }

    @Override
    protected void run(Bootstrap<T> bootstrap, Namespace namespace, T configuration) throws Exception
    {
        final DataSourceFactory dbConfig = strategy.getDataSourceFactory(configuration);

        dbConfig.setMaxSize(1);
        dbConfig.setMinSize(1);

        Flyway flyway = new Flyway();
        flyway.setLocations(MAYOAPP_MIGRATIONS);
        flyway.setDataSource(dbConfig.getUrl(), dbConfig.getUser(), dbConfig.getPassword());
        if (flyway.info().current() == null) {
            flyway.setInitVersion("0000.0001");
            flyway.init();
        }
        flyway.migrate();
    }
}
