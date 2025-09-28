package de.cau.inf.se.sopro.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.cau.inf.se.sopro.model.application.Block


@Dao
interface BlockDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addBlock(block: Block)

    @Query("Select * from Block")
    suspend fun getAll(): List<Block>
}


