# Implementation Plan - Fix "Save Progress" Bug

Fix the bug where clicking "Salvar progresso" deletes the pomodoro instead of saving it, and ensure cancelled progress counts toward statistics.

## Proposed Changes

### UI Layer
#### [MODIFY] [PomodoroScreen.kt](file:///C:/Users/leandro/Desktop/projetos/pessoais/curso/fullfocus/app/src/main/java/com/joseleandro/fullfocus/ui/screen/pomodoro/PomodoroScreen.kt)
- Fix the `ConfirmCancelPomodoroDialog` call in `PomodoroModal`.
- Correct the swapped event calls: `onDiscard` should call `CancelAndDelete`, and `onSaveProgress` should call `CancelAndSave`.

### Data Layer
#### [MODIFY] [PomodoroDataSourceImpl.kt](file:///C:/Users/leandro/Desktop/projetos/pessoais/curso/fullfocus/app/src/main/java/com/joseleandro/fullfocus/data/datasource/PomodoroDataSourceImpl.kt)
- Update `cancelAndSave()` to ensure `elapsedTime` is updated if the session was running when cancelled.
- Update `skip()` to ensure `elapsedTime` is updated if the session was running when skipped.

#### [MODIFY] [StatisticPomodoroDataSourceImpl.kt](file:///C:/Users/leandro/Desktop/projetos/pessoais/curso/fullfocus/app/src/main/java/com/joseleandro/fullfocus/data/datasource/StatisticPomodoroDataSourceImpl.kt)
- Update `calculateStatistics` to include `CANCEL` and `SKIPPED` focus sessions in time calculations and streaks, as long as they have non-zero `elapsedTime`.
- Keep `focusSessionsCompleted` only counting sessions with `SessionStatus.COMPLETED`.

## Verification Plan

### Automated Tests
- Update `StatisticPomodoroDataSourceImplTest.kt` to verify that `CANCEL` sessions with time are included in total time and streaks.

### Manual Verification
- Start a pomodoro, wait a few seconds, pause it, then click cancel and "Salvar progresso".
- Go to the Score screen and verify that the time spent appears in the "Tempo de Foco" and that the day is highlighted in the calendar.
- Verify that "Descartar" correctly removes the records.
