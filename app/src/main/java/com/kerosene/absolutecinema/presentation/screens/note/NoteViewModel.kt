package com.kerosene.absolutecinema.presentation.screens.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerosene.absolutecinema.domain.entity.Note
import com.kerosene.absolutecinema.domain.usecase.GetNoteByMovieIdUseCase
import com.kerosene.absolutecinema.domain.usecase.UpdateNoteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class NoteViewModel @Inject constructor(
    private val getNoteByMovieIdUseCase: GetNoteByMovieIdUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
) : ViewModel() {

    private val _note = MutableStateFlow<Note?>(null)
    val note: StateFlow<Note?> = _note.asStateFlow()

    private val _text = MutableStateFlow("")
    val text: StateFlow<String> = _text.asStateFlow()

    fun load(movieId: Int) {
        viewModelScope.launch {
            getNoteByMovieIdUseCase(movieId).collect { loaded ->
                _note.value = loaded
                if (_text.value != loaded.content) {
                    _text.value = loaded.content
                }
            }
        }
    }

    fun onTextChange(newText: String) {
        _text.value = newText
    }

    fun save() {
        val current = _note.value ?: return
        val newContent = _text.value.trim()
        if (current.content == newContent) return
        viewModelScope.launch {
            updateNoteUseCase(current.copy(content = newContent))
        }
    }
}