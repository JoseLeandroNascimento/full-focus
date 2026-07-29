# Implementation Plan - Enhance FullFocusCalendarStrike

The goal is to improve the style, design, usability, typography, and animation of the `FullFocusCalendarStrike` component to make it more engaging and professional.

## Proposed Changes

### UI Component - Calendar

#### [MODIFY] [FullFocusCalendarStrike.kt](file:///C:/Users/leandro/Desktop/projetos/pessoais/curso/fullfocus/app/src/main/java/com/joseleandro/fullfocus/ui/component/FullFocusCalendarStrike.kt)

- **Header Redesign (`MonthHeaderStriker`):**
    - **Typography:** Use `MaterialTheme.typography.titleLarge` for the month and year for better hierarchy.
    - **Streak Badge:** Transform the "Sequência atual" text into a stylized `SuggestionChip` or a custom `Surface` with a fire icon, making it look like a badge.
    - **Days of Week:** Align the labels better and use `labelMedium` with a subtle color (`onSurfaceVariant`).

- **Day Cell Enhancement (`DayStrike`):**
    - **Focused State:** Use a rounded rectangle (Squircle or high-corner Radius) instead of a simple circle for a more modern feel.
    - **Strike Indicator:** Improve the fire emoji placement. Instead of just `Alignment.TopEnd`, consider a small badge or a glow effect around the day number.
    - **Today vs. Focused:** Add a small dot or a different border color to represent "Today" if it's different from the focused/selected day.
    - **Interaction:** Add a `Modifier.clickable` with a ripple effect to the day cells.

- **Animations:**
    - **Selection Scale:** Add a subtle `scale` animation (e.g., 0.95f to 1.1f) when a day is focused or clicked.
    - **Strike Animation:** Use `AnimatedVisibility` or `updateTransition` to animate the fire icon's appearance with a scale-in and fade-in effect.
    - **Color Transitions:** Continue using `animateColorAsState` for smooth background and text color changes.

## Verification Plan

### Automated Tests
- N/A (UI focused change)

### Manual Verification
- **Compose Previews:** Use the existing and new previews to verify:
    - Light and Dark themes.
    - Focused vs. non-focused states.
    - Streak badge appearance.
- **Visual Check:** Ensure animations are smooth and not distracting.
- **Accessibility:** Verify that the "streak" day has an appropriate content description.
