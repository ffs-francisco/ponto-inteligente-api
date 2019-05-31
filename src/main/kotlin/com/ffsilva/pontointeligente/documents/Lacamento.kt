package com.ffsilva.pontointeligente.documents

import com.ffsilva.pontointeligente.enums.TipoEnum
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class Lacamento(
        @Id val id: String? = null,

        val data: LocalDateTime,
        val tipo: TipoEnum,
        val funcionarioId: String,
        val descricao: String? = "",
        val localizacao: String? = ""
)