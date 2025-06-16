package org.gabrieal.gymtracker.util.systemUtil

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import db.GymTrackerDatabase

actual fun createDriver(): SqlDriver = NativeSqliteDriver(GymTrackerDatabase.Schema, "GYM_TRACKER_DB")