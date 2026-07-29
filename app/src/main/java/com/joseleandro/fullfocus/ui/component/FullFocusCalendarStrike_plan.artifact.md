# Implementation Plan - Enhance FullFocusCalendarStrike

The goal is to improve the style, design, usability, typography, and animation of the `FullFocusCalendarStrike` component to make it more engaging and professional.

## Proposed Changes

### [Component Name] FullFocusCalendarStrike

#### [MODIFY] [FullFocusCalendarStrike.kt](file:///C:/Users/leandro/Desktop/projetos/pessoais/curso/fullfocus/app/src/main/java/com/joseleandro/fullfocus/ui/component/FullFocusCalendarStrike.kt)

- **Header Redesign (`MonthHeaderStriker`):**
    - Improve month/year typography.
    - Transform the "Sequência atual" text into a stylized Badge/Chip with an icon.
    - Refine the Days of Week labels with better spacing and alignment.
- **Day Cell Enhancement (`DayStrike`):**
    - Implement a more modern highlight for the focused/current day.
    - Add a distinct "Today" indicator.
    - Improve the "Fire" (strike) icon placement and styling.
    - Add a click listener to the day cell.
- **Typography & Colors:**
    - Use `MaterialTheme.typography` consistently (e.g., `titleMedium` for month, `labelMedium` for days).
    - Utilize the primary color for streaks and highlights.
- **Animations:**
    - Add a scale animation (`animateFloatAsState`) when a day is focused.
    - Use `Crossfade` or `AnimatedContent` for the strike icon visibility if needed.
    - Smooth color transitions.

## Verification Plan

### Manual Verification
- Render the `FullFocusCalendarStrike` in Compose Preview.
- Verify light and dark theme compatibility.
- Check accessibility (talkback) for the strike indicators.
