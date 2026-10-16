package com.example.swpp.tododapp.taskdetail

import androidx.test.filters.MediumTest
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.swpp.tododapp.R
import com.example.swpp.tododapp.data.Task
import org.junit.Test
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.swpp.tododapp.ServiceLocator
import com.example.swpp.tododapp.data.source.FakeAndroidTestRepository
import com.example.swpp.tododapp.data.source.TasksRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import kotlinx.coroutines.test.runTest
import org.hamcrest.core.IsNot.not


@MediumTest
@RunWith(AndroidJUnit4::class)
class TaskDetailFragmentTest {
    private lateinit var repository: TasksRepository

    @Before
    fun initRepository() {
        repository = FakeAndroidTestRepository()
        ServiceLocator.tasksRepository = repository
    }

    @After
    fun cleanupDb() = runTest {
        ServiceLocator.resetRepository()
    }

    @Test
    fun activeTaskDetails_DisplayedInUi() = runTest {
        // GIVEN: 저장소에 진행 중인 작업을 넣는다.
        val activeTask = Task("Task Title", "Task Description", false)
        repository.saveTask(activeTask)

        // WHEN: 해당 작업의 상세 화면을 연다.
        val args = TaskDetailFragmentArgs(activeTask.id).toBundle()
        launchFragmentInContainer<TaskDetailFragment>(args, R.style.AppTheme)

        // THEN: 제목, 설명, 완료 여부가 올바르게 표시된다.
        onView(withId(R.id.task_detail_title_text))
            .check(matches(isDisplayed()))
            .check(matches(withText("Task Title")))

        onView(withId(R.id.task_detail_description_text))
            .check(matches(isDisplayed()))
            .check(matches(withText("Task Description")))

        onView(withId(R.id.task_detail_complete_checkbox))
            .check(matches(isDisplayed()))
            .check(matches(not(isChecked())))
    }
    }