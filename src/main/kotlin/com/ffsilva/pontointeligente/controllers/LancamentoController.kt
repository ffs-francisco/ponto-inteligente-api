package com.ffsilva.pontointeligente.controllers

import com.ffsilva.pontointeligente.documents.Funcionario
import com.ffsilva.pontointeligente.documents.Lancamento
import com.ffsilva.pontointeligente.dtos.LancamentoDto
import com.ffsilva.pontointeligente.enums.TipoEnum
import com.ffsilva.pontointeligente.response.Response
import com.ffsilva.pontointeligente.services.impl.FuncionarioServiceImpl
import com.ffsilva.pontointeligente.services.impl.LancamentoServiceImpl
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/lancamentos")
class LancamentoController(
        val lancamentoService: LancamentoServiceImpl,
        val funcionarioService: FuncionarioServiceImpl
) {

    @Value("\${paginacao.qtd_por_pagina}")
    val qtdPorPagina: Int = 15

    @PostMapping
    fun adicinar(@Valid @RequestBody lancamentoDto: LancamentoDto, result: BindingResult): ResponseEntity<Response<LancamentoDto>> {
        val response: Response<LancamentoDto> = Response()

        validarFuncionario(lancamentoDto, result)
        if (result.hasErrors()) {
            result.allErrors.forEach { response.errors.add(it.defaultMessage.toString()) }
            return ResponseEntity.badRequest().body(response)
        }

        val lancamento: Lancamento = converterLancamentoDtoParaLancamento(lancamentoDto, result)
        lancamentoService.persistir(lancamento)

        response.data = converterLancamentoParaLancamentoDto(lancamento)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{id}")
    fun listarPorId(@PathVariable("id") id: String): ResponseEntity<Response<LancamentoDto>> {
        val response: Response<LancamentoDto> = Response()

        val lancamento = lancamentoService.buscarPorId(id)
        if (!lancamento.isPresent) {
            response.errors.add("Lançamento não encontrado para o id $id")
            return ResponseEntity.badRequest().body(response)
        }

        response.data = converterLancamentoParaLancamentoDto(lancamento.get())
        return ResponseEntity.ok(response)
    }

    @GetMapping("/funcionario/{funcionarioId}")
    fun listarPorFuncionarioId(@PathVariable("funcionarioId") funcionarioId: String,
                               @RequestParam(value = "pag", defaultValue = "0") pag: Int,
                               @RequestParam(value = "ord", defaultValue = "id") ord: String,
                               @RequestParam(value = "dir", defaultValue = "DESC") dir: String
    ): ResponseEntity<Response<Page<LancamentoDto>>> {
        val response: Response<Page<LancamentoDto>> = Response()

        val pageRequest: PageRequest = PageRequest.of(pag, qtdPorPagina, Sort.Direction.valueOf(dir), ord)
        val lanacamentos: Page<Lancamento> = lancamentoService.buscarPorFuncionarioId(funcionarioId, pageRequest)

        val lancamentosDto: Page<LancamentoDto> = lanacamentos.map { lancamento ->
            converterLancamentoParaLancamentoDto(lancamento)
        }

        response.data = lancamentosDto
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{id}")
    fun atualizar(@PathVariable("id") id: String,
                  @Valid @RequestBody lancamentoDto: LancamentoDto, result: BindingResult): ResponseEntity<Response<LancamentoDto>> {
        val response: Response<LancamentoDto> = Response()

        validarFuncionario(lancamentoDto, result)
        lancamentoDto.id = id
        val lancamento: Lancamento = converterLancamentoDtoParaLancamento(lancamentoDto, result)

        if (result.hasErrors()) {
            result.allErrors.forEach { response.errors.add(it.defaultMessage.toString()) }
            return ResponseEntity.badRequest().body(response)
        }

        response.data = converterLancamentoParaLancamentoDto(lancamentoService.persistir(lancamento))
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{id}")
    fun remover(@PathVariable("id") id: String): ResponseEntity<Response<String>> {
        val response: Response<String> = Response()

        val lancamento = lancamentoService.buscarPorId(id)
        if (!lancamento.isPresent) {
            response.errors.add("Error ao remover lançamento. Registro não encontrado para o id $id")
            return ResponseEntity.badRequest().body(response)
        }

        lancamentoService.remover(id)
        return ResponseEntity.ok().build()
    }


    /*
    *  PRIVATE METHODS
    * */
    private fun converterLancamentoParaLancamentoDto(lancamento: Lancamento): LancamentoDto {
        return LancamentoDto(
                id = lancamento.id,
                data = lancamento.data.toString(),
                tipo = lancamento.tipo.name,
                descricao = lancamento.descricao,
                localizacao = lancamento.localizacao,
                funcionarioId = lancamento.funcionarioId
        )
    }

    private fun converterLancamentoDtoParaLancamento(lancamentoDto: LancamentoDto, result: BindingResult): Lancamento {
        if (lancamentoDto.id != null) {
            if (!lancamentoService.buscarPorId(lancamentoDto.id!!).isPresent)
                result.addError(ObjectError("lancamento", "Lançamento não encontrado."))
        }

        return Lancamento(
                id = lancamentoDto.id!!,
                data = LocalDateTime.parse(lancamentoDto.data),
                tipo = TipoEnum.valueOf(lancamentoDto.tipo!!),
                descricao = lancamentoDto.descricao,
                localizacao = lancamentoDto.localizacao,
                funcionarioId = lancamentoDto.funcionarioId!!
        )
    }

    private fun validarFuncionario(lancamentoDto: LancamentoDto, result: BindingResult) {
        if (lancamentoDto.funcionarioId == null) {
            result.addError(ObjectError("funcionario", "Funcionario não informado."))
            return
        }

        val funcionario: Optional<Funcionario> = funcionarioService.buscarPorId(lancamentoDto.funcionarioId)
        if (!funcionario.isPresent) {
            result.addError(ObjectError("funcioanrio", "Funcinário não encontrado. ID inexistente."))
        }
    }
}