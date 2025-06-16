package org.gabrieal.gymtracker.util.systemUtil

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import db.GymTrackerDatabase

actual fun createDriver(): SqlDriver = AndroidSqliteDriver(GymTrackerDatabase.Schema, activityReference!!, "GYM_TRACKER_DB")