package com.aimr.aimrpos.domain.usecase

import com.aimr.aimrpos.domain.model.Role
import com.aimr.aimrpos.domain.model.User

class PermissionChecker(private val permissionRepository: com.aimr.aimrpos.domain.repository.PermissionRepository,
                       private val userRoleRepository: com.aimr.aimrpos.domain.repository.UserRoleRepository,
                       private val roleRepository: com.aimr.aimrpos.domain.repository.RoleRepository) {

    suspend fun hasPermission(userId: String, permissionKey: String): Boolean {
        val userRoles = userRoleRepository.getByUser(userId).first()
        val roles = userRoles.mapNotNull { roleRepository.getById(it.roleId) }
        return roles.any { role ->
            val permissions = kotlinx.serialization.json.Json.parseToJsonElement(role.permissionsJson).jsonArray
            permissions.any { it.jsonPrimitive.content == permissionKey }
        }
    }

    suspend fun getUserPermissions(userId: String): Set<String> {
        val userRoles = userRoleRepository.getByUser(userId).first()
        val permissions = mutableSetOf<String>()
        userRoles.forEach { userRole ->
            val role = roleRepository.getById(userRole.roleId)
            if (role != null) {
                val permissionArray = kotlinx.serialization.json.Json.parseToJsonElement(role.permissionsJson).jsonArray
                permissionArray.forEach { permissions.add(it.jsonPrimitive.content) }
            }
        }
        return permissions
    }
}