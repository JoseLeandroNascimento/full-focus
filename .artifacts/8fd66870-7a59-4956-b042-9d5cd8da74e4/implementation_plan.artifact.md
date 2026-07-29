# Interatividade no Gráfico de Foco

O objetivo é tornar o seletor de período do gráfico de foco interativo, permitindo que o usuário alterne entre diferentes visualizações (ex: "Por semana", "Por mês") e veja os dados correspondentes.

## Proposed Changes

### [UI Layer - Score Screen]

#### [MODIFY] [ScoreUiState.kt](file:///C:/Users/leandro/Desktop/projetos/pessoais/curso/fullfocus/app/src/main/java/com/joseleandro/fullfocus/ui/screen/score/ScoreUiState.kt)
Adicionar campos para gerenciar a seleção do gráfico:
- `selectedChartPeriod: String` (ex: "Por semana").
- `chartPeriodOptions: List<String>`.

#### [MODIFY] [ScoreViewModel.kt](file:///C:/Users/leandro/Desktop/projetos/pessoais/curso/fullfocus/app/src/main/java/com/joseleandro/fullfocus/ui/screen/score/ScoreViewModel.kt)
- Adicionar função `onChartPeriodSelected(period: String)`.
- Atualizar a lista `weeklyActivity` com dados diferentes dependendo da opção selecionada (mockando os dados).

#### [MODIFY] [FullFocusWeeklyChart.kt](file:///C:/Users/leandro/Desktop/projetos/pessoais/curso/fullfocus/app/src/main/java/com/joseleandro/fullfocus/ui/component/FullFocusWeeklyChart.kt)
- Adicionar parâmetros `selectedPeriod`, `periodOptions` e o callback `onPeriodSelected`.
- Implementar um `DropdownMenu` nativo do Material 3 no seletor de período para permitir a troca.

#### [MODIFY] [ScoreScreen.kt](file:///C:/Users/leandro/Desktop/projetos/pessoais/curso/fullfocus/app/src/main/java/com/joseleandro/fullfocus/ui/screen/score/ScoreScreen.kt)
- Passar os novos estados e callbacks para o componente `FullFocusWeeklyChart`.

## Verification Plan

### Manual Verification
- Clicar no seletor do gráfico e verificar se o menu de opções aparece.
- Selecionar uma opção (ex: "Por mês") e garantir que o gráfico atualize as barras e os labels (mesmo que com dados mockados).
- Validar se o estado do seletor reflete a opção escolhida.
