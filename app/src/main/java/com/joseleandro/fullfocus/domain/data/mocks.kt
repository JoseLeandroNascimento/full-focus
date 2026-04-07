package com.joseleandro.fullfocus.domain.data

import androidx.compose.ui.graphics.Color

val tasksListMock: List<TaskDomain> = listOf(
    TaskDomain(
        title = "Estudar matemática",
        pomodorosCheck = 1,
        pomodorosTotal = 2
    ),
    TaskDomain(
        title = "Enviar email",
        pomodorosCheck = 0,
        pomodorosTotal = 4
    ),
    TaskDomain(
        title = "Estudar geografia",
        pomodorosCheck = 1,
        pomodorosTotal = 6
    ),
    TaskDomain(
        title = "Estudar português",
        pomodorosCheck = 4,
        pomodorosTotal = 8
    ),
    TaskDomain(
        title = "Estudar inglês",
        pomodorosCheck = 1,
        pomodorosTotal = 2
    ),
    TaskDomain(
        title = "Estudar química",
        pomodorosCheck = 1,
        pomodorosTotal = 2
    ),
    TaskDomain(
        title = "Estudar física",
        pomodorosCheck = 1,
        pomodorosTotal = 2
    ),
    TaskDomain(
        title = "Estudar biologia",
        pomodorosCheck = 1,
        pomodorosTotal = 2
    ),
    TaskDomain(
        title = "Estudar ciências",
        pomodorosCheck = 1,
        pomodorosTotal = 2
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