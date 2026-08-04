package com.aimr.aimrpos.domain.usecase

import com.aimr.aimrpos.domain.model.WorkflowRule
import com.aimr.aimrpos.domain.repository.NotificationRepository
import com.aimr.aimrpos.domain.repository.WorkflowRuleRepository
import kotlinx.coroutines.flow.first

class WorkflowAutomationEngine(
    private val workflowRuleRepository: WorkflowRuleRepository,
    private val notificationRepository: NotificationRepository
) {
    suspend fun evaluateAndExecute(
        entityType: String,
        action: String,
        context: Map<String, Any>,
        userId: String
    ): WorkflowResult {
        val rule = workflowRuleRepository.getRule(entityType, action) ?: return WorkflowResult.NoRule
        if (!rule.isActive) return WorkflowResult.RuleInactive

        val matches = EvaluateWorkflowRuleUseCase().invoke(rule, context)
        return if (matches) {
            if (rule.autoApprove) {
                WorkflowResult.AutoApproved
            } else {
                val notifiedUsers = kotlinx.serialization.json.Json.parseToJsonElement(rule.notifyUsersJson).jsonArray.map { it.jsonPrimitive.content }
                notifiedUsers.forEach { notifyUserId ->
                    val notification = CreateNotificationUseCase().invoke(
                        userId = notifyUserId,
                        title = "Approval Required: ${rule.name}",
                        message = "Action: $action on $entityType",
                        type = "APPROVAL_REQUEST",
                        relatedEntityType = entityType,
                        relatedEntityId = context["entityId"]?.toString()
                    )
                    notificationRepository.insert(notification)
                }
                WorkflowResult.NotificationSent(notifiedUsers)
            }
        } else {
            WorkflowResult.NoMatch
        }
    }
}

sealed class WorkflowResult {
    object NoRule : WorkflowResult()
    object RuleInactive : WorkflowResult()
    object NoMatch : WorkflowResult()
    object AutoApproved : WorkflowResult()
    data class NotificationSent(val userIds: List<String>) : WorkflowResult()
}