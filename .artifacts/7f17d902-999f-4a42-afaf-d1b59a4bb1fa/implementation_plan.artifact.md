# Implementation Plan - Minimalist & Data-Driven Hero Card

The user wants a cleaner design that prioritizes useful information and avoids visual clutter ("noise").

## Proposed Changes

### UI & Styling

#### [MODIFY] [FullFocusHeroStreakCard.kt](file:///C:/Users/leandro/Desktop/projetos/pessoais/curso/fullfocus/app/src/main/java/com/joseleandro/fullfocus/ui/component/FullFocusHeroStreakCard.kt)

- **Remove "Noise"**:
    - Remove the background horizontal gradient and the large faded fire icon.
    - Use a simple `Card` with a subtle surface color and an outlined border to match the `MonthlyActivityCard` style.
- **Hero Section**:
    - Keep the Lottie fire animation on the left as the main visual.
    - Large, clear current streak number.
- **Useful Information**:
    - **Progress towards Record**: Add a `LinearProgressIndicator` showing the current streak relative to the `highestStreak`. This provides immediate visual context of how well the user is doing.
    - **Minimalist Stats**: Move the "Recorde" and "Streak Freezes" (adding it back as it's useful) to small, integrated "stat badges" at the bottom.
- **Refined Typography**: Use a cleaner hierarchy with standard Material 3 typography.

## Verification Plan

### Manual Verification
- Render the previews to ensure the layout is clean and the progress bar correctly reflects the streak/record ratio.
- Verify that the card looks consistent with other "outlined" cards in the app (like the monthly activity card).
