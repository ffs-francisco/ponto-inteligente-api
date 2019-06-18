package com.ffsilva.pontointeligente.controllers

import com.ffsilva.pontointeligente.documents.Empresa
import com.ffsilva.pontointeligente.dtos.EmpresaDto
import com.ffsilva.pontointeligente.response.Response
import com.ffsilva.pontointeligente.services.EmpresaService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/empresas")
class EmpresaController(
        val empresaService: EmpresaService
) {

    @GetMapping("/cnpj/{cnpj}")
    fun buscarPorCnpj(@PathVariable("cnpj") cnpj: String): ResponseEntity<Response<EmpresaDto>> {
        val response: Response<EmpresaDto> = Response()

        val empresa = empresaService.buscarPorCnpj(cnpj)
        if (!empresa.isPresent) {
            response.errors.add("Empresa não encontrada para o CNPJ ${cnpj}")
            return ResponseEntity.badRequest().body(response)
        }

        response.data = converterEmpresaPataDto(empresa.get())
        return ResponseEntity.ok(response)
    }

    private fun converterEmpresaPataDto(empresa: Empresa): EmpresaDto {
        return EmpresaDto(razaoSocial = empresa.razaoSocial, cnpj = empresa.cnpj, id = empresa.id)
    }
}