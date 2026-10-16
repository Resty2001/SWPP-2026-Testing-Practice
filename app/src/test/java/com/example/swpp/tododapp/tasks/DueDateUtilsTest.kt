package com.example.swpp.tododapp.tasks

import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test

class DueDateUtilsTest {

    @Test
    fun dueDateLabel_completedWithDueDate_returnsClear() {
        // GIVEN: A completed task due three days after the reference date.
        val dueDate = LocalDate.parse("2026-10-20")
        val today = LocalDate.parse("2026-10-17")

        // WHEN: Its due-date label is calculated.
        val result = dueDateLabel(dueDate, today, isCompleted = true)

        // THEN: Completion takes precedence over the due date.
        assertEquals("CLEAR", result)
    }

    @Test
    fun dueDateLabel_completedWithoutDueDate_returnsClear() {
        // GIVEN: A completed task without a due date.
        val today = LocalDate.parse("2026-10-17")

        // WHEN: Its due-date label is calculated.
        val result = dueDateLabel(null, today, isCompleted = true)

        // THEN: Completion still produces the clear label.
        assertEquals("CLEAR", result)
    }

    @Test
    fun dueDateLabel_activeWithoutDueDate_returnsEmptyString() {
        // GIVEN: An active task without a due date.
        val today = LocalDate.parse("2026-10-17")

        // WHEN: Its due-date label is calculated.
        val result = dueDateLabel(null, today, isCompleted = false)

        // THEN: There is no label to display.
        assertEquals("", result)
    }

    @Test
    fun dueDateLabel_activeDueInThreeDays_returnsDMinus3() {
        // GIVEN: An active task due three days after today.
        val dueDate = LocalDate.parse("2026-10-20")
        val today = LocalDate.parse("2026-10-17")

        // WHEN: Its due-date label is calculated.
        val result = dueDateLabel(dueDate, today, isCompleted = false)

        // THEN: The label reports the three calendar days remaining.
        assertEquals("D-3", result)
    }

    @Test
    fun dueDateLabel_activeOverdueByThreeDays_returnsDPlus3() {
        // GIVEN: An active task whose due date was three days ago.
        val dueDate = LocalDate.parse("2026-10-14")
        val today = LocalDate.parse("2026-10-17")

        // WHEN: Its due-date label is calculated.
        val result = dueDateLabel(dueDate, today, isCompleted = false)

        // THEN: The label reports three overdue calendar days.
        assertEquals("D+3", result)
    }

    @Test
    fun dueDateLabel_dueToday_returnsDDay() {
        // GIVEN: An active task due on the reference date.
        val date = LocalDate.parse("2026-10-17")

        // WHEN: Its due-date label is calculated.
        val result = dueDateLabel(date, date, isCompleted = false)

        // THEN: The label identifies the due date as today.
        assertEquals("D-Day", result)
    }

    @Test
    fun dueDateLabel_dueTomorrow_returnsDMinus1() {
        // GIVEN: An active task due one day after the reference date.
        val dueDate = LocalDate.parse("2026-10-18")
        val today = LocalDate.parse("2026-10-17")

        // WHEN: Its due-date label is calculated.
        val result = dueDateLabel(dueDate, today, isCompleted = false)

        // THEN: The label reports one calendar day remaining.
        assertEquals("D-1", result)
    }

    @Test
    fun dueDateLabel_dueYesterday_returnsDPlus1() {
        // GIVEN: An active task whose due date was one day before the reference date.
        val dueDate = LocalDate.parse("2026-10-16")
        val today = LocalDate.parse("2026-10-17")

        // WHEN: Its due-date label is calculated.
        val result = dueDateLabel(dueDate, today, isCompleted = false)

        // THEN: The label reports one overdue calendar day.
        assertEquals("D+1", result)
    }

    @Test
    fun dueDateLabel_crossingMonthBoundary_returnsDMinus1() {
        // GIVEN: A due date one day after the end of October.
        val dueDate = LocalDate.parse("2026-11-01")
        val today = LocalDate.parse("2026-10-31")

        // WHEN: Its due-date label is calculated.
        val result = dueDateLabel(dueDate, today, isCompleted = false)

        // THEN: The label counts the calendar day across the month boundary.
        assertEquals("D-1", result)
    }

    @Test
    fun dueDateLabel_crossingYearBoundary_returnsDMinus1() {
        // GIVEN: A due date one day after the end of 2026.
        val dueDate = LocalDate.parse("2027-01-01")
        val today = LocalDate.parse("2026-12-31")

        // WHEN: Its due-date label is calculated.
        val result = dueDateLabel(dueDate, today, isCompleted = false)

        // THEN: The label counts the calendar day across the year boundary.
        assertEquals("D-1", result)
    }

    @Test
    fun dueDateLabel_crossingLeapDay_returnsDMinus2() {
        // GIVEN: The interval from February 28 to March 1 in leap year 2024.
        val dueDate = LocalDate.parse("2024-03-01")
        val today = LocalDate.parse("2024-02-28")

        // WHEN: Its due-date label is calculated.
        val result = dueDateLabel(dueDate, today, isCompleted = false)

        // THEN: February 29 is included in the calendar-day distance.
        assertEquals("D-2", result)
    }

    @Test
    fun dueDateLabel_oneFullMonthAhead_returnsDMinus31() {
        // GIVEN: The same day of the next month, 31 calendar days after today.
        val today = LocalDate.parse("2026-01-15")
        val dueDate = LocalDate.parse("2026-02-15")

        // WHEN: Its due-date label is calculated.
        val result = dueDateLabel(dueDate, today, isCompleted = false)

        // THEN: The full month is counted as 31 days.
        assertEquals("D-31", result)
    }

    @Test
    fun dueDateLabel_oneFullMonthOverdue_returnsDPlus31() {
        // GIVEN: A due date 31 calendar days before today.
        val today = LocalDate.parse("2026-02-15")
        val dueDate = LocalDate.parse("2026-01-15")

        // WHEN: Its due-date label is calculated.
        val result = dueDateLabel(dueDate, today, isCompleted = false)

        // THEN: The full month is counted as 31 overdue days.
        assertEquals("D+31", result)
    }
}
