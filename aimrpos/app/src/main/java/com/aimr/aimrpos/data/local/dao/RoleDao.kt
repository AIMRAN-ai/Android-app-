package com.aimr.aimrpos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aimr.aimrpos.data.local.entity.RoleEntity
import com.aimr.aimrpos.data.local.entity.PermissionEntity
import com.aimr.aimrpos.data.local.entity.UserRoleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RoleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(role: RoleEntity)

    @Query("SELECT * FROM roles WHERE isDeleted = 0 ORDER BY name ASC")
    fun getAll(): Flow<List<RoleEntity>>

    @Query("SELECT * FROM roles WHERE id = :id AND isDeleted = 0")
    suspend fun getById(id: String): RoleEntity?

    @Query("SELECT * FROM roles WHERE isSystemRole = 1 AND isDeleted = 0")
    fun getSystemRoles(): Flow<List<RoleEntity>>

    @Query("UPDATE roles SET syncStatus = :status WHERE id = :id")
    suspend fun updateSyncStatus(id: String, status: String)
}

@Dao
interface PermissionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(permission: PermissionEntity)

    @Query("SELECT * FROM permissions WHERE isDeleted = 0 ORDER BY category ASC, key ASC")
    fun getAll(): Flow<List<PermissionEntity>>

    @Query("SELECT * FROM permissions WHERE key = :key AND isDeleted = 0 LIMIT 1")
    suspend fun getByKey(key: String): PermissionEntity?

    @Query("SELECT * FROM permissions WHERE category = :category AND isDeleted = 0")
    fun getByCategory(category: String): Flow<List<PermissionEntity>>
}

@Dao
interface UserRoleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userRole: UserRoleEntity)

    @Query("SELECT * FROM user_roles WHERE userId = :userId AND isDeleted = 0")
    fun getByUser(userId: String): Flow<List<UserRoleEntity>>

    @Query("SELECT * FROM user_roles WHERE roleId = :roleId AND isDeleted = 0")
    fun getByRole(roleId: String): Flow<List<UserRoleEntity>>

    @Query("DELETE FROM user_roles WHERE userId = :userId AND roleId = :roleId")
    suspend fun delete(userId: String, roleId: String)

    @Query("UPDATE user_roles SET syncStatus = :status WHERE id = :id")
    suspend fun updateSyncStatus(id: String, status: String)
}