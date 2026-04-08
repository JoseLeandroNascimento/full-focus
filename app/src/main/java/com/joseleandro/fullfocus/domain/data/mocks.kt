package com.joseleandro.fullfocus.domain.data

import androidx.compose.ui.graphics.Color

val tasksListMock: List<TaskDomain> = listOf(
    TaskDomain(
        id = 1,
        title = "Estudar matemática",
        progress = 1,
        pomodoros = 2
    ),
    TaskDomain(
        id = 2,
        title = "Enviar email",
        progress = 0,
        pomodoros = 4
    ),
    TaskDomain(
        id = 3,
        title = "Estudar geografia",
        progress = 1,
        pomodoros = 6
    ),
    TaskDomain(
        id = 4,
        title = "Estudar português",
        progress = 4,
        pomodoros = 8
    ),
    TaskDomain(
        id = 5,
        title = "Estudar inglês",
        progress = 1,
        pomodoros = 2
    ),
    TaskDomain(
        id = 6,
        title = "Estudar química",
        progress = 1,
        pomodoros = 2
    ),
    TaskDomain(
        id = 7,
        title = "Estudar física",
        progress = 1,
        pomodoros = 2
    ),
    TaskDomain(
        id = 8,
        title = "Estudar biologia",
        progress = 1,
        pomodoros = 2
    ),
    TaskDomain(
        id = 9,
        title = "Estudar ciências",
        progress = 1,
        pomodoros = 2
    )
)

val tagsListMock: List<TagDomain> = listOf(
    TagDomain(
        id = 1,
        name = "Trabalho",
        color = Color(0xFF3B82F6)
    ),
    TagDomain(
        id = 2,
        name = "Estudo",
        color = Color(0xFF8B5CF6)
    ),
    TagDomain(
        id = 3,
        name = "Saúde",
        color = Color(0xFF10B981)
    ),
    TagDomain(
        id = 4,
        name = "Lazer",
        color = Color(0xFFF59E0B)
    ),
    TagDomain(
        id = 5,
        name = "Leitura",
        color = Color(0xFFEF4444)
    ),
    TagDomain(
        id = 6,
        name = "Exercício",
        color = Color(0xFF22C55E)
    ),
    TagDomain(
        id = 7,
        name = "Projeto",
        color = Color(0xFF6366F1)
    ),
    TagDomain(
        id = 8,
        name = "Pessoal",
        color = Color(0xFFEC4899)
    ),
    TagDomain(
        id = 9,
        name = "Finanças",
        color = Color(0xFF14B8A6)
    ),
    TagDomain(
        id = 10,
        name = "Urgente",
        color = Color(0xFFDC2626)
    )
)