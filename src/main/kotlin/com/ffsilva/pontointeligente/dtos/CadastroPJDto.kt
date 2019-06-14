package com.ffsilva.pontointeligente.dtos

import org.hibernate.validator.constraints.Length
import org.hibernate.validator.constraints.br.CNPJ
import org.hibernate.validator.constraints.br.CPF
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class CadastroPJDto(

        val id: String? = null,

        @get:NotBlank(message = "Nome não pode ser vazio.")
        @get:Length(min = 2, max = 200, message = "Nome deve conter entre 2 e 200 caracteres.")
        val nome: String = "",

        @get:NotBlank(message = "Email não pode ser vazio.")
        @get:Length(min = 5, max = 200, message = "Email deve conter entre 5 e 200 caracteres.")
        @get:Email(message = "Email é inválido.")
        val email: String = "",

        @get:NotBlank(message = "Senha não pode vazia.")
        val senha: String = "",

        @get:NotBlank(message = "CPF não pode vazio.")
        @get:CPF(message = "CPF inválido.")
        val cpf: String = "",

        @get:NotBlank(message = "CNPJ não pode vazio.")
        @get:CNPJ(message = "CNPJ inválido.")
        internal val cnpj: String = "",

        @get:NotBlank(message = "Razão social não pode ser vazia.")
        @get:Length(min = 5, max = 200, message = "Razão social deve conter entre 5 e 200 caracteres.")
        val razaoSoial: String = ""
)