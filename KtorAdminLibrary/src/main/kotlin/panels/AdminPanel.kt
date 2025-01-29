package panels

import models.actions.Action
import models.order.Order

interface AdminPanel {
    fun getSingularName(): String
    fun getPluralName(): String
    fun getGroupName(): String?
    fun getDatabaseKey(): String?
    fun getPrimaryKey(): String
    fun getDisplayFormat(): String?
    fun getSearches(): List<String>
    fun getFilters(): List<String>
    fun getAccessRoles(): List<String>?
    fun getDefaultOrder(): Order?
    fun getDefaultActions(): List<Action>
    fun getCustomActions(): List<String>
}

fun List<AdminPanel>.findWithPluralName(name: String?) = find { it.getPluralName() == name }