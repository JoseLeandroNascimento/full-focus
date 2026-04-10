package com.joseleandro.fullfocus.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.core.navigation.Screen
import com.joseleandro.fullfocus.ui.theme.FullFocusTheme

@Composable
fun FullFocusModalDrawerSheet(
    modifier: Modifier = Modifier,
    drawerState: DrawerState,
    onNavigate: (Screen) -> Unit
) {
    ModalDrawerSheet(
        drawerState = drawerState,
        modifier = modifier,
        drawerTonalElevation = 6.dp,
        drawerContainerColor = MaterialTheme.colorScheme.surface
    ) {

        FullFocusDrawerHeader()

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 24.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 12.dp)
                .padding(top = 16.dp)
        ) {


            NavigationDrawerItem(
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ri_target_fill),
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = "Metas do Dia",
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                selected = false,
                onClick = {}
            )

            NavigationDrawerItem(
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.boxicons_seal_check_filled),
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = "Todas as Tarefas",
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                selected = false,
                onClick = {}
            )

            NavigationDrawerItem(
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_bookmark_24),
                        contentDescription = null
                    )
                },
                label = { Text(text = "Tags", style = MaterialTheme.typography.labelLarge) },
                selected = false,
                onClick = {
                    onNavigate(Screen.ManageTagScreen)
                }
            )

            NavigationDrawerItem(
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.streamline_sharp_star_badge_solid),
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = "Minhas Conquistas",
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                selected = false,
                onClick = {}
            )

            NavigationDrawerItem(
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.rounded_bar_chart_24),
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = "Estatísticas de Foco",
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                selected = false,
                onClick = {}
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surface.copy(alpha = .4f))
                .padding(bottom = 16.dp, top = 8.dp)
                .padding(horizontal = 12.dp)
        ) {
            NavigationDrawerItem(
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_settings_24),
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = "Configurações",
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                selected = false,
                onClick = {}
            )
            PremiumBanner(
                modifier = Modifier.padding(top = 4.dp),
                onClick = {}
            )
        }
    }
}

@Composable
fun FullFocusDrawerHeader(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Surface(
            modifier = Modifier.size(40.dp),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(id = R.drawable.sharp_target_24),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Column {
            Text(
                text = "Full Focus",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Foco e Produtividade",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
fun PremiumBanner(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.outline_crown_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(40.dp)
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Full Focus PRO",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Desbloqueie estatísticas avançadas e foco ilimitado.",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                    )
                }
            }
        }
    }
}


/**
 *1. Seção: Execução (O "Agora")
 * •
 * Metas do Dia:
 * ◦
 * O que é: O centro de comando do usuário.
 * ◦
 * Conteúdo: Uma lista filtrada apenas com as tarefas que vencem hoje ou que foram marcadas como "Prioridade Máxima".
 * ◦
 * Funcionalidade: Deve permitir que o usuário foque no "Big 3" (as 3 tarefas mais importantes do dia) para evitar sobrecarga.
 * •
 * Timer Pomodoro:
 * ◦
 * O que é: A ferramenta técnica de foco.
 * ◦
 * Conteúdo: Um cronômetro visual (geralmente circular), contador de ciclos (quantos pomodoros já foram feitos) e botões de controle (Iniciar, Pausar, Pular).
 * ◦
 * Funcionalidade: Configurações rápidas de tempo de foco e descanso, e sons de ambiente (ruído branco, chuva) para auxiliar na concentração.
 * 2. Seção: Organização (O "Backlog")
 * •
 * Todas as Tarefas:
 * ◦
 * O que é: O repositório central de tudo o que precisa ser feito.
 * ◦
 * Conteúdo: Uma lista completa com busca e filtros. Pode ser dividida por abas: "Pendentes", "Concluídas" e "Arquivadas".
 * ◦
 * Funcionalidade: Ordenação por data, prioridade ou nome. É aqui que o usuário faz o "despejo mental" de novas ideias.
 * •
 * Tags:
 * ◦
 * O que é: A gestão de categorias e contextos.
 * ◦
 * Conteúdo: Uma lista das etiquetas criadas (ex: Trabalho, Estudo, Casa, Lazer) com suas respectivas cores.
 * ◦
 * Funcionalidade: Criar, editar e excluir tags. Ao clicar em uma tag, o usuário vê todas as tarefas vinculadas a ela.
 * 3. Seção: Resultados (O "Progresso")
 * •
 * Minhas Conquistas:
 * ◦
 * O que é: O sistema de gamificação para manter o engajamento.
 * ◦
 * Conteúdo: Badges (medalhas) desbloqueáveis (ex: "Focado por 7 dias seguidos", "100 tarefas concluídas") e o nível atual do usuário.
 * ◦
 * Funcionalidade: Visualização de marcos alcançados e desafios ativos.
 * •
 * Estatísticas de Foco:
 * ◦
 * O que é: Análise de dados sobre o desempenho do usuário.
 * ◦
 * Conteúdo: Gráficos de barras ou linhas mostrando:
 * ▪
 * Horas de foco por dia/semana.
 * ▪
 * Horário em que o usuário é mais produtivo.
 * ▪
 * Taxa de conclusão de tarefas.
 * ◦
 * Funcionalidade: Filtros por período (semanal, mensal) para o usuário entender seus padrões de comportamento.
 * 4. Rodapé e Extras
 * •
 * Configurações:
 * ◦
 * Conteúdo: Preferências de tema (Claro/Escuro), gerenciamento de notificações, sons do sistema, opções de backup/sincronização e conta do usuário.
 * •
 * Full Focus PRO (Banner):
 * ◦
 * O que é: A porta de entrada para a monetização.
 * ◦
 * Conteúdo: Destaque para recursos exclusivos como: sincronização em nuvem, estatísticas detalhadas ilimitadas, criação de tags personalizadas sem limites e temas exclusivos.
 * Essa estrutura separa o planejamento (Organização) da ação (Execução) e fornece o feedback necessário (Resultados) para o usuário continuar evoluindo.
 *
 *
 */


@Preview
@Composable
private fun FullFocusModalDrawerSheetLightPreview() {

    FullFocusTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        FullFocusModalDrawerSheet(
            drawerState = rememberDrawerState(DrawerValue.Open),
            onNavigate = {}
        )
    }
}

@Preview
@Composable
private fun FullFocusModalDrawerSheetDarkPreview() {

    FullFocusTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        FullFocusModalDrawerSheet(
            drawerState = rememberDrawerState(DrawerValue.Open),
            onNavigate = {}
        )
    }
}
