package com.ffsilva.pontointeligente.dtos

import org.hibernate.validator.constraints.Length
import org.hibernate.validator.constraints.br.CNPJ
import org.hibernate.validator.constraints.br.CPF
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class CadastroPFDto(

        val id: String? = null,

        @get:NotBlank(message = "Nome não pode ser vazio.")
        @get:Length(min = 3, max = 200, message = "Nome deve conter entre 3 e 200 caracteres.")
        val nome: String = "",

        @get:NotBlank(message = "Email não pode ser vazio.")
        @get:Length(min = 5, max = 200, message = "Email deve conter entre 5 e 200 caracteres.")
        @get:Email(message = "Email inválido.")
        val email: String = "",

        @get:NotBlank(message = "Senha não deve ser vazia.")
        val senha: String = "",

        @NotBlank(message = "CPF não deve ser vazio.")
        @get:CPF(message = "CPF inválido.")
        val cpf: String = "",

        @NotBlank(message = "CNPJ não deve ser vazio.")
        @get:CNPJ(message = "CNPJ inválido.")
        val cnpj: String = "",

        val empresaId: String = "",

        val valorHora: String? = null,
        val qtdHorasTrabalhoDia: String? = null,
        val qtdHorasAlmoco: String? = null
)