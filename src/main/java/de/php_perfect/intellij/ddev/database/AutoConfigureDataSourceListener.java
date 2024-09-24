package de.php_perfect.intellij.ddev.database;

import com.intellij.openapi.project.Project;
import de.php_perfect.intellij.ddev.DatabaseInfoChangedListener;
import de.php_perfect.intellij.ddev.cmd.DatabaseInfo;
import de.php_perfect.intellij.ddev.settings.DdevSettingsState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class AutoConfigureDataSourceListener implements DatabaseInfoChangedListener {
    private static final @NotNull String HOST = "192.168.0.168";
    private final @NotNull Project project;


    public AutoConfigureDataSourceListener(@NotNull Project project) {
        this.project = project;
    }

    @Override
    public void onDatabaseInfoChanged(@Nullable DatabaseInfo databaseInfo) {
        if (databaseInfo == null) {
            return;
        }

        if (!DdevSettingsState.getInstance(this.project).autoConfigureDataSource) {
            return;
        }

        if (databaseInfo.type() == null || databaseInfo.version() == null || databaseInfo.name() == null || databaseInfo.username() == null || databaseInfo.password() == null) {
            return;
        }

        final DataSourceConfig.Type type = switch (databaseInfo.type()) {
            case MYSQL -> DataSourceConfig.Type.MYSQL;
            case MARIADB -> DataSourceConfig.Type.MARIADB;
            case POSTGRESQL -> DataSourceConfig.Type.POSTGRESQL;
        };

        DdevDataSourceManager.getInstance(this.project).updateDdevDataSource(new DataSourceConfig("DDEV", "DDEV generated data source", type, databaseInfo.version(), HOST, databaseInfo.publishedPort(), databaseInfo.name(), databaseInfo.username(), databaseInfo.password()));
    }
}
