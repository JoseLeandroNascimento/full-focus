# Walkthrough: Gráfico de Foco Interativo

Tornei o gráfico de tempo de foco interativo, permitindo que o usuário alterne entre visualizações por semana e por mês, com os dados e eixos se ajustando automaticamente.

## Novidades na Interatividade

### 🖱️ Seletor de Período Funcional
- **Dropdown Menu**: O botão de período (ex: "Por semana") agora abre um menu de opções nativo do Material 3.
- **Troca de Contexto**: Ao selecionar "Por mês", o gráfico é reconstruído instantaneamente com dados agrupados por semanas, alterando os rótulos e as magnitudes das barras.

### 📊 Atualização Dinâmica de Dados
- **ViewModel Logic**: Implementei a função `onChartPeriodSelected` no `ScoreViewModel`. Ela gerencia a troca dos dados mockados, simulando como seria o comportamento com dados reais do banco de dados.
- **Animação**: As barras do gráfico mantêm a animação de entrada ao trocar de período, proporcionando um feedback visual fluido para o usuário.

## Componentes Atualizados

- [FullFocusWeeklyChart.kt](file:///C:/Users/leandro/Desktop/projetos/pessoais/curso/fullfocus/app/src/main/java/com/joseleandro/fullfocus/ui/component/FullFocusWeeklyChart.kt): Agora inclui o `DropdownMenu` e gerencia o estado de abertura do seletor.
- [ScoreViewModel.kt](file:///C:/Users/leandro/Desktop/projetos/pessoais/curso/fullfocus/app/src/main/java/com/joseleandro/fullfocus/ui/screen/score/ScoreViewModel.kt): Centraliza a lógica de alteração dos dados do gráfico.
- [ScoreUiState.kt](file:///C:/Users/leandro/Desktop/projetos/pessoais/curso/fullfocus/app/src/main/java/com/joseleandro/fullfocus/ui/screen/score/ScoreUiState.kt): Adicionados os campos `selectedChartPeriod` e `chartPeriodOptions`.

> [!TIP]
> Experimente trocar entre "Por semana" e "Por mês" diretamente no app para ver a transição dos dados e como o eixo Y se mantém consistente com a escala de horas.

render_diffs(file:///C:/Users/leandro/Desktop/projetos/pessoais/curso/fullfocus/app/src/main/java/com/joseleandro/fullfocus/ui/component/FullFocusWeeklyChart.kt)
