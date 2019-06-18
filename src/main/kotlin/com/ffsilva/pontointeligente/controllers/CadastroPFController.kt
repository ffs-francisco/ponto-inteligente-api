package com.ffsilva.pontointeligente.controllers

import com.ffsilva.pontointeligente.documents.Empresa
import com.ffsilva.pontointeligente.documents.Funcionario
import com.ffsilva.pontointeligente.dtos.CadastroPFDto
import com.ffsilva.pontointeligente.enums.PerfilEnum
import com.ffsilva.pontointeligente.response.Response
import com.ffsilva.pontointeligente.services.EmpresaService
import com.ffsilva.pontointeligente.services.FuncionarioService
import com.ffsilva.pontointeligente.utils.SenhaUtils
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/cadastrar-pf")
class CadastroPFController(
        val empresaService: EmpresaService,
        val funcionarioService: FuncionarioService
) {

    @PostMapping
    fun cadastrar(@Valid @RequestBody cadastroPFDto: CadastroPFDto, result: BindingResult): ResponseEntity<Response<CadastroPFDto>> {
        val response: Response<CadastroPFDto> = Response()

        val empresa = empresaService.buscarPorCnpj(cadastroPFDto.cnpj)
        validarDadosExistentes(cadastroPFDto, empresa.get(), result)

        if (result.hasErrors()) {
            result.allErrors.forEach { response.errors.add(it.defaultMessage.toString()) }
            return ResponseEntity.badRequest().body(response)
        }

        var funcionario: Funcionario = converterDtoParaFuncionario(cadastroPFDto, empresa.get())
        funcionario = funcionarioService.persistir(funcionario)

        response.data = converterCadastroDtoTo(funcionario, empresa.get())
        return ResponseEntity.ok(response)
    }

    private fun converterCadastroDtoTo(funcionario: Funcionario, empresa: Empresa): CadastroPFDto? {
        return CadastroPFDto(
                id = funcionario.id,
                nome = funcionario.nome,
                email = funcionario.email,
                senha = "",
                cpf = funcionario.cpf,

                valorHora = funcionario.valorHora.toString(),
                qtdHorasAlmoco = funcionario.qtdHorasAlmoco.toString(),
                qtdHorasTrabalhoDia = funcionario.qtdHorasTrabalhoDia.toString(),

                empresaId = empresa.id.toString(),
                cnpj = empresa.cnpj
        )
    }

    private fun converterDtoParaFuncionario(cadastroPFDto: CadastroPFDto, empresa: Empresa): Funcionario {
        return Funcionario(
                id = cadastroPFDto.id,
                nome = cadastroPFDto.nome,
                email = cadastroPFDto.email,
                senha = SenhaUtils().gerarBCrypt(cadastroPFDto.senha),
                cpf = cadastroPFDto.cpf,
                perfil = PerfilEnum.ROLE_USUARIO,

                empresaId = empresa.id.toString(),
                valorHora = cadastroPFDto.valorHora?.toDouble(),
                qtdHorasAlmoco = cadastroPFDto.qtdHorasAlmoco?.toFloat(),
                qtdHorasTrabalhoDia = cadastroPFDto.qtdHorasAlmoco?.toFloat()
        )
    }

    private fun validarDadosExistentes(cadastroPFDto: CadastroPFDto, empresa: Empresa?, result: BindingResult) {
        if (empresa == null)
            result.allErrors.add(ObjectError("empresa", "Empresa não encontrada."))

        if (funcionarioService.buscarPorCpf(cadastroPFDto.cpf) != null)
            result.allErrors.add(ObjectError("funcionario", "Funcionário já existente."))

        if (funcionarioService.buscarPorEmail(cadastroPFDto.email) != null)
            result.allErrors.add(ObjectError("funcionario", "E-mail já cadastrado."))
    }
}