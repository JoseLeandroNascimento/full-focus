package com.joseleandro.fullfocus.data.local.preferences.model

import com.joseleandro.fullfocus.R

enum class SoundBackground(
    val title: String,
    val description: String,
    val icon: Int,
    val soundRes: Int?
) {

    RAIN(
        "Chuva Forte",
        "Som relaxante de chuva",
        R.drawable.wpf_audio_wave,
        R.raw.chuva_forte_a_tarde_6106
    ),
    CAFETERIA(
        "Cafeteria",
        "Ambiente de cafeteria",
        R.drawable.uiw_coffee,
        R.raw.canteen_background_33214
    ),
    WHITE_NOISE(
        "Ruído Branco",
        "Foco profundo",
        R.drawable.wpf_audio_wave,
        R.raw.white_noise_358382
    ),
    FOREST(
        "Floresta",
        "Natureza e pássaros",
        R.drawable.material_symbols_forest_outline,
        R.raw.forest_ambience_296528
    ),
    BIRD("Passáro", "pássaros", R.drawable.mingcute_bird_line, R.raw.bird_chipping_426107),
    FARM("Fazenda", "campo", R.drawable.lucide_lab_farm, R.raw.farm_ambience_409990),
    MUTE("Silêncio", "Sem som de fundo", R.drawable.mingcute_volume_mute_line, null),

}