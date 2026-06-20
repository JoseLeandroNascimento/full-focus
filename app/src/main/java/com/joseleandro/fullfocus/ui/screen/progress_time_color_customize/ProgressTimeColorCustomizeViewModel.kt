package com.joseleandro.fullfocus.ui.screen.progress_time_color_customize

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseleandro.fullfocus.domain.effect.PickerColorEffect
import com.joseleandro.fullfocus.domain.repository.PomodoroSettingRepository
import com.joseleandro.fullfocus.ui.event.PickerColorEvent
import com.joseleandro.fullfocus.ui.screen.progress_time_color_customize.component.PickerColorType
import com.joseleandro.fullfocus.ui.state.PickerColorUiState
import com.joseleandro.fullfocus.ui.theme.ColorStyle
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProgressTimeColorCustomizeViewModel(
    private val pomodoroSettingRepository: PomodoroSettingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PickerColorUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<PickerColorEffect>()
    val effect = _effect.asSharedFlow()

    fun init(type: PickerColorType, initialColor: ColorStyle) {
        _uiState.update { it.copy(type = type, selectedColor = initialColor) }
    }

    fun onEvent(event: PickerColorEvent) {
        when (event) {
            is PickerColorEvent.OnColorSelected -> {
                _uiState.update { it.copy(selectedColor = event.color) }
            }

            is PickerColorEvent.OnTabChanged -> {
                _uiState.update { it.copy(selectedTab = event.tabIndex) }
            }

            is PickerColorEvent.OnToggleCustomPicker -> {
                _uiState.update { it.copy(showCustomPicker = event.show) }
            }

            PickerColorEvent.OnConfirm -> {
                viewModelScope.launch {
                    saveColor(_uiState.value.type, _uiState.value.selectedColor)
                    _effect.emit(PickerColorEffect.ConfirmColor(_uiState.value.selectedColor))
                }
            }

            PickerColorEvent.OnCancel -> {
                viewModelScope.launch {
                    _effect.emit(PickerColorEffect.NavigateBack)
                }
            }
        }
    }

    private suspend fun saveColor(type: PickerColorType, color: ColorStyle) {
        when (type) {
            PickerColorType.FOCUS_PICKER_COLOR -> pomodoroSettingRepository.updateFocusProgressColor(
                color
            )

            PickerColorType.SHORT_BREAK_PICKER_COLOR -> pomodoroSettingRepository.updateShortBreakProgressColor(
                color
            )

            PickerColorType.LONG_BREAK_PICKER_COLOR -> pomodoroSettingRepository.updateLongBreakProgressColor(
                color
            )
        }
    }
}
