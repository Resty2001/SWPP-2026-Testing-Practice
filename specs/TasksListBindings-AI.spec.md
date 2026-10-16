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
  - A non-null active task due in the future displays the day-count label and makes the view visible. `[Integration]`
    - Tip: Given local date `2026-10-17`, a `TextView` initially `GONE`, and `Task(isCompleted=false, dueDate=2026-10-20)` / When `setDueDateLabel(textView, task)` is called / Then `textView.text` is exactly `D-3` and `textView.visibility` is `View.VISIBLE`.
  - A non-null completed task displays `CLEAR` and makes the view visible, even when it has no due date. `[Integration]`
    - Tip: Given local date `2026-10-17`, a `TextView` initially `GONE`, and `Task(isCompleted=true, dueDate=null)` / When `setDueDateLabel(textView, task)` is called / Then `textView.text` is exactly `CLEAR` and `textView.visibility` is `View.VISIBLE`.
- Error cases
  - None; null task and missing due date are valid inputs and result in an empty hidden label. `[Integration]`
    - Tip: Given a `TextView` initially showing `D-3` and visible, and `task=null` / When `setDueDateLabel(textView, task)` is called / Then its text is exactly `""` and visibility is `View.GONE`.
- Edge cases
  - At the due-date boundary, an active task due today shows `D-Day`. `[Integration]`
    - Tip: Given local date `2026-10-17`, a `TextView` initially `GONE`, and `Task(isCompleted=false, dueDate=2026-10-17)` / When `setDueDateLabel(textView, task)` is called / Then its text is exactly `D-Day` and visibility is `View.VISIBLE`.
  - One day after the due-date boundary, an active task shows `D+1`. `[Integration]`
    - Tip: Given local date `2026-10-17`, a `TextView` initially `GONE`, and `Task(isCompleted=false, dueDate=2026-10-16)` / When `setDueDateLabel(textView, task)` is called / Then its text is exactly `D+1` and visibility is `View.VISIBLE`.
  - One day before the due-date boundary, an active task shows `D-1`. `[Integration]`
    - Tip: Given local date `2026-10-17`, a `TextView` initially `GONE`, and `Task(isCompleted=false, dueDate=2026-10-18)` / When `setDueDateLabel(textView, task)` is called / Then its text is exactly `D-1` and visibility is `View.VISIBLE`.
  - When a recycled view is bound to an active task with no due date, any old label is cleared and the view is hidden. `[Integration]`
    - Tip: Given local date `2026-10-17`, a `TextView` showing `D-3` and visible, and `Task(isCompleted=false, dueDate=null)` / When `setDueDateLabel(textView, task)` is called / Then its text is exactly `""` and visibility is `View.GONE`.

**Undefined behavior** (optional)
- None.
