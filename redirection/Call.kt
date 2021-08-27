package com.example.forwarding

import java.util.Date

@Entity
data class Call(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "coming_call") val coming: Int?,
    @ColumnInfo(name = "going_call") val going: Int?,
    @ColumnInfo(name = "date") val date: Date
)
