package com.example.swpp.tododapp.tasks

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.swpp.tododapp.data.Task
import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TasksListBindingsTest {

    private fun newTextView(): TextView {
        val context = ApplicationProvider.getApplicationContext<Context>()
        return TextView(context)
    }

    @Test
    fun setDueDateLabel_activeTaskThreeDaysAhead_showsDMinus3() {
        // GIVEN: An active task due three days from the device-local date and a hidden label view.
        val today = LocalDate.now()
        val task = Task(isCompleted = false, dueDate = today.plusDays(3))
        val textView = newTextView().apply { visibility = View.GONE }

        // WHEN: The binding adapter is called immediately.
        setDueDateLabel(textView, task)

        // THEN: It displays the three-day label.
        assertEquals("D-3", textView.text.toString())
        assertEquals(View.VISIBLE, textView.visibility)
    }

    @Test
    fun setDueDateLabel_completedWithoutDueDate_showsClear() {
        // GIVEN: A completed task without a due date and a hidden label view.
        val task = Task(isCompleted = true, dueDate = null)
        val textView = newTextView().apply { visibility = View.GONE }

        // WHEN: The binding adapter is called.
        setDueDateLabel(textView, task)

        // THEN: Completion is shown despite the missing date.
        assertEquals("CLEAR", textView.text.toString())
        assertEquals(View.VISIBLE, textView.visibility)
    }

    @Test
    fun setDueDateLabel_completedWithDueDate_showsClear() {
        // GIVEN: A completed task with a future due date and a hidden label view.
        val today = LocalDate.now()
        val task = Task(isCompleted = true, dueDate = today.plusDays(3))
        val textView = newTextView().apply { visibility = View.GONE }

        // WHEN: The binding adapter is called immediately.
        setDueDateLabel(textView, task)

        // THEN: Completion takes precedence and is visible.
        assertEquals("CLEAR", textView.text.toString())
        assertEquals(View.VISIBLE, textView.visibility)
    }

    @Test
    fun setDueDateLabel_nullTaskAfterCompletedTask_clearsAndHidesLabel() {
        // GIVEN: A view previously bound to a completed task and showing CLEAR.
        val textView = newTextView().apply { visibility = View.GONE }
        setDueDateLabel(textView, Task(isCompleted = true, dueDate = null))
        assertEquals("CLEAR", textView.text.toString())
        assertEquals(View.VISIBLE, textView.visibility)

        // WHEN: The same view is bound to a null task.
        setDueDateLabel(textView, null)

        // THEN: The prior label is cleared and hidden.
        assertEquals("", textView.text.toString())
        assertEquals(View.GONE, textView.visibility)
    }

    @Test
    fun setDueDateLabel_taskDueToday_showsDDay() {
        // GIVEN: An active task due on the device-local date and a hidden label view.
        val today = LocalDate.now()
        val task = Task(isCompleted = false, dueDate = today)
        val textView = newTextView().apply { visibility = View.GONE }

        // WHEN: The binding adapter is called immediately.
        setDueDateLabel(textView, task)

        // THEN: It displays the due-today label.
        assertEquals("D-Day", textView.text.toString())
        assertEquals(View.VISIBLE, textView.visibility)
    }

    @Test
    fun setDueDateLabel_taskDueTomorrow_showsDMinus1() {
        // GIVEN: An active task due one day after the device-local date and a hidden label view.
        val today = LocalDate.now()
        val task = Task(isCompleted = false, dueDate = today.plusDays(1))
        val textView = newTextView().apply { visibility = View.GONE }

        // WHEN: The binding adapter is called immediately.
        setDueDateLabel(textView, task)

        // THEN: It displays one day remaining.
        assertEquals("D-1", textView.text.toString())
        assertEquals(View.VISIBLE, textView.visibility)
    }

    @Test
    fun setDueDateLabel_taskDueYesterday_showsDPlus1() {
        // GIVEN: An active task due one day before the device-local date and a hidden label view.
        val today = LocalDate.now()
        val task = Task(isCompleted = false, dueDate = today.minusDays(1))
        val textView = newTextView().apply { visibility = View.GONE }

        // WHEN: The binding adapter is called immediately.
        setDueDateLabel(textView, task)

        // THEN: It displays one overdue day.
        assertEquals("D+1", textView.text.toString())
        assertEquals(View.VISIBLE, textView.visibility)
    }

    @Test
    fun setDueDateLabel_activeTaskWithoutDueDateAfterDatedTask_clearsAndHidesLabel() {
        // GIVEN: A view previously bound to a dated task and showing D-3.
        val today = LocalDate.now()
        val textView = newTextView().apply { visibility = View.GONE }
        setDueDateLabel(textView, Task(isCompleted = false, dueDate = today.plusDays(3)))
        assertEquals("D-3", textView.text.toString())
        assertEquals(View.VISIBLE, textView.visibility)

        // WHEN: The same view is rebound to an active task without a due date.
        setDueDateLabel(textView, Task(isCompleted = false, dueDate = null))

        // THEN: The former due-date label is cleared and hidden.
        assertEquals("", textView.text.toString())
        assertEquals(View.GONE, textView.visibility)
    }

    @Test
    fun setDueDateLabel_uncheckedTaskWithoutDueDateAfterCompleted_clearsAndHidesLabel() {
        // GIVEN: A view previously bound to a completed task without a date, showing CLEAR.
        val textView = newTextView().apply { visibility = View.GONE }
        setDueDateLabel(textView, Task(isCompleted = true, dueDate = null))
        assertEquals("CLEAR", textView.text.toString())
        assertEquals(View.VISIBLE, textView.visibility)

        // WHEN: The same view is rebound to the now-active task without a due date.
        setDueDateLabel(textView, Task(isCompleted = false, dueDate = null))

        // THEN: The former completion label is cleared and hidden.
        assertEquals("", textView.text.toString())
        assertEquals(View.GONE, textView.visibility)
    }
}
