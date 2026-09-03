package com.dannyrodrygues.petlife.feature.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dannyrodrygues.petlife.core.auth.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val isLoading: Boolean = false,
    val isLoginSuccessful: Boolean = false,
    val errorMessage: String? = null,
)

class LoginViewModel(
    private val repository: AuthRepository = AuthRepository(),
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(LoginUiState())

    val uiState: StateFlow<LoginUiState> =
        _uiState.asStateFlow()

    fun signIn(
        email: String,
        password: String,
    ) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.update {
                it.copy(
                    errorMessage = "Preencha o e-mail e a senha.",
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                )
            }

            runCatching {
                repository.signIn(
                    email = email.trim(),
                    password = password,
                )
            }.onSuccess {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isLoginSuccessful = true,
                        errorMessage = null,
                    )
                }
            }.onFailure {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isLoginSuccessful = false,
                        errorMessage = "E-mail ou senha inválidos.",
                    )
                }
            }
        }
    }
    fun consumeLoginSuccess() {
        _uiState.update {
            it.copy(
                isLoginSuccessful = false,
            )
        }
    }
}