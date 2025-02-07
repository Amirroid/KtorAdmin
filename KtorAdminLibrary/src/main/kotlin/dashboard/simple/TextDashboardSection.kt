package dashboard.simple

import dashboard.base.DashboardSection
import models.chart.TextDashboardAggregationFunction

abstract class TextDashboardSection : DashboardSection {
    override val sectionType: String
        get() = SECTION_TYPE

    abstract val tableName: String
    abstract val fieldName: String

    abstract val aggregationFunction: TextDashboardAggregationFunction


    companion object {
        private const val SECTION_TYPE = "text"
    }
}