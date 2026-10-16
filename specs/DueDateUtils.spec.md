# Spec: `DueDateUtils.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `dueDateLabel(dueDate: LocalDate?, today: LocalDate, isCompleted: Boolean): String`

**Intent**: Build the task-list label from completion status and the due date. Completed tasks show `CLEAR`, active tasks without a date show nothing, and active dated tasks show `D-Day`, `D-N`, or `D+N` using the calendar-day distance from today.

**Pre-conditions**
- `today` is a non-null `LocalDate`; `dueDate` may be null and `isCompleted` may be either Boolean value.
- No locale, time zone, or wall-clock lookup is needed; the caller supplies the reference date.

**Post-conditions**
- Success cases
  - A completed task returns `CLEAR`, regardless of its supplied due date. `[Unit]`
    - Tip: Given `dueDate=2026-10-20`, `today=2026-10-17`, and `isCompleted=true` / When `dueDateLabel` is called / Then the result is exactly `CLEAR`.
  - A completed task without a due date also returns `CLEAR`. `[Unit]`
    - Tip: Given `dueDate=null`, `today=2026-10-17`, `isCompleted=true` / When `dueDateLabel` is called / Then return exactly `CLEAR`.
  - An active task with no due date returns an empty string. `[Unit]`
    - Tip: Given `dueDate=null`, `today=2026-10-17`, and `isCompleted=false` / When `dueDateLabel` is called / Then the result is exactly `""`.
  - An active task due in the future returns `D-N`, where N is the full number of calendar days from today to the due date. `[Unit]`
    - Tip: Given `dueDate=2026-10-20`, `today=2026-10-17`, and `isCompleted=false` / When `dueDateLabel` is called / Then the result is exactly `D-3`.
  - An active task overdue by N calendar days returns `D+N`. `[Unit]`
    - Tip: Given `dueDate=2026-10-14`, `today=2026-10-17`, and `isCompleted=false` / When `dueDateLabel` is called / Then the result is exactly `D+3`.
- Error cases
  - None. `[Unit]`
- Edge cases
  - At the date boundary, an active task due today returns `D-Day`. `[Unit]`
    - Tip: Given `dueDate=2026-10-17`, `today=2026-10-17`, and `isCompleted=false` / When `dueDateLabel` is called / Then the result is exactly `D-Day`.
  - One calendar day after the boundary returns `D-1`. `[Unit]`
    - Tip: Given `dueDate=2026-10-18`, `today=2026-10-17`, and `isCompleted=false` / When `dueDateLabel` is called / Then the result is exactly `D-1`.
  - One calendar day before the boundary returns `D+1`. `[Unit]`
    - Tip: Given `dueDate=2026-10-16`, `today=2026-10-17`, and `isCompleted=false` / When `dueDateLabel` is called / Then the result is exactly `D+1`.
  - Calendar-day distance remains correct across a month boundary. `[Unit]`
    - Tip: Given `dueDate=2026-11-01`, `today=2026-10-31`, and `isCompleted=false` / When `dueDateLabel` is called / Then the result is exactly `D-1`.
  - Calendar-day distance remains correct across a year boundary. `[Unit]`
    - Tip: Given `dueDate=2027-01-01`, `today=2026-12-31`, and `isCompleted=false` / When `dueDateLabel` is called / Then the result is exactly `D-1`.
  - Calendar-day distance includes February 29 in a leap year. `[Unit]`
    - Tip: Given `dueDate=2024-03-01`, `today=2024-02-28`, and `isCompleted=false` / When `dueDateLabel` is called / Then the result is exactly `D-2`.

  - A full calendar month must be counted in total days, not only in the day component of a year-month-day period. `[Unit]`
    - Tip: Given `today=2026-01-15`, `dueDate=2026-02-15`, and `isCompleted=false` / When `dueDateLabel` is called / Then the result is exactly `D-31`.
  - The same full-month boundary works for overdue tasks. `[Unit]`
    - Tip: Given `today=2026-02-15`, `dueDate=2026-01-15`, and `isCompleted=false` / When `dueDateLabel` is called / Then the result is exactly `D+31`.

**Undefined behavior** (optional)
- None.
