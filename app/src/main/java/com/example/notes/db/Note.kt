package com.example.notes.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "notes_table")
data class Note (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "note")
    val note: String
): Parcelable