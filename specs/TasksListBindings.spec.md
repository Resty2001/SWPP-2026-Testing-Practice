# Spec: `TasksListBindings.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `setDueDateLabel(textView: TextView, task: Task?)`

**Intent**: Bind the task's due-date label to its list-row `TextView`, using the current local date as the reference date. Show the view with its label when one exists, and clear and hide the view when the task or label is absent so recycled rows cannot show stale text.

**Pre-conditions**
- `textView` is a valid `TextView` in the task-list row.
- `task` may be null; when present, it may have a nullable due date and either completion state.
- The binding invocation's current local date is the reference date used to calculate an active task's label.

**Post-conditions**
- Success cases
  - An active task due three days after the device-local date displays `D-3` and makes the view visible. `[Integration]`
    - Tip: Given `today=LocalDate.now()` on the test device, `dueDate=today.plusDays(3)`, `isCompleted=false`, and a `TextView` initially `GONE` / When `setDueDateLabel(textView, task)` is called immediately / Then `textView.text` is exactly `D-3` and `textView.visibility` is `View.VISIBLE`.
  - A completed task without a due date displays `CLEAR`, overriding the lack of a date. `[Integration]`
    - Tip: Given `Task(isCompleted=true, dueDate=null)` and a `TextView` initially `GONE` / When `setDueDateLabel(textView, task)` is called / Then `textView.text` is exactly `CLEAR` and `textView.visibility` is `View.VISIBLE`.
  - A completed task with a due date also displays `CLEAR`. `[Integration]`
    - Tip: Given `today=LocalDate.now()` on the test device, `Task(isCompleted=true, dueDate=today.plusDays(3))`, and a `TextView` initially `GONE` / When `setDueDateLabel(textView, task)` is called immediately / Then `textView.text` is exactly `CLEAR` and `textView.visibility` is `View.VISIBLE`.
  - A null task removes the label left by a prior binding. `[Integration]`
    - Tip: Given one `TextView` first bound to `Task(isCompleted=true, dueDate=null)` so its text is `CLEAR` and it is `View.VISIBLE` / When `setDueDateLabel(textView, null)` is called on the same view / Then its text is exactly `""` and its visibility is `View.GONE`.
- Error cases
  - None. A null task or null due date is a valid input, not a rejection case.
- Edge cases
  - A task due today shows `D-Day`. `[Integration]`
    - Tip: Given `today=LocalDate.now()` on the test device, `Task(isCompleted=false, dueDate=today)`, and a `TextView` initially `GONE` / When `setDueDateLabel(textView, task)` is called immediately / Then its text is exactly `D-Day` and visibility is `View.VISIBLE`.
  - An active task due tomorrow shows `D-1`. `[Integration]`
    - Tip: Given `today=LocalDate.now()` on the test device, `Task(isCompleted=false, dueDate=today.plusDays(1))`, and a `TextView` initially `GONE` / When `setDueDateLabel(textView, task)` is called immediately / Then its text is exactly `D-1` and visibility is `View.VISIBLE`.
  - An active task due yesterday shows `D+1`. `[Integration]`
    - Tip: Given `today=LocalDate.now()` on the test device, `Task(isCompleted=false, dueDate=today.minusDays(1))`, and a `TextView` initially `GONE` / When `setDueDateLabel(textView, task)` is called immediately / Then its text is exactly `D+1` and visibility is `View.VISIBLE`.
  - Reusing a row for an active task without a due date clears its former due-date label. `[Integration]`
    - Tip: Given one `TextView` first bound to `Task(isCompleted=false, dueDate=LocalDate.now().plusDays(3))` so its text is `D-3` and it is `View.VISIBLE` / When the same view is bound to `Task(isCompleted=false, dueDate=null)` / Then its text is exactly `""` and its visibility is `View.GONE`.
  - Unchecking a completed task without a due date clears the prior `CLEAR` label. `[Integration]`
    - Tip: Given one `TextView` first bound to `Task(isCompleted=true, dueDate=null)` so its text is `CLEAR` and it is `View.VISIBLE` / When the same view is rebound to `Task(isCompleted=false, dueDate=null)` / Then its text is exactly `""` and its visibility is `View.GONE`.

**Undefined behavior** (optional)
- None.