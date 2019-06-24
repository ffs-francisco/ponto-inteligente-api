package com.ffsilva.pontointeligente.controllers

import com.ffsilva.pontointeligente.documents.Funcionario
import com.ffsilva.pontointeligente.dtos.FuncionarioDto
import com.ffsilva.pontointeligente.response.Response
import com.ffsilva.pontointeligente.services.FuncionarioService
import com.ffsilva.pontointeligente.utils.SenhaUtils
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/funcionarios")
class FuncionarioController(
        val funcionarioService: FuncionarioService
) {

    @PutMapping("/{id}")
    fun atualizar(@PathVariable("id") id: String, @Valid @RequestBody funcionarioDto: FuncionarioDto, result: BindingResult): ResponseEntity<Response<FuncionarioDto>> {
        val response: Response<FuncionarioDto> = Response()

        val funcionario = funcionarioService.buscarPorId(id)
        if (!funcionario.isPresent)
            result.addError(ObjectError("funcionario", "Funcionário não encontrado."))

        if (result.hasErrors()) {
            result.allErrors.forEach { response.errors.add(it.defaultMessage.toString()) }
            return ResponseEntity.badRequest().body(response)
        }

        val funcionarioAtualizar: Funcionario = atualizarDadosFuncionario(funcionario.get(), funcionarioDto)

        response.data = converterFuncionarioToDto(funcionarioService.persistir(funcionarioAtualizar))
        return ResponseEntity.ok(response)
    }

    private fun converterFuncionarioToDto(funcionario: Funcionario): FuncionarioDto {
        return FuncionarioDto(
                id = funcionario.id,
                nome = funcionario.nome,
                email = funcionario.email,
                senha = "",
                valorHora = funcionario.valorHora.toString(),
                qtdHorasTrabalhoDia = funcionario.qtdHorasTrabalhoDia.toString()
        )
    }

    private fun atualizarDadosFuncionario(funcionario: Funcionario, dto: FuncionarioDto): Funcionario {
        return Funcionario(
                id = funcionario.id,
                nome = funcionario.nome,
                email = funcionario.email,
                senha = if (dto.senha == null) funcionario.senha else SenhaUtils().gerarBCrypt(dto.senha),
                cpf = funcionario.cpf,
                perfil = funcionario.perfil,

                empresaId = funcionario.empresaId,
                valorHora = dto.valorHora?.toDouble(),
                qtdHorasAlmoco = dto.qtdHorasAlmoco?.toFloat(),
                qtdHorasTrabalhoDia = dto.qtdHorasTrabalhoDia?.toFloat()
        )
    }
}