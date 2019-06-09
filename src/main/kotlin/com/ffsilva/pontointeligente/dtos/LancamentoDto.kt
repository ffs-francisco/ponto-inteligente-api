package com.ffsilva.pontointeligente.dtos

import javax.validation.constraints.NotBlank

data class LancamentoDto(

        var id: String? = null,

        @get:NotBlank(message = "Data não pode ser vazia.")
        val data: String? = null,

        @get:NotBlank(message = "Tipo não pode ser vazio.")
        val tipo: String? = null,

        val descricao: String? = null,
        val localizacao: String? = null,
        val funcionarioId: String? = null
)