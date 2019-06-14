package com.ffsilva.pontointeligente.controllers

import com.ffsilva.pontointeligente.documents.Empresa
import com.ffsilva.pontointeligente.documents.Funcionario
import com.ffsilva.pontointeligente.dtos.CadastroPJDto
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
@RequestMapping("/api/cadastro-pj")
class CadastroPJController(
        val empresaService: EmpresaService,
        val funcionarioService: FuncionarioService
) {

    @PostMapping
    fun cadastrar(@Valid @RequestBody cadastroPJDto: CadastroPJDto, result: BindingResult): ResponseEntity<Response<CadastroPJDto>> {
        val response: Response<CadastroPJDto> = Response()

        validarDadosExistentes(cadastroPJDto, result)
        if (result.hasErrors()) {
            result.allErrors.forEach { response.errors.add(it.defaultMessage.toString()) }
            return ResponseEntity.badRequest().body(response)
        }

        val empresa: Empresa = converterDtoParaEmpresa(cadastroPJDto)
        empresaService.persistir(empresa)

        val funcionario: Funcionario = converterDtoParaFuncionario(cadastroPJDto, empresa)
        funcionarioService.persistir(funcionario)

        response.data = converterCadastroDtoto(funcionario, empresa)
        return ResponseEntity.ok(response)
    }

    private fun converterCadastroDtoto(funcionario: Funcionario, empresa: Empresa): CadastroPJDto {
        return CadastroPJDto(
                nome = funcionario.nome,
                email = funcionario.email,
                senha = "",
                cpf = funcionario.cpf,
                cnpj = empresa.cnpj,
                razaoSoial = empresa.razaoSocial,
                id = funcionario.id
        )
    }

    private fun converterDtoParaFuncionario(cadastroPJDto: CadastroPJDto, empresa: Empresa): Funcionario {
        return Funcionario(
                nome = cadastroPJDto.nome,
                email = cadastroPJDto.email,
                senha = SenhaUtils().gerarBCrypt(cadastroPJDto.senha),
                cpf = cadastroPJDto.cpf,
                perfil = PerfilEnum.ROLE_ADMIN,
                empresaId = empresa.id.toString()
        )
    }

    private fun converterDtoParaEmpresa(cadastroPJDto: CadastroPJDto): Empresa {
        return Empresa(razaoSocial = cadastroPJDto.razaoSoial, cnpj = cadastroPJDto.cnpj)
    }

    private fun validarDadosExistentes(cadastroPJDto: CadastroPJDto, result: BindingResult) {
        if (empresaService.buscarPorCnpj(cadastroPJDto.cnpj) != null)
            result.addError(ObjectError("empresa", "Empresa já existente."))

        if (funcionarioService.buscarPorCpf(cadastroPJDto.cpf) != null)
            result.addError(ObjectError("funcionario", "CPF já esxistente."))

        if (funcionarioService.buscarPorEmail(cadastroPJDto.email) != null)
            result.addError(ObjectError("funcionario", "Email já existente."))
    }
}