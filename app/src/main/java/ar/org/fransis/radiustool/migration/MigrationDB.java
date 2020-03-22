package ar.org.fransis.radiustool.migration;

import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class MigrationDB {
    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "CREATE TABLE `result` ("
                            + "`mTestCase` TEXT,"
                            + "`mReplyMessage` TEXT,"
                            + "`mResponseTime` INTEGER NOT NULL,"
                            + "`mResponseType` TEXT,"
                            + "`mDate` INTEGER,"
                            + "`mId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL "
                            + ")"
            );
        }
    };
}