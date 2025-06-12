package org.gabrieal.gymtracker.util.systemUtil

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.gabrieal.gymtracker.data.db.GymTrackerDatabase

actual fun createDriver(): SqlDriver = NativeSqliteDriver(GymTrackerDatabase.Schema, "GYM_TRACKER_DB")